/*
 * Copyright 2018-2019 Michael Hoffer <info@michaelhoffer.de>. All rights reserved.
 * Copyright 2018-2019 Goethe Center for Scientific Computing, University Frankfurt. All rights reserved.
 * Copyright 2018 Erid Guga. All rights reserved.
 * Copyright 2019 Lukas Maurer. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY Michael Hoffer <info@michaelhoffer.de> "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL Michael Hoffer <info@michaelhoffer.de> OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of Michael Hoffer <info@michaelhoffer.de>.
 */
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

/**
 * Used to create NeuronMetadata from given Json-Files
 */
public class NeuronMetadataMapper {

    private Map<String, NeuronMetadataR> neuronMetadataAll;
    private Map<String, NeuronMetadataR> neuronMetadataExisting;

    private final FileFilter fileFilter = (final File file) -> file.getName().toLowerCase().endsWith(".json");

    public Map<String, NeuronMetadataR> mapAllFromMetadataDirectory(File metadataDirectory) throws IOException {
        File[] files = metadataDirectory.listFiles(fileFilter);
        if (files == null) {
            throw new IOException("No json-Files for NeuronMetadata available");
        }
        this.mapFromJsonFiles(files);
        return neuronMetadataAll;
    }

    public Map<String, NeuronMetadataR> mapExistingFromMetadataDirectory(File metadataDirectory, File swcFileDirectory) throws IOException {
        File[] files = metadataDirectory.listFiles(fileFilter);
        if (files == null) {
            throw new IOException("No json-Files for NeuronMetadata available");
        }
        this.mapFromJsonFiles(files);
        this.neuronMetadataExisting = new HashMap<>();
        this.updateNeuronMetadataExisting(swcFileDirectory);
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
