package edu.gcsc.celltreeedit;


import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;

import javax.swing.*;
import java.io.File;

@ComponentInfo(name = "TestClass", category = "CellTreeEditDistance")
public class TestClass implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    public void loadFolder(@ParamInfo(name="SWC-Folder", style="load-folder-dialog", options = "endings=[\".swc\"]") File f) {

    }

    public void loadFile(@ParamInfo(name="SWC-File", style="load-dialog", options = "endings=[\".swc\"]") File f) {

    }

    public void saveFile(@ParamInfo(name="Output File", style="save-dialog", options = "endings=[\".txt\"]") File f ) {

    }

    public void setBool(@ParamInfo(name="Enable/Disable") boolean b) {

    }

}
