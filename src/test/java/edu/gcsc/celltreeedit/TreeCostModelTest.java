/*
 * Copyright 2018-2019 Michael Hoffer <info@michaelhoffer.de>. All rights reserved.
 * Copyright 2018-2019 Goethe Center for Scientific Computing, University Frankfurt. All rights reserved.
 * Copyright 2018 Erid Guga. All rights reserved.
 * Copyright 2019 Lukas Maurer. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY Michael Hoffer <info@michaelhoffer.de> "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL Michael Hoffer <info@michaelhoffer.de> OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of Michael Hoffer <info@michaelhoffer.de>.
 */
package edu.gcsc.celltreeedit;

import edu.gcsc.celltreeedit.TEDCalculation.NodeData;
import edu.gcsc.celltreeedit.TEDCalculation.TreeCostModel;
import org.junit.Test;

import eu.mihosoft.ext.apted.distance.APTED;
import eu.mihosoft.ext.apted.node.Node;

import static org.junit.Assert.*;

/**
 * Created by Erid on 13.04.2018.
 */
public class TreeCostModelTest {


    private Node<NodeData> node1=new Node<>(new NodeData(1));
    private Node<NodeData> node2=new Node<>(new NodeData(10));
    private Node<NodeData> node3=new Node<>(new NodeData(2.5678));
    private Node<NodeData> node4=new Node<>(new NodeData(-12.23));
    private Node<NodeData> node5=new Node<>(new NodeData(11));
    private Node<NodeData> node6=new Node<>(new NodeData(10));

    APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());

    @Test
    public void del1() throws Exception {
        node1.addChild(node2);

        float result1 = apted.computeEditDistance(node1, node6);
        assertEquals(1,result1,0f);
    }

    /**
     * Baum 1:
     * @throws Exception
     */
    @Test
    public void del2() throws Exception {
        node2.addChild(node5);
        node5.addChild(node6);

        float result1 = apted.computeEditDistance(node1, node2);
        assertEquals(30,result1,0f);
    }

    @Test
    public void ins1() throws Exception {
        node1.addChild(node2);

        float result1 = apted.computeEditDistance(node6, node1);
        assertEquals(1,result1,0f);
    }

    /**
     * Baum 1:
     * @throws Exception
     */
    @Test
    public void ins2() throws Exception {
        node2.addChild(node5);
        node5.addChild(node6);

        float result1 = apted.computeEditDistance(node2, node1);
        assertEquals(30,result1,0f);
    }

    @Test
    public void ren() throws Exception {
        APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());

        float result1 = apted.computeEditDistance(node1, node2);
        float result2 = apted.computeEditDistance(node1, node3);
        float result3 = apted.computeEditDistance(node1, node4);
        float result4 = apted.computeEditDistance(node2, node3);
        float result5 = apted.computeEditDistance(node2, node4);
        float result6 = apted.computeEditDistance(node3, node4);
        assertEquals(9,result1,0f);
        assertEquals(1.5678,result2,1e-7);
        assertEquals(13.23,result3,1e-6);
        assertEquals(7.4322,result4,1e-7);
        assertEquals(22.23,result5,1e-6);
        assertEquals(14.7978,result6,1e-7);

    }
}