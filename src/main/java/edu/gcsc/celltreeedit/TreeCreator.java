package edu.gcsc.celltreeedit;

import eu.mihosoft.vswcreader.SWCSegment;
import node.Node;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * Created by Erid on 12.04.2018.
 */
public class TreeCreator implements parser.InputParser<NodeData>{

    private List<Node<NodeData>> nodeList= new ArrayList<>();
    private int[] firstChild, nextSibling; // Helparrays for finding out wether there is a branching point or an end structure
    private List<SWCSegment> swcSegments;

    public TreeCreator (InputStream inputStream){
        try {
            swcSegments = SWCSegment.fromStream(inputStream);
            int size = swcSegments.size();
            firstChild = new int[size+1];
            nextSibling = new int[size+1];
            Arrays.fill(firstChild,-1);
            Arrays.fill(nextSibling,-1);

            for (int i = 1; i < size; i++) {     // fill the two helparrays with structure information
                if (firstChild[swcSegments.get(i).getParent()] == -1)
                    firstChild[swcSegments.get(i).getParent()] = swcSegments.get(i).getIndex();
                else{
                    int next=firstChild[swcSegments.get(i).getParent()];
                    while(nextSibling[next]!=-1){
                        next=nextSibling[next];
                    }
                    nextSibling[next]=swcSegments.get(i).getIndex();
                }
            }
            }catch (IOException e) {
                System.out.println(e);
            }
    }

    public Node<NodeData> createTree(int label, int segNr){
        Node<NodeData> node=this.createTreeStructure(segNr);
        this.setNodeLabel(label);
        return node;
    }

    public Node<NodeData> createTreeStructure(int segNr){   // bei 0 anfangen    mit Listennummerierung anzugeben
        List<Integer> Index=new ArrayList<>();
        Index.add(swcSegments.get(segNr).getIndex());
        int type=swcSegments.get(segNr).getType();
        List<Double> PosX = new ArrayList<>();
        PosX.add(swcSegments.get(segNr).getPos().getX());
        List<Double> PosY = new ArrayList<>();
        PosY.add(swcSegments.get(segNr).getPos().getY());
        List<Double> PosZ = new ArrayList<>();
        PosZ.add(swcSegments.get(segNr).getPos().getZ());
        List<Double> R = new ArrayList<>();
        R.add(swcSegments.get(segNr).getR());
        List<Integer> Parent=new ArrayList<>();
        Parent.add(swcSegments.get(segNr).getParent());

        Node<NodeData> node= new Node<>(new NodeData(Index,type,PosX,PosY,PosZ,R,Parent));
        nodeList.add(node);
        int nr=segNr+1;
        while(!ifBranchOrEnd(nr)){                         // mit Arraynummerierung anzugeben
          //  System.out.println("Node"+nr+" hat nur ein Kind");
            node.getNodeData().getIndex().add(swcSegments.get(nr).getIndex());                  // mit Listennummerierung anzugeben
            node.getNodeData().setType(swcSegments.get(nr).getType());
            node.getNodeData().getPosX().add(swcSegments.get(nr).getPos().getX());
            node.getNodeData().getPosY().add(swcSegments.get(nr).getPos().getY());
            node.getNodeData().getPosZ().add(swcSegments.get(nr).getPos().getZ());
            node.getNodeData().getRadius().add(swcSegments.get(nr).getR());
            node.getNodeData().getParent().add(swcSegments.get(nr).getParent());
            nr=firstChild[nr];
        }

        if(ifBranchOrEnd(nr)){                                  // mit Arraynummerierung anzugeben
           // System.out.println(nr+" ist ein branch point oder ein endsegment");
            Vector children = this.getChildren(nr);
            for (int i = 0; i < children.size(); i++) {
         //       System.out.println("Kindknoten  "+children.get(i)+" wird an "+nr+" gehängt");
                int aufruf=(Integer) children.get(i)-1;
                node.addChild(createTreeStructure(aufruf));
            }
        }
        return node;
}

