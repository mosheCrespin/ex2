package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

public class CL_Agent {
		public static final double EPS = 0.0001;
		private static int _count = 0;
		private static int _seed = 3331;
		private int _id;
		private geo_location _pos;
		private double _speed;
		private edge_data _curr_edge;
		private node_data _curr_node;
		private directed_weighted_graph _gg;
		private CL_Pokemon _curr_fruit;
		private long _sg_dt;
		private double _value;
		private List<node_data> currPath;
		private Iterator<node_data> iteratorOfCurrPath;
		public int dest=-1;
		
		public CL_Agent(directed_weighted_graph g, int start_node) {
			_gg = g;
			setMoney(0);
			this._curr_node = _gg.getNode(start_node);
			_pos = _curr_node.getLocation();
			_id = -1;
			setSpeed(0);
		}
		public void update(String json) {
			JSONObject line;
			try {
				// "GameServer":{"graph":"A0","pokemons":3,"agents":1}}
				line = new JSONObject(json);
				JSONObject ttt = line.getJSONObject("Agent");
				int id = ttt.getInt("id");
				if(id==this.getID() || this.getID() == -1) {
					if(this.getID() == -1) {_id = id;}
					double speed = ttt.getDouble("speed");
					String p = ttt.getString("pos");
					Point3D pp = new Point3D(p);
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");
					double value = ttt.getDouble("value");
					this._pos = pp;
					this.setCurrNode(src);
					this.set_curr_edge(src,dest);
					this.setSpeed(speed);
					this.setNextNode(dest);
					this.setMoney(value);
					this.dest=dest;///////////////TODO

				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}

		public void setCurrPath(List<node_data> path){
			this.currPath=path;
			this.iteratorOfCurrPath=this.currPath.listIterator();
		}
		public List<node_data> getCurrPath(){return this.currPath;}
		public int getNextNodeViaIterator(){
			if(iteratorOfCurrPath.hasNext()) return iteratorOfCurrPath.next().getKey();
			else return -1;
		}


		//@Override
		public int getSrcNode() {return this._curr_node.getKey();}
		public String toJSON() {
			int d = this.getNextNode();
			String ans = "{\"Agent\":{"
					+ "\"id\":"+this._id+","
					+ "\"value\":"+this._value+","
					+ "\"src\":"+this._curr_node.getKey()+","
					+ "\"dest\":"+d+","
					+ "\"speed\":"+this.getSpeed()+","
					+ "\"pos\":\""+_pos.toString()+"\""
					+ "}"
					+ "}";
			return ans;	
		}
		private void setMoney(double v) {_value = v;}
	
		public boolean setNextNode(int dest) {
			boolean ans = false;
			int src = this._curr_node.getKey();
			this._curr_edge = _gg.getEdge(src, dest);
			if(_curr_edge!=null) {
				ans=true;
			}
			else {_curr_edge = null;}
			return ans;
		}
		public void setCurrNode(int src) {
			this._curr_node = _gg.getNode(src);
		}
		public boolean isMoving() {
			return (this.dest!=-1);
		}
		public String toString() {
			return toJSON();
		}
		public String toString1() {
			String ans=""+this.getID()+","+_pos+", "+isMoving()+","+this.getValue();	
			return ans;
		}
		public int getID() {
			// TODO Auto-generated method stub
			return this._id;
		}
	
		public geo_location getLocation() {
			// TODO Auto-generated method stub
			return _pos;
		}

		
		public double getValue() {
			// TODO Auto-generated method stub
			return this._value;
		}
		public void set_curr_edge(int src, int dest){
			if(dest==-1) this._curr_edge=null;
			else
				this._curr_edge=_gg.getEdge(src,dest);
		}



		public int getNextNode() {
			int ans = -2;
			if(this._curr_edge==null) {
				ans = -1;}
			else {
				ans = this._curr_edge.getDest();
			}
			return ans;
		}

		public double getSpeed() {
			return this._speed;
		}

		public void setSpeed(double v) {
			this._speed = v;
		}
		public CL_Pokemon get_curr_fruit() {
			return _curr_fruit;
		}
		public void set_curr_fruit(CL_Pokemon curr_fruit) {
			this._curr_fruit = curr_fruit;
		}
		public void set_SDT(long ddtt) {//check after how much time this agent will pass
			long ddt = ddtt;
			if(this._curr_edge!=null) {
				double w = get_curr_edge().getWeight();
				geo_location dest = _gg.getNode(get_curr_edge().getDest()).getLocation();
				geo_location src = _gg.getNode(get_curr_edge().getSrc()).getLocation();
				double de = src.distance(dest);
				double dist = _pos.distance(dest);
				if(this.get_curr_fruit().get_edge()==this.get_curr_edge()) {
					 dist = _curr_fruit.getLocation().distance(this._pos);
				}
				double norm = dist/de;
				double dt = w*norm / this.getSpeed(); 
				ddt = (long)(1000.0*dt);
			}
			this.set_sg_dt(ddt);
		}
		
		public edge_data get_curr_edge() {
			return this._curr_edge;
		}
		public long get_sg_dt() {
			return _sg_dt;
		}
		public void set_sg_dt(long _sg_dt) {
			this._sg_dt = _sg_dt;
		}
	}
