package edu.gcsc.celltreeedit.Lucene;

import edu.gcsc.celltreeedit.JsonIO.JsonUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Command Line Interface for querying NeuronMetadata using Lucene.
 */
public class CLI {

    public static void startCLI(File indexPath, File outputDirectory) throws IOException {
        // preparation for queryparsing
        Directory indexDirectory = FSDirectory.open(indexPath.toPath());
        IndexReader indexReader = DirectoryReader.open(indexDirectory);
        final IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        Analyzer analyzer = new CaseInsensitiveKeywordAnalyzer();
        QueryParser queryParser = new QueryParser("neuronId", analyzer);
        Collector collector = new TotalHitCountCollector();
        String query = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("\nEnter Lucene-Query for searching Neuron-Metadata (q to quit):");

            String s = br.readLine();

            if (s.equals("q")) {
                break;
            }

            query = s;

            try {
                // perform search. results limited to 110000 entries
                TopDocs topDocs = indexSearcher.search(queryParser.parse(query), 110000);
                System.out.println("parsed Search: " + queryParser.parse(query).toString());
                // output number of results
                // save to file?
                while (true) {
                    System.out.println("Search produced " + topDocs.totalHits.value + " results. Save result to ... ? (y/n)");
                    s = br.readLine();
                    if (s.toLowerCase().equals("y")) {
                        exportNamesToJson(indexSearcher, topDocs, outputDirectory);
                        break;
                    } else if (s.toLowerCase().equals("n")) {
                        break;
                    }
                }

                // restart
            } catch (ParseException ex) {
                System.out.println("Query could not be parsed. Please try again.");
            }
        }
    }

    private static void exportNamesToJson(IndexSearcher indexSearcher, TopDocs topDocs, File outputDirectory) {
        List<String> neuronNames = new ArrayList<>();
        try {
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                neuronNames.add(indexSearcher.doc(scoreDoc.doc).getField("neuronName").stringValue());
            }
            JsonUtils.writeJSON(neuronNames, outputDirectory);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
