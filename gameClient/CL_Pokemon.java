package gameClient;
import api.edge_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

import java.util.HashSet;

public class CL_Pokemon {
	private final double id;
	private edge_data _edge;
	private double _value;
	private int _type;
	private Point3D _pos;
	private double min_dist;
	private int min_ro;
	private static HashSet<Double> busy=new HashSet<>();
	private static HashSet<Double> isfood=new HashSet<>();

	/**
	 * this method get pokemon data and init this data.
	 * @param id
	 * @param p
	 * @param t
	 * @param v
	 * @param s
	 * @param e
	 */
	public CL_Pokemon(double id,Point3D p, int t, double v, double s, edge_data e) {
		_type = t;
		this.id=id;
		_value = v;
		set_edge(e);
		_pos = p;
		min_dist = -1;
		min_ro = -1;
	}
	public double getId(){return this.id;}

	/**
	 * this method check if the pokemon is busy (if there is agent in the graph, that in the way to eat
	 * this pokemon)
	 * @return
	 */
	public  synchronized boolean isBusy(){return busy.contains(id);}

	/**
	 * this method can declare if this pokemon is busy or not,all this information keep in busy hashset.
	 * @param flag
	 */
	public synchronized void setIsBusy(boolean flag){
		if(flag)
			busy.add(id);
		else busy.remove(id);
	}

	/**
	 * this method check if the pokemon already eaten
	 * @return
	 */
	public  synchronized boolean isStillFood(){return isfood.contains(id);}

	/**
	 * this method get boolean and update if the pokemon already eaten.
	 * @param flag
	 */
	public synchronized void setIsStillFood(boolean flag){//true means this pokemon did not been eaten
		if(flag)
			isfood.add(id);
		else isfood.remove(id);
	}

	public String toString() {return "F:{v="+_value+", t="+_type+"}";}

	/**
	 * this method return edge data the pokemon on.
	 * @return
	 */
	public synchronized edge_data get_edge() {
		return _edge;
	}

	public synchronized void set_edge(edge_data _edge) {
		this._edge = _edge;
	}

	/**
	 * this method return the pokemon location.
	 * @return _pos
	 */
	public Point3D getLocation() {
		return _pos;
	}
	/**
	 * this method return the pokemon type.
	 * @return _type
	 */
	public int getType() {return _type;}
	/**
	 * this method return the pokemon value.
	 * @return _value
	 */
	public double getValue() {return _value;}

}
