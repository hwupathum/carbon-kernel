package org.wso2.carbon.http.client;

import com.google.common.cache.RemovalCause;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.wso2.carbon.http.client.cache.TestClientCache;

public class HttpClientServiceImplTest {

    private static final int ACCESS_TIMEOUT = 1000;

    @Test(description = " Test case for cache eviction")
    public void cacheEvictionTest() throws InterruptedException {
        TestClientCache cache = new TestClientCache(5, ACCESS_TIMEOUT, notification -> {
            if (notification.getCause() == RemovalCause.EXPIRED) {
                System.out.println("Value " + notification.getValue() + " has been expired");
            } else {
                System.out.println(notification.getKey() + " has been removed due to " + notification.getCause());
            }
        });

        cache.put("key", 100);
        Thread.sleep(ACCESS_TIMEOUT / 2);
        Assert.assertEquals(cache.get("key").intValue(), 100); //returns 100
        Thread.sleep(ACCESS_TIMEOUT * 5);
//        cache.put("key2", 200); //invoke removal listener
        Assert.assertNull(cache.get("key")); //return null

    }


}
