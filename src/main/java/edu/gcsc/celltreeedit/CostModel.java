package edu.gcsc.celltreeedit;

import node.Node;

/**
 *
 */
public class CostModel implements costmodel.CostModel<NodeData> {
    /**
     *
     * @param n
     * @return
     */
    @Override
    public float del(Node<NodeData> n) {
        return 1.0f;
    }

    /**
     *
     * @param n
     * @return
     */
    @Override
    public float ins(Node<NodeData> n) {
        return 1.0f;
    }

    /**
     *
     * @param n1
     * @param n2
     * @return
     */
    @Override
    public float ren(Node<NodeData> n1, Node<NodeData> n2) {
        if(n1.getNodeData().getLabel()==n2.getNodeData().getLabel())
            return 0.0f;
        else
            return 1.0f;
    }
}
