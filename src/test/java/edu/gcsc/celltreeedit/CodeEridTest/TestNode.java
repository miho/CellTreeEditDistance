package edu.gcsc.celltreeedit.CodeEridTest;

import java.util.ArrayList;
import java.util.List;

public class TestNode {

    private List<TestNode> children = new ArrayList<>();

    private TestNode parent = null;

    // including own node
    private int noOfIncludedSegments = 0;

    // including own node
    private int noOfAncestors = 0;

    // including own node
    private int noOfDecendents = 0;

    public int getNoOfIncludedSegments() {
        return noOfIncludedSegments;
    }

    public void setNoOfIncludedSegments(int noOfIncludedSegments) {
        this.noOfIncludedSegments = noOfIncludedSegments;
    }

    public int getNoOfAncestors() {
        return noOfAncestors;
    }

    public void setNoOfAncestors(int noOfAncestors) {
        this.noOfAncestors = noOfAncestors;
    }

    public int getNoOfDecendents() {
        return noOfDecendents;
    }

    public void setNoOfDecendents(int noOfDecendents) {
        this.noOfDecendents = noOfDecendents;
    }

    public TestNode addChild(TestNode child) {
        child.setParent(this);
        this.children.add(child);
        return child;
    }

    public void addChildren(List<TestNode> children) {
        children.forEach(each -> each.setParent(this));
        this.children.addAll(children);
    }

    public List<TestNode> getChildren() {
        return children;
    }

    private void setParent(TestNode parent) {
        this.parent = parent;
    }

    public TestNode getParent() {
        return parent;
    }

}
