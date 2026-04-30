package com.bytecoach.chat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bytecoach.common.entity.BaseEntity;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("chat_session")
@EqualsAndHashCode(callSuper = true)
public class ChatSession extends BaseEntity {
    private Long userId;
    private String title;
    private String mode;
    private LocalDateTime lastMessageTime;
}
