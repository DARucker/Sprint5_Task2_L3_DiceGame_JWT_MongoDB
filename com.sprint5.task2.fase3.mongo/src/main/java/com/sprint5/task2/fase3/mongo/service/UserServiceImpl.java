package com.sprint5.task2.fase3.mongo.service;

import com.sprint5.task2.fase3.mongo.dto.Userdto;
import com.sprint5.task2.fase3.mongo.entity.Game;
import com.sprint5.task2.fase3.mongo.entity.Result;
import com.sprint5.task2.fase3.mongo.entity.User;
import com.sprint5.task2.fase3.mongo.dto.Ranking;
import com.sprint5.task2.fase3.mongo.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements  IUserService{
    
    @Autowired
    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
/*
    @Override
    public UserToSave create (UserToSave UserToSave){

        if(!UserToSave.getName().equals("")) {
            Optional<User> UserDb = UserRepoMongo.findByName(UserToSave.getName());
            if (UserDb.isPresent()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Te User with name " + UserToSave.getName() + " exists.");
            }
        }
        User UserForDb = new User();
        List<User> lista = UserRepoMongo.findAll();
        OptionalInt ultimoId = lista.stream().mapToInt(x -> Integer.valueOf(x.getId())).max();
        if(ultimoId.isPresent()){
            int ultimoIdInt = ultimoId.getAsInt();
            int nuevoId = ultimoIdInt + 1;
            String id = Integer.toString(nuevoId);
            UserForDb.setId(id);
        }else {
            UserForDb.setId("1");
        }
        UserForDb.setCreated(LocalDateTime.now());
        UserForDb.setName(UserToSave.getName());
        List<Game> gameList = new ArrayList<>();
        UserForDb.setGames(gameList);

        User saved = UserRepoMongo.save(UserForDb);

        return entityToUserToSave(saved);
    }
*/
/*
    @Override
    public UserToSave update (UserToSave UserToSave){
        log.info("update User: " + UserToSave);
        Optional<User> UserDb = UserRepoMongo.findById(UserToSave.getId());
        if (!UserDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Te User with id " + UserToSave.getId() + " does not exists.");
        }
        User UserUpdate = UserDb.get();
        UserUpdate.setName(UserToSave.getName());
        User updated = UserRepoMongo.save(UserUpdate);
        return entityToUserToSave(updated);
    }
*/
    /*
     * DELETE /Users/{id}/games: elimina las tiradas del jugador/a.
     */

    @Override
    public void deleteGamesByUserId(String id){
        log.info("update user: " + id);
        Optional<User> UserDb = userRepository.findById(id);
        if (!UserDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Te User with id " + id + " does not exists.");
        }
        User UserUpdate = UserDb.get();
        List<Game> gameList = new ArrayList<>();
        UserUpdate.setGames(gameList);
        User updated = userRepository.save(UserUpdate);
    }

    @Override
    public Userdto findById(String id){
        log.info("Find by Id: " + id);
        Optional<User> UserDb = userRepository.findById(id);
        if (!UserDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Te User with id " + id + " does not exists.");
        }
        return entityToDto(UserDb.get());
    }

    /**
     * **  GET /Users/: devuelve el listado de todos los jugadores/as
     * del sistema con su porcentaje medio de éxitos.
     */

    @Override
    public List<Ranking> listAllRanking(){
        List<Ranking> allRanking = new ArrayList<>();
        List<User> userList = userRepository.findAll();
        for (User user : userList){
            String userId = user.getId();
            String firstname = user.getFirstname();
            if(firstname.equals("")){
                firstname="Anonymous";
            }
            List<Game> gamesUser = user.getGames();
            int win = (int) gamesUser.stream().filter(x -> x.getResult().equals(Result.WIN)).count();
            int played = gamesUser.size();
            double calcularRatio = 0;
            if(win>0){calcularRatio =  (double) win /played*100;}
            int ratio = (int) calcularRatio;
            Ranking ranking = new Ranking(userId, firstname, win, played, ratio);
            allRanking.add(ranking);
            log.info("ranking "+ ranking);
        }
        return allRanking;
    }

    /**
     * GET /Users/ranking: devuelve el ranking medio de todos los jugadores/as del sistema. Es decir, el porcentaje medio de logros.
     */
    @Override
    public int rankingAvg(){
        List<Ranking> rankings = listAllRanking();
        int gamesWon = rankings.stream().mapToInt(x -> x.getWin()).sum();
        int gamesPlayed  = rankings.stream().mapToInt(x -> x.getPlayed()).sum();
        double calcularRatio = 0;
        if(gamesWon>0){
            calcularRatio =  (double) gamesWon /gamesPlayed *100;
        }
        return (int) calcularRatio;
    }

    /**
     *     GET /Users/ranking/loser: devuelve al jugador/a con peor porcentaje de éxito.
     */
    @Override
    public Ranking worstUser(){
        List<Ranking> worstRankings = listAllRanking().stream()
                .filter(x -> x.getPlayed()>0)
                .sorted(Comparator.comparingInt(Ranking::getRatio))
                .limit(1)
                .collect(Collectors.toList());
        if(worstRankings.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No games were stored");
        }
        return worstRankings.get(0);
    }

    /**
     *     GET /Users/ranking/winer: devuelve al jugador/a con mejor porcentaje de éxito.
     */
    @Override
    public Ranking bestUser(){
        List<Ranking> bestRankings = listAllRanking()
                .stream()
                .sorted(Comparator.comparingInt(Ranking::getRatio).reversed())
                .limit(1)
                .collect(Collectors.toList());
        if(bestRankings.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No games were stored");
        }
        return bestRankings.get(0);
    }

    @Override
    public Userdto playGame(String id){

        Optional<User> UserDb = userRepository.findById(id);
        if (!UserDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Te User with id " + id + " does not exists.");
        }

        User UserUpdate = UserDb.get();
        List<Game> gameList = UserUpdate.getGames();

        if(gameList.isEmpty()){
            gameList = new ArrayList<>();
        }
        int dice1 = (int) (Math.random()*6);
        int dice2 = (int) (Math.random()*6);
        int points = dice1+dice2;
        Game game = new Game();
        if(points == 7){
            game.setResult(Result.WIN);
        }else {
            game.setResult(Result.LOOSE);
        }
        game.setPoints(points);
        gameList.add(game);
        UserUpdate.setGames(gameList);
        userRepository.save(UserUpdate);
        return entityToDto(UserUpdate);

    }






    /**
     * This method transform an entity into a DTO
     * @param User
     * @return Userdto
     */
    @Override
    public Userdto entityToDto(User User) {
        Userdto Userdto = modelMapper().map(User, Userdto.class);
        if(Userdto.getName().equals("")){
            Userdto.setName("Anonymous");
        }
        return Userdto;
    }
    /**
     * This method recives a DTO object to transform it into an entity
     * @param Userdto
     * @return User
     */
    @Override
    public User dtoToEntity(Userdto Userdto) {
        User user = modelMapper().map(Userdto, User.class);
        return user;
    }

    /**
     *
     * @param User
     * @return UserToSave
     */
    public User entityToUserToSave(User User) {
        User userToSave = modelMapper().map(User, User.class);
        return userToSave;
    }


}
