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
        game_service game = Game_Server_Ex2.getServer(20);
            System.out.println(loadForGames.loadJsonFromString(game.getGraph()));
        System.out.println(game.getPokemons());
        game.addAgent(0);
        game.addAgent(2);
        System.out.println(game.getAgents());


    }

    }







