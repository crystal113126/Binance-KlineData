package com.binanceproject.binance.configuration;

import com.binanceproject.binance.model.KlineDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Collections;

@Configuration
@EnableCaching
@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP,keyspaceConfiguration = RedisConfig.MyKeyspaceConfiguration.class)
public class RedisConfig {

        private final String HOSTNAME;
        private final int PORT;
        private final int DATABASE;
        private final long TIMEOUT;

        public RedisConfig(
                @Value("${redis.hostname}") String hostname,
                @Value("${redis.port}") int port,
                @Value("${redis.database}") int database,
                @Value("${redis.timeout}") long timeout
        ) {

            this.HOSTNAME = hostname;
            this.PORT = port;
            this.DATABASE = database;
            this.TIMEOUT = timeout;
        }

        @Bean(name = "redisConnectionFactory")
        public RedisConnectionFactory redisConnectionFactory() {
            RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
            config.setHostName(HOSTNAME);
            config.setPort(PORT);
            config.setDatabase(DATABASE);

            LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                    .commandTimeout(Duration.ofMillis(TIMEOUT))
                    .build();

            return new LettuceConnectionFactory(config, clientConfig);
        }

        @Bean(name = "stringRedisTemplate")
        public StringRedisTemplate stringRedisTemplate(
                @Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory
        ) {

            StringRedisTemplate template = new StringRedisTemplate();
            template.setConnectionFactory(redisConnectionFactory);

            return template;
        }

        @Bean(name="redisTemplate")
        public RedisTemplate<String, Object> redisTemplate() {
            RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(redisConnectionFactory());
            template.setKeySerializer(new StringRedisSerializer());
            template.setHashKeySerializer(new StringRedisSerializer());
            template.setHashKeySerializer(new JdkSerializationRedisSerializer());
            template.setValueSerializer(new JdkSerializationRedisSerializer());
            template.setEnableTransactionSupport(true);
            template.afterPropertiesSet();
            return template;

         }

    public class MyKeyspaceConfiguration extends KeyspaceConfiguration {
        @Override
        protected Iterable<KeyspaceSettings> initialConfiguration() {
            KeyspaceSettings keyspaceSettings = new KeyspaceSettings(KlineDTO.class, "Kline");
            keyspaceSettings.setTimeToLive(60L);
            return Collections.singleton(keyspaceSettings);
        }
    }
}
