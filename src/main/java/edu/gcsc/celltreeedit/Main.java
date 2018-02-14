package edu.gcsc.celltreeedit;

import eu.mihosoft.vswcreader.SWCSegment;

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
    }
}
