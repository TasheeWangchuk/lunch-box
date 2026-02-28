package com.lunchbox.lunch_box.modules.delivery.service;

import com.lunchbox.lunch_box.modules.delivery.dto.request.CreateRiderRequest;
import com.lunchbox.lunch_box.modules.delivery.dto.response.RiderResponse;

import java.util.List;

public interface RiderService {
    RiderResponse createRider(CreateRiderRequest request);

    RiderResponse getRiderById(Long id);

    List<RiderResponse> getAllRiders();

    void deleteRider(Long id);
}
