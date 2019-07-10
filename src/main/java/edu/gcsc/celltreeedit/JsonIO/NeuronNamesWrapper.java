package edu.gcsc.celltreeedit.JsonIO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper-Class for neuronNames. Used by Jackson-Library to easily read and write json-Files.
 */
public class NeuronNamesWrapper {

    @JsonProperty
    private List<String> neuronNames = new ArrayList<>();

    public List<String> getNeuronNames() {
        return neuronNames;
    }

    public void setNeuronNames(List<String> neuronNames) {
        this.neuronNames = neuronNames;
    }
}
