package edu.gcsc.celltreeedit.JsonIO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;
import java.util.List;

/**
 * Wrapper-Class for neuronNames. Used by Jackson-Library to easily read and write json-Files.
 */
public class NeuronFilesWrapper {

    @JsonProperty
    private List<String> neuronFiles;

    public List<String> getNeuronFiles() {
        return neuronFiles;
    }

    public void setNeuronFiles(List<String> neuronFiles) {
        this.neuronFiles = neuronFiles;
    }
}
