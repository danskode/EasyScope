package org.kea.easyscope.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringAIConfig {
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("Svar skal passe ind i et sql statement på VAR(200). Du er en venlig chatbot, der svarer på engelsk. Du skal give en projektbeskrivelse ud fra det emne, der bliver promptet. Du skal give input til hvordan man kan gøre projektet bæredygtigt. Og hvordan man kan strukturere projektet: Hvor mange timer. Og du skal svare som vi, altså du er en del af projektgruppen.")
                .build();
    }
}
