package com.lunchbox.lunch_box.modules.restaurant.controller;

import com.lunchbox.lunch_box.common.dto.response.ApiResponse;
import com.lunchbox.lunch_box.modules.restaurant.dto.request.CreateStaffRequest;
import com.lunchbox.lunch_box.modules.restaurant.dto.request.UpdateStaffRoleRequest;
import com.lunchbox.lunch_box.modules.restaurant.dto.response.StaffResponse;
import com.lunchbox.lunch_box.modules.restaurant.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class StaffController {

    private final StaffService staffService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StaffResponse>> createStaff(@Valid @RequestBody CreateStaffRequest request) {
        StaffResponse staff = staffService.createStaff(request);
        return new ResponseEntity<>(
                ApiResponse.success(HttpStatus.CREATED.value(), "Staff created successfully", staff),
                HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<StaffResponse>>> getAllStaff() {
        List<StaffResponse> staffList = staffService.getAllStaff();
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "All staff members retrieved successfully", staffList));
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StaffResponse>> updateStaffRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStaffRoleRequest request) {
        StaffResponse staff = staffService.updateStaffRole(id, request.getRole());
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Staff role updated successfully", staff));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()") // Admin can view any; staff can only view their own — enforced in service
    public ResponseEntity<ApiResponse<StaffResponse>> getStaffById(@PathVariable Long id) {
        StaffResponse staff = staffService.getStaffById(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Staff retrieved successfully", staff));
    }

    @GetMapping("/restaurant/{restaurantId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<StaffResponse>>> getStaffByRestaurant(@PathVariable Long restaurantId) {
        List<StaffResponse> staffList = staffService.getStaffByRestaurant(restaurantId);
        return ResponseEntity
                .ok(ApiResponse.success(HttpStatus.OK.value(), "Staff members retrieved successfully", staffList));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Staff deleted successfully", null));
    }
}
