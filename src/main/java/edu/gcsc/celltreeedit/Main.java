package edu.gcsc.celltreeedit;

//import edu.gcsc.celltreeedit.AppProperties.AppParameter;
import com.apporiented.algorithm.clustering.Cluster;
import edu.gcsc.celltreeedit.AppProperties.AppProperties;
import edu.gcsc.celltreeedit.AppProperties.CommandLineParsing;
import edu.gcsc.celltreeedit.Lucene.CLI;
import edu.gcsc.celltreeedit.Lucene.LuceneIndexWriter;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataMapper;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataRImpl;
import javafx.util.Pair;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

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
                System.out.println("inside case 2");
                queryLucene(appProperties);
                break;
            case 3:
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
            result = cellTreeEditDistance.compareFilesFromFilenames(Utils.parseJsonToFileNames(appProperties.getJsonDirectory()), appProperties.getSwcFileDirectory(), 9);
        }
        Utils.printToTxt(result.getKey(), result.getValue(), appProperties.getMatrixExportDirectory(), appProperties.getMatrixExportName());
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
            result = cellTreeEditDistance.compareFilesFromFilenames(Utils.parseJsonToFileNames(appProperties.getJsonDirectory()), appProperties.getSwcFileDirectory(), 9);
        }
        Utils.printToTxt(result.getKey(), result.getValue(), appProperties.getMatrixExportDirectory(), appProperties.getMatrixExportName());
    }

    private static void queryLucene(AppProperties appProperties) throws IOException {
        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataRImpl> neuronMetadata = neuronMetadataMapper.mapFromDirectory(appProperties.getMetadataDirectory());

        File indexDirectory = new File("/media/exdisk/Sem06/BA/ProgramData/LuceneIndex");
        LuceneIndexWriter luceneIndexWriter = new LuceneIndexWriter(indexDirectory);
        luceneIndexWriter.createIndex(neuronMetadata);
        System.out.println("lucene index created!");
        CLI.startCLI(indexDirectory);
//        testQueryLucene(indexDirectory);
        // let user query metadata through lucene
        // let user export names into json file
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
