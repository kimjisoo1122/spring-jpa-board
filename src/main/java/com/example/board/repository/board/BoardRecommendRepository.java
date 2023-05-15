package com.example.board.repository.board;

import com.example.board.entity.BoardRecommendHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardRecommendRepository {

    private final EntityManager em;

    public void save(BoardRecommendHistory boardRecommendHistory) {
        em.persist(boardRecommendHistory);
    }

    public Optional<BoardRecommendHistory> findByMemberIdAndBoardId(Long memberId, Long boardId) {
        String jpql =
                "select br " +
                "from BoardRecommendHistory br " +
                "join br.member m " +
                "join br.board b " +
                "where m.id = :memberId " +
                "and b.id = :boardId";
        List<BoardRecommendHistory> boardRecommendHistories = em.createQuery(jpql, BoardRecommendHistory.class)
                .setParameter("memberId", memberId)
                .setParameter("boardId", boardId)
                .getResultList();
        return boardRecommendHistories.isEmpty() ? Optional.empty() : Optional.of(boardRecommendHistories.get(0));
    }
}
