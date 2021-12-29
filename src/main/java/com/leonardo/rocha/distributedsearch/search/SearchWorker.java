package com.leonardo.rocha.distributedsearch.search;

import com.leonardo.rocha.distributedsearch.model.DocumentData;
import com.leonardo.rocha.distributedsearch.model.Result;
import com.leonardo.rocha.distributedsearch.model.SerializationUtils;
import com.leonardo.rocha.distributedsearch.model.Task;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SearchWorker {

    public static byte[] handleRequest(byte[] requestPayload){
        Task task = (Task) SerializationUtils.deserialize(requestPayload);
        Result result = SearchWorker.createResult(task);
        return SerializationUtils.serialize(result);
    }

    public static Result createResult(Task task) {
        List<String> documents = task.getDocuments();
        System.out.println(String.format("Received %d documents to process", documents.size()));
        Result result = new Result();

        for (String document : documents) {
            List<String> words = parseWordsFromDocument(document);
            DocumentData documentData = TFIDF.createDocumentData(words, task.getSearchTerms());
            result.addDocumentData(document, documentData);
        }
        return result;
    }

    private static List<String> parseWordsFromDocument(String document) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(document);
        } catch (FileNotFoundException e) {
            return Collections.emptyList();
        }

        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = bufferedReader.lines().collect(Collectors.toList());
        List<String> words = TFIDF.getWordsFromDocument(lines);
        return words;
    }
}
