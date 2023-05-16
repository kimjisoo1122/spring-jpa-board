package com.example.board.repository.reply;

import com.example.board.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findByBoardIdOrderByCreateDateAsc(Long boardId);
}