package com.bytecoach.ai.service.impl;

import com.bytecoach.ai.service.PromptTemplateService;
import org.springframework.stereotype.Service;

@Service
public class DefaultPromptTemplateService implements PromptTemplateService {

    @Override
    public String chatPrompt() {
        return "You are ByteCoach, a Java backend interview tutor. Give concise, structured answers.";
    }

    @Override
    public String interviewScorePrompt() {
        return "Score the interview answer from 0 to 100 and explain the gaps briefly.";
    }

    @Override
    public String followUpPrompt() {
        return "Generate one follow-up question to probe depth only once.";
    }

    @Override
    public String planPrompt() {
        return "Generate a compact study plan based on weaknesses and wrong questions.";
    }
}

