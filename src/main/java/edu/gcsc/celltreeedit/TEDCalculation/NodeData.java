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
import java.util.List;

/**
 * Data for a Node from APTED-library. Contains information of the summarized segment from swcFile.
 */
public class NodeData implements Serializable{

    double label;
    int type;
    List<Integer> index;
    List<Integer> parent;
    List<Double> posX;
    List<Double> posY;
    List<Double> posZ;
    List<Double> radius;

    public NodeData(double label){
        this.label=label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeData nodeData = (NodeData) o;

        if (Double.compare(nodeData.label, label) != 0) return false;
        if (type != nodeData.type) return false;
        if (index != null ? !index.equals(nodeData.index) : nodeData.index != null) return false;
        if (parent != null ? !parent.equals(nodeData.parent) : nodeData.parent != null) return false;
        if (posX != null ? !posX.equals(nodeData.posX) : nodeData.posX != null) return false;
        if (posY != null ? !posY.equals(nodeData.posY) : nodeData.posY != null) return false;
        if (posZ != null ? !posZ.equals(nodeData.posZ) : nodeData.posZ != null) return false;
        return radius != null ? radius.equals(nodeData.radius) : nodeData.radius == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(label);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + type;
        result = 31 * result + (index != null ? index.hashCode() : 0);
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (posX != null ? posX.hashCode() : 0);
        result = 31 * result + (posY != null ? posY.hashCode() : 0);
        result = 31 * result + (posZ != null ? posZ.hashCode() : 0);
        result = 31 * result + (radius != null ? radius.hashCode() : 0);
        return result;
    }

    public void setLabel(double label) {

        this.label = label;
    }

    public double getLabel() {

        return label;
    }

    public int getType() {
        return type;
    }

    public List<Integer> getIndex() {
        return index;
    }

    public List<Integer> getParent() {
        return parent;
    }

    public List<Double> getPosX() {
        return posX;
    }

    public List<Double> getPosY() {
        return posY;
    }

    public List<Double> getPosZ() {
        return posZ;
    }

    public List<Double> getRadius() {
        return radius;
    }

    public void setType(int type) {
        this.type = type;
    }

    public NodeData(List<Integer> index, int type, List<Double> posX, List<Double> posY, List<Double> posZ, List<Double> radius, List<Integer> parent) {
        this.type = type;
        this.index = index;
        this.parent = parent;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.radius = radius;
    }

    public void clearLabelCalculationProperties() {
        this.index = null;
        this.parent = null;
        this.posX = null;
        this.posY = null;
        this.posZ = null;
        this.radius = null;
    }
}
