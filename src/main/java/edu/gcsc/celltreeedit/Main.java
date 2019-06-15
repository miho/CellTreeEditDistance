package edu.gcsc.celltreeedit;

import com.apporiented.algorithm.clustering.Cluster;
import edu.gcsc.celltreeedit.AppProperties.AppProperties;
import edu.gcsc.celltreeedit.AppProperties.CommandLineParsing;
import edu.gcsc.celltreeedit.JsonIO.JsonUtils;
import edu.gcsc.celltreeedit.Lucene.CLI;
import edu.gcsc.celltreeedit.Lucene.LuceneIndexWriter;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataMapper;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataRImpl;
import javafx.util.Pair;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Erid on 12.02.2018.
 */
public class Main {

    public static void main(String[] args) throws IOException {

        AppProperties appProperties = CommandLineParsing.parseArguments(args);

        // calcType
        // 0 (default) -> calculate everything
        // 1 -> only calc matrix with minimum resources
        // 2 -> choose for json-file
        // 3 -> preprocess swc-files
        switch (appProperties.getCalcType()) {
            case 0:
                calculateCompletely(appProperties);
                break;
            case 1:
                calculateMatrixOnly(appProperties);
                break;
            case 2:
                queryLucene(appProperties);
                break;
            case 3:
                queryByTypeCombination(appProperties);
                break;
            case 4:
                queryByFileDialog(appProperties);
                break;
            case 5:
                preprocessSWCDirectory(appProperties);
                break;
            default:
                System.out.println("calcType not valid");
                break;
        }
    }

    private static void calculateCompletely(AppProperties appProperties) throws IOException {
        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataRImpl> neuronMetadata = neuronMetadataMapper.mapFromDirectory(appProperties.getMetadataDirectory());

        CellTreeEditDistance cellTreeEditDistance = new CellTreeEditDistance();
        Pair<double[][], String[]> result;
        if (appProperties.getJsonDirectory().getPath().equals("")) {
            result = cellTreeEditDistance.compareFilesFromDirectory(appProperties.getSwcFileDirectory(), 9);
        } else {
            result = cellTreeEditDistance.compareFilesFromFilenames(JsonUtils.parseJsonToFileNames(appProperties.getJsonDirectory()), appProperties.getSwcFileDirectory(), 9);
        }
        Utils.printToTxt(result.getKey(), result.getValue(), appProperties.getOutputDirectory(), appProperties.getMatrixExportName());
        // calculate clustering
        Clustering clustering = Clustering.getInstance();
        Cluster cluster = clustering.createCluster(result.getKey(), result.getValue());
        // generate dendrogram
        clustering.showCluster(cluster);
    }

    private static void calculateMatrixOnly(AppProperties appProperties) throws IOException {
        CellTreeEditDistance cellTreeEditDistance = new CellTreeEditDistance();
        Pair<double[][], String[]> result;
        if (appProperties.getJsonDirectory().getPath().equals("")) {
            result = cellTreeEditDistance.compareFilesFromDirectory(appProperties.getSwcFileDirectory(), 9);
        } else {
            result = cellTreeEditDistance.compareFilesFromFilenames(JsonUtils.parseJsonToFileNames(appProperties.getJsonDirectory()), appProperties.getSwcFileDirectory(), 9);
        }
        Utils.printToTxt(result.getKey(), result.getValue(), appProperties.getOutputDirectory(), appProperties.getMatrixExportName());
    }

    private static void queryLucene(AppProperties appProperties) throws IOException {
        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataRImpl> neuronMetadata = neuronMetadataMapper.mapFromDirectory(appProperties.getMetadataDirectory());

        File indexDirectory = new File(appProperties.getWorkingDirectory() + "/LuceneIndex");
        LuceneIndexWriter luceneIndexWriter = new LuceneIndexWriter(indexDirectory);
        luceneIndexWriter.createIndex(neuronMetadata);
        System.out.println("lucene index created!");
        CLI.startCLI(indexDirectory, appProperties.getOutputDirectory());
//        testQueryLucene(indexDirectory);
        // let user query metadata through lucene
        // let user export names into json file
    }

