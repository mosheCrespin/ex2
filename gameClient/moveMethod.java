package gameClient;

import api.game_service;

public class moveMethod implements Runnable {
    private Arena arena;
    private game_service game;
    private long avgSpeed;
    public moveMethod(Arena arena, game_service game){this.arena=arena; this.game=game;this.avgSpeed=1;}
    @Override
    public void run() {

        while (game.isRunning()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(game.move());
            setAvgSpeed();
        }
        System.out.println(game.toString());
    }
    private void setAvgSpeed(){
        double avg=0;
        for (CL_Agent agent: arena.getAgents())
            avg+=agent.getSpeed();
        this.avgSpeed=(long)avg/ arena.getNumberOfAgents();
    }
}
