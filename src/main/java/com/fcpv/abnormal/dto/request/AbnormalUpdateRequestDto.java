package com.fcpv.abnormal.dto.request;

import com.fcpv.abnormal.enums.AbnormalStatus;
import com.fcpv.abnormal.validator.EnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class AbnormalUpdateRequestDto {

    @NotBlank(message = "title must be not blank")
    private String title;

    @NotBlank(message = "description must be not blank")
    private String description;

    @NotNull(message = "status must be not null")
    @EnumValue(
            name = "status",
            enumClass = AbnormalStatus.class
    )
    private String status;

    // ID của các ảnh cũ muốn giữ lại
    private List<Long> existingImageIds;

    private List<MultipartFile> newImages;
}