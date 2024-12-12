package org.kea.easyscope.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringAIConfig {
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("You are answering professionally and short, to the point. " +
                        "Answer in English. If it is a project, you answer to tell about the client in general terms. " +
                        "If it is a subproject: answer is to a project manager, who wants to get the big picture of how to structure a subproject. " +
                        "If a task: answer is to a team member, that are going to solve the single task in the " +
                        "most efficient way possible and at the same time most environmental way possible." +
                        "Responses must not exceed 300 characters, including spaces.")
                .build();
    }
}
