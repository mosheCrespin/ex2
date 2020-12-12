package gameClient;

import api.*;
import com.google.gson.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;
import kotlin.jvm.Synchronized;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a multi Agents Arena which move on a graph - grabs Pokemons and avoid the Zombies.
 * @author boaz.benmoshe
 *
 */
public class Arena{
	public static final double EPS1 = 0.00001, EPS2=EPS1*EPS1, EPS=EPS2;
	private directed_weighted_graph _gg;
	private List<CL_Agent> _agents;
	private ArrayList<CL_Pokemon> _pokemons;
	private List<String> _info;
	private static Point3D MIN = new Point3D(0, 100,0);
	private static Point3D MAX = new Point3D(0, 100,0);
	private int numberOfAgents;
	private double[][] distanceArr;

	public Arena() {;
		_info = new ArrayList<String>();
	}
	private Arena(directed_weighted_graph g, List<CL_Agent> r, List<CL_Pokemon> p) {
		_gg = g;
		this.setAgents(r);
//		this.setPokemons(p);
	}
	public void setDistanceArr(double [][] arr){this.distanceArr=arr;}

	public double[][] getDistanceArr() {
		return distanceArr;
	}

	public synchronized void setPokemons(String json) {
		this._pokemons=json2Pokemons(json);
		for(CL_Pokemon curr: this._pokemons)
		{
			updateEdge(curr,this._gg);
		}
	}
	public void setNumberOfAgents(String json){
			try {
				JSONObject ttt = new JSONObject(json);
				JSONObject object = ttt.getJSONObject("GameServer");
				this.numberOfAgents= object.getInt("agents");

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	public int getNumberOfAgents(){return this.numberOfAgents;}
	public void setAgents(List<CL_Agent> f) {
		this._agents = f;
	}
	public void setGraph(directed_weighted_graph g) {this._gg =g;}//init();}
	private void init( ) {
		MIN=null; MAX=null;
		double x0=0,x1=0,y0=0,y1=0;
		Iterator<node_data> iter = _gg.getV().iterator();
		while(iter.hasNext()) {
			geo_location c = iter.next().getLocation();
			if(MIN==null) {
				x0 = c.x();
				y0=c.y();
				x1=x0;
				y1=y0;
				MIN = new Point3D(x0,y0);
			}
			if(c.x() < x0) {x0=c.x();}
			if(c.y() < y0) {y0=c.y();}
			if(c.x() > x1) {x1=c.x();}
			if(c.y() > y1) {y1=c.y();}
		}
		double dx = x1-x0, dy = y1-y0;
		MIN = new Point3D(x0-dx/10,y0-dy/10);
		MAX = new Point3D(x1+dx/10,y1+dy/10);
	}
	public List<CL_Agent> getAgents() {return _agents;}
	public ArrayList<CL_Pokemon> getPokemons() {return _pokemons;}

	
	public directed_weighted_graph getGraph() {
		return _gg;
	}
	public List<String> get_info() {
		return _info;
	}
	public void set_info(List<String> _info) {
		this._info = _info;
	}

	////////////////////////////////////////////////////
	public static List<CL_Agent> getAgents(String aa, directed_weighted_graph gg) {
		ArrayList<CL_Agent> ans = new ArrayList<CL_Agent>();
		try {
			JSONObject ttt = new JSONObject(aa);
			JSONArray ags = ttt.getJSONArray("Agents");
			for(int i=0;i<ags.length();i++) {
				CL_Agent c = new CL_Agent(gg,0);
				c.update(ags.get(i).toString());
				ans.add(c);
			}
			//= getJSONArray("Agents");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ans;
	}
	public static ArrayList<CL_Pokemon> json2Pokemons(String fs) {
		ArrayList<CL_Pokemon> ans = new  ArrayList<CL_Pokemon>();
		try {
			JSONObject ttt = new JSONObject(fs);
			JSONArray ags = ttt.getJSONArray("Pokemons");
			for(int i=0;i<ags.length();i++) {
				JSONObject pp = ags.getJSONObject(i);
				JSONObject pk = pp.getJSONObject("Pokemon");
				int t = pk.getInt("type");
				double v = pk.getDouble("value");
				//double s = 0;//pk.getDouble("speed");
				String p = pk.getString("pos");
				CL_Pokemon f = new CL_Pokemon(i,new Point3D(p), t, v, 0, null);
				ans.add(f);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
		return ans;
	}
	public static void updateEdge(CL_Pokemon fr, directed_weighted_graph g) {
		//	oop_edge_data ans = null;
		Iterator<node_data> itr = g.getV().iterator();
		while(itr.hasNext()) {
			node_data v = itr.next();
			Iterator<edge_data> iter = g.getE(v.getKey()).iterator();
			while(iter.hasNext()) {
				edge_data e = iter.next();
				boolean f = isOnEdge(fr.getLocation(), e,fr.getType(), g);
				if(f) {fr.set_edge(e);}
			}
		}
	}

	private static boolean isOnEdge(geo_location p, geo_location src, geo_location dest ) {

		boolean ans = false;
		double dist = src.distance(dest);
		double d1 = src.distance(p) + p.distance(dest);
		if(dist>d1-EPS2) {ans = true;}
		return ans;
	}
	private static boolean isOnEdge(geo_location p, int s, int d, directed_weighted_graph g) {
		geo_location src = g.getNode(s).getLocation();
		geo_location dest = g.getNode(d).getLocation();
		return isOnEdge(p,src,dest);
	}
	private static boolean isOnEdge(geo_location p, edge_data e, int type, directed_weighted_graph g) {
		int src = g.getNode(e.getSrc()).getKey();
		int dest = g.getNode(e.getDest()).getKey();
		if(type<0 && dest>src) {return false;}
		if(type>0 && src>dest) {return false;}
		return isOnEdge(p,src, dest, g);
	}

	private static Range2D GraphRange(directed_weighted_graph g) {
		Iterator<node_data> itr = g.getV().iterator();
		double x0=0,x1=0,y0=0,y1=0;
		boolean first = true;
		while(itr.hasNext()) {
			geo_location p = itr.next().getLocation();
			if(first) {
				x0=p.x(); x1=x0;
				y0=p.y(); y1=y0;
				first = false;
			}
			else {
				if(p.x()<x0) {x0=p.x();}
				if(p.x()>x1) {x1=p.x();}
				if(p.y()<y0) {y0=p.y();}
				if(p.y()>y1) {y1=p.y();}
			}
		}
		Range xr = new Range(x0,x1);
		Range yr = new Range(y0,y1);
		return new Range2D(xr,yr);
	}
	public static Range2Range w2f(directed_weighted_graph g, Range2D frame) {
		Range2D world = GraphRange(g);
		Range2Range ans = new Range2Range(world, frame);
		return ans;
	}


/////////////////////////////////////////////////////////////////////////////////
	static directed_weighted_graph LoadGraphFromJson(String str) {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(directed_weighted_graph.class, new graphJsonDeserializer());
		Gson gson = builder.create();
		return gson.fromJson(str, directed_weighted_graph.class);
	}
	/////////////////////////////////////////////////////////////////////////////////////

	private static class graphJsonDeserializer implements JsonDeserializer<directed_weighted_graph> {
		@Override
		public  directed_weighted_graph deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			JsonArray nodes=jsonObject.get("Nodes").getAsJsonArray();
			node_data currNode;
			directed_weighted_graph g0=new DWGraph_DS();
			for(JsonElement curr: nodes){
				currNode=new NodeData(curr.getAsJsonObject().get("id").getAsInt(),posToDouble(curr.getAsJsonObject().get("pos").getAsString()));
				g0.addNode(currNode);
			}
			JsonArray edges=jsonObject.get("Edges").getAsJsonArray();
			for(JsonElement curr :edges){
				int src=curr.getAsJsonObject().get("src").getAsInt();
				int dest=curr.getAsJsonObject().get("dest").getAsInt();
				double w=curr.getAsJsonObject().get("w").getAsDouble();
				g0.connect(src,dest,w);
			}
			return g0;
		}
	}
	private static geoLocation posToDouble(String str){
		String[] a = str.split(",");
		geoLocation ans =new geoLocation(Double.parseDouble(a[0]),Double.parseDouble(a[1]),Double.parseDouble(a[2]));
		return ans;
	}
}


