package com.sesac.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class})
// 엔티티의 생성 및 수정 시간을 자동으로 관리하기 위한 리스너 클래스
@MappedSuperclass
//MappedSuperclass로 지정된 클래스는 테이블을 생성하지 않고, 해당 클래스를 상속받는 엔티티 클래스들이 매핑 정보를 상속받게 됩니다.
// 부모 클래스를 상속받는 자식 클래스한테 매핑정보를 제공하기 위해
// 이를 통해 코드 재사용성을 높일 수 있고, 매핑 정보를 한 곳에서 관리
@Getter
@Setter
public abstract class BaseTimeEntity {

    @CreatedDate //엔티티가 생성되서 저장될때 시간을 자동으로 저장한다
    @Column(updatable = false) //컬럼의 값을 수정하지 못하게 막음
    private LocalDateTime regDate; //등록날짜

    @LastModifiedDate //수정될때 시간을 자동으로 저장한다
    private LocalDateTime updateDate; //수정날짜


}
