package com.sesac.backend.usermgmt.ldap;

import lombok.RequiredArgsConstructor;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Repository
@RequiredArgsConstructor
public class LdapRepository { // 3. LDAP 데이터를 처리하기 위한 레포지터리 클래스

    private final LdapTemplate ldapTemplate;

    // 특정 UID로 사용자 찾기
    public LdapUserCn findUserByUid(String uid) {
        String baseDn = getBaseDnByUid(uid);
        LdapUserCn temp = ldapTemplate.findOne(
                query().base(baseDn)
                        .where("uid").is(uid), // uid 속성이 입력된 uid인 사용자 찾기
                LdapUserCn.class
        );
        System.out.println(temp);
        return temp;
    }

    // 모든 사용자의 UID 검색
    public List<String> getAllUserIds() {
        return searchByBaseDn(null);
    }

    // 특정 그룹의 UID 검색
    // groupPrefix로는 S, P, E만 입력 받아야 함.
    public List<String> getAllUserIdsByGroup(String groupPrefix) {
        String baseDn = getBaseDnByGroup(groupPrefix);
        return searchByBaseDn(baseDn);
    }

    // UID 접두사를 기준으로 Base DN 반환
    private String getBaseDnByUid(String uid) {
        String prefix = uid.substring(0, 1);
        try {
            LdapGroupPrefix ldapGroupPrefix = LdapGroupPrefix.valueOf(prefix);
            return ldapGroupPrefix.getBaseDn();
        }catch(IllegalArgumentException iae) {
            System.err.println("잘못된 UID가 입력되었습니다: "+iae.getMessage());
            throw iae;
        }
    }

    // 그룹 접두사를 기준으로 Base DN 반환
    private String getBaseDnByGroup(String prefix) {
        try {
            LdapGroupPrefix ldapGroupPrefix = LdapGroupPrefix.valueOf(prefix);
            return ldapGroupPrefix.getBaseDn();
        }catch(IllegalArgumentException iae) {
            System.err.println("잘못된 UID가 입력되었습니다: "+iae.getMessage());
            throw iae;
        }
    }

    // Base DN을 기준으로 사용자 검색
    private List<String> searchByBaseDn(String baseDn) {
        return ldapTemplate.search(
                baseDn == null ?
                        query().base("ou=사용자").where("objectClass").is("inetOrgPerson") : // Users 하위에서 모두 찾기
                        query().base(baseDn).where("objectClass").is("inetOrgPerson"), // 기준 BaseDn에서 모두 찾기,
                (AttributesMapper<String>) attrs -> (String) attrs.get("uid").get()
        );
    }


}