package com.sprint5.task2.fase3.mongo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Game {
    @Id
    private String id;
    private int points;
    private Result result;
}
