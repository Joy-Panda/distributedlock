package com.distributedlock.web;

import com.distributedlock.limit.DistriLimitAnno;
import com.distributedlock.lock.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Auther: wdq
 * @Date: 19-2-14 11:46
 **/
@Slf4j
@RestController
//@Scope("prototype")
public class TestController {

    @Autowired
    private DistributedLock lock;

    @PostMapping("/distributedLock")
    public String distributedLock(String key, String uuid, String secondsToLock, String userId) throws Exception{
//        String uuid = UUID.randomUUID().toString();
        Boolean locked = false;
        try {
            locked = lock.distributedLock(key, uuid, secondsToLock);
            if(locked) {
                log.info("userId:{} is locked - uuid:{}", userId, uuid);
                log.info("do business logic");
                TimeUnit.MICROSECONDS.sleep(3000);
            } else {
                log.info("userId:{} is not locked - uuid:{}", userId, uuid);
            }
        } catch (Exception e) {
            log.error("error", e);
        } finally {
            if(locked) {
                lock.distributedUnlock(key, uuid);
            }
        }

        return "ok";
    }

    @PostMapping("/distributedLimit")
    @ResponseBody
    @DistriLimitAnno(limitKey="limit", limit = 10)
    public String distributedLimit(String userId) {
        log.info(userId);
        return "ok";
    }

}
