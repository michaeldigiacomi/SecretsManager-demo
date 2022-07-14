package com.example.restservice;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.ListSecretsResponse;
import software.amazon.awssdk.services.secretsmanager.model.SecretListEntry;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException;

import java.util.List;

@RestController
public class SecretsController {

	@GetMapping("/secrets")
	public String secrets() {
		
		Region region = Region.US_EAST_1;

        ProfileCredentialsProvider pcp = ProfileCredentialsProvider.create();
        

        SecretsManagerClient secretsClient = SecretsManagerClient.builder()
                .region(region)
                .credentialsProvider(pcp)
                .build();

		List<SecretListEntry> returnval = listAllSecrets(secretsClient);

        GetSecretValueRequest valueRequest = GetSecretValueRequest.builder()
                .secretId(returnval.get(0).arn())
                .build();

        GetSecretValueResponse valueResponse = secretsClient.getSecretValue(valueRequest);
        String secret = valueResponse.secretString();
        
		secretsClient.close();

		return "Secret with ID: " + returnval.get(0).arn() + " = " + secret;
	}

	public static List<SecretListEntry> listAllSecrets(SecretsManagerClient secretsClient) {

        try {
            ListSecretsResponse secretsResponse = secretsClient.listSecrets();
            List<SecretListEntry> secrets = secretsResponse.secretList();

            return secrets;

        } 
		catch (SecretsManagerException e) {
            return null;
        }
    }
}
