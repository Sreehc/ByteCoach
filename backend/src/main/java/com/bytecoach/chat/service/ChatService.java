package com.bytecoach.chat.service;

import com.bytecoach.chat.dto.ChatSendRequest;
import com.bytecoach.chat.vo.ChatMessageVO;
import com.bytecoach.chat.vo.ChatSendVO;
import com.bytecoach.chat.vo.ChatSessionVO;
import java.util.List;

public interface ChatService {
    ChatSendVO send(Long userId, ChatSendRequest request);

    List<ChatSessionVO> listSessions(Long userId);

    List<ChatMessageVO> listMessages(Long userId, Long sessionId);

    void deleteSession(Long userId, Long sessionId);
}
