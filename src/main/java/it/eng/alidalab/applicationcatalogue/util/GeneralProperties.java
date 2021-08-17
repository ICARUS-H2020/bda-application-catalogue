package it.eng.alidalab.applicationcatalogue.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "general")
public class GeneralProperties {

    @Value("${general.redis.url}")
    private String redisUri;

    @Value("${general.redis.port}")
    private int redisPort;

    @Value("${general.catalog-name-default}")
    private String CatalogNameDefault;

    public String getCatalogNameDefault() {
        return CatalogNameDefault;
    }

    public void setCatalogNameDefault(String catalogNameDefault) {
        CatalogNameDefault = catalogNameDefault;
    }

    public String getRedisUri() {
        return redisUri;
    }

    public void setRedisUri(String redisUri) {
        this.redisUri = redisUri;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(int redisPort) {
        this.redisPort = redisPort;
    }
}
