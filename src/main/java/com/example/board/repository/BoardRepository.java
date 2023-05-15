package com.example.board.repository;

import com.example.board.dto.BoardDTO;
import com.example.board.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardRepository {

    private final EntityManager em;

    public void save(Board board) {
        em.persist(board);
    }

    public Optional<Board> findById(Long boardId) {
        return Optional.ofNullable(em.find(Board.class, boardId));
    }

    public List<BoardDTO> findBoardList(int offset, int limit) {
        String jpql =
                "select new com.example.board.dto.BoardDTO(" +
                        "b.id, m.id, m.name, b.createDate, b.title, b.content," +
                        " c.name, b.viewCnt, b.recommendCnt) " +
                "from Board b " +
                "join b.member m " +
                "join b.category c " +
                "order by b.id desc";
        return em.createQuery(jpql, BoardDTO.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

}
