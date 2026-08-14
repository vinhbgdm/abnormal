package com.fcpv.abnormal.service;

import com.fcpv.abnormal.dto.request.AbnormalRequestDto;
import com.fcpv.abnormal.dto.request.AbnormalUpdateRequestDto;
import com.fcpv.abnormal.dto.response.AbnormalResponseDto;

import java.util.List;

public interface AbnormalService {
    long saveAbnormal(AbnormalRequestDto request);

    void updateAbnormal(long abnormalId, AbnormalUpdateRequestDto request);

    void deleteAbnormal(long abnormalId);

    AbnormalResponseDto getAbnormal(long abnormalId);

    List<AbnormalResponseDto> getAllAbnormal();
}
