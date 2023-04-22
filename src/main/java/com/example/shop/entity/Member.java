package com.example.shop.entity;

import com.example.shop.dto.MemberDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
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
        Address address = new Address(memberDTO.getCity(), memberDTO.getStreet(), memberDTO.getZipcode());
        member.setAddress(address);
        member.setName(memberDTO.getName());
        member.setEmail(memberDTO.getEmail());
        member.setPhone(memberDTO.getPhone());
        member.setPassword(memberDTO.getPassword());
        member.setUserRole(UserRole.USER);
        return member;
    }
}
