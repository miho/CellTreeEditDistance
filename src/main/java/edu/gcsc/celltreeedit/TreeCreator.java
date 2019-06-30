package edu.gcsc.celltreeedit;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import eu.mihosoft.ext.apted.node.Node;
import eu.mihosoft.ext.apted.parser.InputParser;
import eu.mihosoft.vswcreader.SWCSegment;

/**
 * Created by Erid on 12.04.2018.
 */
public class TreeCreator implements InputParser <NodeData> , Serializable {

    private List <Node<NodeData>> nodeList = new ArrayList <> ();
    private int[] firstChild, nextSibling; // Helparrays for finding out wether there is a branching point or an end structure
    private List <SWCSegment> swcSegments;

    public TreeCreator(InputStream inputStream) {
        try {
            swcSegments = SWCSegment.fromStream(inputStream);
            int size = swcSegments.size();
            firstChild = new int[size + 1];
            nextSibling = new int[size + 1];
            Arrays.fill(firstChild, -1);
            Arrays.fill(nextSibling, -1);

            for (int i = 1; i < size; i++) { // fill the two helparrays with structure information
                if (firstChild[swcSegments.get(i).getParent()] == -1)
                    firstChild[swcSegments.get(i).getParent()] = swcSegments.get(i).getIndex();
                else {
                    int next = firstChild[swcSegments.get(i).getParent()];
                    while (nextSibling[next] != -1) {
                        next = nextSibling[next];
                    }
                    nextSibling[next] = swcSegments.get(i).getIndex();
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public Node <NodeData> createTree(int label) {
        Node <NodeData> node = this.createTreeStructure(0);
        this.setNodeLabel(label);
        return node;
    }

    public Node <NodeData> createTreeStructure(int segNr) { // bei 0 anfangen    mit Listennummerierung anzugeben
        //Vaterknoten wird mitgenommen
        List <Integer> Index = new ArrayList <> ();
        List <Double> PosX = new ArrayList <> ();
        List <Double> PosY = new ArrayList <> ();
        List <Double> PosZ = new ArrayList <> ();
        List <Double> R = new ArrayList <> ();
        List <Integer> Parent = new ArrayList <> ();
        int vaterID = swcSegments.get(segNr).getParent();
        // System.out.println("vaterseg: "+vaterID);

        if (vaterID != -1) { //Vaterknoteninfos werden als erstes in die liste geschoben, dann die vom aufrufparameter
            Index.add(swcSegments.get(vaterID - 1).getIndex());
            //System.out.println(swcSegments.get(vaterID-1).getIndex());
            PosX.add(swcSegments.get(vaterID - 1).getPos().getX());
            PosY.add(swcSegments.get(vaterID - 1).getPos().getY());
            PosZ.add(swcSegments.get(vaterID - 1).getPos().getZ());
            R.add(swcSegments.get(vaterID - 1).getR());
            Parent.add(swcSegments.get(vaterID - 1).getParent());
        }
        Index.add(swcSegments.get(segNr).getIndex());
        int type = swcSegments.get(segNr).getType();
        PosX.add(swcSegments.get(segNr).getPos().getX());
        PosY.add(swcSegments.get(segNr).getPos().getY());
        PosZ.add(swcSegments.get(segNr).getPos().getZ());
        R.add(swcSegments.get(segNr).getR());
        Parent.add(swcSegments.get(segNr).getParent());

        Node <NodeData> node = new Node <> (new NodeData(Index, type, PosX, PosY, PosZ, R, Parent));
        nodeList.add(node); // vaterknoten wurde als erstes in die liste geschoben
        int nr = segNr + 1; // die echte segmentID
        while (!ifBranchOrEnd(nr)) { // mit Arraynummerierung anzugeben
            //  System.out.println("Node"+nr+" hat nur ein Kind");
            node.getNodeData().getIndex().add(swcSegments.get(nr).getIndex()); // mit Listennummerierung anzugeben
            node.getNodeData().setType(swcSegments.get(nr).getType());
            node.getNodeData().getPosX().add(swcSegments.get(nr).getPos().getX());
            node.getNodeData().getPosY().add(swcSegments.get(nr).getPos().getY());
            node.getNodeData().getPosZ().add(swcSegments.get(nr).getPos().getZ());
            node.getNodeData().getRadius().add(swcSegments.get(nr).getR());
            node.getNodeData().getParent().add(swcSegments.get(nr).getParent());
            nr = firstChild[nr];
        }

        if (ifBranchOrEnd(nr)) { // mit Arraynummerierung anzugeben
            // System.out.println(nr+" ist ein branch point oder ein endsegment");
            Vector children = this.getChildren(nr);
            for (int i = 0; i < children.size(); i++) {
                // System.out.println("Kindknoten  "+children.get(i)+" wird an "+nr+" gehängt");
                int aufruf = (Integer) children.get(i) - 1; // weil Listennummerierung
                node.addChild(createTreeStructure(aufruf));
            }
        }
        return node;
    }

    public void setNodeLabel(int label) {

        if (label == 1) { //top1
            nodeList.forEach(t -> t.getNodeData().setLabel(1));
        }
        if (label == 2) { //top2
            double a = 1.0 / nodeList.size();
            nodeList.forEach(t -> t.getNodeData().setLabel(a));
        }
        if (label == 3) { //number of nodes of tree rooted at i
            nodeList.forEach(t -> t.getNodeData().setLabel(t.getNodeCount()));
        }
        if (label == 4) { //length of a section
            for (int i = 0; i < nodeList.size(); i++) {
                double length = 0;
                Node <NodeData> node = nodeList.get(i);
                for (int j = 1; j < node.getNodeData().getPosX().size(); j++) {
                    int k = j - 1;
                    double x = nodeList.get(i).getNodeData().getPosX().get(j) - nodeList.get(i).getNodeData().getPosX().get(k);
                    double y = nodeList.get(i).getNodeData().getPosY().get(j) - nodeList.get(i).getNodeData().getPosY().get(k);
                    double z = nodeList.get(i).getNodeData().getPosZ().get(j) - nodeList.get(i).getNodeData().getPosZ().get(k);
                    double lengthPart = Math.pow(x * x + y * y + z * z, 0.5);
                    length = length + lengthPart;
                }
                node.getNodeData().setLabel(length);
            }
        }
        if (label == 5) { //approxlength of a section
            for (int i = 0; i < nodeList.size(); i++) {
                double x = nodeList.get(i).getNodeData().getPosX().get(nodeList.get(i).getNodeData().getPosX().size() - 1) - nodeList.get(i).getNodeData().getPosX().get(0);
                double y = nodeList.get(i).getNodeData().getPosY().get(nodeList.get(i).getNodeData().getPosY().size() - 1) - nodeList.get(i).getNodeData().getPosY().get(0);
                double z = nodeList.get(i).getNodeData().getPosZ().get(nodeList.get(i).getNodeData().getPosZ().size() - 1) - nodeList.get(i).getNodeData().getPosZ().get(0);
                double approxlength = Math.pow(x * x + y * y + z * z, 0.5);
                //  System.out.println(i+" : "+approxlength);
                nodeList.get(i).getNodeData().setLabel(approxlength);
            }
        }
        if (label == 6) { //length of tree rooted at 1
            double treeLength = 0;
            for (int i = 0; i < nodeList.size(); i++) {
                Node <NodeData> node = nodeList.get(i);
                for (int j = 1; j < node.getNodeData().getPosX().size(); j++) {
                    int k = j - 1;
                    double x = nodeList.get(i).getNodeData().getPosX().get(j) - nodeList.get(i).getNodeData().getPosX().get(k);
                    double y = nodeList.get(i).getNodeData().getPosY().get(j) - nodeList.get(i).getNodeData().getPosY().get(k);
                    double z = nodeList.get(i).getNodeData().getPosZ().get(j) - nodeList.get(i).getNodeData().getPosZ().get(k);
                    double lengthPart = Math.pow(x * x + y * y + z * z, 0.5);
                    treeLength = treeLength + lengthPart;
                }
            }
            for (int i = 0; i < nodeList.size(); i++) {
                Node <NodeData> node = nodeList.get(i);
                node.getNodeData().setLabel(treeLength);
            }
        }
        if (label == 7) { //length of a section / length of tree rooted at 1
            double treeLength = 0;
            for (int i = 0; i < nodeList.size(); i++) {
                double length = 0;
                Node <NodeData> node = nodeList.get(i);
                for (int j = 1; j < node.getNodeData().getPosX().size(); j++) {
                    int k = j - 1;
                    double x = nodeList.get(i).getNodeData().getPosX().get(j) - nodeList.get(i).getNodeData().getPosX().get(k);
                    double y = nodeList.get(i).getNodeData().getPosY().get(j) - nodeList.get(i).getNodeData().getPosY().get(k);
                    double z = nodeList.get(i).getNodeData().getPosZ().get(j) - nodeList.get(i).getNodeData().getPosZ().get(k);
                    double lengthPart = Math.pow(x * x + y * y + z * z, 0.5);
                    length = length + lengthPart;
                    treeLength = treeLength + length;
                }
            }
            for (int i = 0; i < nodeList.size(); i++) {
                double length = 0;
                Node <NodeData> node = nodeList.get(i);
                for (int j = 1; j < node.getNodeData().getPosX().size(); j++) {
                    int k = j - 1;
                    double x = nodeList.get(i).getNodeData().getPosX().get(j) - nodeList.get(i).getNodeData().getPosX().get(k);
                    double y = nodeList.get(i).getNodeData().getPosY().get(j) - nodeList.get(i).getNodeData().getPosY().get(k);
                    double z = nodeList.get(i).getNodeData().getPosZ().get(j) - nodeList.get(i).getNodeData().getPosZ().get(k);
                    double lengthPart = Math.pow(x * x + y * y + z * z, 0.5);
                    length = length + lengthPart;
                }
                double lengthRelation = length / treeLength;
                nodeList.get(i).getNodeData().setLabel(lengthRelation);
            }
        }
        if (label == 8) { //surface of a section
            for (int i = 0; i < nodeList.size(); i++) {
                double surface = 0;
                Node <NodeData> node = nodeList.get(i);
                for (int j = 1; j < node.getNodeData().getPosX().size(); j++) {
                    int k = j - 1;
                    double x = nodeList.get(i).getNodeData().getPosX().get(j) - nodeList.get(i).getNodeData().getPosX().get(k);
                    double y = nodeList.get(i).getNodeData().getPosY().get(j) - nodeList.get(i).getNodeData().getPosY().get(k);
                    double z = nodeList.get(i).getNodeData().getPosZ().get(j) - nodeList.get(i).getNodeData().getPosZ().get(k);
                    double lengthPart = Math.pow(x * x + y * y + z * z, 0.5);
                    double surfacepart = nodeList.get(i).getNodeData().getRadius().get(j) * 2 * lengthPart * 3.142 + nodeList.get(i).getNodeData().getRadius().get(j) * nodeList.get(i).getNodeData().getRadius().get(j) * 2 * 3.142;
                    surface = surface + surfacepart;
                }
                node.getNodeData().setLabel(surface);
            }
        }
        if (label == 9) { //approxsurface of a section
            double approxlength = 0;
            for (int i = 0; i < nodeList.size(); i++) {
                double x = nodeList.get(i).getNodeData().getPosX().get(nodeList.get(i).getNodeData().getPosX().size() - 1) - nodeList.get(i).getNodeData().getPosX().get(0);
                double y = nodeList.get(i).getNodeData().getPosY().get(nodeList.get(i).getNodeData().getPosY().size() - 1) - nodeList.get(i).getNodeData().getPosY().get(0);
                double z = nodeList.get(i).getNodeData().getPosZ().get(nodeList.get(i).getNodeData().getPosZ().size() - 1) - nodeList.get(i).getNodeData().getPosZ().get(0);
                approxlength = Math.pow(x * x + y * y + z * z, 0.5);
                double avgR = 0;
                for (int j = 0; j < nodeList.get(i).getNodeData().getRadius().size(); j++) {
                    avgR = avgR + nodeList.get(i).getNodeData().getRadius().get(j);
                }
                avgR = avgR / nodeList.get(i).getNodeData().getRadius().size();
                double approxSurface = approxlength * avgR * 2 * 3.142 + avgR * avgR * 2 * 3.142;
                nodeList.get(i).getNodeData().setLabel(approxSurface);
            }
        }
        if (label == 10) { //surface of tree rooted at 1
            double treeSurface = 0;
            for (int i = 0; i < nodeList.size(); i++) {
                Node <NodeData> node = nodeList.get(i);
                for (int j = 1; j < node.getNodeData().getPosX().size(); j++) {
                    int k = j - 1;
                    double x = nodeList.get(i).getNodeData().getPosX().get(j) - nodeList.get(i).getNodeData().getPosX().get(k);
                    double y = nodeList.get(i).getNodeData().getPosY().get(j) - nodeList.get(i).getNodeData().getPosY().get(k);
                    double z = nodeList.get(i).getNodeData().getPosZ().get(j) - nodeList.get(i).getNodeData().getPosZ().get(k);
                    double lengthPart = Math.pow(x * x + y * y + z * z, 0.5);
                    double surfacepart = nodeList.get(i).getNodeData().getRadius().get(j) * 2 * lengthPart * 3.142 + nodeList.get(i).getNodeData().getRadius().get(j) * nodeList.get(i).getNodeData().getRadius().get(j) * 2 * 3.142;
                    treeSurface = treeSurface + surfacepart;
                }
            }
            for (int i = 0; i < nodeList.size(); i++) {
                Node < NodeData > node = nodeList.get(i);
                node.getNodeData().setLabel(treeSurface);
            }
        }
        if (label == 11) { //surface of a section/surface of tree rooted at 1
            double treeSurface = 0;
            for (int i = 0; i < nodeList.size(); i++) {
                Node <NodeData> node = nodeList.get(i);
                for (int j = 1; j < node.getNodeData().getPosX().size(); j++) {
                    int k = j - 1;
                    double x = nodeList.get(i).getNodeData().getPosX().get(j) - nodeList.get(i).getNodeData().getPosX().get(k);
                    double y = nodeList.get(i).getNodeData().getPosY().get(j) - nodeList.get(i).getNodeData().getPosY().get(k);
                    double z = nodeList.get(i).getNodeData().getPosZ().get(j) - nodeList.get(i).getNodeData().getPosZ().get(k);
                    double lengthPart = Math.pow(x * x + y * y + z * z, 0.5);
                    double surfacepart = nodeList.get(i).getNodeData().getRadius().get(j) * 2 * lengthPart * 3.142 + nodeList.get(i).getNodeData().getRadius().get(j) * nodeList.get(i).getNodeData().getRadius().get(j) * 2 * 3.142;
                    treeSurface = treeSurface + surfacepart;
                }
            }
            for (int i = 0; i < nodeList.size(); i++) {
                double surface = 0;
                Node <NodeData> node = nodeList.get(i);
                for (int j = 1; j < node.getNodeData().getPosX().size(); j++) {
                    int k = j - 1;
                    double x = nodeList.get(i).getNodeData().getPosX().get(j) - nodeList.get(i).getNodeData().getPosX().get(k);
                    double y = nodeList.get(i).getNodeData().getPosY().get(j) - nodeList.get(i).getNodeData().getPosY().get(k);
                    double z = nodeList.get(i).getNodeData().getPosZ().get(j) - nodeList.get(i).getNodeData().getPosZ().get(k);
                    double lengthPart = Math.pow(x * x + y * y + z * z, 0.5);
                    double surfacepart = nodeList.get(i).getNodeData().getRadius().get(j) * 2 * lengthPart * 3.142 + nodeList.get(i).getNodeData().getRadius().get(j) * nodeList.get(i).getNodeData().getRadius().get(j) * 2 * 3.142;
                    surface = surface + surfacepart;
                }
                double surfaceRelation = surface / treeSurface;
                node.getNodeData().setLabel(surfaceRelation);
            }
        }
        if (label == 12) { //volume of a section
            for (int i = 0; i < nodeList.size(); i++) {
                double volume = 0;
                Node <NodeData> node = nodeList.get(i);
                for (int j = 1; j < node.getNodeData().getPosX().size(); j++) {
                    int k = j - 1;
                    double x = nodeList.get(i).getNodeData().getPosX().get(j) - nodeList.get(i).getNodeData().getPosX().get(k);
                    double y = nodeList.get(i).getNodeData().getPosY().get(j) - nodeList.get(i).getNodeData().getPosY().get(k);
                    double z = nodeList.get(i).getNodeData().getPosZ().get(j) - nodeList.get(i).getNodeData().getPosZ().get(k);
                    double lengthPart = Math.pow(x * x + y * y + z * z, 0.5);
                    volume = volume + (Math.pow(nodeList.get(i).getNodeData().getRadius().get(j), 2) * lengthPart * 3.142);
                }
                node.getNodeData().setLabel(volume);
            }
        }
        if (label == 13) { //approxvolume of a section
            double approxlength = 0;
            for (int i = 0; i < nodeList.size(); i++) {
                double x = nodeList.get(i).getNodeData().getPosX().get(nodeList.get(i).getNodeData().getPosX().size() - 1) - nodeList.get(i).getNodeData().getPosX().get(0);
                double y = nodeList.get(i).getNodeData().getPosY().get(nodeList.get(i).getNodeData().getPosY().size() - 1) - nodeList.get(i).getNodeData().getPosY().get(0);
                double z = nodeList.get(i).getNodeData().getPosZ().get(nodeList.get(i).getNodeData().getPosZ().size() - 1) - nodeList.get(i).getNodeData().getPosZ().get(0);
                approxlength = Math.pow(x * x + y * y + z * z, 0.5);
                double avgR = 0;
                for (int j = 0; j < nodeList.get(i).getNodeData().getRadius().size(); j++) {
                    avgR = avgR + nodeList.get(i).getNodeData().getRadius().get(j);
                }
                avgR = avgR / nodeList.get(i).getNodeData().getRadius().size();
                double approxVolume = approxlength * avgR * avgR * 3.142;
                nodeList.get(i).getNodeData().setLabel(approxVolume);
            }
        }
        if (label == 14) { //volume of tree rooted at 1
            double treeVolume = 0;
            for (int i = 0; i < nodeList.size(); i++) {
                Node <NodeData> node = nodeList.get(i);
                for (int j = 1; j < node.getNodeData().getPosX().size(); j++) {
                    int k = j - 1;
                    double x = nodeList.get(i).getNodeData().getPosX().get(j) - nodeList.get(i).getNodeData().getPosX().get(k);
                    double y = nodeList.get(i).getNodeData().getPosY().get(j) - nodeList.get(i).getNodeData().getPosY().get(k);
                    double z = nodeList.get(i).getNodeData().getPosZ().get(j) - nodeList.get(i).getNodeData().getPosZ().get(k);
                    double lengthPart = Math.pow(x * x + y * y + z * z, 0.5);
                    treeVolume = treeVolume + (Math.pow(nodeList.get(i).getNodeData().getRadius().get(j), 2) * lengthPart * 3.142);
                }
            }
            for (int i = 0; i < nodeList.size(); i++) {
                Node < NodeData > node = nodeList.get(i);
                node.getNodeData().setLabel(treeVolume);
            }
        }
        if (label == 15) { //volume of a section/volume of tree rooted at 1
            double treeVolume = 0;
            for (int i = 0; i < nodeList.size(); i++) {
                Node <NodeData> node = nodeList.get(i);
                for (int j = 1; j < node.getNodeData().getPosX().size(); j++) {
                    int k = j - 1;
                    double x = nodeList.get(i).getNodeData().getPosX().get(j) - nodeList.get(i).getNodeData().getPosX().get(k);
                    double y = nodeList.get(i).getNodeData().getPosY().get(j) - nodeList.get(i).getNodeData().getPosY().get(k);
                    double z = nodeList.get(i).getNodeData().getPosZ().get(j) - nodeList.get(i).getNodeData().getPosZ().get(k);
                    double lengthPart = Math.pow(x * x + y * y + z * z, 0.5);
                    treeVolume = treeVolume + (Math.pow(nodeList.get(i).getNodeData().getRadius().get(j), 2) * lengthPart * 3.142);
                }
            }
            for (int i = 0; i < nodeList.size(); i++) {
                double volume = 0;
                Node <NodeData> node = nodeList.get(i);
                for (int j = 1; j < node.getNodeData().getPosX().size(); j++) {
                    int k = j - 1;
                    double x = nodeList.get(i).getNodeData().getPosX().get(j) - nodeList.get(i).getNodeData().getPosX().get(k);
                    double y = nodeList.get(i).getNodeData().getPosY().get(j) - nodeList.get(i).getNodeData().getPosY().get(k);
                    double z = nodeList.get(i).getNodeData().getPosZ().get(j) - nodeList.get(i).getNodeData().getPosZ().get(k);
                    double lengthPart = Math.pow(x * x + y * y + z * z, 0.5);
                    volume = volume + (Math.pow(nodeList.get(i).getNodeData().getRadius().get(j), 2) * lengthPart * 3.142);
                }
                double volumeRelation = volume / treeVolume;
                node.getNodeData().setLabel(volumeRelation);
            }
        }
        if (label > 15) {
            System.out.println("Label nicht erkannt");
        }
    }

    public boolean ifBranchOrEnd(int index) {
        if (this.getChildren(index).size() == 1)
            return false;
        else
            return true;
    }

    public Vector <Integer> getChildren(int nodeNr) {
        int first = this.firstChild[nodeNr];
        Vector vector = new Vector();
        if (first == -1) {

        } else {
            vector.add(first);
            while (this.nextSibling[first] != -1) {
                vector.add(nextSibling[first]);
                first = nextSibling[first];
            }
        }
        return vector;
    }

    public List <Node<NodeData>> getNodeList() {
        return nodeList;
    }

    public int[] getFirstChild() {
        return firstChild;
    }

    public int[] getNextSibling() {
        return nextSibling;
    }

    public List <SWCSegment> getSwcSegments() {
        return swcSegments;
    }

    @Override
    public Node <NodeData> fromString(String s) {
        return null;
    }

}