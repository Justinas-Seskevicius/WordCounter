package com.company;

public class Main {

    public static void main(String[] args) throws Exception {
        WordCounter recorder = new WordCounter(args[0]);
        recorder.countWords();
    }
}
