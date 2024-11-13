package com.sesac.backend.evaluation.copyleaks.service;

import java.io.IOException;
import java.util.Map;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class CopyleaksAuthService {

    private static final String AUTH_ENDPOINT = "https://id.copyleaks.com/v3/account/login/api";
    private static final String EMAIL = "y2kf9a0q@gmail.com";
    private static final String API_KEY = "66380d2d-b563-4205-8fb6-d00d3be394fe";

    public String getAccessToken() {

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(AUTH_ENDPOINT);

            String json = String.format("{\"email\":\"%s\",\"key\":\"%s\"}", EMAIL, API_KEY);
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-Type", "application/json");

            CloseableHttpResponse response = client.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            client.close();

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> responseMap = mapper.readValue(responseBody, Map.class);
            return (String) responseMap.get("access_token");
        } catch (IOException e) {
            return "IOException";
        }

    }
}
