package com.pi.dorossai.meeting.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingNotesRequest {
    @NotBlank
    private String transcript;
    
    private boolean includeActions = true;
    private boolean includeDecisions = true;
}
