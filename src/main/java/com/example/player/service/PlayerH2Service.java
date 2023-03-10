/*
 * You can use the following import statements
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 * 
 */

// Write your code here
package com.example.player.service;
import com.example.player.repository.PlayerRepository;
import com.example.player.model.Player;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import com.example.player.model.PlayerRowMapper;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;
import org.springframework.http.HttpStatus;

@Service
public class PlayerH2Service implements PlayerRepository{
   @Autowired
   private JdbcTemplate db;
   
   @Override
   public ArrayList<Player> getPlayers(){
      List<Player> playerList=db.query("select * from team",new PlayerRowMapper());
      ArrayList<Player> players=new ArrayList<>(playerList);
      return players;
    
   }

   @Override
    public Player getPlayerById(int playerId) {
        try{
        Player player=db.queryForObject("SELECT * FROM team WHERE id=?",new PlayerRowMapper(),playerId);
        return player;
        }
        catch( Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }


    }

    @Override
    public Player addPlayer(Player player) {
        db.update("INSERT INTO team(playerName,jerseyNumber,role) VALUES(?,?,?)",
        player.getPlayerName(),player.getJerseyNumber(),player.getRole());
        Player savedPlayer=db.queryForObject("SELECT * FROM team where playerName=? and jerseyNumber=? and role=?",
        new PlayerRowMapper(),player.getPlayerName(),player.getJerseyNumber(),player.getRole());
        return savedPlayer;

    }

    @Override
    public Player updatePlayer(int playerId, Player player) {

        if (player.getPlayerName() != null) {
            db.update("UPDATE team SET name=? WHERE id=?",
            player.getPlayerName(),playerId);
        }
        if (player.getJerseyNumber() != 0) {
            db.update("UPDATE team SET jerseyNumber=? WHERE id=?",
            player.getJerseyNumber(),playerId);
        }
        if (player.getRole() != null) {
            db.update("UPDATE team SET role=? WHERE id=?",
            player.getRole(),playerId);
        }

        return getPlayerById(playerId);
        
         
    }

     @Override
    public void deletePlayer(int playerId) {
       db.update("DELETE from team WHERE id=?",playerId);
       
    }
    
}