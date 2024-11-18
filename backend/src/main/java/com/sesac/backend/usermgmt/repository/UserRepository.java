package com.sesac.backend.usermgmt.repository;


import com.sesac.backend.usermgmt.domain.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserData, String> {
    UserData findByUserNumber(String userNumber);
    UserData findByEmail(String email);
    Boolean existsByUserNumber(String userNumber);
}