package edu.gcsc.celltreeedit.JsonIO;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.gcsc.celltreeedit.AppProperties.AppProperties;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Utility-Class for Json Input/Output Operations.
 */
public class JsonUtils {

    private static AppProperties appProperties;

    public static File[] parseJsonToFiles(File jsonFile) throws IOException {
        List<File> files = new ArrayList<>();
        // maps jsonObjects to javaObjects
        ObjectMapper objectMapper = new ObjectMapper();

        // JsonParser creates TokenStream of JsonFile (Tokens: zB. JsonToken.START_OBJECT)
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser;

        jsonParser = jsonFactory.createJsonParser(jsonFile);

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

    public static void writeToJSON(List<File> files, PathType pathType) throws IOException {
        appProperties = AppProperties.getInstance();

        // differentiate between pathTypes
        switch (pathType) {
            case ABSOLUTE_PATH: // for SWC-Files independent of Neuromorpho-DB lying in BaseDirectory
                break;
            case RELATIVE_TO_BASE_DIRECTORY: // for SWC-Files in Neuromorpho-DB lying in BaseDirectory
                files = removeBaseDirectoryFromPaths(files);
        }

        NeuronFilesWrapper neuronFilesWrapper = new NeuronFilesWrapper();
        neuronFilesWrapper.setNeuronFiles(files);
        ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(new File(appProperties.getOutputDirectory().getPath() + "/swcFiles.json"), neuronFilesWrapper);
    }

    private static List<File> removeBaseDirectoryFromPaths(List<File> files) {
        List<File> newFiles = new ArrayList<>();
        for (File file : files) {
            newFiles.add(new File(file.getPath().replace(appProperties.getBaseDirectory().getPath(), "")));
        }
        return newFiles;
    }
}
