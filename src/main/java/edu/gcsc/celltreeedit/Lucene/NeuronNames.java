package edu.gcsc.celltreeedit.Lucene;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class NeuronNames {

    @JsonProperty
    private List<String> neuronNames = new ArrayList<>();

    public List<String> getNeuronNames() {
        return neuronNames;
    }

    public void setNeuronNames(List<String> neuronNames) {
        this.neuronNames = neuronNames;
    }
}
