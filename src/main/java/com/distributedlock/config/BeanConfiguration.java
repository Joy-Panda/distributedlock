package com.distributedlock.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;


@Configuration
@Slf4j
public class BeanConfiguration {

    /**
     * The script resultType should be one of
     * Long, Boolean, List, or a deserialized value type. It can also be null if the script returns
     * a throw-away status (specifically, OK).
     * @return
     */
    @Bean
    @Qualifier("limitScript")
    public RedisScript<Long> limitScript() {
        RedisScript<Long> redisScript = null;
        try {
            ScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource("/scripts/limit.lua"));
//            log.info("script:{}", scriptSource.getScriptAsString());
            redisScript = RedisScript.of(scriptSource.getScriptAsString(), Long.class);
        } catch (Exception e) {
            log.error("error", e);
        }
        return redisScript;

    }

    @Bean
    @Qualifier("lockScript")
    public RedisScript<Boolean> lockScript() {
        RedisScript<Boolean> redisScript = null;
        try {
            ScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource("/scripts/lock.lua"));
            redisScript = RedisScript.of(scriptSource.getScriptAsString(), Boolean.class);
        } catch (Exception e) {
            log.error("error" , e);
        }
        return redisScript;
    }

    @Bean
    @Qualifier("unlockScript")
    public RedisScript<Long> unlockScript() {
        RedisScript<Long> redisScript = null;
        try {
            ScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource("/scripts/unlock.lua"));
            redisScript = RedisScript.of(scriptSource.getScriptAsString(), Long.class);
        } catch (Exception e) {
            log.error("error" , e);
        }
        return redisScript;
    }

//    @Bean
//    @Qualifier("limitAnother")
//    public RedisScript<Long> limitAnother() {
//        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
//        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("/scripts/limit.lua")));
//        redisScript.setResultType(Long.class);
//        return redisScript;
//    }

    @Bean
    public RedisScript<Long> rateLimitScript() {
        RedisScript<Long> redisScript = null;
        try {
            ScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource("/scripts/ratelimit.lua"));
            redisScript = RedisScript.of(scriptSource.getScriptAsString(), Long.class);
        } catch (Exception e) {
            log.error("error" , e);
        }
        return redisScript;
    }
}