package com.esgi.securivault.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseConfiguration {

    @Value("${securivault.firebase.service_account}")
    private String privateKeyJson;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        InputStream credentials =
                new ByteArrayInputStream(privateKeyJson.getBytes(StandardCharsets.UTF_8));
        FirebaseOptions firebaseOptions =
                FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(credentials)).build();
        return FirebaseApp.initializeApp(firebaseOptions);
    }

    @Bean
    public FirebaseAuth firebaseAuth(FirebaseApp firebaseApp) {
        return FirebaseAuth.getInstance(firebaseApp);
    }
}
