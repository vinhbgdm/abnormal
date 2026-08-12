package com.fcpv.abnormal.controller;

import com.fcpv.abnormal.dto.request.AbnormalRequestDto;
import com.fcpv.abnormal.dto.response.AbnormalResponseDto;
import com.fcpv.abnormal.dto.response.ResponseData;
import com.fcpv.abnormal.dto.response.ResponseError;
import com.fcpv.abnormal.service.AbnormalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/abnormal")
@Slf4j(topic = "Abnormal-CONTROLLER")
@Tag(name = "Abnormal Controller")
@RequiredArgsConstructor
public class AbnormalController {

    private final AbnormalService abnormalService;

    private static final String ERROR_MESSAGE = "errorMessage = {}";

    @Operation(summary = "Add new abnormal", description = "Send a request via this API to create new abnormal")
    @PostMapping("/")
    public ResponseData<Long> addAbnormal(@Valid @ModelAttribute AbnormalRequestDto abnormal) {
        log.info("Request add abnormal {} {}", abnormal.getTitle(), abnormal.getStatus());
        try {
            long abnormalId = abnormalService.saveAbnormal(abnormal);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Add abnormal success", abnormalId);
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Add abnormal fail");
        }
    }

    @Operation(summary = "Update abnormal", description = "Send a request via this API to update abnormal")
    @PutMapping("/{abnormalId}")
    public ResponseData<Void> updateAbnormal(@PathVariable @Min(1) int abnormalId, @Valid @ModelAttribute AbnormalRequestDto abnormal) {
        log.info("Request update abnormalId={}", abnormalId);
        try {
            abnormalService.updateAbnormal(abnormalId, abnormal);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Update abnormal success");
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update abnormal fail");
        }
    }

    @Operation(summary = "Delete abnormal permanently", description = "Send a request via this API to delete abnormal permanently")
    @DeleteMapping("/{abnormalId}")
    public ResponseData<Void> deleteAbnormal(@PathVariable @Min(value = 1, message = "abnormalId must be greater than 0") int abnormalId) {
        log.info("Request delete abnormalId={}", abnormalId);
        try {
            abnormalService.deleteAbnormal(abnormalId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Delete abnormal success");
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete abnormal fail");
        }
    }

    @Operation(summary = "Get abnormal detail", description = "Send a request via this API to get abnormal information")
    @GetMapping("/{abnormalId}")
    public ResponseData<AbnormalResponseDto> getAbnormal(@PathVariable @Min(1) int abnormalId) {
        log.info("Request get abnormal detail, abnormalId={}", abnormalId);
        try {
            AbnormalResponseDto abnormal = abnormalService.getAbnormal(abnormalId);
            return new ResponseData<>(HttpStatus.OK.value(), "abnormal", abnormal);
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Get all abnormal", description = "Send a request via this API to get all abnormal")
    @GetMapping("/list")
    public ResponseData<List<AbnormalResponseDto>> getAllAbnormal() {
        log.info("Request get all abnormal");
        try {
            List<AbnormalResponseDto> abnormalAll = abnormalService.getAllAbnormal();
            return new ResponseData<>(HttpStatus.OK.value(), "abnormalAll", abnormalAll);
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
