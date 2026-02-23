package com.mindhealth.mindhealth.rest;

import com.mindhealth.mindhealth.model.TicketDTO;
import com.mindhealth.mindhealth.service.TicketService;
import com.mindhealth.mindhealth.util.ReferencedException;
import com.mindhealth.mindhealth.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.Data;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
public class TicketResource {

    private final TicketService ticketService;

    public TicketResource(final TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Data
    public static class ValidateRequest {
        private String qrCode;
    }

    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        return ResponseEntity.ok(ticketService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicket(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(ticketService.get(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TicketDTO>> getUserTickets(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.findByUser(userId));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<String> getDownloadUrl(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketDownloadUrl(id));
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestBody ValidateRequest request) {
        return ResponseEntity.ok(ticketService.validateTicket(request.getQrCode()));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createTicket(@RequestBody @Valid final TicketDTO ticketDTO) {
        final Long createdId = ticketService.create(ticketDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateTicket(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final TicketDTO ticketDTO) {
        ticketService.update(id, ticketDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTicket(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = ticketService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
