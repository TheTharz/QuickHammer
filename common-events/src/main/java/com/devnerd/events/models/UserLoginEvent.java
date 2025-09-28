package com.devnerd.events.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginEvent {
    private Long userId;    // wrapper Long
    private String sessionId;
    private long timeStamp;
}
