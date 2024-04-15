package org.wso2.carbon.http.client.cache;

import com.google.common.cache.RemovalListener;

public class TestClientCache extends ClientBaseCache<String, Integer> {

    public TestClientCache(int cacheSize, int expireAfterAccess,
                              RemovalListener<String, Integer> removalListener) {

        super(cacheSize, expireAfterAccess, removalListener);
    }

}
