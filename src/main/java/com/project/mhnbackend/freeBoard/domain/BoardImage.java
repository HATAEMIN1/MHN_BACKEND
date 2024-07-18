package com.project.mhnbackend.freeBoard.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class BoardImage {
    private String fileName;

    private int ord;

    public void changeOrd(int ord) {
    	this.ord = ord;
    }
}
