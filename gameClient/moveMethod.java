package gameClient;

import api.game_service;
import com.google.gson.JsonObject;

public class moveMethod implements Runnable {
    private Arena arena;
    private game_service game;
    private long avgSpeed;
    public moveMethod(Arena arena, game_service game){this.arena=arena; this.game=game;this.avgSpeed=1;}
    @Override
    public void run() {

        while (game.isRunning()) {
            try {
                Thread.sleep(100);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            game.move();
            setAvgSpeed();
        }
    }
    private void setAvgSpeed(){
        double avg=0;
        for (CL_Agent agent: arena.getAgents())
            avg+=agent.getSpeed();
        this.avgSpeed=(long)avg/ arena.getNumberOfAgents();
    }
}
