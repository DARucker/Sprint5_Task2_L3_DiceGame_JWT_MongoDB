package com.sprint5.task2.fase3.mongo.service;

import com.sprint5.task2.fase3.mongo.dto.Ranking;
import com.sprint5.task2.fase3.mongo.dto.Userdto;
import com.sprint5.task2.fase3.mongo.entity.User;

import java.util.List;

public interface IUserService {

    void deleteGamesByUserId(String id);
    Userdto findById(String id);
    List<Ranking> listAllRanking();
    int rankingAvg();
    Ranking worstUser();
    Ranking bestUser();
    Userdto playGame(String id);
    Userdto entityToDto(User user);
    User dtoToEntity(Userdto userdto);
}
