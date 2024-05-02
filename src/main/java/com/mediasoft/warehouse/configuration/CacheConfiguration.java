package com.mediasoft.warehouse.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Конфигурационный класс для настройки кэширования.
 */
@EnableCaching
@Configuration
public class CacheConfiguration implements CachingConfigurer {

    /**
     * Настройка кэширования с использованием библиотеки Caffeine.
     *
     * @return Объект {@link Caffeine}, содержащий настройки кэширования.
     */
    @Bean
    public Caffeine caffeineConfig() {
        return Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES);
    }

    /**
     * Создает менеджер кэша на основе настроек Caffeine.
     *
     * @param caffeine Объект, содержащий настройки кэширования.
     * @return Менеджер кэша для использования в приложении.
     */
    @Bean
    public CacheManager cacheManager(Caffeine caffeine) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }
}
