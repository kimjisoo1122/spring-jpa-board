package com.example.shop.repository;

import com.example.shop.dto.ReplyDTO;
import com.example.shop.entity.Reply;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReplyRepository {

    private final EntityManager em;

    public void save(Reply reply) {
        em.persist(reply);
    }

    public List<ReplyDTO> findByBoardId(Long boardId) {
        String jpql =
                "select " +
                "   new com.example.shop.dto.ReplyDTO(" +
                        "r.id, r.content, b.id, m.id, m.name, r.createDate, r.updateDate)" +
                "from Reply r " +
                "join r.member m " +
                "join r.board b " +
                "where b.id = :boardId " +
                "order by m.createDate desc";
        return em.createQuery(jpql, ReplyDTO.class)
                .setParameter("boardId", boardId)
                .getResultList();
    }
}
