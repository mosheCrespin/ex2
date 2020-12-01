package api;

import gameClient.util.Point3D;

public class geoLocation implements geo_location {
    private double x;
    private double y;
    private double z;

    /**
     * this constructor builds a new geo_location point using x,y,z
     * @param _x
     * @param _y
     * @param _z
     */
    public geoLocation(double _x,double _y,double _z){
        this.x=_x;
        this.y=_y;
        this.z=_z;
    }
    //getters
    public double x() {
        return this.x;
    }
    public double y() {
        return this.y;
    }

    public double z() {
        return this.z;
    }

    /**
     * this method calculate the distance from this location to the given geo_location
     * @param g
     * @return
     */
    public double distance(geo_location g) {
        Point3D temp=new Point3D(this.x,this.y,this.z);
        return temp.distance(g);
    }
}
