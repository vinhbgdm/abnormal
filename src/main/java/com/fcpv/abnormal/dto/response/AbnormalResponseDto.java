package com.fcpv.abnormal.dto.response;

import lombok.*;

import java.io.Serializable;

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

    private String imageUrl;

    private Long userId;

    private String createdBy;

    private String lastModifiedBy;
}