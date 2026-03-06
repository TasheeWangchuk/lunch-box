package com.lunchbox.lunch_box.modules.delivery.controller;

import com.lunchbox.lunch_box.common.dto.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<RiderResponse>> createRider(@Valid @RequestBody CreateRiderRequest request) {
        RiderResponse rider = riderService.createRider(request);
        return new ResponseEntity<>(
                ApiResponse.success(HttpStatus.CREATED.value(), "Rider created successfully", rider),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RIDER')")
    public ResponseEntity<ApiResponse<RiderResponse>> getRiderById(@PathVariable Long id) {
        RiderResponse rider = riderService.getRiderById(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Rider retrieved successfully", rider));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<RiderResponse>>> getAllRiders() {
        List<RiderResponse> riders = riderService.getAllRiders();
        return ResponseEntity
                .ok(ApiResponse.success(HttpStatus.OK.value(), "All riders retrieved successfully", riders));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteRider(@PathVariable Long id) {
        riderService.deleteRider(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Rider deleted successfully", null));
    }
}
