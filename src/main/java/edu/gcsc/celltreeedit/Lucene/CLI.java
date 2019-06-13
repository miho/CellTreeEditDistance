package edu.gcsc.celltreeedit.Lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

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
                // perform search
                TopDocs topDocs = indexSearcher.search(queryParser.parse(query), 110000);
                System.out.println("parsed Search: " + queryParser.parse(query).toString());
                // output number of results
                // save to file?
                while (true) {
                    System.out.println("Search produced " + topDocs.totalHits.value + " results. Save result to ... ? (y/n)");
                    s = br.readLine();
                    if (s.toLowerCase().equals("y")) {
                        DocExport docExport = new DocExport();
                        docExport.exportNamesToJson(indexSearcher, topDocs, outputDirectory);
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
}
