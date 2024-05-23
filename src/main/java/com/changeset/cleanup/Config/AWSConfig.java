//package com.changeset.cleanup.Config;
//
//
//import com.amazonaws.services.secretsmanager.AWSSecretsManager;
//import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
//import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
//import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class AWSConfig {
//
////    @Value("${aws.secretName}")
////    private String secretName;
//
//
//    private String secretName= "arn:aws:secretsmanager:us-east-1:012177264511:secret:sm/appe/use1/ubx2/uat-02TU4W";
//
//    @Bean
//    public DataSource dataSource() {
//        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard().build();
//        GetSecretValueRequest request = new GetSecretValueRequest().withSecretId(secretName);
//        GetSecretValueResult result = client.getSecretValue(request);
//
//        String secretString = result.getSecretString();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode;
//        try {
//            jsonNode = objectMapper.readTree(secretString);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to parse secret string.", e);
//        }
//
//        String url = jsonNode.get("url").asText();
//        String username = jsonNode.get("username").asText();
//        String password = jsonNode.get("password").asText();
//
//        DataSourceConfig dataSource = new DataSourceConfig();
////        dataSource.setDriverClassName("org.postgresql.Driver"); // Set your database driver here
////        dataSource.setUrl(url);
////        dataSource.setUsername(username);
////        dataSource.setPassword(password);
//        return dataSource.dataSource();
//
//    }
//}
