package com.mindhealth.mindhealth.repository;

import com.mindhealth.mindhealth.domain.Category;
import com.mindhealth.mindhealth.domain.Event;
import com.mindhealth.mindhealth.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Event findFirstByCategory(Category category);
    Event findFirstByOrganizer(User user);
    List<Event> findTop6ByOrderByDateCreatedDesc();
    List<Event> findByOrganizer_Id(Long organizerId);
    List<Event> findByDateTimeBetween(OffsetDateTime start, OffsetDateTime end);

    @EntityGraph(attributePaths = {"category", "organizer"})
    @Query("select e from Event e")
    Page<Event> findAllWithRelations(Pageable pageable);

    @Query("""
        select e from Event e
        where (:query is null or lower(e.title) like lower(concat('%', :query, '%'))
          or lower(coalesce(e.description, '')) like lower(concat('%', :query, '%')))
          and (:location is null or lower(coalesce(e.location, '')) like lower(concat('%', :location, '%')))
          and (:categoryId is null or e.category.id = :categoryId)
        order by e.dateTime asc
    """)
    Page<Event> search(@Param("query") String query,
                       @Param("location") String location,
                       @Param("categoryId") Long categoryId,
                       Pageable pageable);

    @Query("""
        select distinct e.title from Event e
        where lower(e.title) like lower(concat(:prefix, '%'))
        order by e.title asc
    """)
    List<String> findAutocompleteTitles(@Param("prefix") String prefix, Pageable pageable);
}
