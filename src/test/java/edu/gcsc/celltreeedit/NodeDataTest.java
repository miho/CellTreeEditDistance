package edu.gcsc.celltreeedit;

import org.junit.Test;

import javax.xml.soap.Node;

import static org.junit.Assert.*;

/**
 * Created by Erid on 13.04.2018.
 */
public class NodeDataTest {

    NodeData nodedate=new NodeData(1,3,3.45,4.56,-9.8,2.258,-1);

    @Test
    public void getLabel() throws Exception {
        NodeData n= new NodeData(1);
        assertEquals(1,n.getLabel(),1.0);
    }


    @Test
    public void getIndex() throws Exception {
        assertEquals(1,nodedate.getIndex());
    }


    @Test
    public void getParent() throws Exception {
        assertEquals(-1, nodedate.getParent());
    }


    @Test
    public void getPosX() throws Exception {
        assertEquals(3,.45,nodedate.getPosX());
    }

    @Test
    public void getPosY() throws Exception {
        assertEquals(4.56,nodedate.getPosY(),0.1);
    }

    @Test
    public void getPosZ() throws Exception {
        assertEquals(-9.8,nodedate.getPosZ(),0.1);
    }

    @Test
    public void getRadius() throws Exception {
        assertEquals(2.258, nodedate.getRadius(), 0.1);
    }

    @Test
    public void getType() throws Exception {
        assertEquals(3,nodedate.getType());
    }
}