package com.example.shop.entity;

import com.example.shop.dto.MemberDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(indexes = @Index(name = "idx_email", columnList = "email"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;
    private String password;
    @Column(unique = true)
    private String email;
    private String phone;

    @Embedded
    private Address address;

    @Column(columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP",
            insertable = false, updatable = false)
    private LocalDateTime joinedDate;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public static Member createMember(MemberDTO memberDTO) {

        Member member = new Member();
        member.address = new Address(memberDTO.getCity(), memberDTO.getStreet(), memberDTO.getZipcode());
        member.name = memberDTO.getName();
        member.email = memberDTO.getEmail();
        member.phone = memberDTO.getPhone();
        member.password = memberDTO.getPassword();
        member.userRole = UserRole.ROLE_USER;
        return member;
    }
}
