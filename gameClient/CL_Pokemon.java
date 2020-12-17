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
	public  synchronized boolean isBusy(){return busy.contains(id);}
	public synchronized void setIsBusy(boolean flag){
		if(flag)
			busy.add(id);
		else busy.remove(id);
	}
	public  synchronized boolean isStillFood(){return isfood.contains(id);}
	public synchronized void setIsStillFood(boolean flag){//true means this pokemon did not been eaten
		if(flag)
			isfood.add(id);
		else isfood.remove(id);
	}

	public String toString() {return "F:{v="+_value+", t="+_type+"}";}
	public synchronized edge_data get_edge() {
		return _edge;
	}

	public synchronized void set_edge(edge_data _edge) {
		this._edge = _edge;
	}

	public Point3D getLocation() {
		return _pos;
	}
	public int getType() {return _type;}
	public double getValue() {return _value;}

}
