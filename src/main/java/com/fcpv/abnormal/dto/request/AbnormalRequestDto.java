package com.fcpv.abnormal.dto.request;

import com.fcpv.abnormal.enums.AbnormalPriority;
import com.fcpv.abnormal.enums.AbnormalStatus;
import com.fcpv.abnormal.validator.EnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AbnormalRequestDto implements Serializable {

    @NotBlank(message = "abnormalNo must be not blank")
    private String abnormalNo;

    @NotBlank(message = "title must be not blank")
    private String title;

    @NotBlank(message = "description must be not blank")
    private String description;

    @NotBlank(message = "category must be not blank")
    private String category;

    @NotNull(message = "status must be not null")
    @EnumValue(name = "status", enumClass = AbnormalStatus.class)
    private String status;

    @NotNull(message = "priority must be not null")
    @EnumValue(name = "priority", enumClass = AbnormalPriority.class)
    private String priority;

    @NotBlank(message = "location must be not blank")
    private String location;

    @NotNull(message = "dueDate must be not null")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dueDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime closeTime;

//    @NotNull(message = "image must be not blank")
    private List<MultipartFile> images;

}
