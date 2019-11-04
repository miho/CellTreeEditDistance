/*
 * Copyright 2018-2019 Michael Hoffer <info@michaelhoffer.de>. All rights reserved.
 * Copyright 2018-2019 Goethe Center for Scientific Computing, University Frankfurt. All rights reserved.
 * Copyright 2018 Erid Guga. All rights reserved.
 * Copyright 2019 Lukas Maurer. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY Michael Hoffer <info@michaelhoffer.de> "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL Michael Hoffer <info@michaelhoffer.de> OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of Michael Hoffer <info@michaelhoffer.de>.
 */
package edu.gcsc.celltreeedit;

import com.apporiented.algorithm.clustering.visualization.ClusterColorRegex;
import edu.gcsc.celltreeedit.AppProperties.AppProperties;
import edu.gcsc.celltreeedit.AppProperties.CommandLineParsing;
import edu.gcsc.celltreeedit.ClusterAnalysis.ClusteringAnalyzer;
import edu.gcsc.celltreeedit.Clustering.DendrogramCreator;
import edu.gcsc.celltreeedit.JsonIO.JsonUtils;
import edu.gcsc.celltreeedit.Lucene.CLI;
import edu.gcsc.celltreeedit.Lucene.LuceneIndexWriter;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataMapper;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataR;
import edu.gcsc.celltreeedit.NeuronMetadata.UniqueMetadataContainer;
import edu.gcsc.celltreeedit.TEDCalculation.CellTreeEditDistance;
import edu.gcsc.celltreeedit.TEDCalculation.TEDClusterResult;
import edu.gcsc.celltreeedit.TEDCalculation.TEDResult;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Erid on 12.02.2018. Extended by Lukas Maurer
 */
public class Main {

    private static AppProperties appProperties = AppProperties.getInstance();

    // Regex to control coloring in Dendrograms
    private static final List<ClusterColorRegex> clusterColorRegexes = Arrays.asList(
            new ClusterColorRegex(Pattern.compile("^.*"), Color.BLACK),
            new ClusterColorRegex(Pattern.compile("^XXX$"), new Color(35, 106, 185)),
            new ClusterColorRegex(Pattern.compile("^XXX$"), new Color(186, 26, 70)),
            new ClusterColorRegex(Pattern.compile("^XXX$"), new Color(68, 141, 118)),
            new ClusterColorRegex(Pattern.compile("^XXX$"), new Color(118, 60, 118)),
            new ClusterColorRegex(Pattern.compile("^XXX$"), Color.GRAY),
            new ClusterColorRegex(Pattern.compile("^XXX$"), new Color(160, 100, 0))
    );

    /**
     * Main-method starts the commandline parsing and calls a function according to case
     *
     * @param args
     * @throws IOException
     * @throws ParseException
     */
    public static void main(String[] args) throws IOException, ParseException {

        CommandLineParsing.parseArguments(args);

        switch (appProperties.getCalcType()) {
            case 0:
                preprocessSWCDirectory();
                break;
            case 1:
                queryLucene();
                break;
            case 2:
                queryByUniqueMetadata();
                break;
            case 3:
                calculateTEDMatrix();
                break;
            case 4:
                calculateTEDMatrixAndDendrogram();
                break;
            case 5:
                calculateDendrogram();
                break;
            case 6:
                calculateTEDMatricesForJsonFilesWithAllLabels();
                break;
            case 7:
                calculateDendrogramsForTEDMatrices();
                break;
            case 8:
                calculateTEDMatrixAndDendrogramByFileDialog();
                break;
            case 9:
                analyzeClusteringOfTEDMatrices();
                break;
            case 10:
                printMetadataOfJsonFiles();
                break;
            case 11:
                copySWCFilesOfJsonFilesToOutput();
                break;
            case 12:
                calculateTEDMatrixOnCluster();
                break;
            case 13:
                reassembleClusterMatricesToTxt();
                break;
            default:
                System.out.println("calcType not valid");
                break;
        }
    }


