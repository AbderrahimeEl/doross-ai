package com.pi.dorossai.mindmap;

import com.pi.dorossai.mindmap.dto.MindMapRequest;
import com.pi.dorossai.mindmap.dto.MindMapResponse;
import com.pi.dorossai.mindmap.service.MindMapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Mind Map", description = "AI-powered mind map generation in Mermaid format")
public class MindMapController {
    private final MindMapService mindMapService;

    @PostMapping("/generate-mindmap")
    @Operation(
        summary = "Generate Mind Map",
        description = "Generate a mind map in Mermaid format for a given topic.",
        security = @SecurityRequirement(name = "BearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Mind map generated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MindMapResponse.class),
                examples = @ExampleObject(
                    name = "Mind Map Example",
                    value = """
                    {
                        \"mindmap\": "graph TD; A[Main Topic] --> B[Subtopic 1]; A --> C[Subtopic 2];"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    public ResponseEntity<MindMapResponse> generateMindMap(@Valid @RequestBody MindMapRequest request) {
        String mermaid = mindMapService.generateMindMapMermaid(request.getTopic());
        MindMapResponse response = new MindMapResponse(mermaid);
        return ResponseEntity.ok(response);
    }
}
