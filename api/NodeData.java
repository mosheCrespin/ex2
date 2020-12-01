package api;

import java.util.HashMap;

public class NodeData implements node_data{
    private static int id_maker;
    private int id;
    private String info;
    private int tag;
    private double weight;
    private geo_location xyz;
    HashMap <Integer,node_data>NeiHashmap;
    int MakersId=0;

    //constructor
    public NodeData(){
        this.id=MakersId;
        MakersId++;
        this.NeiHashmap=new HashMap<Integer,node_data>();
    }

    public int getKey() {
       return this.id;
    }

    public geo_location getLocation() {
               return this.xyz;
    }

    public void setLocation(geo_location p) {
        this.xyz=p;
    }

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double w) {
        this.weight=w;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String s) {
        this.info=s;
    }

    public int getTag() {
      return this.tag;
    }

    public void setTag(int t) {
        this.tag=t;
    }
}
