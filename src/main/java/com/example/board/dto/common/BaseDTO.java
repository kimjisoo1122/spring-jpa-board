package com.example.board.dto.common;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
public abstract class BaseDTO  {
    public LocalDateTime createDate;
    public LocalDateTime updateDate;
}
