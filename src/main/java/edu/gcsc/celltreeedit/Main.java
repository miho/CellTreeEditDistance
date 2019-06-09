package edu.gcsc.celltreeedit;

import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataMapper;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataRImpl;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Erid on 12.02.2018.
 */
public class Main {

    public static void main(String[] args) throws IOException {

//        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
//        String appConfigPath = rootPath + "/main/resources/AppConfig.properties";
        String appConfigPath = "src/main/resources/AppConfig.properties";

        Properties appProps = new Properties();
        appProps.load(new FileInputStream(appConfigPath));

        // get Default-Values Application-Properties
        File swcDirectory = new File(appProps.getProperty("swcDirectory"));
        File metadataPath = new File(appProps.getProperty("metadataPath"));
        String exportedMatrixName = appProps.getProperty("exportedMatrixName");
        File exportedMatrixPath = new File(appProps.getProperty("exportedMatrixPath"));
        int calcType = Integer.parseInt(appProps.getProperty("calcType"));
        File jsonDirectory = new File(appProps.getProperty("jsonDirectory"));
        boolean show = Boolean.parseBoolean(appProps.getProperty("show"));

        for (final String arg: args) {
            if (hasValuedParam(arg, AppParameter.CALC_SHORT)){
                calcType = extractInt(arg, AppParameter.CALC_SHORT);
            }
            if (hasValuedParam(arg, AppParameter.CALC)){
                calcType = extractInt(arg, AppParameter.CALC);
            }
            if (hasValuedParam(arg, AppParameter.JSON_SHORT)){
                jsonDirectory = new File(extractString(arg, AppParameter.JSON_SHORT));
            }
            if (hasValuedParam(arg, AppParameter.JSON)){
                jsonDirectory = new File(extractString(arg, AppParameter.JSON));
            }
            if (hasValuedParam(arg, AppParameter.SHOW_SHORT)) {
                show = extractBoolean(arg, AppParameter.SHOW_SHORT);
            }
            if (hasValuedParam(arg, AppParameter.SHOW)) {
                show = extractBoolean(arg, AppParameter.SHOW);
            }
        }


        CellTreeEditDistance matrix = new CellTreeEditDistance();
        if (!jsonDirectory.getName().equals("")) {
            Set<String> swcFileNames = Utils.parseJsonToFileNames(jsonDirectory);
            matrix.compareFilesFromFilenames(swcFileNames, swcDirectory, 9);
        } else {
            matrix.compareFilesFromChoose(9);
        }



//        SWCTest swcTest= new SWCTest();
//        Set<String> fileNames = swcTest.preprocessSWCDirectory(neuronMetadata, swcDirectory);

//        for (String fileName: fileNames) {
//            if (!neuronMetadata.containsKey(fileName)) {
//                System.out.println("File nicht in Metadaten: " + fileName);
//            }
//        }



    }

    private static boolean hasValuedParam(final String cmdArg,
                                          final AppParameter parameter) {
        return cmdArg.startsWith(parameter.paramName) &&
                cmdArg.length() > parameter.valueOffset;
    }

    private static boolean extractBoolean(final String cmdArg, final AppParameter parameter) {
        return Boolean.parseBoolean(cmdArg.substring(parameter.valueOffset));
    }

    private static int extractInt(final String cmdArg, final AppParameter parameter) {
        return Integer.parseInt(cmdArg.substring(parameter.valueOffset));
    }

    private static String extractString(final String cmdArg, final AppParameter parameter) {
        return cmdArg.substring(parameter.valueOffset);
    }

}