    private static void queryByTypeCombination(AppProperties appProperties) throws IOException {
        System.out.println("inside queryByTypeCombination");
        int noOfTypes = 10;
        int noOfNeuronsPerType = 10;

        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataRImpl> neuronMetadata = neuronMetadataMapper.mapFromDirectory(appProperties.getMetadataDirectory());
        // add all metadata to UniqueMetadata
        for (String neuronMetadataRKey : neuronMetadata.keySet()) {
            UniqueMetadata.addNeuronMetadata(neuronMetadata.get(neuronMetadataRKey));
        }
        List<UniqueMetadata> sortedUniqueMetadata = new ArrayList<>(UniqueMetadata.getUniqueMetadataMap().keySet());
        // sort uniqueMetadata
        sortedUniqueMetadata.sort(Comparator.comparingInt(UniqueMetadata::getNoOfNeurons).reversed());
        System.out.println(sortedUniqueMetadata.size());

        // select neurons depending on typeCount and input-variables
        List<String> selectedNeuronNames = new ArrayList<>();
        int k = 1;
        for (UniqueMetadata uniqueMetadata : sortedUniqueMetadata) {
            if (k > noOfTypes) {
                break;
            }
            // select noOfNeuronsPerType neurons randomly
            selectedNeuronNames.addAll(pickNRandom(uniqueMetadata.getNeuronNames(), noOfNeuronsPerType));
            System.out.println(uniqueMetadata.getSpecies() + ";" + String.join(", ", uniqueMetadata.getBrainRegion()) + ";" + String.join(", ", uniqueMetadata.getCellTypes()) + ";" + uniqueMetadata.getNoOfNeurons() + ";" + uniqueMetadata.getArchives().size());
            k += 1;
        }
        // write to json
        JsonUtils.writeJSON(selectedNeuronNames, appProperties.getOutputDirectory());
    }

    public static List<String> pickNRandom(List<String> lst, int n) {
        List<String> copy = new LinkedList<>(lst);
        Collections.shuffle(copy);
        return n > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, n);
    }

    private static void queryByFileDialog(AppProperties appProperties) throws IOException {
        System.out.println("inside queryByFileDialog");
        File[] files = Utils.choose();
        List<String> selectedNeuronNames = Arrays.stream(files).map(file -> Utils.removeSWCFileExtensions(file.getName())).collect(Collectors.toList());

        // write to json
        JsonUtils.writeJSON(selectedNeuronNames, appProperties.getOutputDirectory());
    }

    private static void testQueryLucene(File indexPath) {

        // query consists of lucene terms and lucene operators
        try {
            Directory indexDirectory = FSDirectory.open(indexPath.toPath());
            IndexReader indexReader = DirectoryReader.open(indexDirectory);
            final IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            Analyzer analyzer = new StandardAnalyzer();
            QueryParser queryParser = new QueryParser("neuronId", analyzer);
            String queryString = "cellType:" + "interneuron" + " AND brainRegion:" + "lumbar";
            Collector collector = new TotalHitCountCollector();
            TopDocs topDocs = indexSearcher.search(queryParser.parse(queryString), 107395);



//            Term t = new Term("cellType", "interneuron");
//            Query query = new TermQuery(t);
//            TopDocs topDocs = indexSearcher.search(query, 10);

            for (ScoreDoc scoreDoc: topDocs.scoreDocs) {
                System.out.println(indexSearcher.doc(scoreDoc.doc).getField("neuronId").stringValue());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void preprocessSWCDirectory(AppProperties appProperties) throws IOException {
        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataRImpl> neuronMetadata = neuronMetadataMapper.mapFromDirectory(appProperties.getMetadataDirectory());

        // preprocess SWC-Directory
        File swcDirectory = new File("/media/exdisk/Sem06/BA/Data/SWC-Files/00_All");
        SWCPreprocessing swcPreprocessing = new SWCPreprocessing();
        swcPreprocessing.preprocessSWCDirectory(neuronMetadata, swcDirectory);
    }
}
