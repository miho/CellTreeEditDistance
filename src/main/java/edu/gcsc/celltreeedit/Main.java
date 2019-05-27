package edu.gcsc.celltreeedit;


import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadata;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Erid on 12.02.2018.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        NeuronMetadataMapper mapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadata> neuronMetadata = mapper.mapFromFile();
        System.out.println(neuronMetadata.size());

        File[] files = Utils.choose();
        String filename;
        for (File file: files) {
            filename = file.getName();
            filename = filename.substring(0, filename.substring(0, filename.lastIndexOf(".")).lastIndexOf("."));
            System.out.println(neuronMetadata.get(filename).neuronName);
        }
//         CellTreeEditDistance matrix=new CellTreeEditDistance();
//         matrix.compareFiles(9);
//         CellTreeEditDistance.showLabels();
//         System.out.print(Main.class.getClassLoader().getResourceAsStream("/src/main/resources/TestSWCFiles/1k.swc"));

    }

}

