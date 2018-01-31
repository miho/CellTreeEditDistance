package edu.gcsc.celltreeedit;

import eu.mihosoft.vswcreader.SWCSegment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        // SWC sample
        String swcContent = ""
                + "# ********************************************* \n"
                + "# SCALE 1.0 1.0 1.0 \n"
                + "1 1 2.3   1.73   -0.12 12.61 -1\n"
                + "2 1 2.3   14.34  -0.13 12.62  1\n"
                + "3 2 62.3 -10.87  -0.14 12.63  1";

        System.out.println("----------------------------------------------------------");
        System.out.println("> original SWC data:");
        System.out.println("----------------------------------------------------------");
        System.out.println(swcContent);

        // read segments
        List<SWCSegment> segments = SWCSegment.fromString(swcContent);

        // TODO do something with the swc data
        // output segments
        System.out.println("----------------------------------------------------------");
        System.out.println("> segment data:");
        System.out.println("----------------------------------------------------------");
        segments.forEach(s -> System.out.println(s));

        // output segments in SWC format
        System.out.println("----------------------------------------------------------");
        System.out.println("> SWC data:");
        System.out.println("----------------------------------------------------------");
        String swcContent2 = SWCSegment.toSWCString(segments);
        System.out.println(swcContent2);


        System.out.println("**************************************************************");
        System.out.println("------------------------From Steam----------------------------");
        List<SWCSegment> segments2;
        try {
            FileInputStream fi = new FileInputStream(
                    new File("C:/Users/Erid/Dropbox/Dokumente/Informatik-UNI/SOSe2017/Bachelorarbeit/194-4-29nj.CNG.SWC"));
//            segments2 = SWCSegment.fromStream(fi);
//            segments2.forEach(t->System.out.println(t));
//            System.out.println(segments2.size());
        }catch(FileNotFoundException e){
            System.out.println(e);
        }

        System.out.println("***************************************************************");
        System.out.println("----------------------Tree_Konstruktor-------------------------");
        try{
            FileInputStream f= new FileInputStream(
                    new File("C:/Users/Erid/Dropbox/Dokumente/Informatik-UNI/SOSe2017/Bachelorarbeit/194-4-29nj.CNG - Kopie.SWC"));
            Tree tree=new Tree(f);
            List<Integer> l= tree.getChildren(1);
            l.forEach(integer -> System.out.println(integer));
        }catch(FileNotFoundException e){
            System.out.println(e);
        }

    }
}
