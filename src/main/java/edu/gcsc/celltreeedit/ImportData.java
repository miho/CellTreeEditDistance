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
    private int[] structure;
    private double[] radius;
    private double[] posX;
    private double[] posY;
    private double[] posZ;
    private int size;
    private double[] label;



    /**
     *
     * @param inputStream
     * @throws IOException
     */
    public void importData(InputStream inputStream, int localfct) throws IOException {
        try {
            List<SWCSegment> swcSegments = SWCSegment.fromStream(inputStream);
            // Fill the parents array
            size = swcSegments.size();//.get(swcSegments.size()-1).getIndex();
            parents = new int[size+1];
            structure = new int[size+1];
            radius= new double[size+1];
            posX= new double[size+1];
            posZ= new double[size+1];
            posY= new double[size+1];
            label= new double[size+1];
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
            if(localfct==0)
                set1();
            if(localfct==1)
                setTop2();
            if(localfct==2)
                setApproxLength();
            if(localfct==3)
                setApproxSurface();
            if(localfct==4)
                setApproxVolume();
            if(localfct==5)
                setTreeLength();
            if(localfct==6)
                setTreeVolume();
            if(localfct==7)
                setTreeSurface();
        } catch (IOException e) {
            System.out.println(e);
        }
           /*
    Label definition:
    1-
    2-
    3-
    4-
     */
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

    public double getRadius(int x){
        return this.radius[x];
    }


    public void set1(){
        Arrays.fill(label,1.0);
    }

    public void setTop2(){
        int n=100/152;
        System.out.print(n+"\n");
        Arrays.fill(label,n);
    }

    public double TreeLength(){
        double length=0;
        for(int i=1; i<size; i++){
            Vector children =this.getChildren(i);
            if(children==null)
                continue;
            else{
                for (int j = 0; j < children.size(); j++) {
                    double x = posX[(int) children.get(j)] - posX[i];
                    double y = posY[(int) children.get(j)] - posY[i];
                    double z = posZ[(int) children.get(j)] - posZ[i];
                    double lengthItoJ = Math.pow(x * x + y * y + z * z, 0.5);
                    length = length + lengthItoJ;
                }
            }
        }
        return length;
    }

    public double TreeSurface(){
        double surface=0;

        for(int i=1; i<size; i++){
            Vector children =this.getChildren(i);
            if(children==null)
                continue;
            else{
                for (int j = 0; j < children.size(); j++) {
                    double x = posX[(int) children.get(j)] - posX[i];
                    double y = posY[(int) children.get(j)] - posY[i];
                    double z = posZ[(int) children.get(j)] - posZ[i];
                    double lengthItoJ = Math.pow(x * x + y * y + z * z, 0.5);
                    surface= surface+ lengthItoJ*getRadius(j)*2*3.142;
                }
        }
        }
        return surface;
    }

    public double TreeVolume(){
        double volume=0;

        for(int i=1; i<size; i++){
            Vector children =this.getChildren(i);
            if(children==null)
                continue;
            else{
                for (int j = 0; j < children.size(); j++) {
                    double x = posX[(int) children.get(j)] - posX[i];
                    double y = posY[(int) children.get(j)] - posY[i];
                    double z = posZ[(int) children.get(j)] - posZ[i];
                    double lengthItoJ = Math.pow(x * x + y * y + z * z, 0.5);
                    volume= volume+ lengthItoJ*Math.pow(getRadius(j),2)*3.142;
                }
            }
        }
        return volume;
    }

    public void setTreeLength(){
        double length=this.TreeLength();
        Arrays.fill(label,length);
    }

    public void setTreeVolume(){
        double volume=this.TreeVolume();
        Arrays.fill(label,volume);
    }

    public void setTreeSurface(){
        double surface=this.TreeSurface();
        Arrays.fill(label,surface);
    }

    public void setApproxLength(){
        double x=posX[size]-posX[1];
        double y=posY[size]-posY[1];
        double z=posZ[size]-posZ[1];
        double approxlength=Math.pow(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2),0.5);
        Arrays.fill(label,approxlength);
    }

    public void setApproxVolume(){
        double averageradius=0;
        for(int i=1;i<size+1;i++)
            averageradius=averageradius+getRadius(i);
        averageradius=averageradius/size;

        double x=posX[size]-posX[1];
        double y=posY[size]-posY[1];
        double z=posZ[size]-posZ[1];
        double approxlength=Math.pow(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2),0.5);

        double approxvolume=Math.pow(averageradius,2)*approxlength*3.142;
        Arrays.fill(label,approxvolume);
    }

    public void setApproxSurface(){
        double averageradius=0;
        for(int i=1;i<size+1;i++)
            averageradius=averageradius+getRadius(i);
        averageradius=averageradius/size;

        double x=posX[size]-posX[1];
        double y=posY[size]-posY[1];
        double z=posZ[size]-posZ[1];
        double approxlength=Math.pow(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2),0.5);

        double approxsurface=averageradius*2*approxlength*3.142;
        Arrays.fill(label,approxsurface);
    }

    public void setAngle(){
        double angle=0;
        for(int i=1;i<size;i++){
            if(firstChild[i]!=-1){
                if ((nextSibling[firstChild[i]]==-1)) {
                    label[i]=90;                                    //senkrecht zum kind, falls nur ein Kind
                }else{
                    while(nextSibling[firstChild[i]]!=-1){

                    }
                }


            }else
                label[i]=0;
        }
    }

    public double[] getLabel() {
        return label;
    }
}