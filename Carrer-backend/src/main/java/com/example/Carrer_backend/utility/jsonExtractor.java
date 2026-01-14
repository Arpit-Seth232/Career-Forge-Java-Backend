package com.example.Carrer_backend.utility;

public class jsonExtractor {

    public static String extractJson(String input) throws Exception {

        int startIndex = input.indexOf("{");
        int endIndex = input.lastIndexOf("}");

        if(startIndex == -1 || endIndex == -1) {
            throw new Exception("No valid Json found in gemini response");
        }

        return input.substring(startIndex, endIndex + 1);


    }
    
}
