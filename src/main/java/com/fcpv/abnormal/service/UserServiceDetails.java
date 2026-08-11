package com.fcpv.abnormal.service;

import com.fcpv.abnormal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceDetails {
    private final UserRepository userRepository;

    public UserDetailsService userServiceDetails() {
        return userRepository::findByUserCode;
    }
}
