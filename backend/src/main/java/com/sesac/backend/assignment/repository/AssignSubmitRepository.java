package com.sesac.backend.assignment.repository;

import com.sesac.backend.assignment.domain.AssignSubmit;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignSubmitRepository extends JpaRepository<AssignSubmit, UUID> {

}
