package com.pi.dorossai.mindmap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response object containing the generated mind map in Mermaid format.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response containing the generated mind map in Mermaid format")
public class MindMapResponse {    @Schema(
        description = "The generated mind map in Mermaid graph TD format. This can be rendered using any Mermaid-compatible viewer.",
        example = "graph TD;\\nA[Computer Science] --> B[Programming]\\nA --> C[Data Structures]\\nA --> D[Algorithms]\\nA --> E[Databases]\\nA --> F[Artificial Intelligence]\\nA --> G[Computer Networks]\\nA --> H[Operating Systems]\\nA --> I[Software Engineering]\\nA --> J[Cybersecurity]\\nB --> B1[Languages]\\nB --> B2[Paradigms]\\nB1 --> B1a[Python]\\nB1 --> B1b[Java]\\nB1 --> B1c[C++]\\nB1 --> B1d[JavaScript]\\nB2 --> B2a[Object-Oriented]\\nB2 --> B2b[Functional]\\nB2 --> B2c[Procedural]\\nC --> C1[Arrays]\\nC --> C2[Linked Lists]\\nC --> C3[Trees]\\nC --> C4[Graphs]\\nD --> D1[Sorting Algorithms]\\nD --> D2[Search Algorithms]\\nD --> D3[Dynamic Programming]\\nD --> D4[Greedy Algorithms]"
    )
    private String mindmap;
}
