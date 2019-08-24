package edu.gcsc.celltreeedit.Lucene;

import edu.gcsc.celltreeedit.JsonIO.JsonUtils;
import edu.gcsc.celltreeedit.JsonIO.PathType;
import edu.gcsc.celltreeedit.Utils;
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

    public static void startCLI(File indexDirectory, File baseDirectory, File outputDirectory, File swcFileDirectory, String jsonName) throws IOException {
        // preparation for queryparsing
        Directory luceneDirectory = FSDirectory.open(indexDirectory.toPath());
        IndexReader indexReader = DirectoryReader.open(luceneDirectory);
        final IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        Analyzer analyzer = new CaseInsensitiveKeywordAnalyzer();
        QueryParser queryParser = new QueryParser("neuronId", analyzer);
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
                // perform search. results limited to 200000 entries
                TopDocs topDocs = indexSearcher.search(queryParser.parse(query), 200000);
                String parsedQuery = queryParser.parse(query).toString();
                System.out.println("Parsed query: " + parsedQuery);
                // output number of results
                // save to file?
                while (true) {
                    System.out.println("Search produced " + topDocs.totalHits.value + " results. Save result? (y/n)");
                    s = br.readLine();
                    if (s.toLowerCase().equals("y")) {
                        exportNamesToJson(indexSearcher, topDocs, "Queried by Lucene. Parsed Query: " + parsedQuery, outputDirectory, swcFileDirectory, jsonName);
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

    private static void exportNamesToJson(IndexSearcher indexSearcher, TopDocs topDocs, String comment, File outputDirectory, File swcFileDirectory, String jsonName) {
        List<String> neuronNames = new ArrayList<>();
        try {
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                neuronNames.add(indexSearcher.doc(scoreDoc.doc).getField("neuronName").stringValue());
            }
            List<File> selectedNeuronFiles = Utils.getFilesForNeuronNames(neuronNames, swcFileDirectory);
            // write to json
            JsonUtils.writeToJSON(selectedNeuronFiles, comment, swcFileDirectory, outputDirectory, jsonName);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
