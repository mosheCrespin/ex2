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
import java.util.ArrayList;


public class MyPanel extends JPanel {
    private Arena arena;
    private gameClient.util.Range2Range _w2f;
    private BufferedImage backGround;
    private BufferedImage boaz;
    private BufferedImage ishay;
    private BufferedImage moshe;
    private JLabel infoA;
    private JLabel infoB;
    private Font font;
    public MyPanel(Arena arena){
        super();
        this.arena=arena;
        this.infoA=new JLabel();
        this.infoB=new JLabel();
        this.font= new Font("font", Font.ITALIC,20);
        this.infoA.setFont(this.font);
        this.infoA.setBackground(Color.WHITE);
        this.infoA.setOpaque(true);
        this.infoB.setFont(this.font);
        this.infoB.setBackground(Color.WHITE);
        this.infoB.setOpaque(true);
        this.add(this.infoA);
        this.add(this.infoB);

        try {
            this.backGround= ImageIO.read(new File("src\\data\\BackGround.jpg"));
            this.boaz=ImageIO.read(new File("src\\data\\boaz.png"));
            this.ishay=ImageIO.read(new File("src\\data\\ishay.png"));
            this.moshe=ImageIO.read(new File("src\\data\\moshe.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this metho update the frame.the size frame doesn't change.
     */
    private void updateFrame() {
        Range rx = new Range(50,this.getWidth()-50);
        Range ry = new Range(this.getHeight()-50,50);
        Range2D frame = new Range2D(rx,ry);
        directed_weighted_graph g = arena.getGraph();
        _w2f = Arena.w2f(g,frame);
    }

    /**
     * this method call all the methods that update or draw in the game page.
     * like update frame ,draw the nodes,edges, pokemons ,agent and info about the game.
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateFrame();
        drawBG(g);
        drawNodes(g);
        drawEdges(g);
        drawAgants(g);
        drawPokemons(g);
        drawInfo(g);


    }

    /**
     * this method draw a photo in the background of the game.
     * @param g
     */
    private void drawBG(Graphics g){

        g.drawImage(backGround,0,0,getWidth(),getHeight(),null);
    }

    /**
     * this method draw info in the game page. how much time left in this game,moves how much pokemons,pokemon and the
     * time left.
     * @param g
     */
    private void drawInfo(Graphics g){
        ArrayList<Integer> info= arena.getInfo();
        this.infoA.setText("Time Left: " + info.get(5) +
                "  Moves: " + info.get(3) + "  Grade: " + info.get(4) + "   ");
        this.infoB.setText(" Pokemons: " + info.get(0) + ", Agents: " + info.get(1) + ", Game Level: " + info.get(2) + "  ");

    }

    /**
     * this method get Graphics g and run over all the pokemons and draw them in the graph.
     * @param g
     */
    private void drawPokemons(Graphics g){
        int r=8;
    for(CL_Pokemon pokemon:arena.getPokemons()){
        geo_location pokemonPosition = pokemon.getLocation();
        geo_location fp = this._w2f.world2frame(pokemonPosition);
        g.drawImage(this.boaz,(int) fp.x(),(int) fp.y()-2,30,30,null);
        g.setColor(Color.BLACK);
        String str= Double.toString(pokemon.getId()).substring(0, Double.toString(pokemon.getId()).indexOf('.'));
        g.drawString("id: " + str + " , Value: "+pokemon.getValue(),(int) fp.x(), (int) fp.y() - 2 * r);
    }
    }

    /**
     * this method get Graphics g and run over all the agents and draw them in the graph.
     * @param g
     */
    private void drawAgants(Graphics g) {
        int r = 8;
        int i=0;
        for (CL_Agent agent : arena.getAgents()) {
            geo_location agentPosition = agent.getLocation();
            geo_location fp = this._w2f.world2frame(agentPosition);
            if(i%2==0)
                g.drawImage(this.moshe,(int) fp.x(),(int) fp.y()-2,30,30,null);
            else g.drawImage(this.ishay,(int) fp.x(),(int) fp.y()-2,30,30,null);
            g.setColor(Color.BLACK);
            String str="";
            if(agent.get_curr_fruit()!=null)
                 str= Double.toString(agent.get_curr_fruit().getId()).substring(0, Double.toString(agent.get_curr_fruit().getId()).indexOf('.')+1);
            g.drawString("to: " + str, (int) fp.x(), (int) fp.y() - 2 * r);
            i++;
        }
    }
    /**
     * this method get Graphics g and run over all the nodes in the graph and send to another method the all
     * edges that start from this node. the other method draw the edge in the graph.
     * @param g
     */
    private void drawEdges(Graphics g){
        g.setColor(Color.BLACK);
        for(node_data node:arena.getGraph().getV()){
            for(edge_data Edge: arena.getGraph().getE(node.getKey()))
            {
                drawEdge(Edge,g);
            }
        }
    }
    /**
     * this method get edge_data e , Graphics g and  draw the edge in the graph.
     * @param g
     */
    private void drawEdge( edge_data e, Graphics g) {
            directed_weighted_graph gg = arena.getGraph();
            geo_location s = gg.getNode(e.getSrc()).getLocation();
            geo_location d = gg.getNode(e.getDest()).getLocation();
            geo_location s0 = this._w2f.world2frame(s);
            geo_location d0 = this._w2f.world2frame(d);
            Graphics2D twoD = (Graphics2D) g;
            twoD.setColor(Color.black);
            twoD.setStroke(new BasicStroke(6));
            twoD.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
    }

    /**
     * this method run over all the nodes in the graph and send all node to another method call drawNode(node_data n, int r, Graphics g)
     * that draw this node.
     * @param g
     */
    private void drawNodes(Graphics g) {
        g.setColor(Color.BLUE);
        g.setFont(new Font("", Font.BOLD, 14));
        for (node_data node : arena.getGraph().getV()) {
            drawNode(node, 5, g);
        }
    }

    /**
     * this method draw the nodes in the graph page.
     * @param n
     * @param r
     * @param g
     */
        private void drawNode(node_data n, int r, Graphics g) {
            geo_location pos = n.getLocation();
            geo_location fp = this._w2f.world2frame(pos);
            g.fillOval((int)fp.x()-r, (int)fp.y()-r, 14, 14);
            g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-2*r);
        }

    }

