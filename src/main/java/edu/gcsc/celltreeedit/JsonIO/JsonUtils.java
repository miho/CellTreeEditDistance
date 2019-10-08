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
package edu.gcsc.celltreeedit.JsonIO;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.gcsc.celltreeedit.Utils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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

    public static void writeToJSON(List<File> files, String comment, File swcFileDirectory, File outputDirectory, String jsonName) throws IOException {
        files = Utils.removeSwcFileDirectoryFromPaths(files, swcFileDirectory);
        writeToJSON(files, comment, outputDirectory, jsonName);
    }

    public static void writeToJSON(List<File> files, String comment, File outputDirectory, String jsonName) throws IOException {
        NeuronFilesWrapper neuronFilesWrapper = new NeuronFilesWrapper();
        neuronFilesWrapper.set_comment(comment);
        neuronFilesWrapper.setNeuronFiles(files.stream().map(File::getPath).collect(Collectors.toList()));
        ObjectMapper mapper = new ObjectMapper();
        jsonName = (jsonName.isEmpty()) ? "swcFile" : FilenameUtils.removeExtension(jsonName);
        File file = Utils.incrementFileNameIfNecessary(outputDirectory, jsonName + ".json");
        mapper.writeValue(file, neuronFilesWrapper);
        System.out.println("File saved to: " + file.getAbsolutePath());
    }


}
