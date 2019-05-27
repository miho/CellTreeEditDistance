package edu.gcsc.celltreeedit.NeuronMetadata;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "href"
})
public class PersistenceVector {

    @JsonProperty("href")
    public String href;

}