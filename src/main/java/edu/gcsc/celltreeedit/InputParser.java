package edu.gcsc.celltreeedit;
import node.Node;

import java.util.Vector;


/**
 *
 */
public class InputParser implements parser.InputParser<NodeData> {

    /**
     * @param importData ein Objekt dieser Klasse speichert die Bauminformation in Arrays.
     * @param z          wie ein zeiger, zeigt welcher segment als node dargestellt wird
     * @return
     */
    public Node<NodeData> createTree(ImportData importData, int z) {
        double[] label=importData.getLabel();
        Node<NodeData> node = new Node<>(new NodeData(label[z]));
        Vector list = importData.getChildren(z);
        if (list == null) {
            return node;
        } else {
            for (int i = 0; i < list.size(); i++) {
                node.addChild(createTree(importData, (Integer) list.get(i)));
            }
        }
        return node;
    }

    /**
     *  must becouse of interface declarations
     * @param s
     * @return
     */
    @Override
    public Node<NodeData> fromString(String s) {
        return null;
    }
}