package com.maxiaseo.accounting.adapters.driven.jpa.mysql.repository;

import com.maxiaseo.accounting.adapters.driven.jpa.mysql.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    void deleteByName(String name);
}
