package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MyPanel extends JPanel {
    private Arena arena;
    private gameClient.util.Range2Range _w2f;
    private BufferedImage backGround;

    public MyPanel(Arena arena){
        super();
        this.arena=arena;
        try {
            this.backGround= ImageIO.read(new File("BackGround.jpg"));//TODO should add path
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void updateFrame() {
        Range rx = new Range(50,this.getWidth()-50);
        Range ry = new Range(this.getHeight()-50,50);
        Range2D frame = new Range2D(rx,ry);
        directed_weighted_graph g = arena.getGraph();
        _w2f = Arena.w2f(g,frame);
    }

    @Override
    protected void paintComponent(Graphics g) {
        updateFrame();
        super.paintComponent(g);
//        drawBG(g);
        drawNodes(g);
        drawEdges(g);
        drawAgants(g);
        drawPokemons(g);

    }
    private void drawBG(Graphics g){
        g.drawImage(backGround,0,0,this);//TODO should resize
    }
    private void drawPokemons(Graphics g){
        int r=8;
    for(CL_Pokemon pokemon:arena.getPokemons()){
        geo_location pokemonPosition = pokemon.getLocation();
        geo_location fp = this._w2f.world2frame(pokemonPosition);
        g.setColor(Color.YELLOW);
        g.fillOval((int)fp.x()-r,(int)fp.y()-r,2*r,2*r);
        g.setColor(Color.BLACK);
        String str= Double.toString(pokemon.getId()).substring(0, Double.toString(pokemon.getId()).indexOf('.')+1);
        g.drawString("id: " + str + " , "+pokemon.getValue(),(int) fp.x(), (int) fp.y() - 2 * r);
    }
    }
    private void drawAgants(Graphics g) {
        int r = 8;

        for (CL_Agent agent : arena.getAgents()) {
            geo_location agentPosition = agent.getLocation();
            geo_location fp = this._w2f.world2frame(agentPosition);
            g.setColor(Color.green);
            g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
            g.setColor(Color.BLACK);
            String str= Double.toString(agent.get_curr_fruit().getId()).substring(0, Double.toString(agent.get_curr_fruit().getId()).indexOf('.')+1);
            g.drawString("to: " + str, (int) fp.x(), (int) fp.y() - 2 * r);
        }
    }
    private void drawEdges(Graphics g){
        g.setColor(Color.BLACK);
        for(node_data node:arena.getGraph().getV()){
            for(edge_data Edge: arena.getGraph().getE(node.getKey()))
            {
                drawEdge(Edge,g);
            }
        }
    }
    private void drawEdge( edge_data e, Graphics g) {
            directed_weighted_graph gg = arena.getGraph();
            geo_location s = gg.getNode(e.getSrc()).getLocation();
            geo_location d = gg.getNode(e.getDest()).getLocation();
            geo_location s0 = this._w2f.world2frame(s);
            geo_location d0 = this._w2f.world2frame(d);
            Graphics2D twoD = (Graphics2D) g;
            twoD.setColor(Color.black);
            twoD.setStroke(new BasicStroke(4));
            twoD.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
//            double weight= e.getWeight();
//            String str= Double.toString(weight).substring(0, Double.toString(weight).indexOf('.')+3);
//            if(e.getSrc()-e.getDest()>0) {
//                twoD.setColor(new Color(69, 28, 28));
//                twoD.drawString(str, (int) (((s0.x()) + (d0.x()))/2 -4 ), (int) (((d0.y()) +(d0.y()))/2)+8);
//            }
//            else{
//                twoD.setColor(Color.red);
//                twoD.drawString(str, (int) (((s0.x()) + (d0.x())) / 2)+4, (int) (((s0.y()) + (d0.y()))/2 )+8);
//            }
    }
    private void drawNodes(Graphics g) {
        g.setColor(Color.BLUE);
        g.setFont(new Font("", Font.BOLD, 10));
        for (node_data node : arena.getGraph().getV()) {
            drawNode(node, 5, g);
        }
    }

        private void drawNode(node_data n, int r, Graphics g) {
            geo_location pos = n.getLocation();
            geo_location fp = this._w2f.world2frame(pos);
            g.fillOval((int)fp.x()-r, (int)fp.y()-r, 10, 10);
            g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-2*r);
        }

    }

