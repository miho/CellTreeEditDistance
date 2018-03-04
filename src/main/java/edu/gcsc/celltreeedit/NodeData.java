package edu.gcsc.celltreeedit;

import java.util.Vector;

/**
 *
 */
public class NodeData {

    private double label;
    private int index;
    private double radius;
    private Vector<Double> position;
    private int type;
    private int parent;

    /**
     *
     * @param label
     */
    public NodeData(double label){
        this.label=label;
    }

    /**
     *
     * @param index
     * @param type
     * @param position
     * @param radius
     * @param parent
     * @param label
     */
    public NodeData(int index, int type, Vector<Double> position, double radius, int parent, double label){
        this.index=index;
        this.type=type;
        this.position=position;
        this.radius=radius;
        this.parent=parent;
        this.label=label;
    }

    /**
     *
     * @return
     */
    public double getLabel(){
        return this.label;
    }
}
