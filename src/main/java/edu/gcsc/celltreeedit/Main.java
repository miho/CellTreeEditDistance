package edu.gcsc.celltreeedit;


import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadata;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Erid on 12.02.2018.
 */
public class Main {

    private static final Map<Integer, NeuronMetadata> neuronMetadata = new HashMap<>();

    public static void main(String[] args) throws IOException {
        NeuronMetadataMapper map = new NeuronMetadataMapper();
        map.mapFromFile();
//         CellTreeEditDistance matrix=new CellTreeEditDistance();
//         matrix.compareFiles(9);
//         CellTreeEditDistance.showLabels();
       // System.out.print(Main.class.getClassLoader().getResourceAsStream("/src/main/resources/TestSWCFiles/1k.swc"));

    }
}

