package com.fcpv.abnormal.service.impl;

import com.fcpv.abnormal.config.Translator;
import com.fcpv.abnormal.dto.request.UserRequestDto;
import com.fcpv.abnormal.dto.request.UserUpdateDto;
import com.fcpv.abnormal.dto.response.UserDetailResponse;
import com.fcpv.abnormal.enums.Department;
import com.fcpv.abnormal.enums.UserRole;
import com.fcpv.abnormal.enums.UserStatus;
import com.fcpv.abnormal.exception.ResourceNotFoundException;
import com.fcpv.abnormal.model.User;
import com.fcpv.abnormal.repository.UserRepository;
import com.fcpv.abnormal.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public long saveUser(UserRequestDto request) {
        if (userRepository.existsByUserCode(request.getUserCode())) {
            throw new ResourceNotFoundException(Translator.toLocale("user.code.already.exists"));
        }
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .userCode(request.getUserCode())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .department(Department.valueOf(request.getDepartment()))
                .role(UserRole.valueOf(request.getRole()))
                .status(UserStatus.valueOf(request.getStatus()))
                .build();
        userRepository.save(user);
        log.info("User has added successfully, userId={}", user.getId());

        return user.getId();
    }

    @Override
    public void updateUser(long userId, UserUpdateDto request) {
        User user = getUserById(userId);
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setDepartment((Department.valueOf(request.getDepartment())));
        user.setRole(UserRole.valueOf(request.getRole()));
        user.setStatus(UserStatus.valueOf(request.getStatus()));
        userRepository.save(user);

        log.info("User has updated successfully, userId={}", userId);
    }

    @Override
    public void deleteUser(long userId) {
        User user = getUserById(userId);
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);

        log.info("User has deleted permanent successfully, userId={}", userId);
    }

    @Override
    public UserDetailResponse getUser(long userId) {
        User user = getUserById(userId);
        return UserDetailResponse.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .userCode(user.getUserCode())
                .phone(user.getPhone())
                .department(user.getDepartment())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }

    @Override
    public List<UserDetailResponse> getAllUsers() {
        log.info("Getting all users");
        return userRepository.findAll()
                .stream()
                .map(user -> UserDetailResponse.builder()
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .userCode(user.getUserCode())
                        .phone(user.getPhone())
                        .department(user.getDepartment())
                        .role(user.getRole())
                        .status(user.getStatus())
                        .build())
                .toList();
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("user.not.found")));
    }

}
