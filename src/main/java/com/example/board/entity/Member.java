package com.example.board.entity;

import com.example.board.dto.MemberDTO;
import com.example.board.entity.common.Address;
import com.example.board.entity.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(indexes = @Index(name = "idx_email", columnList = "email"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    private String name;
    private String password;
    private String phone;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String email;

    @Embedded
    private Address address;

//    @Column(columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP",
//            insertable = false, updatable = false)
//    private LocalDateTime joinedDate;

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

    public void setMemberAuditor(Member member) {
        this.updateUser = member.getId().toString();
        this.createUser = member.getId().toString();
        this.createDate = member.getUpdateDate();
    }
}
