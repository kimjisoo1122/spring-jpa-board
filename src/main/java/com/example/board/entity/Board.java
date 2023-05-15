package com.example.board.entity;

import com.example.board.dto.board.BoardDto;
import com.example.board.entity.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString(exclude = {"member", "category", "replies"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "parent_id")
//    private Board parent;

    @OneToMany(mappedBy = "board")
    private List<Reply> replies = new ArrayList<>();

    private int boardLevel;
    private int viewCnt;
    private int recommendCnt;

    public static Board createBoard(BoardDto boardDTO) {
        Board board = new Board();
        board.title = boardDTO.getTitle();
        board.content = boardDTO.getContent();
        board.viewCnt = 0;
        board.recommendCnt = 0;
        return board;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void increaseViewCnt() {
        this.viewCnt++;
    }
    public void addRecommendation(int cnt) {
        this.recommendCnt += cnt;
    }

    public void removeRecommendation(int cnt) {
        this.recommendCnt -= cnt;
    }
}
