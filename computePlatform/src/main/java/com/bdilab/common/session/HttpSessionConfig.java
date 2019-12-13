package com.bdilab.common.session;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @ClassName HttpSessionConfig
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/11/20 16:01
 * @Version 1.0
 */
@Configuration
//maxInactiveIntervalInSeconds：sessionId默认过期时间为1800秒，即30分钟
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60)
public class HttpSessionConfig {
}
