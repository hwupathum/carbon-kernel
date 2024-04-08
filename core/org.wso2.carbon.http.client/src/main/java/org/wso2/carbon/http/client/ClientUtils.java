package org.wso2.carbon.http.client;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.wso2.carbon.utils.HTTPClientUtils;

public class ClientUtils {

    private ClientUtils() {
    }

    public static CloseableHttpClient createClientWithCustomVerifier() {

        HttpClientBuilder httpClientBuilder = HTTPClientUtils.createClientWithCustomVerifier();

        return httpClientBuilder.build();
    }
}
