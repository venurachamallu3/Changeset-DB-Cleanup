//package com.changeset.cleanup.Config;
//
//
//import lombok.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import javax.sql.DataSource;
//
//@Configuration
//public class DataSourceConfig {
//
////    @Value("${database.url}")
//    private String url ="jdbc:postgresql://rds-use1-ap-pe-discovery-service-db-u-v1.cpqbxulq89lf.us-east-1.rds.amazonaws.com:5432/jobs";
//
////    @Value("${database.username}")
//    private String username="discoveryuat";
//
////    @Value("${spring.datasource.password}")
//    private String password="10N1o7#3bWgO";
//
//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.postgresql.Driver"); // Set your database driver here
//        dataSource.setUrl(url);
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//        return dataSource;
//    }
//}
//
