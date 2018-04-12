package edu.gcsc.celltreeedit;

import javax.xml.soap.Node;

/**
 *
 */
public class NodeData {

    double label;
    int structure;
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

    public NodeData(int index, int structure, double posX, double posY, double posZ, double radius, int parent){
        this.index=index;
        this.structure=structure;
        this.posX=posX;
        this.posY=posY;
        this.posZ=posZ;
        this.radius=radius;
        this.parent=parent;
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
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

    public int getStructure() {

        return structure;
    }

    public void setStructure(int structure) {
        this.structure = structure;
    }
}
