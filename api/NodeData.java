package api;

import java.util.HashMap;

public class NodeData implements node_data{
    private static int id_maker=0;
    private int id;
    private String info;
    private int tag;
    private double weight;
    private geo_location Location;

    //constructor
    public NodeData(){
        this.id=id_maker;
        this.info="";
        this.Location=new geoLocation(0,0,0);
        id_maker++;
    }//deep copy constructor
    public NodeData(node_data other){
        this.id=other.getKey();
        this.info=other.getInfo();
        this.tag=other.getTag();
        this.weight=other.getWeight();
        this.Location= other.getLocation();
    }
    public NodeData(int key,int tag,double weight,String info,geoLocation Location){
        this.id=key;
        this.info=info;
        this.tag=tag;
        this.weight=weight;
        this.Location= Location;
    }

    public int getKey() {
       return this.id;
    }

    public geo_location getLocation() {
               return this.Location;
    }

    public void setLocation(geo_location p) {
        this.Location=p;
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
    public String toString(){
        return " id: " + getKey();
    }

}
