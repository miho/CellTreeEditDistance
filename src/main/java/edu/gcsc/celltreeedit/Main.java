package edu.gcsc.celltreeedit;


import java.io.IOException;

/**
 * Created by Erid on 12.02.2018.
 */
public class Main {
    public static void main(String[] args) throws IOException {
         CellTreeEditDistance matrix=new CellTreeEditDistance();
         matrix.compareFiles(9);
         CellTreeEditDistance.showLabels();
       // System.out.print(Main.class.getClassLoader().getResourceAsStream("/src/main/resources/TestSWCFiles/1k.swc"));

    }
}

