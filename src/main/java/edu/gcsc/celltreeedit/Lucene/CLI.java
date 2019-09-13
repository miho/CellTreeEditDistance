package edu.gcsc.celltreeedit.Lucene;

import edu.gcsc.celltreeedit.JsonIO.JsonUtils;
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
import java.util.Collections;
import java.util.List;

/**
 * Command Line Interface for querying NeuronMetadata using Lucene.
 */
public class CLI {

    public static void startCLI(File indexDirectory, File outputDirectory, File swcFileDirectory, String jsonName) throws IOException {
        // preparation for queryparsing
        Directory luceneDirectory = FSDirectory.open(indexDirectory.toPath());
        IndexReader indexReader = DirectoryReader.open(luceneDirectory);
        final IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        Analyzer analyzer = new CaseInsensitiveKeywordAnalyzer();
        QueryParser queryParser = new QueryParser("neuronId", analyzer);
        String query = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder comment = new StringBuilder("Queried by Lucene.");
        List<File> selectedNeuronFiles = new ArrayList<>();

        while (true) {
            System.out.println("\nYour options: 1. Enter Lucene-Query for searching Neuron-Metadata  2. Quit (q)  3. Save output (s)");

            String s = br.readLine();

            if (s.equals("q")) {
                break;
            }
            if (s.equals("s")) {
                JsonUtils.writeToJSON(selectedNeuronFiles, comment.toString(), swcFileDirectory, outputDirectory, jsonName);
                comment = new StringBuilder("Parsed Lucene Queries: ");
                selectedNeuronFiles = new ArrayList<>();
                continue;
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
                    int totalHits = Math.toIntExact(topDocs.totalHits.value);
                    if (totalHits == 0) {
                        System.out.println("Search produced " + totalHits);
                        break;
                    }
                    System.out.println("Search produced " + totalHits + " results. Your options: 1. Add complete result to output? (c)  2. Add limited result to output (integer) 3. Discard result (d)");
                    s = br.readLine();
                    if (s.toLowerCase().equals("d")) {
                        break;
                    } else if (s.toLowerCase().equals("c")) {
                        comment.append(" ; ").append(query);
                        selectedNeuronFiles.addAll(getFilesForQueryResult(indexSearcher, topDocs, swcFileDirectory, -1));
                        break;
                    } else if (s.matches("(0|[1-9]\\d*)")) {
                        comment.append(" ; ").append(query);
                        int limitSize = Integer.parseInt(s);
                        limitSize = (limitSize > totalHits) ? -1 : limitSize;
                        selectedNeuronFiles.addAll(getFilesForQueryResult(indexSearcher, topDocs, swcFileDirectory, limitSize));
                        break;
                    }
                }

                // restart
            } catch (ParseException ex) {
                System.out.println("Query could not be parsed. Please try again.");
            }
        }
    }


    private static List<File> getFilesForQueryResult(IndexSearcher indexSearcher, TopDocs topDocs, File swcFileDirectory, int limitSize) {
        List<String> neuronNames = new ArrayList<>();
        try {
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                neuronNames.add(indexSearcher.doc(scoreDoc.doc).getField("neuronName").stringValue());
            }
            if (limitSize != -1) {
                Collections.shuffle(neuronNames);
                neuronNames = neuronNames.subList(0, limitSize);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return Utils.getFilesForNeuronnames(neuronNames, swcFileDirectory);
    }
}
