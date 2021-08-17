package it.eng.alidalab.applicationcatalogue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.eng.alidalab.applicationcatalogue.domain.service.Catalogue;
import it.eng.alidalab.applicationcatalogue.domain.service.Framework;
import it.eng.alidalab.applicationcatalogue.services.*;
import it.eng.alidalab.applicationcatalogue.util.ConfigurationServerProperties;
import it.eng.alidalab.applicationcatalogue.util.GeneralProperties;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.exceptions.JedisException;

import javax.persistence.EntityNotFoundException;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@ComponentScan("it.eng.alidalab.applicationcatalogue.services")
@org.springframework.context.annotation.Configuration
public class Configuration {

    @Autowired
    public GeneralProperties generalProperties;

    @Autowired
    public ConfigurationServerProperties configurationServerProperties;

    @Autowired
    ObjectMapper mapper;

    @Bean
    public ServiceService serviceService()
    {
        return new ServiceService();
    }
    @Bean
    public VizConfigurationServices vizConfigurationServices()
    {
        return new VizConfigurationServices();
    }
    @Bean
    public CatalogueService catalogueService()
    {
        return new CatalogueService();
    }
    @Bean
    public MetricService metricService()
    {
        return new MetricService();
    }
    @Bean
    public FrameworkService frameworkService()
    {
        return new FrameworkService();
    }
    @Bean
    public WorkflowService workflowService()
    {
        return new WorkflowService();
    }
    @Bean
    public DatasetService datasetService()
    {
        return new DatasetService();
    }
    @Bean
    public OwnerService ownerService()
    {
        return new OwnerService();
    }
    @Bean
    public StatusService statusService()
    {
        return new StatusService();
    }
    @Bean
    public BDAApplicationService bdaApplicationServices(){
        return new BDAApplicationService();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    private JedisPool jedisPool;

    @Bean
    public JedisPool getJedisPool() {
        try {
            jedisPool = new JedisPool(new JedisPoolConfig(), generalProperties.getRedisUri(),
                    generalProperties.getRedisPort(), Protocol.DEFAULT_TIMEOUT, null);
            return jedisPool;
        } catch (JedisException e) {
            throw new JedisException("Failed to connect to Redis with JedisPool; error: " + e.getMessage());
        }
    }

    @Bean
    InitializingBean initDatabase() {
        Jedis jedis = null;

        try {
            List<Catalogue> cRedis = null;
            List<Framework> fRedis = null;
            try{
            jedis = jedisPool.getResource();
            if(jedis!=null){
                if(jedis.get(configurationServerProperties.getInitCatalogues()) != null)
                    cRedis = mapper.readValue(jedis.get(configurationServerProperties.getInitCatalogues()).replaceAll("\\\\", ""), new TypeReference<List<Catalogue>>(){});
                if(jedis.get(configurationServerProperties.getInitFramework()) != null)
                    fRedis = mapper.readValue(jedis.get(configurationServerProperties.getInitFramework()).replaceAll("\\\\", ""), new TypeReference<List<Framework>>(){});
                }
            }catch (Exception ex){

                ex.printStackTrace();
            }

            if(cRedis==null) {
                try{
                    catalogueService().getCatalogue(generalProperties.getCatalogNameDefault());
                }catch (EntityNotFoundException ex){
                    cRedis = new ArrayList<>();
                    cRedis.add(new Catalogue(generalProperties.getCatalogNameDefault()));
                    System.out.println("Add " + generalProperties.getCatalogNameDefault()+ " Catalogue from property." );
                }

            }

            if(cRedis!=null && cRedis.size()>0){
                //Store Catalog
                catalogueService().saveCatalogue(cRedis);
                System.out.println("Catalogue Added from Redis: "); cRedis.forEach(c -> {System.out.println(c.getName());});
            }
            if(fRedis!=null && fRedis.size()>0) {
                //Store Frameworks
                frameworkService().createFrameworks(fRedis);
                System.out.println("Framework Added from Redis: "); fRedis.forEach(f -> {System.out.println(f.getName());});

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return () -> {
            //System.out.println("Added Frameworks: NONE");
        };
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource sqlDataSource(DataSourceProperties dataSourceProperties)
    {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        driverManagerDataSource.setUrl(dataSourceProperties.getUrl());
        driverManagerDataSource.setUsername(dataSourceProperties.getUsername());
        driverManagerDataSource.setPassword(dataSourceProperties.getPassword());
        return driverManagerDataSource;
    }
}
