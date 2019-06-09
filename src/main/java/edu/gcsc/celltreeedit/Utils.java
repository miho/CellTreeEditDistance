package edu.gcsc.celltreeedit;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.*;


/**
 * Created by Erid on 16.02.2018.
 *
 *  The class implements a file chooser, which can choose a number of Swc-files to compare or a whole folder.
 */
public class Utils {

    /**
     *
     * @return a list of files which were selected
     */
    public static File[] choose(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); //accept files and directories as input
        FileNameExtensionFilter swc=new FileNameExtensionFilter("SWC","SWC");
        fileChooser.addChoosableFileFilter(swc);                            // filter on swc files
        fileChooser.setAcceptAllFileFilterUsed(false);                     // show only swc files
        fileChooser.setMultiSelectionEnabled(true);                       // accept multiple files as input
        fileChooser.showOpenDialog(null);

        if(fileChooser.getSelectedFile().isFile())
            return fileChooser.getSelectedFiles();
        else{
            File folder= new File(fileChooser.getSelectedFile().getAbsolutePath());
            File[] selectedFiles= folder.listFiles(new FilenameFilter() {              // return only swc files
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".swc");
                }
            });
            return selectedFiles;
        }
    }

    /**
     *
     * @return a list of json-files which were selected
     */
    public static File[] chooseJSON(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); //accept files and directories as input
        FileNameExtensionFilter json=new FileNameExtensionFilter("json","json");
        fileChooser.addChoosableFileFilter(json);                            // filter on swc files
        fileChooser.setAcceptAllFileFilterUsed(false);                     // show only swc files
        fileChooser.setMultiSelectionEnabled(true);                       // accept multiple files as input
        fileChooser.showOpenDialog(null);

        if(fileChooser.getSelectedFile().isFile())
            return fileChooser.getSelectedFiles();
        else{
            File folder= new File(fileChooser.getSelectedFile().getAbsolutePath());
            File[] selectedFiles= folder.listFiles(new FilenameFilter() {              // return only swc files
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".json");
                }
            });
            return selectedFiles;
        }
    }


    public static void printToTxt(float[][] results, String[] filenames){
        System.out.println("Please choose the directory where you want to save the file");
        JFileChooser save= new JFileChooser();
        save.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter text = new FileNameExtensionFilter("Text file (*.txt)","txt");
        save.addChoosableFileFilter(text);
        save.setAcceptAllFileFilterUsed(false);
        save.showSaveDialog(null);
        String path=save.getSelectedFile().getAbsolutePath();
        try {
            FileWriter export=new FileWriter(path+".txt");
            BufferedWriter br=new BufferedWriter(export);
           // br.write("#;");
            //for(int i=0;i<results.length;i++){                              //kopfzeile
              //  if(i<results.length-1)
                //    br.write(filenames[i]+";");
                //else
                  //  br.write(filenames[i]);
            //}
            //br.newLine();
            for(int i=0;i<results.length;i++){
                br.write(filenames[i]+";");
                for(int j=0;j<results.length;j++){
                    if(j<results.length-1)
                        br.write(results[i][j]+";");
                    else
                        br.write(results[i][j]+"");
                }
                br.newLine();
            }

            br.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("Done!");
    }

    public static File chooseDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); //accept only directories as input
        fileChooser.showOpenDialog(null);
        return fileChooser.getSelectedFile();
    }

    public static String removeSWCFileExtensions(String filename) {
        if (filename.toLowerCase().endsWith(".cng.swc")) {
            filename = filename.substring(0, filename.toLowerCase().lastIndexOf(".cng.swc"));
        } else if (filename.toLowerCase().endsWith(".ims.swc")) {
            filename = filename.substring(0, filename.toLowerCase().lastIndexOf(".ims.swc"));
        }else if (filename.toLowerCase().endsWith(".swc")) {
            filename = filename.substring(0, filename.toLowerCase().lastIndexOf(".swc"));
        } else {
            System.out.println("wrong fileending: " + filename);
        }
        return filename;
    }

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
}
