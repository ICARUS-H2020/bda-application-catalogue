package it.eng.alidalab.applicationcatalogue.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "keys-configuration-server")
public class ConfigurationServerProperties {
    private String initFramework;
    private String initCatalogues;

    public String getInitCatalogues() {
        return initCatalogues;
    }

    public void setInitCatalogues(String initCatalogues) {
        this.initCatalogues = initCatalogues;
    }

    public String getInitFramework() {
        return initFramework;
    }

    public void setInitFramework(String initFramework) {
        this.initFramework = initFramework;
    }
}
