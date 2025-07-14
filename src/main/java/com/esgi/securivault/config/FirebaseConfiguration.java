package com.esgi.securivault.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import java.io.IOException;
import java.io.InputStream;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;


@Configuration
public class FirebaseConfiguration {

    @Value("${securivault.firebase.service_account}")
    private String firebaseCredentialsPath;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        try {
            ClassPathResource resource = new ClassPathResource(firebaseCredentialsPath);
            InputStream serviceAccount = resource.getInputStream();
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    //.setProjectId("projet-annuel-b3477")
                .build();
            return FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            throw new IOException("Failed to load Firebase credentials file: " + firebaseCredentialsPath, e);
        }
    }

    @Bean
    public FirebaseAuth firebaseAuth(FirebaseApp firebaseApp) {
        return FirebaseAuth.getInstance(firebaseApp);
    }

    @Bean
    public Firestore firestore(FirebaseApp firebaseApp) {
        return FirestoreClient.getFirestore(firebaseApp);
    }
    
}