    /**
     * Starts preprocessing of SWCDirectory
     *
     * @throws IOException if no json-files containing neuronMetadata are found
     */
    private static void preprocessSWCDirectory() throws IOException {
        System.out.println("> Starting preprocessing \n");
        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataR> neuronMetadata = neuronMetadataMapper.mapAllFromMetadataDirectory(appProperties.getMetadataDirectory());

        // preprocess SWC-Directory
        SWCPreprocessing swcPreprocessing = new SWCPreprocessing();
        swcPreprocessing.preprocessSWCDirectory(neuronMetadata, appProperties.getSwcFileDirectory());
    }


    /**
     * Starts CLI for querying SWC-Files using Lucene
     *
     * @throws IOException if no json-files containing neuronMetadata are found
     */
    private static void queryLucene() throws IOException {
        System.out.println("> Starting Lucene");
        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataR> neuronMetadata = neuronMetadataMapper.mapExistingFromMetadataDirectory(appProperties.getMetadataDirectory(), appProperties.getSwcFileDirectory());

        File indexDirectory = new File(appProperties.getWorkingDirectory() + "/LuceneIndex");
        LuceneIndexWriter luceneIndexWriter = new LuceneIndexWriter(indexDirectory);
        luceneIndexWriter.createIndex(neuronMetadata);
        CLI.startCLI(indexDirectory, appProperties.getOutputDirectory(), appProperties.getSwcFileDirectory(), appProperties.getNameOutput());
    }


    /**
     * For querying SWC-Files using a predefined combination of UniqueMetadata
     *
     * @throws IOException if no json-files containing neuronMetadata are found
     */
    private static void queryByUniqueMetadata() throws IOException {
        System.out.println("> Starting query by predefined UniqueMetadata\n");
        int noOfNeuronsPerType = 143;

        // define which uniqueMetadata-groups shall be used
        UniqueMetadataContainer predefinedUniqueMetadataContainer = new UniqueMetadataContainer();
        Set<UniqueMetadataContainer.UniqueMetadata> predefinedUniqueMetadata = new HashSet<>();
        predefinedUniqueMetadata.add(predefinedUniqueMetadataContainer.createUniqueMetadataObject("", "", "rat", new HashSet<>(Arrays.asList("hippocampus")), new HashSet<>(Arrays.asList("principal cell", "pyramidal"))));
        predefinedUniqueMetadata.add(predefinedUniqueMetadataContainer.createUniqueMetadataObject("", "", "rat", new HashSet<>(Arrays.asList("dentate gyrus", "hippocampus")), new HashSet<>(Arrays.asList("principal cell", "granule"))));

        // get existing neuronMetadata and put it in existingUniqueMetadataContainer --> neuronNames for each uniqueMetadata-group --> select neurons randomly from uniqueMetadata-group
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataR> neuronMetadata = neuronMetadataMapper.mapExistingFromMetadataDirectory(appProperties.getMetadataDirectory(), appProperties.getSwcFileDirectory());
        UniqueMetadataContainer existingUniqueMetadataContainer = new UniqueMetadataContainer();
        // add all existing neuronMetadata to UniqueMetadata
        for (String neuronMetadataRKey : neuronMetadata.keySet()) {
            existingUniqueMetadataContainer.addNeuronMetadata(neuronMetadata.get(neuronMetadataRKey));
        }

//         select neurons depending on typeCount and input-variables
        List<String> selectedNeuronNames = new ArrayList<>();
        for (UniqueMetadataContainer.UniqueMetadata uniqueMetadata : predefinedUniqueMetadata) {
            // select noOfNeuronsPerType neurons randomly
            selectedNeuronNames.addAll(pickNRandom(existingUniqueMetadataContainer.getUniqueMetadataMap().get(uniqueMetadata).getNeuronNames(), noOfNeuronsPerType));
            System.out.println("species: " + uniqueMetadata.getSpecies() + "; " + "brainRegion" + String.join(", ", uniqueMetadata.getBrainRegions()) + "; " + "cellTypes: " + String.join(", ", uniqueMetadata.getCellTypes()) + "; " + "noOfNeurons: " + existingUniqueMetadataContainer.getUniqueMetadataMap().get(uniqueMetadata).getNoOfNeurons() + "; " + "noOfArchives: " + existingUniqueMetadataContainer.getUniqueMetadataMap().get(uniqueMetadata).getArchives().size());
        }

        List<File> selectedNeuronFiles = Utils.getFilesForNeuronnames(selectedNeuronNames, appProperties.getSwcFileDirectory());
        // write to json
        JsonUtils.writeToJSON(selectedNeuronFiles, "queried by uniqueMetadata", appProperties.getSwcFileDirectory(), appProperties.getOutputDirectory(), appProperties.getNameOutput());
    }

