package edu.gcsc.celltreeedit;

import eu.mihosoft.vswcreader.SWCSegment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Erid on 26.01.2018.
 *
 * The class implements a tree with the input of a .swc file.
 * The implementation of the tree consists on creating parents-, children- and nextSibling arrays.
 */
public class Tree{

    public static final int NOT_FOUND=-1;
    private int root;
    private int[] parents;
    private int[] firstChild;
    private int[] nextSibling;

    /**
     * Constructor with a .swc file as input
     * @param inputStream
     */
    public Tree(InputStream inputStream){

        try {
           List<SWCSegment> swcSegments = SWCSegment.fromStream(inputStream);
            // Fill the parents array
            int size= swcSegments.size();
            int[] parents = new int[size +1];             // size+1 weil Array anders von Listen ab 0 anfängt
            int[] firstChild = new int[size +1];
            int[] nextSibling = new int[size +1];
            Arrays.fill(firstChild, Tree.NOT_FOUND);
            Arrays.fill(nextSibling, Tree.NOT_FOUND);
            root=1;

            swcSegments.forEach(t -> parents[t.getIndex()] = t.getParent());

            // Fill the children and nextSibling array

            for (int i = 2; i <= size; i++) {
                int n= parents[i];
                    if (firstChild[n] == Tree.NOT_FOUND) {                 // children[n]
                        firstChild[n] = i;                                  // children[n]
                    } else {
                        int lastChild = firstChild[n];
                        if (nextSibling[lastChild] == Tree.NOT_FOUND) {
                            nextSibling[lastChild] = i;
                        } else {
                            int lastSibling = nextSibling[lastChild];
                            nextSibling[lastSibling] = i;
                        }
                    }
            }
        }catch (IOException e){
            System.out.println(e);
        }

    }

    /**
     *
     * @param other
     */
    public Tree(Tree other, int root){
        this.root=root;

    }

    /**
     *
     * @param nodeID
     * @return
     */
    public List<Integer> getChildren(Tree tree, int nodeID){
        List<Integer> childs= new ArrayList<Integer>();
        childs.add(firstChild[nodeID]);
        int lastChild= this.firstChild[nodeID];
        while(nextSibling[lastChild]!= Tree.NOT_FOUND){
            lastChild=nextSibling[lastChild];
            childs.add(lastChild);
        }
        return childs;
    }



    public Tree subtree(int nodeID){
        return new Tree(this, nodeID);
    }

    public void setRoot(int root){
        this.root=root;
    }


}
