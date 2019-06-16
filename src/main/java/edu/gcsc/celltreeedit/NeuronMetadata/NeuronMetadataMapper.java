package edu.gcsc.celltreeedit.NeuronMetadata;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.gcsc.celltreeedit.Utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NeuronMetadataMapper {

    private final FileFilter fileFilter = (final File file) -> file.getName().toLowerCase().endsWith(".json");

    public Map<String, NeuronMetadataRImpl> mapFromChooseJSON() throws IOException {
        // select jsonFiles from disk
        File[] files = Utils.chooseJson();
        return this.mapFromJsonFiles(files);
    }

    public Map<String, NeuronMetadataRImpl> mapFromDirectory(File directory) throws IOException {
        if (directory.isFile() && directory.getName().toLowerCase().endsWith(".json")) {
            return this.mapFromJsonFiles(new File[] {directory});
        }
        File[] files = directory.listFiles(fileFilter);
        if (files == null) {
            throw new IOException("No json-Files for NeuronMetadata available");
        }
        return this.mapFromJsonFiles(files);
    }


    public Map<String, NeuronMetadataRImpl> mapFromJsonFiles(File[] files) throws IOException {

        NeuronMetadataRImpl neuronMetadataPOJO;
        Map<String, NeuronMetadataRImpl> neuronMetadata = new HashMap<>();

        // maps jsonObjects to javaObjects
        ObjectMapper objectMapper = new ObjectMapper();

        // JsonParser creates TokenStream of JsonFile (Tokens: zB. JsonToken.START_OBJECT)
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser;

        // loop through files
        for (File file: files) {
            jsonParser = jsonFactory.createJsonParser(file);

            // as long as the end of the file is not reached
            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {

                // only map jsonObjects inside JsonArray 'neuronResources'
                if ("neuronResources".equals(jsonParser.getCurrentName())) {
                    if (jsonParser.nextToken() != JsonToken.START_ARRAY) {
                        throw new IllegalStateException("Expected a JsonArray");
                    }
                    // skip START_ARRAY Token
                    jsonParser.nextToken();
                    // loop through all neurons from JsonArray and add them to HashMap
                    while(jsonParser.nextToken() != JsonToken.END_ARRAY) {
                        neuronMetadataPOJO = objectMapper.readValue(jsonParser, NeuronMetadataRImpl.class);
                        neuronMetadata.put(neuronMetadataPOJO.getNeuronName(), neuronMetadataPOJO);
                    }
                }
            }
        }
        return neuronMetadata;
    }

}
