package edu.gcsc.celltreeedit;

/**
 * Created by Erid on 26.01.2018.
 */
public class Forest {

    private  final Tree tree;
    private final int root;

    public Forest(Tree tree, int root){
        this.tree=tree;
        tree.setRoot(root);

    }

    public Forest subForest(Forest forest, int node){
        return new Forest(forest.tree, node);
    }





}
