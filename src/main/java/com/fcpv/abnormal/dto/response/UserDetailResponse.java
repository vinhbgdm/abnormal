package com.fcpv.abnormal.dto.response;

import com.fcpv.abnormal.enums.Department;
import com.fcpv.abnormal.enums.UserRole;
import com.fcpv.abnormal.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class UserDetailResponse implements Serializable {
    private String fullName;
    private String email;
    private String userCode;
    private String phone;
    private Department department;
    private UserRole role;
    private UserStatus status;

    public UserDetailResponse(String fullName, String email, String userCode, String phone, Department department, UserRole role, UserStatus status) {
        this.fullName = fullName;
        this.email = email;
        this.userCode = userCode;
        this.phone = phone;
        this.department = department;
        this.role = role;
        this.status = status;
    }
}
