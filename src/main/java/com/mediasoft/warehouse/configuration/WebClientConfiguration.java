package com.mediasoft.warehouse.configuration;

import com.mediasoft.warehouse.configuration.properties.RestConfigurationProperties;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

/**
 * Конфигурационный класс для настройки {@link WebClient}.
 */
@Configuration
@RequiredArgsConstructor
public class WebClientConfiguration {
    public static final int TIMEOUT = 5000;
    private final RestConfigurationProperties properties;

    /**
     * Создает WebClient с таймаутом подключения и чтения для currencyService.
     *
     * @return Объект WebClient с настроенными параметрами таймаута.
     */
    @Bean
    public WebClient currencyServiceWebClient() {
        final var tcpClient = TcpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
                });

        return WebClient.builder()
                .baseUrl(properties.currencyServiceProperties().getHost())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .build();
    }

    /**
     * Создает WebClient с таймаутом подключения и чтения для crmService.
     *
     * @return Объект WebClient с настроенными параметрами таймаута.
     */
    @Bean
    public WebClient crmServiceWebClient() {
        final var tcpClient = TcpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
                });

        return WebClient.builder()
                .baseUrl(properties.crmServiceProperties().getHost())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .build();
    }

    /**
     * Создает WebClient с таймаутом подключения и чтения для accountService.
     *
     * @return Объект WebClient с настроенными параметрами таймаута.
     */
    @Bean
    public WebClient accountServiceWebClient() {
        final var tcpClient = TcpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
                });

        return WebClient.builder()
                .baseUrl(properties.accountServiceProperties().getHost())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .build();
    }
}
