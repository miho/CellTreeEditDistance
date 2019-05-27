package edu.gcsc.celltreeedit.NeuronMetadata;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.gcsc.celltreeedit.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;

public class NeuronMetadataMapper {

    private File[] files;

    public Map<Integer, NeuronMetadata> mapFromFile() throws IOException {

        System.out.println("inside mapFromFile");

        this.files = Utils.chooseJSON();

        //read json file data to String
//        byte[] jsonData = Files.readAllBytes(Paths.get(this.files[0].getCanonicalPath()));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jasonFactory = new JsonFactory();
        JsonParser jsonParser = jasonFactory.createJsonParser(this.files[0]);

        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            //get the current token
            String fieldname = jsonParser.getCurrentName();
            System.out.println(fieldname);
            if ("neuronResources".equals(fieldname)) {
//                Iterator<NeuronMetadata> it = jsonParser.readValuesAs(NeuronMetadata.class);
//                while (it.hasNext()) {
//                    System.out.println(it.next());
//                }
                jsonParser.skipChildren();
            }

        }

//        //create ObjectMapper instance
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        //convert json string to object
//        NeuronMetadata emp = objectMapper.readValue(jsonData, NeuronMetadata.class);

        /* TODO:
        File Dialog um Json-Files auszuwählen.
        Files in Liste speichern und loop über liste
        file stream öffnen? evtl direkt über jackson?
        Mit JacksonParser oder ObjectMapper JSON-Array durchgehen und NeuronMetadata eintragen
        Rückgabe der HashMap an Main-Funktion
         */
        return null;
    }

}
