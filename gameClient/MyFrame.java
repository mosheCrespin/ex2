package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 *
 */
public class MyFrame extends JFrame {
	private int _ind;
	private Arena arena;
	private gameClient.util.Range2Range _w2f;
	private BufferedImage backGround;
	private int oldwidth;
	private int oldheight;
	private boolean flag=false;
//	private Graphics bufferGraphics=null;
//	private BufferStrategy bufferStrategy=null;
	MyFrame(String a,Arena arena) {//write somthing on the title
		super(a);
		_ind = 0;
		this.arena=arena;
		try {
			this.backGround= ImageIO.read(new File("BackGround.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
//	public void update(Arena ar) {
//		this.arena = ar;
//		System.out.println("2222222222222222222");
//		updateFrame();
//		super.repaint();
//	}

	private void updateFrame() {
		Range rx = new Range(50,this.getWidth()-50);
		Range ry = new Range(this.getHeight()-50,50);
		Range2D frame = new Range2D(rx,ry);
		directed_weighted_graph g = arena.getGraph();
		_w2f = Arena.w2f(g,frame);
	}
	public void paint(Graphics g) {
//		if(bufferStrategy==null){
//			this.createBufferStrategy(2);
//			bufferStrategy=this.getBufferStrategy();
//			bufferGraphics=bufferStrategy.getDrawGraphics();
//		}
		if(arena.getGraph()==null) super.paint(g);
		BufferedImage bufferedImage=new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB);
		Graphics2D gg=bufferedImage.createGraphics();
		super.paintComponents(gg);
		int w = this.getWidth();
		int h = this.getHeight();
		Font font = g.getFont().deriveFont( 18.0f );
		g.setFont(font);
		this.oldheight=getHeight();
		this.oldwidth=getWidth();
		updateFrame();
		drawBG(g);
		drawAgants(g);
		drawPokemons(g);
		drawGraph(g);
		drawInfo(g);

	}
	private void drawBG(Graphics g){

		g.drawImage(backGround,0,0,this);}

	private void drawInfo(Graphics g) {
		List<String> str = arena.get_info();
		String dt = "none";
		for(int i=0;i<str.size();i++) {
			g.drawString(str.get(i)+" dt: "+dt,100,60+i*20);
		}
	}
	private void drawGraph(Graphics g) {
		directed_weighted_graph gg = arena.getGraph();
		Iterator<node_data> iter = gg.getV().iterator();
		while(iter.hasNext()) {
			node_data n = iter.next();
			g.setColor(Color.blue);
			drawNode(n,10,g);
			Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
			while(itr.hasNext()) {
				edge_data e = itr.next();

//				g.setColor(Color.BLACK);
				drawEdge(e, (Graphics2D)g);
			}
		}
	}
	private void drawPokemons(Graphics g) {
		List<CL_Pokemon> fs = arena.getPokemons();
		if(fs!=null) {
		Iterator<CL_Pokemon> itr = fs.iterator();
		
		while(itr.hasNext()) {
			int i=0;
			CL_Pokemon f = itr.next();
			Point3D c = f.getLocation();
			int r=10;
			g.setColor(Color.green);
			if(f.getType()<0) {g.setColor(Color.red);}
			if(c!=null) {
				geo_location fp = this._w2f.world2frame(c);
				g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
				g.drawString(""+fs.get(i).getValue(), (int)fp.x(), (int)fp.y()-2*r);
			}
			i++;
		}
		}
	}
	private void drawAgants(Graphics g) {
		List<CL_Agent> agent = arena.getAgents();
	//	Iterator<OOP_Point3D> itr = rs.iterator();
		g.setColor(Color.BLACK);
		int i=0;
		while(agent!=null && i<agent.size()) {
			geo_location agentPosition = agent.get(i).getLocation();
			int r=8;
			if(agentPosition!=null) {
				geo_location fp = this._w2f.world2frame(agentPosition);
				g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
				g.setColor(Color.BLACK);
				g.drawString(""+agent.get(i).getID(), (int)fp.x(), (int)fp.y()-2*r);
			}
			i++;
		}
	}
	private void drawNode(node_data n, int r, Graphics g) {
		geo_location pos = n.getLocation();
		geo_location fp = this._w2f.world2frame(pos);
		g.fillOval((int)fp.x()-r, (int)fp.y()-r, 15*getWidth()/oldwidth, 15*getWidth()/oldwidth);
		g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-2*r);
	}
	private void drawEdge(edge_data e, Graphics2D g) {
		directed_weighted_graph gg = arena.getGraph();
		geo_location s = gg.getNode(e.getSrc()).getLocation();
		geo_location d = gg.getNode(e.getDest()).getLocation();
		geo_location s0 = this._w2f.world2frame(s);
		geo_location d0 = this._w2f.world2frame(d);
		Graphics2D twoD = (Graphics2D) g;
		twoD.setColor(Color.blue);
		twoD.setStroke(new BasicStroke(4));
		twoD.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());


	//	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
	}
//	public void paint(Graphics2D g){
//		Graphics2D twoD = (Graphics2D) g;
//		twoD.setColor(Color.orange);
//		twoD.setStroke(new BasicStroke(4));
//		twoD.drawLine(5, 5, 480, 5);
//	}
}
