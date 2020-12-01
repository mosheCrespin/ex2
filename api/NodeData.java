package api;

public class NodeData implements node_data{
    private static int id_maker;
    private int id;
    private String info;
    private int tag;
    private geo_location xyz;

    public int getKey() {
       return this.id;
    }

    public geo_location getLocation() {
        return null;
    }

    public void setLocation(geo_location p) {

    }

    public double getWeight() {
        return 0;
    }

    public void setWeight(double w) {

    }

    public String getInfo() {
        return null;
    }

    public void setInfo(String s) {

    }

    public int getTag() {
        return 0;
    }

    public void setTag(int t) {

    }
}
