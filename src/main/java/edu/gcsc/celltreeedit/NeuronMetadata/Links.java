package edu.gcsc.celltreeedit.NeuronMetadata;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "self",
        "measurements",
        "persistence_vector"
})
public class Links {

    @JsonProperty("self")
    public Self self;
    @JsonProperty("measurements")
    public Measurements measurements;
    @JsonProperty("persistence_vector")
    public PersistenceVector persistenceVector;

}