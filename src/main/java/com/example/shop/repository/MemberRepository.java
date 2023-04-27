package com.example.shop.repository;

import com.example.shop.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Optional<Member> findById(Long memberId) {
        return Optional.ofNullable(em.find(Member.class, memberId));
    }

    public Optional<Member> findByEmail(String email) {
        String jpql = "select m from Member m where m.email = :email";
        List<Member> findMember = em.createQuery(jpql, Member.class)
                .setParameter("email", email)
                .getResultList();
        return findMember.isEmpty() ? Optional.empty() : Optional.of(findMember.get(0));
    }
}
