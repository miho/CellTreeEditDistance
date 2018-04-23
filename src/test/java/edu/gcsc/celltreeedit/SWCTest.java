package edu.gcsc.celltreeedit;

import eu.mihosoft.vswcreader.SWCSegment;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by Erid on 23.04.2018.
 */
public class SWCTest {

    @Test
    public void readFile(){
        try {
            FileInputStream fileInputStream=new FileInputStream(new File("C:\\Users\\Erid\\Desktop\\files\\test.swc"));
            List<SWCSegment> swcSegments=new ArrayList<SWCSegment>();
            swcSegments=SWCSegment.fromStream(fileInputStream);
            assertEquals(10,swcSegments.size());

            int[] excpected={1,1,0,0,0,6,-1};
            for(int i=0; i<swcSegments.size();i++){
                int[] swcZeile= {swcSegments.get(i).getIndex(),swcSegments.get(i).getType(),(int)swcSegments.get(i).getPos().getX(),
                        (int)swcSegments.get(i).getPos().getY(),(int)swcSegments.get(i).getPos().getZ(),
                        (int)swcSegments.get(i).getR(),swcSegments.get(i).getParent()};
                swcSegments.forEach(t-> assertArrayEquals(excpected,swcZeile));

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
