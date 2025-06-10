package com.pi.dorossai.meeting.controller;

import com.pi.dorossai.meeting.dto.MeetingNotesRequest;
import com.pi.dorossai.meeting.dto.MeetingNotesResponse;
import com.pi.dorossai.meeting.service.MeetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Meeting Notes", description = "Meeting transcript processing and analysis services")
public class MeetingController {
    
    private final MeetingService meetingService;
    
    @PostMapping("/process-meeting-notes")
    @Operation(summary = "Process Meeting Notes", description = "Extract structured information from meeting transcripts")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<MeetingNotesResponse> processMeetingNotes(@Valid @RequestBody MeetingNotesRequest request) {
        log.info("Received meeting notes processing request for transcript of length: {}", 
                request.getTranscript().length());
        
        MeetingNotesResponse response = meetingService.processMeetingNotes(request);
        return ResponseEntity.ok(response);
    }
}
