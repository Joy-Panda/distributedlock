package com.distributedlock.sc;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

/**
 * 限流业务
 *
 * @author wdq
 */
@Component
public class RateLimiterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RateLimiterService.class);


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private DefaultRedisScript<Long> redisScript;

    @PostConstruct
    public void init() {
        redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        //Lua脚本放置在classpath下，通过ClassPathResource进行加载
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("/sc/rateLimiter.lua")));
        LOGGER.info("RateLimiterHandler[分布式限流处理器]脚本加载完成");
    }


    /**
     * 限制检查
     *
     * @param limitKey  限流模块key
     * @param limitTime 限流时间
     * @param limit     限流单位数量
     * @return
     */
    public void checkLimiter(String limitKey, Long limitTime, Long limit) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RateLimiterHandler[分布式限流处理器]开始执行限流操作");
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RateLimiterHandler[分布式限流处理器]参数值为-limitTimes={},limitTimeout={}", limit, limitTime);
        }

        //执行Lua脚本
        List<String> keyList = Lists.newArrayList();
        // 设置key值为注解中的值
        keyList.add(limitKey);
        //调用脚本并执行
        Long result = redisTemplate.execute(redisScript, keyList, limitTime, limit);
        LOGGER.info("当前请求次数={}", result);

        if (Objects.nonNull(result) && result == 0L) {
            LOGGER.info("由于超过单位时间={}-允许的请求次数={} [触发限流]", limitTime, limit);
            throw new RuntimeException("超限");
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RateLimiterHandler[分布式限流处理器]限流执行结果-result={},请求[正常]响应", result);
        }
    }
}
