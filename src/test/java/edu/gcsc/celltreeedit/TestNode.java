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
