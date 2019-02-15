package com.distributed;

import com.distributedlock.Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @Auther: wdq
 * @Date: 19-2-14 11:48
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DistributeLockTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void distrubtedLock() {
        String url = "/distributedLock";
        String uuid = "abcdefg";
//        log.info("uuid:{}", uuid);
        String key = "redisLock";
        String secondsToLive = "10";

        for (int i = 0; i < 100; i++) {
            final int userId = i;
            new Thread(() -> {
                MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                params.add("uuid", uuid);
                params.add("key", key);
                params.add("secondsToLock", secondsToLive);
                params.add("userId", String.valueOf(userId));
                String result = testRestTemplate.postForObject(url, params, String.class);
                System.out.println("-------------" + result);
            }
            ).start();
        }

    }
}
