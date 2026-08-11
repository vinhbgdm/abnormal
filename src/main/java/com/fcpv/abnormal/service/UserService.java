package com.fcpv.abnormal.service;

import com.fcpv.abnormal.dto.request.UserRequestDto;
import com.fcpv.abnormal.dto.request.UserUpdateDto;
import com.fcpv.abnormal.dto.response.UserDetailResponse;

import java.util.List;

public interface UserService {
    long saveUser(UserRequestDto request);

    void updateUser(long userId, UserUpdateDto request);

    void deleteUser(long userId);

    UserDetailResponse getUser(long userId);

    List<UserDetailResponse> getAllUsers();
}
