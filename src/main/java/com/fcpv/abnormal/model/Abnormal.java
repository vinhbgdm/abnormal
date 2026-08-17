package com.fcpv.abnormal.model;

import com.fcpv.abnormal.enums.AbnormalPriority;
import com.fcpv.abnormal.enums.AbnormalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

    @Column(name = "abnormal_no")
    private String abnormalNo;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AbnormalStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private AbnormalPriority priority;

    @Column(name = "location")
    private String location;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "close_time")
    private LocalDateTime closeTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "abnormal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AbnormalImage> images = new ArrayList<>();

}
