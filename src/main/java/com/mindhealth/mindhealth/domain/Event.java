package com.mindhealth.mindhealth.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Entity
@NoArgsConstructor(force = true)
@EntityListeners(AuditingEntityListener.class)
public class Event {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;
    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime dateTime;

    @Column
    private String location;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer availableSeats;

    // ✅ Keep JPA relationship for Hibernate
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)  // Ensure it's NOT NULL in JPA
    private Category category;

    // ✅ New field for DynamoDB, used to store category ID separately
    @Column(name = "category_id", insertable = false, updatable = false)  // Only for JPA compatibility
    private String categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    @Column(name = "organizer_id", insertable = false, updatable = false)  // Only for JPA compatibility
    private String organizerId;

    @OneToMany(mappedBy = "event")
    private Set<Ticket> eventTickets;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    // ✅ Sync category and categoryId for DynamoDB
    public void setCategory(Category category) {
        this.category = category;
        this.categoryId = category != null ? category.getId().toString() : null;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
        this.organizerId = organizer != null ? organizer.getId().toString() : null;
    }
}