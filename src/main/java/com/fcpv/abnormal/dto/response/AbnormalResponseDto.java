package com.fcpv.abnormal.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbnormalResponseDto implements Serializable {

    private Long id;

    private String abnormalNo;

    private String title;

    private String description;

    private String category;

    private String status;

    private String priority;

    private String location;

    private LocalDateTime dueDate;

    private LocalDateTime closeTime;

    private List<AbnormalImageResponseDto> imageUrls;

    private Long userId;

    private String createdBy;

    private String lastModifiedBy;
}