package com.fcpv.abnormal.service;

import com.fcpv.abnormal.dto.request.SignInToken;
import com.fcpv.abnormal.dto.response.TokenResponse;

public interface AuthenticationService {

    TokenResponse getAccessToken(SignInToken signInToken);

    TokenResponse getRefreshToken(String signInToken);
}
