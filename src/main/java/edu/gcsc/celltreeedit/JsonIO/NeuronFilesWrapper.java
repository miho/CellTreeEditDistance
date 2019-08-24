package edu.gcsc.celltreeedit.JsonIO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;
import java.util.List;

/**
 * Wrapper-Class for neuronNames. Used by Jackson-Library to easily read and write json-Files.
 */
public class NeuronFilesWrapper {

    @JsonProperty
    private String _comment;

    @JsonProperty
    private List<String> neuronFiles;

    public String get_comment() {
        return _comment;
    }

    public void set_comment(String _comment) {
        this._comment = _comment;
    }

    public List<String> getNeuronFiles() {
        return neuronFiles;
    }

    public void setNeuronFiles(List<String> neuronFiles) {
        this.neuronFiles = neuronFiles;
    }
}
