package edu.gcsc.celltreeedit.TEDCalculation;

import java.io.Serializable;
import java.util.List;

/**
 * Data for a Node from APTED-library. Contains information of the summarized segment from swcFile.
 */
public class NodeData implements Serializable{

    double label;
    int type;
    List<Integer> index;
    List<Integer> parent;
    List<Double> posX;
    List<Double> posY;
    List<Double> posZ;
    List<Double> radius;

    public NodeData(double label){
        this.label=label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeData nodeData = (NodeData) o;

        if (Double.compare(nodeData.label, label) != 0) return false;
        if (type != nodeData.type) return false;
        if (index != null ? !index.equals(nodeData.index) : nodeData.index != null) return false;
        if (parent != null ? !parent.equals(nodeData.parent) : nodeData.parent != null) return false;
        if (posX != null ? !posX.equals(nodeData.posX) : nodeData.posX != null) return false;
        if (posY != null ? !posY.equals(nodeData.posY) : nodeData.posY != null) return false;
        if (posZ != null ? !posZ.equals(nodeData.posZ) : nodeData.posZ != null) return false;
        return radius != null ? radius.equals(nodeData.radius) : nodeData.radius == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(label);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + type;
        result = 31 * result + (index != null ? index.hashCode() : 0);
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (posX != null ? posX.hashCode() : 0);
        result = 31 * result + (posY != null ? posY.hashCode() : 0);
        result = 31 * result + (posZ != null ? posZ.hashCode() : 0);
        result = 31 * result + (radius != null ? radius.hashCode() : 0);
        return result;
    }

    public void setLabel(double label) {

        this.label = label;
    }

    public double getLabel() {

        return label;
    }

    public int getType() {
        return type;
    }

    public List<Integer> getIndex() {
        return index;
    }

    public List<Integer> getParent() {
        return parent;
    }

    public List<Double> getPosX() {
        return posX;
    }

    public List<Double> getPosY() {
        return posY;
    }

    public List<Double> getPosZ() {
        return posZ;
    }

    public List<Double> getRadius() {
        return radius;
    }

    public void setType(int type) {
        this.type = type;
    }

    public NodeData(List<Integer> index, int type, List<Double> posX, List<Double> posY, List<Double> posZ, List<Double> radius, List<Integer> parent) {
        this.type = type;
        this.index = index;
        this.parent = parent;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.radius = radius;
    }
}
