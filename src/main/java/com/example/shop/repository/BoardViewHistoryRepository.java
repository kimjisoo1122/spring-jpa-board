package com.example.shop.repository;

import com.example.shop.entity.BoardViewHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardViewHistoryRepository {

    private final EntityManager em;

    public void save(BoardViewHistory boardViewHistory) {
        em.persist(boardViewHistory);
    }

    public Optional<BoardViewHistory> findByMemberIdAndBoardId(Long memberId, Long boardId) {
        String jpql =
                "select bw " +
                "from BoardViewHistory bw " +
                "join bw.member m " +
                "join bw.board b " +
                "where m.id = :memberId " +
                "and b.id = :boardId";
        List<BoardViewHistory> boardViewHistories = em.createQuery(jpql, BoardViewHistory.class)
                .setParameter("memberId", memberId)
                .setParameter("boardId", boardId)
                .getResultList();
        return boardViewHistories.isEmpty() ? Optional.empty() : Optional.of(boardViewHistories.get(0));
    }
}
