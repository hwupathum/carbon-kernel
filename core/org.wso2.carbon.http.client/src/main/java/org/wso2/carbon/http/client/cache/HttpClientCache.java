/*
 * Copyright (c) 2024, WSO2 LLC. (https://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.carbon.http.client.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

public class HttpClientCache extends ClientBaseCache<String, CloseableHttpClient> {

    private static final Log log = LogFactory.getLog(HttpClientCache.class);
    private static HttpClientCache instance;


    private HttpClientCache(int cacheSize, int expireAfterAccess) {

        super(cacheSize, expireAfterAccess, notification -> {
            // This is not called at expire
            // https://stackoverflow.com/questions/21986551/guava-cachebuilder-doesnt-call-removal-listener
            try {
                if (notification.getValue() != null) {
                    notification.getValue().close();
                    log.info("Http Client - " + notification.getKey() + " removed due to " +
                            notification.getCause());
                }
            } catch (IOException e) {
                log.error("Error occurred while closing the http client for key: " + notification.getKey(), e);
            }
        });
    }

    public static HttpClientCache getInstance() {

        if (instance == null) {
            int ttlMinutes = 1;
            int cacheTimeoutMinutes = 5;
            instance = new HttpClientCache(100, (ttlMinutes + cacheTimeoutMinutes) * 60 * 1000);
        }
        return instance;
    }
}
