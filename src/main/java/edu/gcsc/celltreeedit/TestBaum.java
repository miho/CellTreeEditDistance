package edu.gcsc.celltreeedit;

import eu.mihosoft.vswcreader.SWCSegment;
import node.Node;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erid on 12.04.2018.
 */
public class TestBaum implements parser.InputParser<NodeData>{

    List<Node<NodeData>> knotenListe= new ArrayList<>();

    public Node<NodeData> createTree(InputStream inputStream, int label) throws IOException {
        try {
            List<SWCSegment> swcSegments = SWCSegment.fromStream(inputStream);
            int size = swcSegments.size();

            for (int i = 0; i < size; i++) {
                Node<NodeData> treeNode = new Node<>(new NodeData(swcSegments.get(i).getIndex(),
                        swcSegments.get(i).getType(),
                        swcSegments.get(i).getPos().getX(),
                        swcSegments.get(i).getPos().getY(),
                        swcSegments.get(i).getPos().getZ(),
                        swcSegments.get(i).getR(),
                        swcSegments.get(i).getParent()));
                knotenListe.add(treeNode);
            }

            for(int i=1;i<size;i++){
                knotenListe.get(swcSegments.get(i).getParent()-1).addChild(knotenListe.get(swcSegments.get(i).getIndex()-1));
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        this.setNodeLabel(label);
        return knotenListe.get(0);
    }

    public void setNodeLabel(int label){

        int number=knotenListe.size();
        int length=0;                     // of tree rooted at 1
        int surface=0;                    // of tree rooted at 1
        int volume=0;                     // of tree rooted at 1
        int[] labels= new int[number];

        if(label==1){                                   //top1
            knotenListe.forEach(s->s.getNodeData().setLabel(1));
        }
        if(label==2){                                   //top2
            double j=1.0/number;
            knotenListe.forEach(s->s.getNodeData().setLabel((j)));
        }
        if(label==3){                                   //length of a section

        }
        if(label==4){                                   //length of tree rooted at i

        }
        if(label==5){                                   //length of tree rooted at 1

        }
        if(label==6){                                   //length of a section/length of tree rooted at i

        }
        if(label==7){                                   // length of tree rooted at i / length of tree rooted at 1

        }
    }


    @Override
    public Node<NodeData> fromString(String s) {
        return null;
    }

}