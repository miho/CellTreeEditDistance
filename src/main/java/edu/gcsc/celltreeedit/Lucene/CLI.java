package edu.gcsc.celltreeedit.Lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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

    public static void startCLI(File indexPath) throws IOException {
        // preparation for queryparsing
        Directory indexDirectory = FSDirectory.open(indexPath.toPath());
        IndexReader indexReader = DirectoryReader.open(indexDirectory);
        final IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        Analyzer analyzer = new StandardAnalyzer();
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
                System.out.println("Search produced " + topDocs.totalHits.value + " results. Save result to ... ? (y/n)");
                s = br.readLine();
                // output number of results
                // save to file?
                // restart
            } catch (ParseException ex) {
                System.out.println("Query could not be parsed. Please try again.");
            }
        }
    }
}
