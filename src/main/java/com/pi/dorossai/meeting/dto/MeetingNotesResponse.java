package com.pi.dorossai.meeting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingNotesResponse {
    private String summary;
    private List<String> actionItems;
    private List<String> keyDecisions;
    private List<String> followUpQuestions;
}
