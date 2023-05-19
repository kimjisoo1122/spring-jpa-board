package com.example.board.repository.board;

import com.example.board.dto.board.BoardSearchCondition;
import com.example.board.entity.Board;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.board.entity.QBoard.board;
import static com.example.board.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Board> search(BoardSearchCondition condition, Pageable pageable) {
        long offset = (long) (pageable.getPageNumber() - 1) * pageable.getPageSize();
        List<Board> content = queryFactory
                .select(board)
                .from(board)
                .leftJoin(board.member, member)
                .where(
                        titleLike(condition.getTitle()),
                        contentLike(condition.getContent()),
                        writerLike(condition.getWriter()))
                .orderBy(board.id.desc())
                .offset(offset)
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(board.count())
                .from(board)
                .leftJoin(board.member, member)
                .where(
                        titleLike(condition.getTitle()),
                        contentLike(condition.getContent()),
                        writerLike(condition.getWriter()));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
    private BooleanExpression titleLike(String title) {
        return StringUtils.hasText(title) ? board.title.like(generateLikePattern(title)) : null;
    }

    private BooleanExpression contentLike(String content) {
        return StringUtils.hasText(content) ? board.content.like(generateLikePattern(content)) : null;
    }

    private BooleanExpression writerLike(String writer) {
        return StringUtils.hasText(writer) ? board.member.name.like(generateLikePattern(writer)) : null;
    }

    private String generateLikePattern(String text) {
        return "%" + text + "%";
    }
}
