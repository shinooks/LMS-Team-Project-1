package com.sesac.backend.evaluation.copyleaks.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

@Service
public class CopyleaksScanService {

    private static final String SUBMIT_ENDPOINT = "https://api.copyleaks.com/v3/scans/submit/file";
    private static final String WEBHOOK_URL = "https://yourdomain.com/api/webhook/copyleaks";

    public void submitFileForScan(String accessToken, UUID scanId, byte[] fileData, String fileName) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(SUBMIT_ENDPOINT + "/" + scanId);

        // 파일 데이터를 Base64로 인코딩
        String base64Content = Base64.getEncoder().encodeToString(fileData);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("base64", base64Content);
        requestBody.put("filename", fileName);

        // 옵션 설정 (웹훅 URL 지정)
        Map<String, Object> options = new HashMap<>();
        Map<String, String> httpCallback = new HashMap<>();
        httpCallback.put("url", WEBHOOK_URL);
        options.put("httpCallback", httpCallback);
        requestBody.put("properties", options);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestBody);

        StringEntity entity = new StringEntity(json);
        httpPut.setEntity(entity);
        httpPut.setHeader("Content-Type", "application/json");
        httpPut.setHeader("Authorization", "Bearer " + accessToken);

        CloseableHttpResponse response = client.execute(httpPut);
        int statusCode = response.getStatusLine().getStatusCode();
        client.close();

        if (statusCode != 201) {
            throw new IOException("Failed to submit file for scan. HTTP Status Code: " + statusCode);
        }
    }
}
