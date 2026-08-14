package com.fcpv.abnormal.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AbnormalImageResponseDto {
    private Long id;
    private String imageUrl;
}
