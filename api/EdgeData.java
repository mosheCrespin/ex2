package api;

public class EdgeData implements edge_data{
    private int src;
    private int dest;
    private double weight;
    private String info;
    private int tag;
    public EdgeData(int src,int dest,double weight,String info,int tag){
        this.src=src;
        this.dest=dest;
        this.weight=weight;
        this.info=info;
        this.tag=tag;
    }
    public EdgeData(int src,int dest,double weight){
        this.src=src;
        this.dest=dest;
        this.weight=weight;
        this.info="";
    }
    public int getSrc() {
        return this.src;
    }

    public int getDest() {
        return this.dest;
    }

    public double getWeight() {
        return this.weight;
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
        StringBuilder str= new StringBuilder();
        str.append("dest: ").append(this.dest).append(", weight: ").append(this.weight);
        return str +"";
    }
}