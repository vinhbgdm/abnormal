package com.fcpv.abnormal.dto.request;

import com.fcpv.abnormal.enums.Department;
import com.fcpv.abnormal.enums.UserRole;
import com.fcpv.abnormal.enums.UserStatus;
import com.fcpv.abnormal.validator.EnumValue;
import com.fcpv.abnormal.validator.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserRequestDto implements Serializable {

    @NotBlank(message = "fullName must be not blank")
    private String fullName;

    @Email(message = "email invalid format")
    private String email;

    @NotBlank(message = "userCode must be not blank")
    private String userCode;

    @NotNull(message = "password must be not null")
    private String password;

    @PhoneNumber
    private String phone;

    @NotNull(message = "department must be not null")
    @EnumValue(name = "department", enumClass = Department.class)
    private String department;

    @NotNull(message = "role must be not null")
    @EnumValue(name = "role", enumClass = UserRole.class)
    private String role;

    @NotNull(message = "status must be not null")
    @EnumValue(name = "status", enumClass = UserStatus.class)
    private String status;
}
