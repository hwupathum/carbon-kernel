package org.wso2.carbon.http.client.services;

import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public interface HttpClientService {

    public CloseableHttpClient getClosableHttpClient(String key);

    public void getCloseableHttpClientWithCustomVerifier(String key);

    HttpsURLConnection getHttpsURLConnection(URL url, String httpMethod) throws IOException;
}
