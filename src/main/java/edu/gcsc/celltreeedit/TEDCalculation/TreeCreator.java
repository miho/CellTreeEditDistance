package edu.gcsc.celltreeedit.TEDCalculation;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import edu.gcsc.celltreeedit.Utils;
import eu.mihosoft.ext.apted.node.Node;
import eu.mihosoft.ext.apted.parser.InputParser;
import eu.mihosoft.vswcreader.SWCSegment;
import org.apache.commons.math3.util.MathArrays;


/**
 * Created by Erid on 12.04.2018.
 * Used to create a tree from a list of swcSegments
 */
public class TreeCreator implements InputParser <NodeData> , Serializable {

    private List<Node<NodeData>> nodeList = new ArrayList <> ();
    private int[] firstChild, nextSibling; // Helparrays for finding out wether there is a branching point or an end structure
    private List<SWCSegment> swcSegments;

    public TreeCreator(InputStream inputStream) {
        try {
            swcSegments = SWCSegment.fromStream(inputStream);
            int size = swcSegments.size();
            firstChild = new int[size + 1];
            nextSibling = new int[size + 1];
            Arrays.fill(firstChild, -1);
            Arrays.fill(nextSibling, -1);

            for (int i = 1; i < size; i++) { // fill the two helparrays with structure information
                int parentOfCurrentSWCSegment = swcSegments.get(i).getParent();
                if (parentOfCurrentSWCSegment == -1) {
                    parentOfCurrentSWCSegment = 1;
                    swcSegments.get(i).setParent(1);
                }
                if (firstChild[parentOfCurrentSWCSegment] == -1)
                    firstChild[parentOfCurrentSWCSegment] = swcSegments.get(i).getIndex();
                else {
                    int next = firstChild[parentOfCurrentSWCSegment];
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

    public Node<NodeData> createTree(int label) {
        Node<NodeData> node = this.createTreeStructure(0);
        this.setNodeLabel(label);
        return node;
    }

    public Node<NodeData> createTreeStructure(int segNr) { // bei 0 anfangen    mit Listennummerierung anzugeben
        // Vaterknoten wird mitgenommen
        List<Integer> Index = new ArrayList<>();
        List<Double> PosX = new ArrayList<>();
        List<Double> PosY = new ArrayList<>();
        List<Double> PosZ = new ArrayList<>();
        List<Double> R = new ArrayList<>();
        List<Integer> Parent = new ArrayList<>();
        int vaterID = swcSegments.get(segNr).getParent();
        // System.out.println("vaterseg: "+vaterID);

        if (vaterID != -1) { // Vaterknoteninfos werden als erstes in die liste geschoben, dann die vom aufrufparameter
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

        Node<NodeData> node = new Node<> (new NodeData(Index, type, PosX, PosY, PosZ, R, Parent));
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

        if (label == 1) { // top_1
            nodeList.forEach(t -> t.getNodeData().setLabel(1));
        }

        if (label == 2) { // top_2
            double a = 1.0 / nodeList.size();
            nodeList.forEach(t -> t.getNodeData().setLabel(a));
        }

        if (label == 3) { // l_sec length of t[i]
            nodeList.forEach(node -> {
                node.getNodeData().setLabel(calculate_l_sec(node));
            });
        }

        if (label == 4) { // l_soma length from t[i] to soma
            calculate_l_soma(nodeList.get(0));
        }

        if (label == 5) { // l_tree length of T[i]
            calculate_l_tree(nodeList.get(0));
        }

        if (label == 6) { // L_sec length of t[i] / length of T
            calculate_L_sec();
        }

        if (label == 7) { // L_soma length from t[i] to soma / length of T
            calculate_L_soma();
        }

        if (label == 8) { // L_tree length of T[i] / length of T
            calculate_L_tree();
        }

        if (label == 9) { // v_sec volume of t[i]
            nodeList.forEach(node -> {
                node.getNodeData().setLabel(calculate_v_sec(node));
            });
        }

        // assumed first node in nodeList is root!
        if (label == 10) { // v_soma volume from t[i] to soma
            calculate_v_soma(nodeList.get(0));
        }

        if (label == 11) { // v_tree volume of T[i]
            calculate_v_tree(nodeList.get(0));
        }

        if (label == 12) { // V_sec volume of t[i] / volume of T
            calculate_V_sec();
        }

        if (label == 13) { // V_soma volume from t[i] to soma / volume of T
            calculate_V_soma();
        }

        if (label == 14) { // V_tree volume of T[i] / volume of T
            calculate_V_tree();
        }

        if (label == 15) { // s_sec surface of t[i]
            nodeList.forEach(node -> {
                node.getNodeData().setLabel(calculate_s_sec(node));
            });
        }

        // assumed first node in nodeList is root!
        if (label == 16) { // s_soma surface from t[i] to soma
            calculate_s_soma(nodeList.get(0));
        }

        if (label == 17) { // s_tree surface of T[i]
            calculate_s_tree(nodeList.get(0));
        }

        if (label == 18) { // S_sec surface of t[i] / surface of T
            calculate_S_sec();
        }

        if (label == 19) { // S_soma surface from t[i] to soma / surface of T
            calculate_S_soma();
        }

        if (label == 20) { // S_tree surface of T[i] / surface of T
            calculate_S_tree();
        }

        if (label == 21) { // vS_sec volume of t[i] / surface of T
            calculate_vS_sec();
        }

        if (label == 22) { // a_sec angle between children of t[i]
            calculate_a_sec(nodeList.get(0));
        }

        if (label == 23) { // r_sec average radius of segment
            nodeList.forEach(node -> {
                node.getNodeData().setLabel(calculate_r_sec(node));
            });
        }

        if (label == 24) {
            double radiusSum = 0;
            for (Node<NodeData> node: nodeList) {
                double r = calculate_r_sec(node);
                node.getNodeData().setLabel(r);
                radiusSum += r;
            }
            for (Node<NodeData> node: nodeList) {
                node.getNodeData().setLabel(node.getNodeData().getLabel() / radiusSum);
            }
        }

        if (label > 24) {
            throw new RuntimeException("Label is not valid");
        }
    }

    private double calculateLengthAtIndex(Node<NodeData> currentNode, int j) {
        double x = currentNode.getNodeData().getPosX().get(j) - currentNode.getNodeData().getPosX().get(j-1);
        double y = currentNode.getNodeData().getPosY().get(j) - currentNode.getNodeData().getPosY().get(j-1);
        double z = currentNode.getNodeData().getPosZ().get(j) - currentNode.getNodeData().getPosZ().get(j-1);
        return Math.sqrt(x * x + y * y + z * z);
    }

    private double calculateLengthSquaredAtIndex(Node<NodeData> currentNode, int j) {
        double x = currentNode.getNodeData().getPosX().get(j) - currentNode.getNodeData().getPosX().get(j-1);
        double y = currentNode.getNodeData().getPosY().get(j) - currentNode.getNodeData().getPosY().get(j-1);
        double z = currentNode.getNodeData().getPosZ().get(j) - currentNode.getNodeData().getPosZ().get(j-1);
        return x * x + y * y + z * z;
    }

    /**
     * Calculates label l_sec for currentNode.
     * @param currentNode
     * @return
     */
    private double calculate_l_sec(Node<NodeData> currentNode) {
        double length = 0;
        // j=1 --> parent node skipped
        for (int j = 1; j < currentNode.getNodeData().getPosX().size(); j++) {
            length += calculateLengthAtIndex(currentNode, j);
        }
        return length;
    }

    // calculate l_soma. is called with root node. calculates l_sec for root node then calls calculate_l_soma_rec()
    private void calculate_l_soma(Node<NodeData> node) {
        node.getNodeData().setLabel(calculate_l_sec(node));
        calculate_l_soma_rec(node);
    }

    private void calculate_l_soma_rec(Node<NodeData> parentNode) {
        List<Node<NodeData>> childNodes = parentNode.getChildren();
        if (childNodes.isEmpty()) {
            return;
        }
        double parentLabel = parentNode.getNodeData().getLabel();
        for (Node<NodeData> childNode: childNodes) {
            childNode.getNodeData().setLabel(calculate_l_sec(childNode) + parentLabel);
            calculate_l_soma_rec(childNode);
        }
    }

    // calculate l_tree. is called with parentNode, calculates l_tree bottom up in Postorder
    private double calculate_l_tree(Node<NodeData> parentNode) {
        List<Node<NodeData>> childNodes = parentNode.getChildren();
        if (childNodes.isEmpty()) {
            double l_sec = calculate_l_sec(parentNode);
            parentNode.getNodeData().setLabel(l_sec);
            return l_sec;
        }
        double l_tree = 0;
        for (Node<NodeData> childNode: childNodes) {
            l_tree += calculate_l_tree(childNode);
        }
        l_tree += calculate_l_sec(parentNode);
        parentNode.getNodeData().setLabel(l_tree);
        return l_tree;
    }

    // calculate L_sec. is called with parentNode, first calculates all l_sec and lengthT. Second L_sec
    private void calculate_L_sec() {
        double lengthT = 0;
        for (Node<NodeData> currentNode: nodeList) {
            double l_sec = calculate_l_sec(currentNode);
            currentNode.getNodeData().setLabel(l_sec); // first set with l_sec later divide by lengthT
            lengthT += l_sec;
        }
        for (Node<NodeData> currentNode: nodeList) {
            currentNode.getNodeData().setLabel(currentNode.getNodeData().getLabel()/lengthT);
        }
    }

    // calculate L_soma. could be optimized as lengthT could be calculated during calculate_l_soma call
    private void calculate_L_soma() {
        double lengthT = 0;
        for (Node<NodeData> currentNode: nodeList) {
            lengthT += calculate_l_sec(currentNode);
        }
        calculate_l_soma(nodeList.get(0));
        for (Node<NodeData> currentNode: nodeList) {
            currentNode.getNodeData().setLabel(currentNode.getNodeData().getLabel()/lengthT);
        }
    }

    // calculate L_tree.
    private void calculate_L_tree() {
        calculate_l_tree(nodeList.get(0));
        double lengthT = nodeList.get(0).getNodeData().getLabel();
        for (Node<NodeData> currentNode: nodeList) {
            currentNode.getNodeData().setLabel(currentNode.getNodeData().getLabel()/lengthT);
        }
    }

    /**
     * Calculates label v_sec for currentNode.
     * @param currentNode
     * @return
     */
    private double calculate_v_sec(Node<NodeData> currentNode) {
        double volume = 0;
        for (int j = 1; j < currentNode.getNodeData().getPosX().size(); j++) {
            double length = calculateLengthAtIndex(currentNode, j);
            double r1 = currentNode.getNodeData().getRadius().get(j-1); // radius of node closer to parent. node with index 0 is parent node
            double r2 = currentNode.getNodeData().getRadius().get(j); // radius of node closer to leafs
            double volumePart = length*Math.PI/3*(r1*r1+r1*r2+r2*r2);
            volume += volumePart;
        }
        return volume;
    }

    // calculate v_soma. is called with root node. calculates v_sec for root node then calls calculate_v_soma_rec()
    private void calculate_v_soma(Node<NodeData> node) {
        node.getNodeData().setLabel(calculate_v_sec(node));
        calculate_v_soma_rec(node);
    }

    private void calculate_v_soma_rec(Node<NodeData> parentNode) {
        List<Node<NodeData>> childNodes = parentNode.getChildren();
        if (childNodes.isEmpty()) {
            return;
        }
        double parentLabel = parentNode.getNodeData().getLabel();
        for (Node<NodeData> childNode: childNodes) {
            childNode.getNodeData().setLabel(calculate_v_sec(childNode) + parentLabel);
            calculate_v_soma_rec(childNode);
        }
    }

    // calculate v_tree. is called with parentNode, calculates v_tree bottom up in Postorder
    private double calculate_v_tree(Node<NodeData> parentNode) {
        List<Node<NodeData>> childNodes = parentNode.getChildren();
        if (childNodes.isEmpty()) {
            double v_sec = calculate_v_sec(parentNode);
            parentNode.getNodeData().setLabel(v_sec);
            return v_sec;
        }
        double v_tree = 0;
        for (Node<NodeData> childNode: childNodes) {
            v_tree += calculate_v_tree(childNode);
        }
        v_tree += calculate_v_sec(parentNode);
        parentNode.getNodeData().setLabel(v_tree);
        return v_tree;
    }

    // calculate V_sec. is called with parentNode, first calculates all v_sec and volumeT. Second V_sec
    private void calculate_V_sec() {
        double volumeT = 0;
        for (Node<NodeData> currentNode: nodeList) {
            double v_sec = calculate_v_sec(currentNode);
            currentNode.getNodeData().setLabel(v_sec); // first set with v_sec later divide by volumeT
            volumeT += v_sec;
        }
        for (Node<NodeData> currentNode: nodeList) {
            currentNode.getNodeData().setLabel(currentNode.getNodeData().getLabel()/volumeT);
        }
    }

    // calculate V_soma. can be optimized as volumeT could be calculated during calculate_v_soma call
    private void calculate_V_soma() {
        double volumeT = 0;
        for (Node<NodeData> currentNode: nodeList) {
            volumeT += calculate_v_sec(currentNode);
        }
        calculate_v_soma(nodeList.get(0));
        for (Node<NodeData> currentNode: nodeList) {
            currentNode.getNodeData().setLabel(currentNode.getNodeData().getLabel()/volumeT);
        }
    }

    // calculate V_tree.
    private void calculate_V_tree() {
        calculate_v_tree(nodeList.get(0));
        double volumeT = nodeList.get(0).getNodeData().getLabel();
        for (Node<NodeData> currentNode: nodeList) {
            currentNode.getNodeData().setLabel(currentNode.getNodeData().getLabel()/volumeT);
        }
    }

    /**
     * Calculates label s_sec for currentNode.
     * @param currentNode
     * @return
     */
    private double calculate_s_sec(Node<NodeData> currentNode) {
        double surface = 0;
        double lengthSquared, surfacePart;
        Double r1, r2;
        for (int j = 1; j < currentNode.getNodeData().getPosX().size(); j++) {
            lengthSquared = calculateLengthSquaredAtIndex(currentNode, j);
            r1 = currentNode.getNodeData().getRadius().get(j-1); // radius of node closer to root
            r2 = currentNode.getNodeData().getRadius().get(j); // radius of node closer to leafs
//            if (Utils.doublesAlmostEqual(r1, r2, 1e-12, 1)) {
//                surfacePart = 2 * r1 * Math.PI * length;
//            } else {
//                surfacePart = (r1+r2)*Math.PI*Math.sqrt((r1-r2)*(r1-r2)+length*length);
//            }
            surfacePart = (r1+r2)*Math.PI*Math.sqrt((r1-r2)*(r1-r2)+lengthSquared);
            surface += surfacePart;
        }
        return surface;
    }

    // calculate s_soma. is called with root node. calculates s_sec for root node then calls calculate_s_soma_rec()
    private void calculate_s_soma(Node<NodeData> node) {
        node.getNodeData().setLabel(calculate_s_sec(node));
        calculate_s_soma_rec(node);
    }

    private void calculate_s_soma_rec(Node<NodeData> parentNode) {
        List<Node<NodeData>> childNodes = parentNode.getChildren();
        if (childNodes.isEmpty()) {
            return;
        }
        double parentLabel = parentNode.getNodeData().getLabel();
        for (Node<NodeData> childNode: childNodes) {
            childNode.getNodeData().setLabel(calculate_s_sec(childNode) + parentLabel);
            calculate_s_soma_rec(childNode);
        }
    }

    // calculate s_tree. is called with parentNode, calculates s_tree bottom up in Postorder
    private double calculate_s_tree(Node<NodeData> parentNode) {
        List<Node<NodeData>> childNodes = parentNode.getChildren();
        if (childNodes.isEmpty()) {
            double s_sec = calculate_s_sec(parentNode);
            parentNode.getNodeData().setLabel(s_sec);
            return s_sec;
        }
        double s_tree = 0;
        for (Node<NodeData> childNode: childNodes) {
            s_tree += calculate_s_tree(childNode);
        }
        s_tree += calculate_s_sec(parentNode);
        parentNode.getNodeData().setLabel(s_tree);
        return s_tree;
    }

    // calculate S_sec. is called with parentNode, first calculates all s_sec and surfaceT. Second S_sec
    private void calculate_S_sec() {
        double surfaceT = 0;
        for (Node<NodeData> currentNode: nodeList) {
            double s_sec = calculate_s_sec(currentNode);
            currentNode.getNodeData().setLabel(s_sec); // first set with s_sec later divide by surfaceT
            surfaceT += s_sec;
        }
        for (Node<NodeData> currentNode: nodeList) {
            currentNode.getNodeData().setLabel(currentNode.getNodeData().getLabel()/surfaceT);
        }
    }

    // calculate S_soma. can be optimized as surfaceT could be calculated during calculate_s_soma call
    private void calculate_S_soma() {
        double surfaceT = 0;
        for (Node<NodeData> currentNode: nodeList) {
            surfaceT += calculate_s_sec(currentNode);
        }
        calculate_s_soma(nodeList.get(0));
        for (Node<NodeData> currentNode: nodeList) {
            currentNode.getNodeData().setLabel(currentNode.getNodeData().getLabel()/surfaceT);
        }
    }

    // calculate S_tree.
    private void calculate_S_tree() {
        calculate_s_tree(nodeList.get(0));
        double surfaceT = nodeList.get(0).getNodeData().getLabel();
        for (Node<NodeData> currentNode: nodeList) {
            currentNode.getNodeData().setLabel(currentNode.getNodeData().getLabel()/surfaceT);
        }
    }

    // calculate vS_sec.
    private void calculate_vS_sec() {
        // first calculate surface of T and save it
        calculate_s_tree(nodeList.get(0));
        double surfaceT = nodeList.get(0).getNodeData().getLabel();

        nodeList.get(0).getNodeData().setLabel(0);
        // second calculate volume of t[i] and divide with surfaceT
        for (Node<NodeData> currentNode: nodeList) {
            double v_sec = calculate_v_sec(currentNode);
            currentNode.getNodeData().setLabel(v_sec/surfaceT);
        }
    }

    // calculate S_tree.
    private void calculate_a_sec(Node<NodeData> node) {
        List<Node<NodeData>> childNodes = node.getChildren();
        if (childNodes == null || childNodes.isEmpty()) {
            node.getNodeData().setLabel(0);
            return;
        }
        int noOfChildNodes = childNodes.size();
        int noOfCalculations = noOfChildNodes * (noOfChildNodes - 1) / 2;

        // for each combination of children calculate the angle between their first nodes
        double sum = 0;
        double[] v1;
        double[] v2;
        for (int i = 0; i < noOfChildNodes; i++) {
            v1 = getVectorOfChild(childNodes.get(i));
            for (int j = i + 1; j < noOfChildNodes; j++) {
                v2 = getVectorOfChild(childNodes.get(j));
                if ((v1[0] == 0 && v1[1] == 0 && v1[2] == 0) || (v2[0] == 0 && v2[1] == 0 && v2[2] == 0)) {
                    noOfCalculations--;
                } else {
                    double cosAngle = MathArrays.cosAngle(v1, v2);
                    if (Utils.doublesAlmostEqual(Math.abs(cosAngle), 1d, 0d, 3)) {
                        sum += (cosAngle > 0) ? 0d : Math.PI;
                    } else {
                        sum += Math.acos(cosAngle);
                    }
                }
            }
        }
        // set label of current node
        if (noOfCalculations == 0) {
            node.getNodeData().setLabel(0);
        } else {
            node.getNodeData().setLabel(sum / noOfCalculations);
        }
        // recursively call children to set their labels
        for (Node<NodeData> childNode : childNodes) {
            calculate_a_sec(childNode);
        }
    }

    /**
     * Calculates label r_sec for currentNode.
     * @param currentNode
     * @return
     */
    private double calculate_r_sec(Node<NodeData> currentNode) {
        double radius = 0;
        // j=1 --> parent node skipped
        for (int j = 1; j < currentNode.getNodeData().getPosX().size(); j++) {
            radius += currentNode.getNodeData().radius.get(j);
        }
        return radius / currentNode.getNodeData().getPosX().size();
    }

    private double[] getVectorOfChild(Node<NodeData> childNode) {
        double[] v = new double[3];
        // PosX etc from NodeData contains value of parent -> get(last) - get(0)
        NodeData nodeData = childNode.getNodeData();
        int last = nodeData.getPosX().size() - 1;
        v[0] = nodeData.getPosX().get(last) - nodeData.getPosX().get(0);
        v[1] = nodeData.getPosY().get(last) - nodeData.getPosY().get(0);
        v[2] = nodeData.getPosZ().get(last) - nodeData.getPosZ().get(0);
        return v;
    }


    private boolean ifBranchOrEnd(int index) {
        return (this.getChildren(index).size() != 1);
    }

    private Vector <Integer> getChildren(int nodeNr) {
        int first = this.firstChild[nodeNr];
        Vector vector = new Vector();
        if (first != -1) {
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