package com.fcpv.abnormal.repository;

import com.fcpv.abnormal.model.Abnormal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AbnormalRepository extends JpaRepository<Abnormal, Long>, JpaSpecificationExecutor<Abnormal> {

}
