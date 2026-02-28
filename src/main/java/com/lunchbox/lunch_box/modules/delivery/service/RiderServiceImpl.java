package com.lunchbox.lunch_box.modules.delivery.service;

import com.lunchbox.lunch_box.modules.delivery.dto.request.CreateRiderRequest;
import com.lunchbox.lunch_box.modules.delivery.dto.response.RiderResponse;
import com.lunchbox.lunch_box.modules.user.entity.User;
import com.lunchbox.lunch_box.modules.user.enums.AppRole;
import com.lunchbox.lunch_box.modules.user.enums.AuthProvider;
import com.lunchbox.lunch_box.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public RiderResponse createRider(CreateRiderRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already registered");
        }
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken");
        }

        User rider = new User();
        rider.setUsername(request.getUsername());
        rider.setEmail(request.getEmail());
        rider.setPassword(passwordEncoder.encode(request.getPassword()));
        rider.setPhone(request.getPhone());
        rider.setRole(AppRole.RIDER);
        rider.setProvider(AuthProvider.LOCAL);
        rider.setActive(true);
        rider.setEmailVerified(false);

        User saved = userRepository.save(rider);
        return mapToResponse(saved);
    }

    @Override
    public RiderResponse getRiderById(Long id) {
        User rider = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rider not found"));

        if (rider.getRole() != AppRole.RIDER) {
            throw new RuntimeException("User is not a rider");
        }

        return mapToResponse(rider);
    }

    @Override
    public List<RiderResponse> getAllRiders() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == AppRole.RIDER)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRider(Long id) {
        User rider = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rider not found"));
        if (rider.getRole() == AppRole.RIDER) {
            userRepository.deleteById(id);
        }
    }

    private RiderResponse mapToResponse(User user) {
        return RiderResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
