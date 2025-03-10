package com.sesac.backend.usermgmt.ldap;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class LdapUserController {
    // UserService로부터 서비스 메소드를 호출하기 위한 객체를 주입받음
    private final LdapUserService ldapUserService;

    // 5. 사용자 조회 및 검색을 위한 엔드포인트 제공

    // 특정 UID의 사용자 검색
    @GetMapping("/{uid}")
    public ResponseEntity<LdapUserCn> getUserByUid(@PathVariable String uid) {
        LdapUserCn user = ldapUserService.findUserByUid(uid);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    // 모든 사용자 UID 검색
    @GetMapping("/all-ids")
    public ResponseEntity<List<String>> getAllUserIds() {
        return ResponseEntity.ok(ldapUserService.getAllUserIds());
    }

    // 학생 그룹의 사용자 UID 검색
    @GetMapping("/group/students")
    public ResponseEntity<List<String>> getUserIdsByGroup() {
        try {
            List<String> userIds = ldapUserService.getAllUserIdsByGroup("S");
            return ResponseEntity.ok(userIds);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(List.of(e.getMessage()));
        }
    }

    // 특정 그룹의 사용자 UID 검색
    @GetMapping("/group/professor")
    public ResponseEntity<List<String>> getProfesorIdsByGroup() {
        try {
            List<String> userIds = ldapUserService.getAllUserIdsByGroup("S");
            return ResponseEntity.ok(userIds);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(List.of(e.getMessage()));
        }
    }

    // 특정 그룹의 사용자 UID 검색
    @GetMapping("/group/staff")
    public ResponseEntity<List<String>> getStaffIdsByGroup() {
        try {
            List<String> userIds = ldapUserService.getAllUserIdsByGroup("S");
            return ResponseEntity.ok(userIds);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(List.of(e.getMessage()));
        }
    }
}