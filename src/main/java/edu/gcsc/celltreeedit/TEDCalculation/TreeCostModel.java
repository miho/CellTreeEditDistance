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
package edu.gcsc.celltreeedit.TEDCalculation;

import java.io.Serializable;

import eu.mihosoft.ext.apted.costmodel.CostModel;
import eu.mihosoft.ext.apted.node.Node;

/**
 * Defines the Costfunctions used by APTED-Algorithm
 */
public class TreeCostModel implements CostModel<NodeData>, Serializable {

    /**
     *
     * @param n
     * @return
     */
    @Override
    public float del(Node<NodeData> n) {
        return Math.abs((float) n.getNodeData().getLabel());
    }

    /**
     *
     * @param n
     * @return
     */
    @Override
    public float ins(Node<NodeData> n) {
        return Math.abs((float) n.getNodeData().getLabel());
    }

    /**
     *
     * @param n1
     * @param n2
     * @return
     */
    @Override
    public float ren(Node<NodeData> n1, Node<NodeData> n2) {
        if(n1.getNodeData().getLabel() == n2.getNodeData().getLabel())
          return 0;
        else return Math.abs((float) n1.getNodeData().getLabel() - (float) n2.getNodeData().getLabel());
    }
}
