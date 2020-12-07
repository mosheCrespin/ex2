package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.SplittableRandom;

public class myGame {

    public static void main(String[] args) throws FileNotFoundException {
        game_service game = Game_Server_Ex2.getServer(16);
        game.startGame();

//        System.out.println(game.timeToEnd());
//        System.out.println(game.isRunning());
        System.out.println(game.toString());
        System.out.println(game.getAgents());
        System.out.println(game.getPokemons());

//        System.out.println(game.getAgents());
//        while (game.isRunning()) {
//            System.out.println(loadForGames.LoadGraphFromJson(game.getGraph()));
//            System.out.println(loadForGames.LoadPocimonsFromJson(game.getPokemons()));
//            System.out.println(loadForGames.LoadAgentstsFromJson(game.getAgents()));
//            game.addAgent(0);
//            game.addAgent(2);
//            System.out.println(game.getAgents());
//        }


    }

    }







