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
public class TreeCreator implements parser.InputParser<NodeData>{

    List<Node<NodeData>> nodeList= new ArrayList<>();

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
                nodeList.add(treeNode);
            }

            for(int i=1;i<size;i++){
                nodeList.get(swcSegments.get(i).getParent()-1).addChild(nodeList.get(swcSegments.get(i).getIndex()-1));
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        this.setNodeLabel(label);
        return nodeList.get(0);
    }

    public void setNodeLabel(int label){

        int number= nodeList.size();
        int length=0;                     // of tree rooted at 1
        int surface=0;                    // of tree rooted at 1
        int volume=0;                     // of tree rooted at 1
        int[] labels= new int[number];

        if(label==1){                                   //top1
            nodeList.forEach(s->s.getNodeData().setLabel(1));
        }
        if(label==2){                                   //top2
            double j=1.0/number;
            nodeList.forEach(s->s.getNodeData().setLabel((j)));
        }
        if(label==3){                                   //length of a section
        }
        if(label==4){                                   //number of nodes of tree rooted at i
            int treeLength=0;
            for(int i=0; i<nodeList.size(); i++){
                treeLength=nodeList.get(i).getNodeCount();
                nodeList.get(i).getNodeData().setLabel(treeLength);
            }
        }
        if(label==5){                                   //length of tree rooted at 1
        }
        if(label==6){                                   //length of a section/length of tree rooted at i
        }
        if(label==7){                                   //length of tree rooted at i / length of tree rooted at 1
        }
        if(label==8){                                   //surface of a section
        }
        if(label==9){                                   //surface of tree rooted at i
        }
        if(label==10){                                  //surface of tree rooted at 1
        }
        if(label==11){                                  //surface of a section/surface of tree rooted at i
        }
        if(label==12){                                  //surface of tree rooted at i / surface of tree rooted at 1
        }
        if(label==12){                                  //volume of a section
        }
        if(label==14){                                  //volume of tree rooted at i
        }
        if(label==15){                                  //volume of tree rooted at 1
        }
        if(label==16){                                  //volume of a section/volume of tree rooted at i
        }
        if(label==17){                                  //volume of tree rooted at i / volume of tree rooted at 1
        }
        if(label==18){                                  //angle between children of i
        }
        else{

        }
    }


    @Override
    public Node<NodeData> fromString(String s) {
        return null;
    }

}