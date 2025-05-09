package com.horizonx.file_services.adapters.driven.jpa.mysql.entity;

import com.horizonx.file_services.domain.util.ConstantsDomain;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "excel-file")

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "upload_time", nullable = false)
    private LocalDateTime uploadTime;

    @Column(name = "uploaded_by")
    private String uploadedBy;

    @Column(name = "fortnight-date", nullable = false)
    private LocalDate fortNightDate;

     @Enumerated(EnumType.STRING)
    private ConstantsDomain.TimeFormat timeFormat;

    @Column(name = "form_id")
    private Integer formId;


}
