package com.company;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class WordCounter {

    private Map<Character, Map<String, Integer>> occurrences;
    private Map<String, Integer> exclusions;
    private String[] textFiles;

    public WordCounter(String inputDirectory) throws Exception{
        occurrences = new HashMap<>();
        exclusions = setUpExclusions(inputDirectory);
        textFiles = getTextFilesPaths(inputDirectory);
    }


    public void countWords() throws Exception {
        for(String textFile : textFiles){
            try(BufferedReader br = new BufferedReader(new FileReader(textFile))){
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                while (line != null) {
                    sb.append(line).append(System.lineSeparator());
                    line = br.readLine();
                }
                recordOccurrences(sb.toString());
            }
        }
        writeExclusionsToFile();
        writeOccurrencesToFiles();
    }

    private void recordOccurrences(String text){
        String[] words = text.split("\\s+");
        for(String word : words){
            String normalized = word.replaceAll("[\\W]|_", "").toLowerCase();
            if(exclusions.containsKey(normalized)){
                exclusions.put(normalized, exclusions.get(normalized) + 1);
                continue;
            }

            char firstLetter = normalized.charAt(0);
            if(occurrences.containsKey(firstLetter)){
                if (occurrences.get(firstLetter).containsKey(normalized)){
                    Map<String, Integer> wordOccurences = occurrences.get(firstLetter);
                    wordOccurences.put(normalized, wordOccurences.get(normalized) + 1);
                } else {
                    occurrences.get(firstLetter).put(normalized, 1);
                }
            } else {
                Map<String, Integer> wordOccurences = new HashMap<>();
                wordOccurences.put(normalized, 1);
                occurrences.put(firstLetter, wordOccurences);
            }
        }
    }

    private void writeExclusionsToFile() throws Exception{
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream( "output/encounteredExcludedWords.txt"), StandardCharsets.UTF_8))) {
            for(Map.Entry<String, Integer> exclusion : exclusions.entrySet()){
                writer.write(exclusion.getKey() + " " + exclusion.getValue() + "\n");
            }
        }
    }


    private void writeOccurrencesToFiles() throws Exception{
        for (Map.Entry<Character, Map<String, Integer>> letterOccurrence : occurrences.entrySet()){
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream( "output/" + Character.toUpperCase(letterOccurrence.getKey()) + ".txt"), StandardCharsets.UTF_8))) {
                for(Map.Entry<String, Integer> wordOccurrence : letterOccurrence.getValue().entrySet()){
                    writer.write(wordOccurrence.getKey() + " " + wordOccurrence.getValue() + "\n");
                }
            }
        }
    }

    private String[] getTextFilesPaths(String inputDirectory) throws Exception{
        try (Stream<Path> paths = Files.walk(Paths.get(inputDirectory))) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> !path.toString().equals(inputDirectory + "/exclude.txt"))
                    .map(Path::toString)
                    .toArray(String[]::new);
        }
    }

    private Map<String, Integer> setUpExclusions(String inputDirectory) throws Exception{
        Map<String, Integer> exclude = new HashMap<>();
        try(BufferedReader br = new BufferedReader(new FileReader(inputDirectory + "/exclude.txt"))){
            String line = br.readLine();
            while(line != null){
                exclude.put(line.toLowerCase(), 0);
                line = br.readLine();
            }
        }
        return exclude;
    }
}
