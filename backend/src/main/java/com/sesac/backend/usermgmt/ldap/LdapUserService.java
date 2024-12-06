package com.sesac.backend.usermgmt.ldap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LdapUserService {
    // LdapRepository로부터 DAO를 주입 받음
    @Autowired
    private LdapRepository ldapRepository;

    // 4. 컨트롤러로부터 LDAP 조회를 제공할 서비스 클래스
    public LdapUserCn findUserByUid(String uid) {
        return ldapRepository.findUserByUid(uid);
    }

    public List<String> getAllUserIds(){
        return ldapRepository.getAllUserIds();
    }

    public List<String> getAllUserIdsByGroup(String groupPrefix){
        return ldapRepository.getAllUserIdsByGroup(groupPrefix);
    }
}