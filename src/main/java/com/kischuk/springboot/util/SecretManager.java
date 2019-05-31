package com.kischuk.springboot.util;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.services.secretsmanager.*;
import com.amazonaws.services.secretsmanager.model.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class SecretManager {

    protected AWSSecretsManager client; 

    public SecretManager() {
        String endpoint = "secretsmanager.us-east-1.amazonaws.com";
        String region = "us-east-1";

        BasicAWSCredentials credentials = new BasicAWSCredentials("YOUR API KEY", "YOUR API SECRET");
        
        AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
        AWSSecretsManagerClientBuilder clientBuilder = AWSSecretsManagerClientBuilder.standard();
        clientBuilder.setEndpointConfiguration(config);
        clientBuilder.withCredentials(new AWSStaticCredentialsProvider(credentials));
        client = clientBuilder.build();
    }

    public String getSecretValue(String secretName) {

        String secret;
        ByteBuffer binarySecretData;
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(secretName);
        GetSecretValueResult getSecretValueResult = null;
        try {
            getSecretValueResult = client.getSecretValue(getSecretValueRequest);
        } catch(ResourceNotFoundException e) {
            System.out.println("The requested secret " + secretName + " was not found");
        } catch (InvalidRequestException e) {
            System.out.println("The request was invalid due to: " + e.getMessage());
        } catch (InvalidParameterException e) {
            System.out.println("The request had invalid params: " + e.getMessage());
        }

        if(getSecretValueResult == null) {
            return null;
        }

        // Depending on whether the secret was a string or binary, one of these fields will be populated
        if(getSecretValueResult.getSecretString() != null) {
            return getSecretValueResult.getSecretString();
        } else {
            return getSecretValueResult.getSecretBinary().toString();
        }
    }
    
    public HashMap<String, String> getSecretValueMap(String secretName) throws IOException {
        return propsFromString( getSecretValue(secretName) );
    }


    private HashMap<String, String> propsFromString(String value) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(value, HashMap.class);
    }
}
