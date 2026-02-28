package com.lunchbox.lunch_box.modules.delivery.controller;

import com.lunchbox.lunch_box.modules.delivery.dto.request.CreateRiderRequest;
import com.lunchbox.lunch_box.modules.delivery.dto.response.RiderResponse;
import com.lunchbox.lunch_box.modules.delivery.service.RiderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rider")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class RiderController {

    private final RiderService riderService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RiderResponse> createRider(@Valid @RequestBody CreateRiderRequest request) {
        return new ResponseEntity<>(riderService.createRider(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RIDER')")
    public ResponseEntity<RiderResponse> getRiderById(@PathVariable Long id) {
        return ResponseEntity.ok(riderService.getRiderById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RiderResponse>> getAllRiders() {
        return ResponseEntity.ok(riderService.getAllRiders());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRider(@PathVariable Long id) {
        riderService.deleteRider(id);
        return ResponseEntity.noContent().build();
    }
}
