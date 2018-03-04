package edu.gcsc.celltreeedit;

import eu.mihosoft.vswcreader.SWCSegment;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * The class will save the tree- and label-relevant Data of a Swc-file in four arrays.
 */
public class ImportData {
    private int[] parents, firstChild, nextSibling;
    private double[] label;
    private int[] structure;
    private double[] radius;
    private double[] posX;
    private double[] posY;
    private double[] posZ;


    /**
     *
     * @param inputStream
     * @throws IOException
     */
    public void importData(InputStream inputStream) throws IOException {
        try {
            List<SWCSegment> swcSegments = SWCSegment.fromStream(inputStream);
            // Fill the parents array
            int size = swcSegments.size();//.get(swcSegments.size()-1).getIndex();
            parents = new int[size+1];
            label= new double[size+1];
            structure = new int[size+1];
            radius= new double[size+1];
            posX= new double[size+1];
            posZ= new double[size+1];
            posY= new double[size+1];

            swcSegments.forEach(t -> parents[t.getIndex()] = t.getParent());

            //fill the arrays
            swcSegments.forEach(t -> structure[t.getIndex()]=t.getType());

            swcSegments.forEach(t -> radius[t.getIndex()]=t.getR());

            swcSegments.forEach(t -> posX[t.getIndex()]=t.getPos().getX());

            swcSegments.forEach(t -> posY[t.getIndex()]=t.getPos().getY());

            swcSegments.forEach(t -> posZ[t.getIndex()]=t.getPos().getZ());

            firstChild = new int[size+1];
            nextSibling = new int[size+1];
            int[] lastChild = new int[size+1];

            for (int i = 2; i < size+1;i++) {
                int p = parents[i];
                if (firstChild[p] ==0) {
                    firstChild[p] = i;
                    lastChild[p] = i;
                } else {
                    nextSibling[lastChild[p]] = i;
                    lastChild[p] = i;
                }
            }

        } catch (IOException e) {
            System.out.println(e);
        }

    }


    /**
     *
     * @param nodeNr
     * @return
     */
    public Vector<Integer> getChildren(int nodeNr){
        int first= this.firstChild[nodeNr];
        Vector vector= new Vector();
        if(first==0) {
            return null;
        } else{
            vector.add(first);
            while (this.nextSibling[first]!=0){
                vector.add(nextSibling[first]);
                first= nextSibling[first];
            }
        }
        return vector;
    }

    /**
     *
     * @param z
     * @return
     */
    public double getLabel(int z){
        return label[z];
    }

    public void top1(){
        Arrays.fill(label,1);
    }

    public void top2(){
        double value=1/parents.length;
        Arrays.fill(label,value);
    }

    public void lSec(){
    }
}
