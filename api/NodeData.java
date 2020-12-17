package api;

import java.util.HashMap;

public class NodeData implements node_data{
    private static int id_maker=0;
    private int id;
    private String info;
    private int tag;
    private double weight;
    private geo_location Location;


    /**
     * This method Allows to do deep copy constructor
     *This function copy all his data.
     * @param other-the node data we want to copy.
     */
    public NodeData(node_data other){// deep copy constructor
        this.id=other.getKey();
        this.info=other.getInfo();
        this.tag=other.getTag();
        this.weight=other.getWeight();
        this.Location= other.getLocation();
    }

    /**
     *constructor that make anew node.
     */
    public NodeData(){
        this.id=id_maker;
        id_maker++;
    }

    /**
     * constructor that get data about the node and init this node with this data.
     * @param key
     * @param tag
     * @param weight
     * @param info
     * @param Location
     */
    public NodeData(int key,int tag,double weight,String info,geoLocation Location){
        this.id=key;
        this.info=info;
        this.tag=tag;
        this.weight=weight;
        this.Location= Location;
    }

    /**
     * constructor that get node's id and location and init the node with this data.
     * @param key
     * @param location
     */
    public NodeData(int key,geoLocation location)
    {
        this.id=key; this.Location=location;
    }

    /**
     * constructor that get node's id  and init the node with this data.
     * @param key
     */
    public NodeData(int key){
        this.id=key;
    }

    /**
     * Returns the key (id) associated with this node.
     * @return
     */
    public int getKey() {
       return this.id;
    }

    /** Returns the location of this node, if
     * none return null.
     *
     * @return node's id.
     */
    public geo_location getLocation() {
        return this.Location;
    }

   /** Allows changing this node's location.
     * @param p - the new location  (position) of this node.
     */
    public void setLocation(geo_location p) {
        this.Location=p;
    }

    /**
     * Returns the weight associated with this node.
     * @return node's location.
     */
    public double getWeight() {
        return this.weight;
    }
    /**
     * Allows changing this node's weight.
     * @param w - the new node's weight.
     */
    public void setWeight(double w) {
        this.weight=w;
    }
    /**
     * Returns the remark (meta data) associated with this node.
     * @return the node's info.
     */
    public String getInfo() {
        return this.info;
    }
    /**
     * Allows changing the remark (meta data) associated with this node.
     * @param s is the new node's info.
     */
    public void setInfo(String s) {
        this.info=s;
    }
    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     * @return the node's tag.
     */
    public int getTag() {
      return this.tag;
    }
    /**
     * Allows setting the "tag" value for temporal marking an node - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    public void setTag(int t) {
        this.tag=t;
    }

    /**
     * Returns String with the node's id.
     * @return  a String with node's id.
     */
    public String toString(){
        return " id: " + getKey();
    }

}
