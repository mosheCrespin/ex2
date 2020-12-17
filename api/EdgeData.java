package api;

public class EdgeData implements edge_data{
    private int src;
    private int dest;
    private double weight;
    private String info;
    private int tag;

    /**
     * constructor for the EdgeData that get src,dest,weight,info and tag.
     * @param src
     * @param dest
     * @param weight
     * @param info
     * @param tag
     */
    public EdgeData(int src,int dest,double weight,String info,int tag){
        this.src=src;
        this.dest=dest;
        this.weight=weight;
        this.info=info;
        this.tag=tag;
    }

    /**
     * constructor for the EdgeData that get src,dest and weight.
     * @param src
     * @param dest
     * @param weight
     */
    public EdgeData(int src,int dest,double weight){
        this.src=src;
        this.dest=dest;
        this.weight=weight;
        this.info="";
    }
    /**
     * The id of the source node of this edge.
     * @return the edge's src.
     */
    public int getSrc() {
        return this.src;
    }
    /**
     * The id of the destination node of this edge
     * @return edge's dest.
     */
    public int getDest() {
        return this.dest;
    }
    /**
     * Returns the remark (meta data) associated with this edge.
     * @return edge's weight.
     */
    public double getWeight() {
        return this.weight;
    }
    /**
     * Returns the remark (meta data) associated with this edge.
     * @return node's info.
     */
    public String getInfo() {
        return this.info;
    }
    /**
     * Allows changing the remark (meta data) associated with this edge.
     * @param s
     */
    public void setInfo(String s) {
        this.info=s;
    }
    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     * @return
     */
    public int getTag() {
        return this.tag;
    }
    /**
     * This method allows setting the "tag" value for temporal marking an edge - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    public void setTag(int t) {
        this.tag=t;
    }

    /**
     *
     * @return
     */
    public String toString(){
        StringBuilder str= new StringBuilder();
        str.append("|dest: ").append(this.dest).append(", weight: ").append(this.weight).append(", info: ").append(this.info).append(", tag:").append(this.tag).append(" |\n");
        return str +"";
    }
}