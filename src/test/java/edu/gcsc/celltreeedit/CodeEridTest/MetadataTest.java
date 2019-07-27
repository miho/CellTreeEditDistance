package edu.gcsc.celltreeedit.CodeEridTest;

import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataMapper;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataR;
import edu.gcsc.celltreeedit.NeuronMetadata.UniqueMetadataContainer;
import groovy.json.internal.IO;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class MetadataTest {

    @Test
    public void allMetadataTest() throws IOException {
        File metadataDirectory = new File(BaseDirectory.baseDirectory.getPath() + "/Test");
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataR> neuronMetadata = neuronMetadataMapper.mapAllFromMetadataDirectory(metadataDirectory);

        // check size
        assertEquals(neuronMetadata.size(), relevantNeuronMetadata.length);

        // for each string in relevantNeuronMetadata check entries of neuronMetadata
        NeuronMetadataR neuronMetadataR;
        List<String> testList;

        for (int i = 0; i < relevantNeuronMetadata.length; i++) {
            neuronMetadataR = neuronMetadata.get(relevantNeuronMetadata[i][1]);

            // test neuronId
            assertEquals(Integer.parseInt(relevantNeuronMetadata[i][0]), neuronMetadataR.getNeuronId().intValue());

            // test archive
            assertEquals(relevantNeuronMetadata[i][2], neuronMetadataR.getArchive());

            // test brainRegion
            testList = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                if (!relevantNeuronMetadata[i][3 + j].isEmpty()) {
                    testList.add(relevantNeuronMetadata[i][3 + j]);
                }
            }
            assertEquals(testList, neuronMetadataR.getBrainRegion());

            // test cellType
            testList = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                if (!relevantNeuronMetadata[i][7 + j].isEmpty()) {
                    testList.add(relevantNeuronMetadata[i][7 + j]);
                }
            }
            assertEquals(testList, neuronMetadataR.getCellType());

            // test species
            assertEquals(relevantNeuronMetadata[i][10], neuronMetadataR.getSpecies());
        }
    }


    @Test
    public void existingMetadataTest() throws IOException {
        File metadataDirectory = new File(BaseDirectory.baseDirectory.getPath() + "/Test");
        File swcFileDirectory = new File(BaseDirectory.baseDirectory.getPath() + "/Test/TestSWCFiles");
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataR> neuronMetadata = neuronMetadataMapper.mapExistingFromMetadataDirectory(metadataDirectory, swcFileDirectory);

        // check size
        assertEquals(neuronMetadata.size(), existingNeuronMetadata.length);

        // for each string in relevantNeuronMetadata check entries of neuronMetadata
        NeuronMetadataR neuronMetadataR;
        List<String> testList;

        for (int i = 0; i < existingNeuronMetadata.length; i++) {
            neuronMetadataR = neuronMetadata.get(existingNeuronMetadata[i][1]);

            // test neuronId
            assertEquals(Integer.parseInt(existingNeuronMetadata[i][0]), neuronMetadataR.getNeuronId().intValue());

            // test archive
            assertEquals(existingNeuronMetadata[i][2], neuronMetadataR.getArchive());

            // test brainRegion
            testList = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                if (!existingNeuronMetadata[i][3 + j].isEmpty()) {
                    testList.add(existingNeuronMetadata[i][3 + j]);
                }
            }
            assertEquals(testList, neuronMetadataR.getBrainRegion());

            // test cellType
            testList = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                if (!existingNeuronMetadata[i][7 + j].isEmpty()) {
                    testList.add(existingNeuronMetadata[i][7 + j]);
                }
            }
            assertEquals(testList, neuronMetadataR.getCellType());

            // test species
            assertEquals(existingNeuronMetadata[i][10], neuronMetadataR.getSpecies());
        }
    }

    @Test
    public void uniqueMetadataTest() throws IOException {
        File metadataDirectory = new File(BaseDirectory.baseDirectory.getPath() + "/Test");
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataR> neuronMetadata = neuronMetadataMapper.mapAllFromMetadataDirectory(metadataDirectory);

        // add all existing neuronMetadata to UniqueMetadata
        UniqueMetadataContainer uniqueMetadataContainer = new UniqueMetadataContainer();
        for (String neuronMetadataRKey : neuronMetadata.keySet()) {
            uniqueMetadataContainer.addNeuronMetadata(neuronMetadata.get(neuronMetadataRKey));
        }

        // numberOfUniqueMetadata correct also sizes of datastructures
        assertEquals(24, uniqueMetadataContainer.getNoOfUniqueMetadata());
        assertEquals(24, uniqueMetadataContainer.getUniqueMetadataMap().size());
        assertEquals(500, uniqueMetadataContainer.getFileNameToUniqueMetadataMap().size());

        // numberOfNeurons per uniqueMetadataCategory correct

        // go through relevantNeuronMetadata and test whether category is correct, whether it contains the name and archive
        UniqueMetadataContainer.UniqueMetadata uniqueMetadata;
        Set<String> cellTypes;
        Set<String> brainRegion;
        String species;
        String neuronName;
        String archive;
        for (int i = 0; i < relevantNeuronMetadata.length; i++) {
            neuronName = relevantNeuronMetadata[i][1];
            archive = relevantNeuronMetadata[i][2];
            brainRegion = new HashSet<>();
            cellTypes = new HashSet<>();
            species = relevantNeuronMetadata[i][10];

            for (int j = 0; j < 4; j++ ) {
                if (!relevantNeuronMetadata[i][3 + j].equals("")) {
                    brainRegion.add(relevantNeuronMetadata[i][3 + j]);
                }
            }
            for (int j = 0; j < 3; j++ ) {
                if (!relevantNeuronMetadata[i][7 + j].equals("")) {
                    cellTypes.add(relevantNeuronMetadata[i][7 + j]);
                }
            }

            uniqueMetadata = new UniqueMetadataContainer().createUniqueMetadataObject(cellTypes, brainRegion, species, neuronName, archive);
            assertEquals(uniqueMetadata, uniqueMetadataContainer.getFileNameToUniqueMetadataMap().get(neuronName));
            assertTrue(uniqueMetadataContainer.getUniqueMetadataMap().get(uniqueMetadata).getNeuronNames().contains(neuronName));
            assertTrue(uniqueMetadataContainer.getUniqueMetadataMap().get(uniqueMetadata).getArchives().contains(archive));
        }

        for (int i = 0; i < neuronNamesForUniqueMetadataCategory.length; i++) {
            assertEquals(neuronCountsPerUniqueMetadataCategory[i], uniqueMetadataContainer.getFileNameToUniqueMetadataMap().get(neuronNamesForUniqueMetadataCategory[i]).getNoOfNeurons());
        }
    }

    private String[] neuronNamesForUniqueMetadataCategory = new String[]{
            "test_set_skeleton_246",
            "n419",
            "5-27-11cell1-nonFS-BC",
            "10-15-2011-xs2-fse-sham-SGC",
            "Tracetest_N360_square_Map2Tau2_14_semi-auto_1",
            "oi57rpy2-1",
            "oi33lpy1-1",
            "oi28rpy1-1",
            "cnic_001",
            "DS3_030701",
            "IF1_190301",
            "ALA",
            "ADAR",
            "AIBR",
            "ADFR",
            "AFDR",
            "AINR",
            "ALMR",
            "AQR",
            "ALNR",
            "AIYR",
            "AIZR",
            "AS2",
            "MSN-shell"
    };

    private int[] neuronCountsPerUniqueMetadataCategory = new int[]{
            37,
            1,
            1,
            3,
            411,
            6,
            1,
            1,
            2,
            3,
            1,
            1,
            2,
            2,
            2,
            4,
            2,
            2,
            1,
            4,
            2,
            6,
            4,
            1,
    };

    private String[] relevantNeuronMetadataHeader = new String[]{
            "neuron_id", "neuron_name", "archive", "brain_region/0", "brain_region/1", "brain_region/2", "brain_region/3", "cell_type/0", "cell_type/1", "cell_type/2", "species"
    };

    private String[][] relevantNeuronMetadata = new String[][]{
            {"1", "cnic_001", "Wearne_Hof", "neocortex", "prefrontal", "layer 3", "", "Local projecting", "pyramidal", "principal cell", "monkey" },
            {"10", "cnic_041", "Wearne_Hof", "neocortex", "prefrontal", "layer 3", "", "Local projecting", "pyramidal", "principal cell", "monkey" },
            {"100", "n419", "Turner", "hippocampus", "CA1", "", "", "pyramidal", "principal cell", "", "rat" },
            {"1000", "DS3_030701", "Staiger", "neocortex", "somatosensory", "barrel", "layer 4", "Spiny", "stellate", "principal cell", "rat" },
            {"10000", "AS2", "OpenWorm", "somatic nervous system", "ventral nerve cord", "", "", "Motoneuron", "principal cell", "", "C. elegans" },
            {"100000", "test_set_skeleton_246", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100001", "test_set_skeleton_261", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100002", "test_set_skeleton_266", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100003", "test_set_skeleton_29", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100004", "test_set_skeleton_310", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100005", "test_set_skeleton_321", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100006", "test_set_skeleton_354", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100007", "test_set_skeleton_358", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100008", "test_set_skeleton_364", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100009", "test_set_skeleton_371", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"10001", "AS11", "OpenWorm", "somatic nervous system", "ventral nerve cord", "", "", "Motoneuron", "principal cell", "", "C. elegans" },
            {"100010", "test_set_skeleton_397", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100011", "test_set_skeleton_40", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100012", "test_set_skeleton_402", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100013", "test_set_skeleton_404", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100014", "test_set_skeleton_416", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100015", "test_set_skeleton_423", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100016", "test_set_skeleton_429", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100017", "test_set_skeleton_43", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100018", "test_set_skeleton_441", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100019", "test_set_skeleton_469", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"10002", "AS10", "OpenWorm", "somatic nervous system", "ventral nerve cord", "", "", "Motoneuron", "principal cell", "", "C. elegans" },
            {"100020", "test_set_skeleton_480", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100021", "test_set_skeleton_496", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100022", "test_set_skeleton_5", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100023", "test_set_skeleton_508", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100024", "test_set_skeleton_530", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100025", "test_set_skeleton_545", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100026", "test_set_skeleton_547", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100027", "test_set_skeleton_557", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100028", "test_set_skeleton_582", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100029", "test_set_skeleton_588", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"10003", "AS1", "OpenWorm", "somatic nervous system", "ventral nerve cord", "", "", "Motoneuron", "principal cell", "", "C. elegans" },
            {"100030", "test_set_skeleton_607", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100031", "test_set_skeleton_62", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100032", "test_set_skeleton_66", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100033", "test_set_skeleton_670", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100034", "test_set_skeleton_675", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100035", "test_set_skeleton_73", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100036", "test_set_skeleton_99", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100037", "Tracetest_N360_square_Map2Tau2_14_semi-auto_1", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100038", "Tracetest_N360_square_Map2Tau2_14_semi-auto_10", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100039", "Tracetest_N360_square_Map2Tau2_14_semi-auto_11", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10004", "AQR", "OpenWorm", "somatic nervous system", "", "", "", "putative sensory", "somatic", "principal cell", "C. elegans" },
            {"100040", "Tracetest_N360_square_Map2Tau2_14_semi-auto_12", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100041", "Tracetest_N360_square_Map2Tau2_14_semi-auto_13", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100042", "Tracetest_N360_square_Map2Tau2_14_semi-auto_14", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100043", "Tracetest_N360_square_Map2Tau2_14_semi-auto_15", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100044", "Tracetest_N360_square_Map2Tau2_14_semi-auto_16", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100045", "Tracetest_N360_square_Map2Tau2_14_semi-auto_17", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100046", "Tracetest_N360_square_Map2Tau2_14_semi-auto_18", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100047", "Tracetest_N360_square_Map2Tau2_14_semi-auto_19", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100048", "Tracetest_N360_square_Map2Tau2_14_semi-auto_2", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100049", "Tracetest_N360_square_Map2Tau2_14_semi-auto_20", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10005", "ALNR", "OpenWorm", "somatic nervous system", "", "", "", "somatic", "principal cell", "", "C. elegans" },
            {"100050", "Tracetest_N360_square_Map2Tau2_14_semi-auto_21", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100051", "Tracetest_N360_square_Map2Tau2_14_semi-auto_22", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100052", "Tracetest_N360_square_Map2Tau2_14_semi-auto_23", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100053", "Tracetest_N360_square_Map2Tau2_14_semi-auto_24", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100054", "Tracetest_N360_square_Map2Tau2_14_semi-auto_25", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100055", "Tracetest_N360_square_Map2Tau2_14_semi-auto_27", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100056", "Tracetest_N360_square_Map2Tau2_14_semi-auto_28", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100057", "Tracetest_N360_square_Map2Tau2_14_semi-auto_3", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100058", "Tracetest_N360_square_Map2Tau2_14_semi-auto_4", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100059", "Tracetest_N360_square_Map2Tau2_14_semi-auto_5", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10006", "ALNL", "OpenWorm", "somatic nervous system", "", "", "", "somatic", "principal cell", "", "C. elegans" },
            {"100060", "Tracetest_N360_square_Map2Tau2_14_semi-auto_6", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100061", "Tracetest_N360_square_Map2Tau2_14_semi-auto_7", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100062", "Tracetest_N360_square_Map2Tau2_14_semi-auto_8", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100063", "Tracetest_N360_square_Map2Tau2_14_semi-auto_9", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100064", "Tracetest_N360_square_Map2Tau2_14_semi-auto_NEW_1", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100065", "Tracetest_N360_square_Map2Tau2_14_semi-auto_NEW_10", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100066", "Tracetest_N360_square_Map2Tau2_14_semi-auto_NEW_2", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100067", "Tracetest_N360_square_Map2Tau2_14_semi-auto_NEW_3", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100068", "Tracetest_N360_square_Map2Tau2_14_semi-auto_NEW_4", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100069", "Tracetest_N360_square_Map2Tau2_14_semi-auto_NEW_5", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10007", "ALMR", "OpenWorm", "somatic nervous system", "", "", "", "Mechanosensory", "somatic", "principal cell", "C. elegans" },
            {"100070", "Tracetest_N360_square_Map2Tau2_14_semi-auto_NEW_6", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100071", "Tracetest_N360_square_Map2Tau2_14_semi-auto_NEW_7", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100072", "Tracetest_N360_square_Map2Tau2_14_semi-auto_NEW_8", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100073", "Tracetest_N360_square_Map2Tau2_14_semi-auto_NEW_9", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100074", "Tracetest_N360_square_Map2Tau2_18_semi-auto_1", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100075", "Tracetest_N360_square_Map2Tau2_18_semi-auto_10", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100076", "Tracetest_N360_square_Map2Tau2_18_semi-auto_11", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100077", "Tracetest_N360_square_Map2Tau2_18_semi-auto_12", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100078", "Tracetest_N360_square_Map2Tau2_18_semi-auto_13", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100079", "Tracetest_N360_square_Map2Tau2_18_semi-auto_14", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10008", "ALML", "OpenWorm", "somatic nervous system", "", "", "", "Mechanosensory", "somatic", "principal cell", "C. elegans" },
            {"100080", "Tracetest_N360_square_Map2Tau2_18_semi-auto_15", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100081", "Tracetest_N360_square_Map2Tau2_18_semi-auto_16", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100082", "Tracetest_N360_square_Map2Tau2_18_semi-auto_17", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100083", "Tracetest_N360_square_Map2Tau2_18_semi-auto_18", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100084", "Tracetest_N360_square_Map2Tau2_18_semi-auto_19", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100085", "Tracetest_N360_square_Map2Tau2_18_semi-auto_2", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100086", "Tracetest_N360_square_Map2Tau2_18_semi-auto_20", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100087", "Tracetest_N360_square_Map2Tau2_18_semi-auto_21", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100088", "Tracetest_N360_square_Map2Tau2_18_semi-auto_22", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100089", "Tracetest_N360_square_Map2Tau2_18_semi-auto_23", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10009", "ALA", "OpenWorm", "somatic nervous system", "dorsal ganglion", "", "", "interneuron", "", "", "C. elegans" },
            {"100090", "Tracetest_N360_square_Map2Tau2_18_semi-auto_25", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100091", "Tracetest_N360_square_Map2Tau2_18_semi-auto_26", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100092", "Tracetest_N360_square_Map2Tau2_18_semi-auto_27", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100093", "Tracetest_N360_square_Map2Tau2_18_semi-auto_28", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100094", "Tracetest_N360_square_Map2Tau2_18_semi-auto_3", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100095", "Tracetest_N360_square_Map2Tau2_18_semi-auto_4", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100096", "Tracetest_N360_square_Map2Tau2_18_semi-auto_5", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100097", "Tracetest_N360_square_Map2Tau2_18_semi-auto_6", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100098", "Tracetest_N360_square_Map2Tau2_18_semi-auto_7", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100099", "Tracetest_N360_square_Map2Tau2_18_semi-auto_8", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10010", "AIZR", "OpenWorm", "somatic nervous system", "ventral ganglion", "", "", "Ring", "interneuron", "", "C. elegans" },
            {"100100", "Tracetest_N360_square_Map2Tau2_18_semi-auto_9", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100101", "Tracetest_N360_square_Map2Tau2_26_semi-auto_1", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100102", "Tracetest_N360_square_Map2Tau2_26_semi-auto_11", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100103", "Tracetest_N360_square_Map2Tau2_26_semi-auto_12", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100104", "Tracetest_N360_square_Map2Tau2_26_semi-auto_13", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100105", "Tracetest_N360_square_Map2Tau2_26_semi-auto_14", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100106", "Tracetest_N360_square_Map2Tau2_26_semi-auto_15", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100107", "Tracetest_N360_square_Map2Tau2_26_semi-auto_16", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100108", "Tracetest_N360_square_Map2Tau2_26_semi-auto_17", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100109", "Tracetest_N360_square_Map2Tau2_26_semi-auto_18", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10011", "AIZL", "OpenWorm", "somatic nervous system", "ventral ganglion", "", "", "Ring", "interneuron", "", "C. elegans" },
            {"100110", "Tracetest_N360_square_Map2Tau2_26_semi-auto_19", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100111", "Tracetest_N360_square_Map2Tau2_26_semi-auto_2", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100112", "Tracetest_N360_square_Map2Tau2_26_semi-auto_20", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100113", "Tracetest_N360_square_Map2Tau2_26_semi-auto_21", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100114", "Tracetest_N360_square_Map2Tau2_26_semi-auto_22", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100115", "Tracetest_N360_square_Map2Tau2_26_semi-auto_23", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100116", "Tracetest_N360_square_Map2Tau2_26_semi-auto_24", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100117", "Tracetest_N360_square_Map2Tau2_26_semi-auto_25", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100118", "Tracetest_N360_square_Map2Tau2_26_semi-auto_26", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100119", "Tracetest_N360_square_Map2Tau2_26_semi-auto_27", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10012", "AIYR", "OpenWorm", "somatic nervous system", "ventral ganglion", "", "", "Amphid", "interneuron", "", "C. elegans" },
            {"100120", "Tracetest_N360_square_Map2Tau2_26_semi-auto_29", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100121", "Tracetest_N360_square_Map2Tau2_26_semi-auto_3", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100122", "Tracetest_N360_square_Map2Tau2_26_semi-auto_30", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100123", "Tracetest_N360_square_Map2Tau2_26_semi-auto_31", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100124", "Tracetest_N360_square_Map2Tau2_26_semi-auto_32", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100125", "Tracetest_N360_square_Map2Tau2_26_semi-auto_33", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100126", "Tracetest_N360_square_Map2Tau2_26_semi-auto_4", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100127", "Tracetest_N360_square_Map2Tau2_26_semi-auto_5", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100128", "Tracetest_N360_square_Map2Tau2_26_semi-auto_6", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100129", "Tracetest_N360_square_Map2Tau2_26_semi-auto_7", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10013", "AIYL", "OpenWorm", "somatic nervous system", "ventral ganglion", "", "", "Amphid", "interneuron", "", "C. elegans" },
            {"100130", "Tracetest_N360_square_Map2Tau2_26_semi-auto_8", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100131", "Tracetest_N360_square_Map2Tau2_26_semi-auto_9", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100132", "Tracetest_N360_square_Map2Tau2_49_semi-auto_1", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100133", "Tracetest_N360_square_Map2Tau2_49_semi-auto_10", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100134", "Tracetest_N360_square_Map2Tau2_49_semi-auto_11", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100135", "Tracetest_N360_square_Map2Tau2_49_semi-auto_12", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100136", "Tracetest_N360_square_Map2Tau2_49_semi-auto_13", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100137", "Tracetest_N360_square_Map2Tau2_49_semi-auto_14", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100138", "Tracetest_N360_square_Map2Tau2_49_semi-auto_15", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100139", "Tracetest_N360_square_Map2Tau2_49_semi-auto_16", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10014", "AINR", "OpenWorm", "somatic nervous system", "lateral ganglion", "", "", "Ring", "interneuron", "", "C. elegans" },
            {"100140", "Tracetest_N360_square_Map2Tau2_49_semi-auto_17", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100141", "Tracetest_N360_square_Map2Tau2_49_semi-auto_18", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100142", "Tracetest_N360_square_Map2Tau2_49_semi-auto_19", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100143", "Tracetest_N360_square_Map2Tau2_49_semi-auto_2", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100144", "Tracetest_N360_square_Map2Tau2_49_semi-auto_20", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100145", "Tracetest_N360_square_Map2Tau2_49_semi-auto_21", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100146", "Tracetest_N360_square_Map2Tau2_49_semi-auto_22", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100147", "Tracetest_N360_square_Map2Tau2_49_semi-auto_23", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100148", "Tracetest_N360_square_Map2Tau2_49_semi-auto_24", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100149", "Tracetest_N360_square_Map2Tau2_49_semi-auto_25", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10015", "AINL", "OpenWorm", "somatic nervous system", "lateral ganglion", "", "", "Ring", "interneuron", "", "C. elegans" },
            {"100150", "Tracetest_N360_square_Map2Tau2_49_semi-auto_26", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100151", "Tracetest_N360_square_Map2Tau2_49_semi-auto_27", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100152", "Tracetest_N360_square_Map2Tau2_49_semi-auto_28", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100153", "Tracetest_N360_square_Map2Tau2_49_semi-auto_29", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100154", "Tracetest_N360_square_Map2Tau2_49_semi-auto_3", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100155", "Tracetest_N360_square_Map2Tau2_49_semi-auto_30", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100156", "Tracetest_N360_square_Map2Tau2_49_semi-auto_31", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100157", "Tracetest_N360_square_Map2Tau2_49_semi-auto_32", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100158", "Tracetest_N360_square_Map2Tau2_49_semi-auto_33", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100159", "Tracetest_N360_square_Map2Tau2_49_semi-auto_34", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10016", "AIMR", "OpenWorm", "somatic nervous system", "ventral ganglion", "", "", "Ring", "interneuron", "", "C. elegans" },
            {"100160", "Tracetest_N360_square_Map2Tau2_49_semi-auto_35", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100161", "Tracetest_N360_square_Map2Tau2_49_semi-auto_4", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100162", "Tracetest_N360_square_Map2Tau2_49_semi-auto_5", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100163", "Tracetest_N360_square_Map2Tau2_49_semi-auto_6", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100164", "Tracetest_N360_square_Map2Tau2_49_semi-auto_7", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100165", "Tracetest_N360_square_Map2Tau2_49_semi-auto_8", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100166", "Tracetest_N360_square_Map2Tau2_49_semi-auto_9", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100167", "Tracetest_N360_square_Map2Tau2_55_semi-auto_10", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100168", "Tracetest_N360_square_Map2Tau2_55_semi-auto_11", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100169", "Tracetest_N360_square_Map2Tau2_55_semi-auto_12", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10017", "AIML", "OpenWorm", "somatic nervous system", "ventral ganglion", "", "", "Ring", "interneuron", "", "C. elegans" },
            {"100170", "Tracetest_N360_square_Map2Tau2_55_semi-auto_13", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100171", "Tracetest_N360_square_Map2Tau2_55_semi-auto_14", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100172", "Tracetest_N360_square_Map2Tau2_55_semi-auto_15", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100173", "Tracetest_N360_square_Map2Tau2_55_semi-auto_16", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100174", "Tracetest_N360_square_Map2Tau2_55_semi-auto_17", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100175", "Tracetest_N360_square_Map2Tau2_55_semi-auto_18", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100176", "Tracetest_N360_square_Map2Tau2_55_semi-auto_19", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100177", "Tracetest_N360_square_Map2Tau2_55_semi-auto_2", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100178", "Tracetest_N360_square_Map2Tau2_55_semi-auto_20", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100179", "Tracetest_N360_square_Map2Tau2_55_semi-auto_21", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10018", "AIBR", "OpenWorm", "somatic nervous system", "lateral ganglion", "", "", "Amphid", "interneuron", "", "C. elegans" },
            {"100180", "Tracetest_N360_square_Map2Tau2_55_semi-auto_22", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100181", "Tracetest_N360_square_Map2Tau2_55_semi-auto_23", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100182", "Tracetest_N360_square_Map2Tau2_55_semi-auto_24", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100183", "Tracetest_N360_square_Map2Tau2_55_semi-auto_25", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100184", "Tracetest_N360_square_Map2Tau2_55_semi-auto_26", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100185", "Tracetest_N360_square_Map2Tau2_55_semi-auto_27", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100186", "Tracetest_N360_square_Map2Tau2_55_semi-auto_28", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100187", "Tracetest_N360_square_Map2Tau2_55_semi-auto_29", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100188", "Tracetest_N360_square_Map2Tau2_55_semi-auto_3", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100189", "Tracetest_N360_square_Map2Tau2_55_semi-auto_30", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10019", "AIBL", "OpenWorm", "somatic nervous system", "lateral ganglion", "", "", "Amphid", "interneuron", "", "C. elegans" },
            {"100190", "Tracetest_N360_square_Map2Tau2_55_semi-auto_31", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100191", "Tracetest_N360_square_Map2Tau2_55_semi-auto_32", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100192", "Tracetest_N360_square_Map2Tau2_55_semi-auto_33", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100193", "Tracetest_N360_square_Map2Tau2_55_semi-auto_34", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100194", "Tracetest_N360_square_Map2Tau2_55_semi-auto_36", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100195", "Tracetest_N360_square_Map2Tau2_55_semi-auto_37", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100196", "Tracetest_N360_square_Map2Tau2_55_semi-auto_38", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100197", "Tracetest_N360_square_Map2Tau2_55_semi-auto_39", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100198", "Tracetest_N360_square_Map2Tau2_55_semi-auto_4", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100199", "Tracetest_N360_square_Map2Tau2_55_semi-auto_40", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"1002", "DS3_100101_wL", "Staiger", "neocortex", "somatosensory", "barrel", "layer 4", "Spiny", "stellate", "principal cell", "rat" },
            {"10020", "AIAR", "OpenWorm", "somatic nervous system", "ventral ganglion", "", "", "Ring", "interneuron", "", "C. elegans" },
            {"100200", "Tracetest_N360_square_Map2Tau2_55_semi-auto_41", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100201", "Tracetest_N360_square_Map2Tau2_55_semi-auto_42", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100202", "Tracetest_N360_square_Map2Tau2_55_semi-auto_43", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100203", "Tracetest_N360_square_Map2Tau2_55_semi-auto_44", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100204", "Tracetest_N360_square_Map2Tau2_55_semi-auto_45", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100205", "Tracetest_N360_square_Map2Tau2_55_semi-auto_46", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100206", "Tracetest_N360_square_Map2Tau2_55_semi-auto_47", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100207", "Tracetest_N360_square_Map2Tau2_55_semi-auto_48", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100208", "Tracetest_N360_square_Map2Tau2_55_semi-auto_49", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100209", "Tracetest_N360_square_Map2Tau2_55_semi-auto_5", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10021", "AIAL", "OpenWorm", "somatic nervous system", "ventral ganglion", "", "", "Ring", "interneuron", "", "C. elegans" },
            {"100210", "Tracetest_N360_square_Map2Tau2_55_semi-auto_50", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100211", "Tracetest_N360_square_Map2Tau2_55_semi-auto_51", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100212", "Tracetest_N360_square_Map2Tau2_55_semi-auto_52", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100213", "Tracetest_N360_square_Map2Tau2_55_semi-auto_53", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100214", "Tracetest_N360_square_Map2Tau2_55_semi-auto_54", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100215", "Tracetest_N360_square_Map2Tau2_55_semi-auto_6", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100216", "Tracetest_N360_square_Map2Tau2_55_semi-auto_7", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100217", "Tracetest_N360_square_Map2Tau2_55_semi-auto_8", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100218", "Tracetest_N360_square_Map2Tau2_55_semi-auto_9", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100219", "Tracetest_N360_square_Map2Tau2_58_semi-auto_1", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10022", "AFDR", "OpenWorm", "somatic nervous system", "lateral ganglion", "", "", "Multisensory", "somatic", "principal cell", "C. elegans" },
            {"100220", "Tracetest_N360_square_Map2Tau2_58_semi-auto_10", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100221", "Tracetest_N360_square_Map2Tau2_58_semi-auto_11", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100222", "Tracetest_N360_square_Map2Tau2_58_semi-auto_12", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100223", "Tracetest_N360_square_Map2Tau2_58_semi-auto_13", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100224", "Tracetest_N360_square_Map2Tau2_58_semi-auto_14", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100225", "Tracetest_N360_square_Map2Tau2_58_semi-auto_15", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100226", "Tracetest_N360_square_Map2Tau2_58_semi-auto_16", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100227", "Tracetest_N360_square_Map2Tau2_58_semi-auto_17", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100228", "Tracetest_N360_square_Map2Tau2_58_semi-auto_18", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100229", "Tracetest_N360_square_Map2Tau2_58_semi-auto_19", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10023", "AFDL", "OpenWorm", "somatic nervous system", "lateral ganglion", "", "", "Multisensory", "somatic", "principal cell", "C. elegans" },
            {"100230", "Tracetest_N360_square_Map2Tau2_58_semi-auto_2", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100231", "Tracetest_N360_square_Map2Tau2_58_semi-auto_20", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100232", "Tracetest_N360_square_Map2Tau2_58_semi-auto_21", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100233", "Tracetest_N360_square_Map2Tau2_58_semi-auto_22", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100234", "Tracetest_N360_square_Map2Tau2_58_semi-auto_23", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100235", "Tracetest_N360_square_Map2Tau2_58_semi-auto_24", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100236", "Tracetest_N360_square_Map2Tau2_58_semi-auto_25", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100237", "Tracetest_N360_square_Map2Tau2_58_semi-auto_26", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100238", "Tracetest_N360_square_Map2Tau2_58_semi-auto_27", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100239", "Tracetest_N360_square_Map2Tau2_58_semi-auto_28", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10024", "ADLR", "OpenWorm", "somatic nervous system", "lateral ganglion", "", "", "Multisensory", "somatic", "principal cell", "C. elegans" },
            {"100240", "Tracetest_N360_square_Map2Tau2_58_semi-auto_29", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100241", "Tracetest_N360_square_Map2Tau2_58_semi-auto_3", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100242", "Tracetest_N360_square_Map2Tau2_58_semi-auto_30", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100243", "Tracetest_N360_square_Map2Tau2_58_semi-auto_31", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100244", "Tracetest_N360_square_Map2Tau2_58_semi-auto_32", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100245", "Tracetest_N360_square_Map2Tau2_58_semi-auto_33", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100246", "Tracetest_N360_square_Map2Tau2_58_semi-auto_34", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100247", "Tracetest_N360_square_Map2Tau2_58_semi-auto_4", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100248", "Tracetest_N360_square_Map2Tau2_58_semi-auto_5", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100249", "Tracetest_N360_square_Map2Tau2_58_semi-auto_6", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10025", "ADLL", "OpenWorm", "somatic nervous system", "lateral ganglion", "", "", "Multisensory", "somatic", "principal cell", "C. elegans" },
            {"100250", "Tracetest_N360_square_Map2Tau2_58_semi-auto_7", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100251", "Tracetest_N360_square_Map2Tau2_58_semi-auto_8", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100252", "Tracetest_N360_square_Map2Tau2_58_semi-auto_9", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100253", "Tracetest_N360_square_Betatub_5_semi-auto_1", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100254", "Tracetest_N360_square_Betatub_5_semi-auto_10", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100255", "Tracetest_N360_square_Betatub_5_semi-auto_11", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100256", "Tracetest_N360_square_Betatub_5_semi-auto_13", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100257", "Tracetest_N360_square_Betatub_5_semi-auto_14", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100258", "Tracetest_N360_square_Betatub_5_semi-auto_15", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100259", "Tracetest_N360_square_Betatub_5_semi-auto_16", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10026", "ADFR", "OpenWorm", "somatic nervous system", "lateral ganglion", "", "", "Chemosensory", "somatic", "principal cell", "C. elegans" },
            {"100260", "Tracetest_N360_square_Betatub_5_semi-auto_17", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100261", "Tracetest_N360_square_Betatub_5_semi-auto_18", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100262", "Tracetest_N360_square_Betatub_5_semi-auto_19", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100263", "Tracetest_N360_square_Betatub_5_semi-auto_2", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100264", "Tracetest_N360_square_Betatub_5_semi-auto_22", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100265", "Tracetest_N360_square_Betatub_5_semi-auto_23", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100266", "Tracetest_N360_square_Betatub_5_semi-auto_24", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100267", "Tracetest_N360_square_Betatub_5_semi-auto_25", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100268", "Tracetest_N360_square_Betatub_5_semi-auto_26", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100269", "Tracetest_N360_square_Betatub_5_semi-auto_27", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10027", "ADFL", "OpenWorm", "somatic nervous system", "lateral ganglion", "", "", "Chemosensory", "somatic", "principal cell", "C. elegans" },
            {"100270", "Tracetest_N360_square_Betatub_5_semi-auto_29", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100271", "Tracetest_N360_square_Betatub_5_semi-auto_3", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100272", "Tracetest_N360_square_Betatub_5_semi-auto_31", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100273", "Tracetest_N360_square_Betatub_5_semi-auto_32", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100274", "Tracetest_N360_square_Betatub_5_semi-auto_34", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100275", "Tracetest_N360_square_Betatub_5_semi-auto_35", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100276", "Tracetest_N360_square_Betatub_5_semi-auto_36", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100277", "Tracetest_N360_square_Betatub_5_semi-auto_37", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100278", "Tracetest_N360_square_Betatub_5_semi-auto_38", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100279", "Tracetest_N360_square_Betatub_5_semi-auto_39", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10028", "ADER", "OpenWorm", "somatic nervous system", "", "", "", "somatic", "principal cell", "", "C. elegans" },
            {"100280", "Tracetest_N360_square_Betatub_5_semi-auto_4", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100281", "Tracetest_N360_square_Betatub_5_semi-auto_5", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100282", "Tracetest_N360_square_Betatub_5_semi-auto_6", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100283", "Tracetest_N360_square_Betatub_5_semi-auto_7", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100284", "Tracetest_N360_square_Betatub_5_semi-auto_8", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100285", "Tracetest_N360_square_Betatub_5_semi-auto_9", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100286", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_1", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100287", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_10", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100288", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_11", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100289", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_12", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10029", "ADEL", "OpenWorm", "somatic nervous system", "", "", "", "somatic", "principal cell", "", "C. elegans" },
            {"100290", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_13", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100291", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_14", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100292", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_15", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100293", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_16", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100294", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_17", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100295", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_18", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100296", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_19", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100297", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_2", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100298", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_20", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100299", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_21", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"1003", "IF1_030400", "Staiger", "neocortex", "somatosensory", "barrel", "layer 4", "Spiny", "stellate", "principal cell", "rat" },
            {"10030", "ADAR", "OpenWorm", "somatic nervous system", "", "", "", "Integrative ring", "interneuron", "", "C. elegans" },
            {"100300", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_22", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100301", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_23", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100302", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_24", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100303", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_25", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100304", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_26", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100305", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_27", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100306", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_28", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100307", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_29", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100308", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_3", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100309", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_30", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10031", "ADAL", "OpenWorm", "somatic nervous system", "", "", "", "Integrative ring", "interneuron", "", "C. elegans" },
            {"100310", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_32", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100311", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_33", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100312", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_34", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100313", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_35", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100314", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_36", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100315", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_38", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100316", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_39", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100317", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_4", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100318", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_40", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100319", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_5", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10032", "MSN-shell", "Spiga", "ventral striatum", "nucleus accumbens", "shell", "", "medium spiny", "principal cell", "", "rat" },
            {"100320", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_6", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100321", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_7", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100322", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_8", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100323", "Tracetest_N360_semicircle_Map2Tau2_120_semi-auto_9", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100324", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_1", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100325", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_10", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100326", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_11", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100327", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_12", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100328", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_13", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100329", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_14", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10033", "10-15-2011-xs2-fse-sham-SGC", "Santhakumar", "hippocampus", "dentate gyrus", "molecular layer", "inner", "semilunar granule", "principal cell", "", "rat" },
            {"100330", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_15", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100331", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_16", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100332", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_17", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100333", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_18", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100334", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_19", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100335", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_2", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100336", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_20", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100337", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_21", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100338", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_22", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100339", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_23", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10034", "10-02-2011-xs2-fse-sham-SGC", "Santhakumar", "hippocampus", "dentate gyrus", "molecular layer", "inner", "semilunar granule", "principal cell", "", "rat" },
            {"100340", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_24", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100341", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_25", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100342", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_26", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100343", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_27", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100344", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_28", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100345", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_29", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100346", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_3", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100347", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_30", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100348", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_31", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100349", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_32", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10035", "10-17-2011-xs4-fse-sham-SGC", "Santhakumar", "hippocampus", "dentate gyrus", "molecular layer", "inner", "semilunar granule", "principal cell", "", "rat" },
            {"100350", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_33", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100351", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_34", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100352", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_35", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100353", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_36", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100354", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_37", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100355", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_38", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100356", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_39", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100357", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_4", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100358", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_40", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100359", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_41", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10036", "5-27-11cell1-nonFS-BC", "Santhakumar", "hippocampus", "dentate gyrus", "granule layer", "", "Non-fast spiking", "basket", "interneuron", "rat" },
            {"100360", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_42", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100361", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_43", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100362", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_44", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100363", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_45", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100364", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_46", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100365", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_47", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100366", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_5", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100367", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_6", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100368", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_7", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100369", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_8", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10037", "oi57rpy2-1", "Kisvarday", "neocortex", "occipital", "layer 2-3", "", "pyramidal", "principal cell", "", "cat" },
            {"100370", "Tracetest_N360_semicircle_Map2Tau2_124_semi-auto_9", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100371", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_1", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100372", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_10", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100373", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_11", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100374", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_12", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100375", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_13", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100376", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_14", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100377", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_15", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100378", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_16", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100379", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_17", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10038", "oi57rpy1-1", "Kisvarday", "neocortex", "occipital", "layer 2-3", "", "pyramidal", "principal cell", "", "cat" },
            {"100380", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_19", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100381", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_2", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100382", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_20", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100383", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_21", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100384", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_22", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100385", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_23", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100386", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_24", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100387", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_25", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100388", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_26", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100389", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_27", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10039", "oi57lpy6-1", "Kisvarday", "neocortex", "occipital", "layer 2-3", "", "pyramidal", "principal cell", "", "cat" },
            {"100390", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_28", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100391", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_29", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100392", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_3", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100393", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_30", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100394", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_31", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100395", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_32", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100396", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_33", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100397", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_34", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100398", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_35", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100399", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_36", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"1004", "IF1_190301", "Staiger", "neocortex", "somatosensory", "barrel", "layer 4", "Star", "pyramidal", "principal cell", "rat" },
            {"10040", "oi53rpy1-1", "Kisvarday", "neocortex", "occipital", "layer 2-3", "", "pyramidal", "principal cell", "", "cat" },
            {"100400", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_37", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100401", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_38", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100402", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_39", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100403", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_4", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100404", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_40", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100405", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_41", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100406", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_42", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100407", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_43", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100408", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_44", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100409", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_5", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10041", "oi33lpy1-1", "Kisvarday", "neocortex", "occipital", "layer 4", "", "Spiny", "stellate", "interneuron", "cat" },
            {"100410", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_6", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100411", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_7", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100412", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_8", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100413", "Tracetest_N360_semicircle_Map2Tau2_126_semi-auto_9", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100414", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_1", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100415", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_10", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100416", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_11", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100417", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_12", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100418", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_13", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100419", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_14", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10042", "oi28rpy1-1", "Kisvarday", "neocortex", "occipital", "layer 5-6", "", "pyramidal", "principal cell", "", "cat" },
            {"100420", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_15", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100421", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_16", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100422", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_17", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100423", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_18", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100424", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_19", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100425", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_2", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100426", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_20", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100427", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_21", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100428", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_22", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100429", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_23", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10043", "Oi27rpy2-1", "Kisvarday", "neocortex", "occipital", "layer 2-3", "", "pyramidal", "principal cell", "", "cat" },
            {"100430", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_24", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100431", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_25", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100432", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_26", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100433", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_27", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100434", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_28", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100435", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_3", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100436", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_30", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100437", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_31", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100438", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_32", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100439", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_33", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"10044", "Oi27rpy1-1", "Kisvarday", "neocortex", "occipital", "layer 2-3", "", "pyramidal", "principal cell", "", "cat" },
            {"100440", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_34", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100441", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_35", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100442", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_36", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100443", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_37", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100444", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_38", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100445", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_39", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100446", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_4", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100447", "Tracetest_N360_semicircle_Map2Tau_74_semi-auto_40", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" }
    };

    private String[][] existingNeuronMetadata = new String[][]{
            {"10000", "AS2", "OpenWorm", "somatic nervous system", "ventral nerve cord", "", "", "Motoneuron", "principal cell", "", "C. elegans" },
            {"100000", "test_set_skeleton_246", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100010", "test_set_skeleton_397", "Kornfeld", "basal ganglia", "Area X", "", "", "principal cell", "", "", "zebra finch" },
            {"100037", "Tracetest_N360_square_Map2Tau2_14_semi-auto_1", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100048", "Tracetest_N360_square_Map2Tau2_14_semi-auto_2", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100057", "Tracetest_N360_square_Map2Tau2_14_semi-auto_3", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100058", "Tracetest_N360_square_Map2Tau2_14_semi-auto_4", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100059", "Tracetest_N360_square_Map2Tau2_14_semi-auto_5", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100060", "Tracetest_N360_square_Map2Tau2_14_semi-auto_6", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100061", "Tracetest_N360_square_Map2Tau2_14_semi-auto_7", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100062", "Tracetest_N360_square_Map2Tau2_14_semi-auto_8", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },
            {"100063", "Tracetest_N360_square_Map2Tau2_14_semi-auto_9", "Kuddannaya", "neocortex", "frontal", "", "", "principal cell", "", "", "rat" },

    };
}
