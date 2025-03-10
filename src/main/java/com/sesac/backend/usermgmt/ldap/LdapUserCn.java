package com.sesac.backend.usermgmt.ldap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entry(base = "ou=Users", objectClasses = {"inetOrgPerson"})
public final class LdapUserCn {

    @Id // LDAP는 dn을 이용해 객체를 식별한다 -> RDB의 PK와 같은 역할
    @JsonIgnore // JSON 직렬화 제외 -> 출력되는 데이터에서 제외함
    private Name dn;

    @Attribute(name = "uid")
    @NotNull(message = "UID는 필수 입력 값입니다.")
    @NotBlank(message = "UID는 공백일 수 없습니다.")
    private String uid;

    @Attribute(name = "cn")
    @NotNull(message = "이름은 필수 입력 값입니다.")
    @NotBlank(message = "이름은 공백일 수 없습니다.")
    private String fullName;

    @Attribute(name = "sn")
    @NotNull(message = "성은 필수 입력 값입니다.")
    @NotBlank(message = "성은 공백일 수 없습니다.")
    private String surname;

    @Attribute(name = "mail")
    private String email;

    @Attribute(name = "userPassword")
    @JsonIgnore // JSON 직렬화/역직렬화에서 제외
    private String password;

    @Attribute(name = "description")
    private String description; // JSON 형태로 저장된 부서 정보

    // Getters and Setters
}