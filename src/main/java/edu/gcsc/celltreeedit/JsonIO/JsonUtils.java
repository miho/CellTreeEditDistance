package edu.gcsc.celltreeedit.JsonIO;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.gcsc.celltreeedit.Utils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Utility-Class for Json Input/Output Operations.
 */
public class JsonUtils {

    public static File[] parseJsonToFiles(File jsonFile) throws IOException {
        List<File> files = new ArrayList<>();
        // maps jsonObjects to javaObjects
        ObjectMapper objectMapper = new ObjectMapper();

        // JsonParser creates TokenStream of JsonFile (Tokens: zB. JsonToken.START_OBJECT)
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser;

        jsonParser = jsonFactory.createParser(jsonFile);

        // as long as the end of the file is not reached
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {

            // only map jsonObjects inside JsonArray 'neuronResources'
            if ("neuronFiles".equals(jsonParser.getCurrentName())) {
                if (jsonParser.nextToken() != JsonToken.START_ARRAY) {
                    throw new IllegalStateException("Expected a JsonArray");
                }
                // loop through all neurons from JsonArray and add them to HashSet
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    files.add(new File(objectMapper.readValue(jsonParser, String.class)));
                }
            }
        }
        return files.toArray(new File[1]);
    }

    public static void writeToJSON(List<File> files, File swcFileDirectory, File outputDirectory, String jsonName) throws IOException {
        files = removeBaseDirectoryFromPaths(files, swcFileDirectory);
        writeToJSON(files, outputDirectory, jsonName);
    }

    public static void writeToJSON(List<File> files, File destinationDirectory, String jsonName) throws IOException {
        NeuronFilesWrapper neuronFilesWrapper = new NeuronFilesWrapper();
        neuronFilesWrapper.setNeuronFiles(files);
        ObjectMapper mapper = new ObjectMapper();
        File file = Utils.incrementFileNameIfNecessary(destinationDirectory, jsonName);
        mapper.writeValue(file, neuronFilesWrapper);
        System.out.println("File saved to: " + file.getAbsolutePath());
    }

    private static List<File> removeBaseDirectoryFromPaths(List<File> files, File baseDirectory) {
        List<File> newFiles = new ArrayList<>();
        for (File file : files) {
            newFiles.add(new File(file.getPath().replace(baseDirectory.getPath(), "")));
        }
        return newFiles;
    }
}
