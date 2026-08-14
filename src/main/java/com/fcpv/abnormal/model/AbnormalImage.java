package com.fcpv.abnormal.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_abnormal_image")
@Getter
@Setter
public class AbnormalImage extends AbstractEntity<Long> {

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "abnormal_id", nullable = false)
    private Abnormal abnormal;
}