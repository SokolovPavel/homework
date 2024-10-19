package otus.highload.homework.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ReplicationDataSourceConfig {

    private static final int READ_REPLICAS_COUNT = 2;

    @Bean
    @Primary
    @NonNull
    DataSource replicatingDataSource(@NonNull DataSource writeDataSource,
                                     @NonNull DataSource readNode1DataSource,
                                     @NonNull DataSource readNode2DataSource) {
        var replicatingDataSource = new ReplicationRoutingDataSource(READ_REPLICAS_COUNT);
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("write", writeDataSource);
        dataSourceMap.put("read-0", readNode1DataSource);
        dataSourceMap.put("read-1", readNode2DataSource);
        replicatingDataSource.setTargetDataSources(dataSourceMap);
        replicatingDataSource.setDefaultTargetDataSource(writeDataSource);
        return replicatingDataSource;
    }

    @Bean
    @NonNull
    @ConfigurationProperties("spring.datasource.write")
    public DataSourceProperties writeDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @NonNull
    public DataSource writeDataSource(@NonNull DataSourceProperties writeDataSourceProperties) {
        return writeDataSourceProperties
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    @NonNull
    @ConfigurationProperties("spring.datasource.read.first")
    public DataSourceProperties readNode1DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @NonNull
    public DataSource readNode1DataSource(@NonNull DataSourceProperties readNode1DataSourceProperties) {
        return readNode1DataSourceProperties
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    @NonNull
    @ConfigurationProperties("spring.datasource.read.second")
    public DataSourceProperties readNode2DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @NonNull
    public DataSource readNode2DataSource(@NonNull DataSourceProperties readNode2DataSourceProperties) {
        return readNode2DataSourceProperties
                .initializeDataSourceBuilder()
                .build();
    }
}
