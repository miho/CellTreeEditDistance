package edu.gcsc.celltreeedit;

import javax.xml.soap.Node;

/**
 *
 */
public class NodeData {

    double label;
    int type;
    int index;
    int parent;
    double posX;
    double posY;
    double posZ;
    double radius;
    /**
     *
     * @param label
     */
    public NodeData(double label){
        this.label=label;
    }

    public NodeData(int index, int type, double posX, double posY, double posZ, double radius, int parent){
        this.index=index;
        this.type=type;
        this.posX=posX;
        this.posY=posY;
        this.posZ=posZ;
        this.radius=radius;
        this.parent=parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeData nodeData = (NodeData) o;

        if (Double.compare(nodeData.label, label) != 0) return false;
        if (type != nodeData.type) return false;
        if (index != nodeData.index) return false;
        if (parent != nodeData.parent) return false;
        if (Double.compare(nodeData.posX, posX) != 0) return false;
        if (Double.compare(nodeData.posY, posY) != 0) return false;
        if (Double.compare(nodeData.posZ, posZ) != 0) return false;
        return Double.compare(nodeData.radius, radius) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(label);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + type;
        result = 31 * result + index;
        result = 31 * result + parent;
        temp = Double.doubleToLongBits(posX);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(posY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(posZ);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(radius);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public double getLabel(){
        return label;
    }

    public void setLabel(double label){
        this.label=label;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getPosZ() {
        return posZ;
    }

    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public int getType() {

        return type;
    }

    public void setType(int Type) {
        this.type = type;
    }
}
