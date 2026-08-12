package com.fcpv.abnormal.service.impl;

import com.fcpv.abnormal.dto.request.AbnormalRequestDto;
import com.fcpv.abnormal.dto.response.AbnormalResponseDto;
import com.fcpv.abnormal.enums.AbnormalStatus;
import com.fcpv.abnormal.exception.ResourceNotFoundException;
import com.fcpv.abnormal.model.Abnormal;
import com.fcpv.abnormal.model.User;
import com.fcpv.abnormal.repository.AbnormalRepository;
import com.fcpv.abnormal.repository.UserRepository;
import com.fcpv.abnormal.service.AbnormalService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AbnormalServiceImpl implements AbnormalService {

    private final AbnormalRepository abnormalRepository;
    private final UserRepository userRepository;

    private static final String UPLOAD_DIR = "uploads/abnormal/";

    @Override
    public long saveAbnormal(AbnormalRequestDto request) {

        Abnormal abnormal = new Abnormal();
        abnormal.setTitle(request.getTitle());
        abnormal.setDescription(request.getDescription());
        abnormal.setStatus(AbnormalStatus.valueOf(request.getStatus()));

        // Lưu ảnh
        String imageUrl = saveImage(request.getImage());
        abnormal.setImageUrl(imageUrl);

        // Lấy user hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userCode = authentication.getName();
        User user = userRepository.findByUserCode(userCode);
        abnormal.setUser(user);

        abnormalRepository.save(abnormal);
        log.info("Abnormal has added successfully, userId={}", abnormal.getId());
        return abnormal.getId();
    }

    @Override
    public void updateAbnormal(long abnormalId, AbnormalRequestDto request) {

        Abnormal abnormal = abnormalRepository.findById(abnormalId).orElseThrow(() -> new ResourceNotFoundException("Abnormal not found"));

        abnormal.setTitle(request.getTitle());
        abnormal.setDescription(request.getDescription());
        abnormal.setStatus(AbnormalStatus.valueOf(request.getStatus()));

        if (request.getImage() != null
                && !request.getImage().isEmpty()) {

            // Kiểm tra ảnh mới có giống ảnh cũ không
            if (!isSameImage(abnormal.getImageUrl(), request.getImage())) {

                String oldImageUrl = abnormal.getImageUrl();

                String newImageUrl = saveImage(request.getImage());

                abnormal.setImageUrl(newImageUrl);

                // Xóa ảnh cũ
                deleteImage(oldImageUrl);
            }
        }

        abnormalRepository.save(abnormal);
        log.info("Abnormal has updated successfully, userId={}", abnormal.getId());
    }

    @Override
    public void deleteAbnormal(long abnormalId) {

        Abnormal abnormal = abnormalRepository.findById(abnormalId).orElseThrow(() -> new ResourceNotFoundException("Abnormal not found"));

        abnormalRepository.delete(abnormal);

        log.info("Delete abnormal successfully, abnormalId={}", abnormalId);
    }

    @Override
    public AbnormalResponseDto getAbnormal(long abnormalId) {

        log.info("Get abnormal: {}", abnormalId);

        Abnormal abnormal = abnormalRepository.findById(abnormalId).orElseThrow(() -> new ResourceNotFoundException("Abnormal not found"));

        return mapToResponse(abnormal);
    }

    @Override
    public List<AbnormalResponseDto> getAllAbnormal() {

        log.info("Get all abnormal");

        return abnormalRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private AbnormalResponseDto mapToResponse(
            Abnormal abnormal) {

        return AbnormalResponseDto.builder()
                .id(abnormal.getId())
                .title(abnormal.getTitle())
                .description(abnormal.getDescription())
                .status(abnormal.getStatus().name())
                .imageUrl(abnormal.getImageUrl())
                .userId(abnormal.getUser() != null ? abnormal.getUser().getId() : null)
                .build();
    }

    private String saveImage(@NotNull MultipartFile image) {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFileName = image.getOriginalFilename();

            String extension = "";

            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(
                        originalFileName.lastIndexOf(".")
                );
            }

            String fileName = UUID.randomUUID() + extension;

            Path filePath = uploadPath.resolve(fileName);

            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/abnormal/" + fileName;

        } catch (IOException e) {
            log.error("Failed to save abnormal image: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save image");
        }
    }

    private boolean isSameImage(String oldImageUrl, MultipartFile newImage) {

        if (oldImageUrl == null) { return false; }

        try {
            Path oldImagePath = Paths.get("." + oldImageUrl);
            if (!Files.exists(oldImagePath)) { return false; }

            byte[] oldImageBytes = Files.readAllBytes(oldImagePath);
            byte[] newImageBytes = newImage.getBytes();

            return Arrays.equals(oldImageBytes, newImageBytes);
        } catch (IOException e) {
            log.error("Cannot compare images: {}", e.getMessage());
            return false;
        }
    }

    private void deleteImage(String imageUrl) {

        if (imageUrl == null) { return; }

        try {
            Path path = Paths.get("." + imageUrl);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.error("Cannot delete old image: {}", e.getMessage());
        }
    }
}
