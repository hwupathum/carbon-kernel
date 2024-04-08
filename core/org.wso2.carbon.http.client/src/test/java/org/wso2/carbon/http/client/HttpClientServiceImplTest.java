package org.wso2.carbon.http.client;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpClientServiceImplTest {


    @Test(groups = "wso2.carbon.http.client", description = "Test method")
    public void testGetCloseableHttpClientWithConnectionPool() {

        // Setting up the connection manager
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();


        // Getting the CloseableHttpClient using the same connection manager
        CloseableHttpClient httpClient1 = HttpClientBuilder.create().useSystemProperties()
                .setConnectionManager(connectionManager)
//                .setConnectionManagerShared(true)
                .build();

       CloseableHttpClient httpClient2 = HttpClientBuilder.create().useSystemProperties()
                .setConnectionManager(connectionManager)
//                .setConnectionManagerShared(true)
                .build();

        // Closing the CloseableHttpClient
        try {
            httpClient1.close();
            httpClient2.close();
        } catch (Exception e) {
            System.out.println("Error occurred while closing the CloseableHttpClient");
        }

    }

}
