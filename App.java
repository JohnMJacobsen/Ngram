package edu.sdsu.cs.datastructures;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.Scanner;

public class Ngrams {

    public static void main(String args[]) {

        File inFile = new File(args[0]);
        File outFile = new File(args[1]);
        int counter = 0;
        String token;
        IMap<Integer, String> unigrams = new BalancedMap<>();

        try {
            Scanner scnr = new Scanner(new FileInputStream(inFile));
            Formatter f = new Formatter(outFile);

            while (scnr.hasNext()) {
                token = scnr.next();
                unigrams.add(counter, token);
                counter++;
            }

            IMap<String, Integer> bigrams = fillBigrams(unigrams);
            f.format(printNgrams(bigrams));

            f.format("\n");

            IMap<String, Integer> trigrams = fillTrigrams(unigrams);
            f.format(printNgrams(trigrams));

            scnr.close();
            f.close();

        } catch (FileNotFoundException e) {
            System.out.println("Bad Healey");
        }
    }

    private static IMap<String, Integer> fillBigrams(IMap<Integer, String> unigrams) {
        int occurrences;
        String tempString;
        IMap<String, Integer> bigrams = new BalancedMap<>();

        for (int i = 0; i < unigrams.size() - 1; ++i) {
            tempString = unigrams.getValue(i) + " " + unigrams.getValue(i + 1);
            if (!bigrams.contains(tempString)) {
                bigrams.add(tempString, 1);
            } else {
                occurrences = bigrams.getValue(tempString);
                bigrams.delete(tempString);
                bigrams.add(tempString, occurrences + 1);
            }
        }
        return bigrams;
    }

    private static IMap<String, Integer> fillTrigrams(IMap<Integer, String> unigrams) {
        int occurrences;
        String tempString;
        IMap<String, Integer> trigrams = new BalancedMap<>();

        for (int i = 0; i < unigrams.size() - 2; ++i) {
            tempString = unigrams.getValue(i) + " " + unigrams.getValue(i + 1) + " " + unigrams.getValue(i + 2);
            if (!trigrams.contains(tempString)) {
                trigrams.add(tempString, 1);
            } else {
                occurrences = trigrams.getValue(tempString);
                trigrams.delete(tempString);
                trigrams.add(tempString, occurrences + 1);
            }
        }
        return trigrams;
    }

    private static String printNgrams(IMap<String, Integer> Ngrams) {
        String outputString = "";
        int maxCounts = 100;
        for (int i = 0; i < maxCounts; maxCounts--) {
            for (String key : Ngrams.keyset()) {
                if (Ngrams.getValue(key) == maxCounts) {
                    outputString += (Ngrams.getValue(key) + " " + key + "\n");
                }
            }
        }
        return outputString;
    }
}
