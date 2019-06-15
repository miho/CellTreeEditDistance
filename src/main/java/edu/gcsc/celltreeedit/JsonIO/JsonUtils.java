package edu.gcsc.celltreeedit.JsonIO;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JsonUtils {


    public static Set<String> parseJsonToFileNames(File jsonFile) throws IOException {
        String fileName;
        Set<String> fileNames = new HashSet<>();
        // maps jsonObjects to javaObjects
        ObjectMapper objectMapper = new ObjectMapper();

        // JsonParser creates TokenStream of JsonFile (Tokens: zB. JsonToken.START_OBJECT)
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser;

        jsonParser = jsonFactory.createJsonParser(jsonFile);

        // as long as the end of the file is not reached
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {

            // only map jsonObjects inside JsonArray 'neuronResources'
            if ("neuronNames".equals(jsonParser.getCurrentName())) {
                if (jsonParser.nextToken() != JsonToken.START_ARRAY) {
                    throw new IllegalStateException("Expected a JsonArray");
                }
                // loop through all neurons from JsonArray and add them to HashMap
                while(jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    fileName = objectMapper.readValue(jsonParser, String.class);
                    fileNames.add(fileName);
                }
            }
        }
        return fileNames;
    }

    public static void writeJSON(List<String> neuronNames, File outputDirectory) throws IOException {
        NeuronNamesWrapper neuronNamesWrapper = new NeuronNamesWrapper();
        neuronNamesWrapper.getNeuronNames().addAll(neuronNames);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(outputDirectory.getPath() + "/swcFiles.json"), neuronNamesWrapper);
    }
}
