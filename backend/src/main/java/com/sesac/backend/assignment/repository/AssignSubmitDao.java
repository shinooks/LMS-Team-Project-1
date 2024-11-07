package com.sesac.backend.assignment.repository;

import com.sesac.backend.assignment.domain.AssignSubmit;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * @author dongjin
 * 과제 제출 Repository
 * AssignSubmit 테이블 연결
 */
@Repository
public interface AssignSubmitDao extends JpaRepository<AssignSubmit, UUID> {

}