    private static List<String> pickNRandom(List<String> lst, int n) {
        List<String> copy = new LinkedList<>(lst);
        Collections.shuffle(copy);
        return n > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, n);
    }

    /**
     * Calculates TEDMatrix for neurons defined in a json-file
     *
     * @throws IOException
     */
    private static void calculateTEDMatrix() throws IOException {
        System.out.println("> Starting TED calculation\n");
        TEDResult result = calculateTEDMatrixForJson();
        String nameOutput = Utils.renameMatrixOutputWithJson(appProperties.getNameOutput(), appProperties.getFileInput().getName());
        Utils.printMatrixToTxt(result.getDistanceMatrix(), result.getFileNames(), appProperties.getOutputDirectory(), nameOutput);
    }

    private static TEDResult calculateTEDMatrixForJson() throws IOException {
        CellTreeEditDistance cellTreeEditDistance = new CellTreeEditDistance();
        File[] files = JsonUtils.parseJsonToFiles(appProperties.getFileInput());
        for (int i = 0; i < files.length; i++) {
            files[i] = new File(appProperties.getSwcFileDirectory() + "/" + files[i].getPath());
        }
        return cellTreeEditDistance.compareFilesFromFiles(files, appProperties.getLabel());
    }

    /**
     * Calculates TEDMatrix for neurons defined in a json-file and creates a Dendrogram for it
     *
     * @throws IOException
     */
    private static void calculateTEDMatrixAndDendrogram() throws IOException {
        System.out.println("> Starting TED calculation and Dendrogram calculation\n");
        TEDResult result = calculateTEDMatrixForJson();
        String nameOutput = Utils.renameMatrixOutputWithJson(appProperties.getNameOutput(), appProperties.getFileInput().getName());
        if (appProperties.isSaveOutput()) {
            nameOutput = FilenameUtils.removeExtension(Utils.printMatrixToTxt(result.getDistanceMatrix(), result.getFileNames(), appProperties.getOutputDirectory(), nameOutput).getName());
        }
        DendrogramCreator.createDendrogram(result, appProperties.getMetadataDirectory(), appProperties.getOutputDirectory(), nameOutput, appProperties.isRenameDendrogram(), appProperties.isSaveOutput(), clusterColorRegexes);
    }

    /**
     * Calculates dendrogram for a given distance-matrix
     *
     * @throws IOException
     */
    private static void calculateDendrogram() throws IOException {
        System.out.println("> Starting Dendrogram calculation\n");
        TEDResult result = Utils.readMatrixFromTxt(appProperties.getFileInput());
        String nameOutput = (appProperties.getNameOutput().isEmpty()) ? result.getName() : appProperties.getNameOutput();
        DendrogramCreator.createDendrogram(result, appProperties.getMetadataDirectory(), appProperties.getOutputDirectory(), nameOutput, appProperties.isRenameDendrogram(), appProperties.isSaveOutput(), clusterColorRegexes);
    }

    /**
     * Calculates TEDMatrices for several json-files and all implemented labels
     *
     * @throws IOException
     */
    private static void calculateTEDMatricesForJsonFilesWithAllLabels() throws IOException {
        System.out.println("> Starting TED calculation for Json-Files with all labels");

        File[] jsonFiles = Utils.chooseJSONFiles();

        for (File jsonFile : jsonFiles) {
            System.out.println("\nJsonFile: " + jsonFile.getName());
            File[] files = JsonUtils.parseJsonToFiles(jsonFile);
            for (int i = 0; i < files.length; i++) {
                files[i] = new File(appProperties.getSwcFileDirectory() + "/" + files[i].getPath());
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            CellTreeEditDistance cellTreeEditDistance;
            TEDResult result;
            String labelNumber;
            for (int i = 1; i < 25; i += 1) {
                Date date = new Date();
                System.out.println(dateFormat.format(date) + " selected Label " + i);
                cellTreeEditDistance = new CellTreeEditDistance();
                result = cellTreeEditDistance.compareFilesFromFiles(files, i);
                if (i < 10) {
                    labelNumber = "0" + i;
                } else {
                    labelNumber = Integer.toString(i);
                }
                Utils.printMatrixToTxt(result.getDistanceMatrix(), result.getFileNames(), appProperties.getOutputDirectory(), FilenameUtils.removeExtension(jsonFile.getName()) + "_Matrix_Label" + labelNumber + ".txt");
            }
        }
    }


    /**
     * Calculates dendrograms for several distance-matrices
     *
     * @throws IOException
     */
    private static void calculateDendrogramsForTEDMatrices() throws IOException {
        System.out.println("> Starting Dendrogram calculation for TEDMatrices");

        List<TEDResult> results = Utils.readMatricesFromTxt();
        for (TEDResult result : results) {
            DendrogramCreator.createDendrogram(result, appProperties.getMetadataDirectory(), appProperties.getOutputDirectory(), result.getName(), appProperties.isRenameDendrogram(), appProperties.isSaveOutput(), clusterColorRegexes);
        }
    }

    /**
     * Calculates TEDMatrix and a dendrogram. SWC-Files are given by filedialog
     *
     */
    private static void calculateTEDMatrixAndDendrogramByFileDialog() {
        System.out.println("> Starting TED calculation and Dendrogram calculation by file-dialog");
        // Files in File[] speichern
        File[] files = Utils.chooseSWCFiles();
        File outputDirectory = new File("/" + FilenameUtils.getPath(files[0].getAbsolutePath()));
        String nameOutput = Utils.renameMatrixOutput(appProperties.getNameOutput());
        // calculateTED
        CellTreeEditDistance cellTreeEditDistance = new CellTreeEditDistance();
        TEDResult result = cellTreeEditDistance.compareFilesFromFiles(files, appProperties.getLabel());
        if (appProperties.isSaveOutput()) {
            nameOutput = FilenameUtils.removeExtension(Utils.printMatrixToTxt(result.getDistanceMatrix(), result.getFileNames(), outputDirectory, nameOutput).getName());
        }
        DendrogramCreator.createDendrogram(result, outputDirectory, nameOutput, appProperties.isSaveOutput(), clusterColorRegexes);
    }

    /** TODO: Test and improve
     * Analyzes Clusterings of multiple matrices and displays the results.
     *
     *
     * @throws IOException
     */
    private static void analyzeClusteringOfTEDMatrices() throws IOException {
        System.out.println("> Starting Analysis of Clustering for TEDMatrices");

        List<TEDResult> results = Utils.readMatricesFromTxt();

        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataR> neuronMetadata = neuronMetadataMapper.mapAllFromMetadataDirectory(appProperties.getMetadataDirectory());

        for (TEDResult result : results) {
            ClusteringAnalyzer.analyzeClusteringOfTEDResult(result, neuronMetadata);
        }
    }

    /**
     * Prints Metadata of a json-file to a csv-file
     *
     * @throws IOException
     */
    public static void printMetadataOfJsonFiles() throws IOException {
        System.out.println("> Starting print Metadata for Json-files");
        // input: jsonFiles
        // output: file with Filenames to metadata mapping
        // filename, archive, species, brainRegions, cellTypes, ageClassification, minAge, maxAge, physicalIntegrity, domain, attributes, protocol, reconstructionSoftware, experimentCondition, filesize
        File[] jsonFiles = Utils.chooseJSONFiles();

        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataR> neuronMetadata = neuronMetadataMapper.mapExistingFromMetadataDirectory(appProperties.getMetadataDirectory(), appProperties.getSwcFileDirectory());

        for (File jsonFile : jsonFiles) {
            System.out.println("\nJsonFile: " + jsonFile.getName());
            File[] files = JsonUtils.parseJsonToFiles(jsonFile);
            String[] filenames = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                filenames[i] = Utils.removeSWCFileExtensions(files[i].getName());
            }
            Utils.printMetadataForFilenames(filenames, neuronMetadata, appProperties.getOutputDirectory(), FilenameUtils.removeExtension(jsonFile.getName()));
        }
    }

    /**
     * Copies SWCFiles from the swcFileDirectory to the Outputdirectory. Multiple Jsonfiles are possible
     *
     * @throws IOException
     */
    public static void copySWCFilesOfJsonFilesToOutput() throws IOException {
        System.out.println("> Starting copy of SWC-Files from Json to Output-directory");
        File[] jsonFiles = Utils.chooseJSONFiles();
        String outputDirectory;
        for (File jsonFile : jsonFiles) {
            File[] files = JsonUtils.parseJsonToFiles(jsonFile);

            for (int i = 0; i < files.length; i++) {
                files[i] = new File(appProperties.getSwcFileDirectory() + "/" + files[i].getPath());
            }
            // get directory name from jsonFileName
            // create Directory
            String newFolder = FilenameUtils.removeExtension(FilenameUtils.getName(jsonFile.getName()));
            outputDirectory = appProperties.getOutputDirectory().getAbsolutePath() + "/" + newFolder;
            new File(outputDirectory).mkdirs();

            FileWriter export = new FileWriter(outputDirectory + "/" + "relativePaths");
            BufferedWriter br = new BufferedWriter(export);

            for (int i = 0; i < files.length; i++) {
                // copy File to directory
                Utils.copyFile(files[i], outputDirectory);
                br.write("./" + newFolder + "/" + files[i].getName() + " ");
            }
            br.close();
        }
    }

    /**
     * Calculates part of a TEDMatrix. can be used for calculation on a cluster
     *
     * @throws IOException
     */
    public static void calculateTEDMatrixOnCluster() throws IOException {
        System.out.println("> Starting calculation of TEDMatrix on Cluster");

        File[] files = JsonUtils.parseJsonToFiles(appProperties.getFileInput());
        CellTreeEditDistance cellTreeEditDistance = new CellTreeEditDistance();
        TEDClusterResult tedClusterResult = cellTreeEditDistance.compareFilesForCluster(files, appProperties.getRows(), appProperties.getIteration(), appProperties.getLabel(), appProperties.getSwcFileDirectory());

        Utils.printClusterMatrixToTxt(tedClusterResult, appProperties.getFileInput(), appProperties.getOutputDirectory(), "Clustermatrix_" + appProperties.getLabel() + "_" + appProperties.getIteration());
    }

    /**
     * Reassembles partially calculated TEDMatrices to one big Matrix
     *
     * @throws IOException
     */
    public static void reassembleClusterMatricesToTxt() throws IOException {
        File[] files = JsonUtils.parseJsonToFiles(appProperties.getFileInput());
        Utils.reassembleClusterMatrixToTxt(appProperties.getDirectoryInput(), files, appProperties.getOutputDirectory(), "ReassembledMatrix");
    }
}
