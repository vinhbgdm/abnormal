package com.fcpv.abnormal.dto.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbnormalResponseDto implements Serializable {

    private Long id;

    private String title;

    private String description;

    private String status;

    private List<AbnormalImageResponseDto> imageUrls;

    private Long userId;

    private String createdBy;

    private String lastModifiedBy;
}