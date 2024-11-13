package com.sesac.backend.evaluation.copyleaks.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CopyleaksResultService {

    public void downloadResult(String accessToken, UUID scanId, String resultId) throws IOException {
        String resultEndpoint = String.format("https://api.copyleaks.com/v3/downloads/%s/result.json", scanId);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(resultEndpoint);
        httpGet.setHeader("Authorization", "Bearer " + accessToken);

        CloseableHttpResponse response = client.execute(httpGet);
        String responseBody = EntityUtils.toString(response.getEntity());
        client.close();

        // 결과 파싱 및 처리
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> resultData = mapper.readValue(responseBody, Map.class);

        log.info("Scan Result: {}", resultData);
    }
}