    public void setNodeLabel(int label){

        if(label==1){                                   //top1
            nodeList.forEach(t->t.getNodeData().setLabel(1));
        }
        if(label==2){                                   //top2
            double a=1.0/nodeList.size();
            nodeList.forEach(t->t.getNodeData().setLabel(a));
        }
        if(label==3){                                   //number of nodes of tree rooted at i
            nodeList.forEach(t->t.getNodeData().setLabel(t.getNodeCount()));
        }
        if(label==4){                                   //length of a section
            for(int i=0;i<nodeList.size(); i++){
                double length=0;
                Node<NodeData> node= nodeList.get(i);
                for(int j=1; j<node.getNodeData().getPosX().size();j++){
                    int k=j-1;
                    double x=nodeList.get(i).getNodeData().getPosX().get(j)-nodeList.get(i).getNodeData().getPosX().get(k);
                    double y=nodeList.get(i).getNodeData().getPosY().get(j)-nodeList.get(i).getNodeData().getPosY().get(k);
                    double z=nodeList.get(i).getNodeData().getPosZ().get(j)-nodeList.get(i).getNodeData().getPosZ().get(k);
                    double lengthPart=Math.pow(x * x + y * y + z * z, 0.5);
                    length=length+lengthPart;
                }
                System.out.println("node "+i+" hat length:"+length);
                node.getNodeData().setLabel(length);
            }
        }
        if(label==5){                                   //approxlength of a section
            for(int i=0;i<nodeList.size(); i++){
                double x=nodeList.get(i).getNodeData().getPosX().get(nodeList.size()-1)-nodeList.get(i).getNodeData().getPosX().get(0);
                double y=nodeList.get(i).getNodeData().getPosY().get(nodeList.size()-1)-nodeList.get(i).getNodeData().getPosY().get(0);
                double z=nodeList.get(i).getNodeData().getPosZ().get(nodeList.size()-1)-nodeList.get(i).getNodeData().getPosZ().get(0);
                double approxlength=Math.pow(x * x + y * y + z * z, 0.5);
                nodeList.get(i).getNodeData().setLabel(approxlength);
            }
        }
        if(label==5){                                   //length of tree rooted at 1
        }
        if(label==6){                                   //length of a section/length of tree rooted at i
        }
        if(label==7){                                   //length of tree rooted at i / length of tree rooted at 1
        }
        if(label==8){                                   //surface of a section
            for(int i=0;i<nodeList.size(); i++){
                double surface=0;
                Node<NodeData> node= nodeList.get(i);
                for(int j=1; j<node.getNodeData().getPosX().size();j++){
                    int k=j-1;
                    double x=nodeList.get(i).getNodeData().getPosX().get(j)-nodeList.get(i).getNodeData().getPosX().get(k);
                    double y=nodeList.get(i).getNodeData().getPosY().get(j)-nodeList.get(i).getNodeData().getPosY().get(k);
                    double z=nodeList.get(i).getNodeData().getPosZ().get(j)-nodeList.get(i).getNodeData().getPosZ().get(k);
                    double lengthPart=Math.pow(x * x + y * y + z * z, 0.5);
                    surface=surface+nodeList.get(i).getNodeData().getRadius().get(j)*2*lengthPart*3.142;
                }
                System.out.println("node "+i+" has surface "+surface);
                node.getNodeData().setLabel(surface);
            }
        }
        if(label==9){                                   //approxsurface of a section
            double approxlength=0;
            for(int i=0;i<nodeList.size(); i++){
                double x=nodeList.get(i).getNodeData().getPosX().get(nodeList.size()-1)-nodeList.get(i).getNodeData().getPosX().get(0);
                double y=nodeList.get(i).getNodeData().getPosY().get(nodeList.size()-1)-nodeList.get(i).getNodeData().getPosY().get(0);
                double z=nodeList.get(i).getNodeData().getPosZ().get(nodeList.size()-1)-nodeList.get(i).getNodeData().getPosZ().get(0);
                approxlength=Math.pow(x * x + y * y + z * z, 0.5);
            }
            double avgR=0;
            for(int i=0;i<nodeList.size();i++){
                for(int j=0;j<nodeList.get(i).getNodeData().getRadius().size();j++){
                    avgR=avgR+nodeList.get(i).getNodeData().getRadius().get(j);
                }
            }
            avgR=avgR/swcSegments.size();
            double approxSurface=approxlength*avgR*2*3.142;
            nodeList.forEach(t->t.getNodeData().setLabel(approxSurface));
        }
        if(label==9){                                   //surface of tree rooted at i
        }
        if(label==10){                                  //surface of tree rooted at 1
        }
        if(label==11){                                  //surface of a section/surface of tree rooted at i
        }
        if(label==12){                                  //surface of tree rooted at i / surface of tree rooted at 1
        }
        if(label==12){                                  //volume of a section
            for(int i=0;i<nodeList.size(); i++){
                double volume=0;
                Node<NodeData> node= nodeList.get(i);
                for(int j=1; j<node.getNodeData().getPosX().size();j++){
                    int k=j-1;
                    double x=nodeList.get(i).getNodeData().getPosX().get(j)-nodeList.get(i).getNodeData().getPosX().get(k);
                    double y=nodeList.get(i).getNodeData().getPosY().get(j)-nodeList.get(i).getNodeData().getPosY().get(k);
                    double z=nodeList.get(i).getNodeData().getPosZ().get(j)-nodeList.get(i).getNodeData().getPosZ().get(k);
                    double lengthPart=Math.pow(x * x + y * y + z * z, 0.5);
                    volume=volume+(Math.pow(nodeList.get(i).getNodeData().getRadius().get(j),2)*2*lengthPart*3.142);
                }
                System.out.println("node "+i+" has volume "+volume);
                node.getNodeData().setLabel(volume);
            }
        }
        if(label==14){                                   //approxvolume of a section
            double approxlength=0;
            for(int i=0;i<nodeList.size(); i++){
                double x=nodeList.get(i).getNodeData().getPosX().get(nodeList.size()-1)-nodeList.get(i).getNodeData().getPosX().get(0);
                double y=nodeList.get(i).getNodeData().getPosY().get(nodeList.size()-1)-nodeList.get(i).getNodeData().getPosY().get(0);
                double z=nodeList.get(i).getNodeData().getPosZ().get(nodeList.size()-1)-nodeList.get(i).getNodeData().getPosZ().get(0);
                approxlength=Math.pow(x * x + y * y + z * z, 0.5);
            }
            double avgR=0;
            for(int i=0;i<nodeList.size();i++){
                for(int j=0;j<nodeList.get(i).getNodeData().getRadius().size();j++){
                    avgR=avgR+nodeList.get(i).getNodeData().getRadius().get(j);
                }
            }
            avgR=avgR/swcSegments.size();
            double approxVolume=approxlength*avgR*2*3.142;
            nodeList.forEach(t->t.getNodeData().setLabel(approxVolume));
        }
        if(label==14){                                  //volume of tree rooted at i
        }
        if(label==15){                                  //volume of tree rooted at 1
        }
        if(label==16){                                  //volume of a section/volume of tree rooted at i
        }
        if(label==17){                                  //volume of tree rooted at i / volume of tree rooted at 1
        }
        if(label==18){                                  //angle between children of i
        }
        else{

        }
    }


    public List<Node<NodeData>> getNodeList() {
        return nodeList;
    }

    public int[] getFirstChild() {
        return firstChild;
    }

    public int[] getNextSibling() {
        return nextSibling;
    }

    public List<SWCSegment> getSwcSegments() {
        return swcSegments;
    }

    public boolean ifBranchOrEnd(int index){
        if(this.getChildren(index).size()==1)
            return false;
        else
            return true;
    }

    public Vector<Integer> getChildren(int nodeNr){
        int first= this.firstChild[nodeNr];
        Vector vector= new Vector();
        if(first==-1) {

        } else{
            vector.add(first);
            while (this.nextSibling[first]!=-1){
                vector.add(nextSibling[first]);
                first= nextSibling[first];
            }
        }
        return vector;
    }

    @Override
    public Node<NodeData> fromString(String s) {
        return null;
    }

}