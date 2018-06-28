package edu.gcsc.celltreeedit;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;


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
}
