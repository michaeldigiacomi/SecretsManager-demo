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

        SecretsManagerClient secretsClient = SecretsManagerClient.builder()
                .region(region)
                .build();

        ListSecretsResponse secretsResponse = secretsClient.listSecrets();
        
		List<SecretListEntry> returnval = secretsResponse.secretList();

        System.out.println(returnval);

        GetSecretValueRequest valueRequest = GetSecretValueRequest.builder()
                .secretId(returnval.get(0).arn())
                .build();
        

        GetSecretValueResponse valueResponse = secretsClient.getSecretValue(valueRequest);
        String secret = valueResponse.secretString();
        
		secretsClient.close();

		return "Secret with ID: " + returnval.get(0).arn() + " = " + secret;
	}
}
