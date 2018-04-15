package edu.gcsc.celltreeedit;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FilenameFilter;


/**
 * Created by Erid on 16.02.2018.
 *
 *  The class implements a file chooser, which can choose a number of Swc-files to compare or a whole folder.
 */
public class ChooseFiles {

    private File[] selectedFiles;
    /**
     *
     * @return a list of files which were selected
     */
    public File[] choose(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("C:\\Users\\Erid\\Dropbox\\Dokumente\\Informatik-UNI\\SoSe2017\\Bachelorarbeit"));
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
            selectedFiles= folder.listFiles(new FilenameFilter() {              // return only swc files
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".swc");
                }
            });
            return selectedFiles;
        }
    }


}
