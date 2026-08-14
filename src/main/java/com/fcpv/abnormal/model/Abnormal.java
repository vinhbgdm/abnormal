package com.fcpv.abnormal.model;

import com.fcpv.abnormal.enums.AbnormalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_abnormal")
public class Abnormal extends AbstractEntity<Long> {

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AbnormalStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "abnormal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AbnormalImage> images = new ArrayList<>();

}
