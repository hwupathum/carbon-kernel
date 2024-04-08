package org.wso2.carbon.http.client.services;

import org.apache.http.impl.client.CloseableHttpClient;
import org.wso2.carbon.http.client.ClientUtils;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class HttpClientServiceImpl implements HttpClientService {

    private final Map<String, CloseableHttpClient> closeableHttpClientMap = new HashMap<>();

    private void setClosableHttpClient(String key, CloseableHttpClient closeableHttpClient) {
        closeableHttpClientMap.put(key, closeableHttpClient);
    }

    @Override
    public CloseableHttpClient getClosableHttpClient(String key) {
        return closeableHttpClientMap.get(key);
    }

    @Override
    public void getCloseableHttpClientWithCustomVerifier(String key) {
        if (closeableHttpClientMap.containsKey(key)) {
            return;
        }
        setClosableHttpClient(key, ClientUtils.createClientWithCustomVerifier());
    }

    @Override
    public HttpsURLConnection getHttpsURLConnection(URL url, String httpMethod) throws IOException {

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod(httpMethod);
        return connection;
    }
}
