package com.example.Carrer_backend.Service;

import java.util.Base64;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import com.google.genai.types.GenerateContentResponse;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String MODEL = "gemini-2.5-flash";


    public String call_gemini(String resumeBase64) throws Exception {
        try {
            byte[] resumeBytes = Base64.getDecoder().decode(resumeBase64);

            String prompt = "You are an expert ATS (Applicant Tracking System), resume analyst, and career advisor.\n" + //
                                "\n" + //
                                "Your task is to analyze the provided resume content and return a strictly valid, professional, and machine-readable JSON object.\n" + //
                                "\n" + //
                                "IMPORTANT RULES:\n" + //
                                "1. Output ONLY valid JSON.\n" + //
                                "2. Do NOT include markdown, explanations, comments, or extra text.\n" + //
                                "3. Do NOT wrap the response in code fences.\n" + //
                                "4. Use null or empty arrays if information is missing.\n" + //
                                "5. Ensure all fields exist exactly as specified.\n" + //
                                "6. Scores must be realistic and consistent with resume content.\n" + //
                                "7. JSON must be parseable directly by standard JSON parsers (Jackson / Gson).\n" + //
                                ""+ "Analyze the following resume content and extract structured information.\n" + //
                                "\n" + //
                                "Resume Content:\n" + //
                                "{{RESUME_TEXT_OR_BASE64_EXTRACTED_TEXT}}\n" + //
                                "\n" + //
                                "Return the response strictly in the following JSON structure:\n" + //
                                "\n" + //
                                "{\n" + //
                                "  \"basic_info\": {\n" + //
                                "    \"full_name\": \"\",\n" + //
                                "    \"email\": \"\",\n" + //
                                "    \"phone\": \"\",\n" + //
                                "    \"location\": \"\",\n" + //
                                "    \"linkedin\": \"\",\n" + //
                                "    \"github\": \"\",\n" + //
                                "    \"portfolio\": \"\"\n" + //
                                "  },\n" + //
                                "\n" + //
                                "  \"professional_summary\": \"\",\n" + //
                                "\n" + //
                                "  \"skills\": {\n" + //
                                "    \"programming_languages\": [],\n" + //
                                "    \"frameworks\": [],\n" + //
                                "    \"databases\": [],\n" + //
                                "    \"tools\": [],\n" + //
                                "    \"cloud_and_devops\": [],\n" + //
                                "    \"other_skills\": []\n" + //
                                "  },\n" + //
                                "\n" + //
                                "  \"experience\": [\n" + //
                                "    {\n" + //
                                "      \"job_title\": \"\",\n" + //
                                "      \"company\": \"\",\n" + //
                                "      \"location\": \"\",\n" + //
                                "      \"employment_type\": \"\",\n" + //
                                "      \"start_date\": \"\",\n" + //
                                "      \"end_date\": \"\",\n" + //
                                "      \"responsibilities\": [],\n" + //
                                "      \"technologies_used\": []\n" + //
                                "    }\n" + //
                                "  ],\n" + //
                                "\n" + //
                                "  \"projects\": [\n" + //
                                "    {\n" + //
                                "      \"project_name\": \"\",\n" + //
                                "      \"description\": \"\",\n" + //
                                "      \"technologies_used\": [],\n" + //
                                "      \"outcomes\": \"\"\n" + //
                                "    }\n" + //
                                "  ],\n" + //
                                "\n" + //
                                "  \"education\": [\n" + //
                                "    {\n" + //
                                "      \"degree\": \"\",\n" + //
                                "      \"field_of_study\": \"\",\n" + //
                                "      \"institution\": \"\",\n" + //
                                "      \"start_year\": \"\",\n" + //
                                "      \"end_year\": \"\",\n" + //
                                "      \"grade_or_cgpa\": \"\"\n" + //
                                "    }\n" + //
                                "  ],\n" + //
                                "\n" + //
                                "  \"certifications\": [\n" + //
                                "    {\n" + //
                                "      \"name\": \"\",\n" + //
                                "      \"issued_by\": \"\",\n" + //
                                "      \"year\": \"\"\n" + //
                                "    }\n" + //
                                "  ],\n" + //
                                "\n" + //
                                "  \"achievements\": [],\n" + //
                                "\n" + //
                                "  \"ats_analysis\": {\n" + //
                                "    \"overall_ats_score\": {\n" + //
                                "      \"score\": 0,\n" + //
                                "      \"out_of\": 100\n" + //
                                "    },\n" + //
                                "    \"keyword_coverage\": \"LOW | MEDIUM | HIGH\",\n" + //
                                "    \"formatting_quality\": \"POOR | AVERAGE | GOOD | EXCELLENT\",\n" + //
                                "    \"readability_score\": \"LOW | MEDIUM | HIGH\",\n" + //
                                "    \"missing_critical_sections\": []\n" + //
                                "  },\n" + //
                                "\n" + //
                                "  \"profile_evaluation\": {\n" + //
                                "    \"overall_profile_score\": {\n" + //
                                "      \"score\": 0,\n" + //
                                "      \"out_of\": 100\n" + //
                                "    },\n" + //
                                "    \"strengths\": [],\n" + //
                                "    \"weaknesses\": []\n" + //
                                "  },\n" + //
                                "\n" + //
                                "  \"recommendations\": {\n" + //
                                "    \"suggestions_to_enhance\": [\n" + //
                                "      {\n" + //
                                "        \"area\": \"\",\n" + //
                                "        \"recommendation\": \"\"\n" + //
                                "      }\n" + //
                                "    ],\n" + //
                                "    \"suggested_roles\": []\n" + //
                                "  }\n" + //
                                "}\n" + //
                                "\n" + //
                                "Guidelines:\n" + //
                                "- ATS score reflects keyword relevance, formatting, and role alignment.\n" + //
                                "- Profile score reflects experience depth, skill strength, and project impact.\n" + //
                                "- Suggestions must be actionable and specific.\n" + //
                                "- Suggested roles must match the candidate's skills and experience level.\n" + //
                                "";

            Client client = Client.builder()
                    .apiKey(apiKey)
                    .build();

            GenerateContentResponse response = client.models.generateContent(
                    MODEL,
                    Content.builder()
                            .parts(List.of(
                                    Part.fromBytes(resumeBytes, "application/pdf"),
                                    Part.fromText(
                                            prompt)))
                            .build(),
                    null);
            return response.text();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }  

}