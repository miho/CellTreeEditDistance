package edu.gcsc.celltreeedit.NeuronMetadata;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.gcsc.celltreeedit.AppProperties.AppProperties;
import edu.gcsc.celltreeedit.Utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to create NeuronMetadata from given Json-Files
 */
public class NeuronMetadataMapper {

    private static AppProperties appProperties = AppProperties.getInstance();

    private Map<String, NeuronMetadataR> neuronMetadataAll;
    private Map<String, NeuronMetadataR> neuronMetadataExisting;

    private final FileFilter fileFilter = (final File file) -> file.getName().toLowerCase().endsWith(".json");

    public Map<String, NeuronMetadataR> mapAllFromMetadataDirectory() throws IOException {
        File directory = appProperties.getMetadataDirectory();
        File[] files = directory.listFiles(fileFilter);
        if (files == null) {
            throw new IOException("No json-Files for NeuronMetadata available");
        }
        this.mapFromJsonFiles(files);
        return neuronMetadataAll;
    }

    public Map<String, NeuronMetadataR> mapExistingFromMetadataDirectory() throws IOException {
        File directory = appProperties.getMetadataDirectory();
        File[] files = directory.listFiles(fileFilter);
        if (files == null) {
            throw new IOException("No json-Files for NeuronMetadata available");
        }
        this.mapFromJsonFiles(files);
        this.neuronMetadataExisting = new HashMap<>();
        this.updateNeuronMetadataExisting(appProperties.getSwcFileDirectory());
        return this.neuronMetadataExisting;
    }


    private void mapFromJsonFiles(File[] files) throws IOException {

        NeuronMetadataRImpl neuronMetadataPOJO;
        this.neuronMetadataAll = new HashMap<>();

        // maps jsonObjects to javaObjects
        ObjectMapper objectMapper = new ObjectMapper();

        // JsonParser creates TokenStream of JsonFile (Tokens: zB. JsonToken.START_OBJECT)
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser;

        // loop through files
        for (File file: files) {
            jsonParser = jsonFactory.createParser(file);

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
                        this.neuronMetadataAll.put(neuronMetadataPOJO.getNeuronName(), neuronMetadataPOJO);
                    }
                }
            }
        }
    }

    private void updateNeuronMetadataExisting(File directory) {
        File[] subFiles = directory.listFiles();
        if (subFiles == null) {
            return;
        }
        for (File subFile: subFiles) {
            if (subFile.isFile()) {
                String fileNameWithoutSWCFileExtension = Utils.removeSWCFileExtensions(subFile.getName());
                this.neuronMetadataExisting.put(fileNameWithoutSWCFileExtension, this.neuronMetadataAll.get(fileNameWithoutSWCFileExtension));
            } else {
                if (subFile.getName().equals("00_Ignore")) {
                    continue;
                }
                // recursively check next directory
                this.updateNeuronMetadataExisting(subFile);
            }
        }
    }
}
