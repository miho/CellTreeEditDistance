package edu.gcsc.celltreeedit;
import node.Node;

import java.util.Vector;


/**
 *
 */
public class InputParser implements parser.InputParser {

    /**
     * @param importData ein Objekt dieser Klasse speichert die Bauminformation in Arrays.
     * @param z          muss immer auf 1 (Wurzel) anfangen, damit der volle Baum entsteht.
     * @return
     */
    public Node<NodeData> createTree(ImportData importData, int z) {
        Node<NodeData> node = new Node<>(new NodeData((1)));        // falls label != top1 eine andere funktion aufrufen
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
     *
     * @param s
     * @return
     */
    @Override
    public Node<NodeData> fromString(String s) {
        return null;
    }
}