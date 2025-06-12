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
@Tag(name = "Mind Map", description = "AI-powered mind map generation in Mermaid format for visual concept organization")
public class MindMapController {
    private final MindMapService mindMapService;

    @PostMapping("/generate-mindmap")
    @Operation(
        summary = "Generate Mind Map",
        description = "Generate a hierarchical mind map in Mermaid format for a given topic. The mind map includes main concepts and their relationships, organized in a tree-like structure. Useful for visual learning, concept organization, and knowledge representation."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Mind map generated successfully",
            content = @Content(                mediaType = "application/json",
                schema = @Schema(implementation = MindMapResponse.class),
                examples = {                    @ExampleObject(
                        name = "Computer Science Mind Map",
                        value = """
                        {
                            "mindmap": "graph TD;\\nA[Computer Science] --> B[Programming]\\nA --> C[Data Structures]\\nA --> D[Algorithms]\\nA --> E[Databases]\\nA --> F[Artificial Intelligence]\\nA --> G[Computer Networks]\\nA --> H[Operating Systems]\\nA --> I[Software Engineering]\\nA --> J[Cybersecurity]\\nB --> B1[Languages]\\nB --> B2[Paradigms]\\nB1 --> B1a[Python]\\nB1 --> B1b[Java]\\nB1 --> B1c[C++]\\nB1 --> B1d[JavaScript]\\nB2 --> B2a[Object-Oriented]\\nB2 --> B2b[Functional]\\nB2 --> B2c[Procedural]\\nC --> C1[Arrays]\\nC --> C2[Linked Lists]\\nC --> C3[Trees]\\nC --> C4[Graphs]\\nD --> D1[Sorting Algorithms]\\nD --> D2[Search Algorithms]\\nD --> D3[Dynamic Programming]\\nD --> D4[Greedy Algorithms]\\nE --> E1[SQL]\\nE --> E2[NoSQL]\\nF --> F1[Machine Learning]\\nF --> F2[Deep Learning]\\nF --> F3[Natural Language Processing]\\nF --> F4[Computer Vision]\\nG --> G1[OSI Model]\\nG --> G2[TCP/IP]\\nG --> G3[Network Security]\\nH --> H1[Process Management]\\nH --> H2[Memory Management]\\nH --> H3[File Systems]\\nH --> H4[Concurrency]\\nI --> I1[Agile Development]\\nI --> I2[Version Control]\\nI --> I3[Testing]\\nJ --> J1[Encryption]\\nJ --> J2[Ethical Hacking]\\nJ --> J3[Network Security]"
                        }
                        """
                    ),@ExampleObject(
                        name = "Physics Mind Map",
                        value = """
                        {
                            "mindmap": "graph TD;\\nPhysics[Physics] --> Mechanics[Mechanics]\\nPhysics --> Electromagnetism[Electromagnetism]\\nPhysics --> Thermodynamics[Thermodynamics]\\nPhysics --> QuantumPhysics[Quantum Physics]\\nPhysics --> Relativity[Relativity]\\nMechanics --> NewtonLaws[Newton's Laws]\\nMechanics --> Kinematics[Kinematics]\\nMechanics --> Dynamics[Dynamics]\\nElectromagnetism --> ElectricFields[Electric Fields]\\nElectromagnetism --> MagneticFields[Magnetic Fields]\\nElectromagnetism --> EMWaves[Electromagnetic Waves]\\nThermodynamics --> Laws[Laws of Thermodynamics]\\nThermodynamics --> HeatTransfer[Heat Transfer]\\nThermodynamics --> Entropy[Entropy]\\nQuantumPhysics --> WaveFunctions[Wave Functions]\\nQuantumPhysics --> Uncertainty[Uncertainty Principle]\\nQuantumPhysics --> QuantumEntanglement[Quantum Entanglement]\\nRelativity --> Special[Special Relativity]\\nRelativity --> General[General Relativity]"
                        }
                        """
                    ),                    @ExampleObject(
                        name = "Machine Learning Mind Map",
                        value = """
                        {
                            "mindmap": "graph TD;\\nML[Machine Learning] --> Supervised[Supervised Learning]\\nML --> Unsupervised[Unsupervised Learning]\\nML --> Reinforcement[Reinforcement Learning]\\nML --> DeepLearning[Deep Learning]\\nSupervised --> Classification[Classification]\\nSupervised --> Regression[Regression]\\nClassification --> LogisticRegression[Logistic Regression]\\nClassification --> SVM[Support Vector Machines]\\nClassification --> DecisionTrees[Decision Trees]\\nRegression --> LinearRegression[Linear Regression]\\nRegression --> PolynomialRegression[Polynomial Regression]\\nUnsupervised --> Clustering[Clustering]\\nUnsupervised --> DimensionalityReduction[Dimensionality Reduction]\\nClustering --> KMeans[K-Means]\\nClustering --> HierarchicalClustering[Hierarchical Clustering]\\nDimensionalityReduction --> PCA[Principal Component Analysis]\\nDimensionalityReduction --> tSNE[t-SNE]\\nDeepLearning --> CNN[Convolutional Neural Networks]\\nDeepLearning --> RNN[Recurrent Neural Networks]\\nDeepLearning --> Transformers[Transformers]"
                        }
                        """
                    ),                    @ExampleObject(
                        name = "Project Management Mind Map",
                        value = """
                        {
                            "mindmap": "graph TD;\\nPM[Project Management] --> Planning[Planning]\\nPM --> Execution[Execution]\\nPM --> Monitoring[Monitoring & Control]\\nPM --> Closing[Closing]\\nPlanning --> ScopeDefinition[Scope Definition]\\nPlanning --> ScheduleDevelopment[Schedule Development]\\nPlanning --> BudgetEstimation[Budget Estimation]\\nPlanning --> RiskAssessment[Risk Assessment]\\nExecution --> TeamManagement[Team Management]\\nExecution --> ResourceAllocation[Resource Allocation]\\nExecution --> QualityAssurance[Quality Assurance]\\nExecution --> StakeholderEngagement[Stakeholder Engagement]\\nMonitoring --> ProgressTracking[Progress Tracking]\\nMonitoring --> PerformanceMetrics[Performance Metrics]\\nMonitoring --> ChangeControl[Change Control]\\nClosing --> Deliverables[Final Deliverables]\\nClosing --> Documentation[Documentation]\\nClosing --> LessonsLearned[Lessons Learned]"
                        }
                        """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Invalid Request",
                    value = """
                    {
                        "status": 400,
                        "error": "Bad Request",
                        "message": "Topic is required",
                        "path": "/api/generate-mindmap",
                        "timestamp": "2025-06-12T14:35:22"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Server Error",
                    value = """
                    {
                        "status": 500,
                        "error": "Internal Server Error",
                        "message": "An unexpected error occurred while generating the mind map",
                        "path": "/api/generate-mindmap",
                        "timestamp": "2025-06-12T14:35:22"
                    }
                    """
                )
            )        )
    })
    public ResponseEntity<MindMapResponse> generateMindMap(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "The topic for which to generate a mind map. Provide a clear, specific topic for better results. Complex or broad topics will generate more extensive mind maps.",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MindMapRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Machine Learning Request",
                        value = """
                        {
                            "topic": "Machine Learning and Its Applications"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Biology Request",
                        value = """
                        {
                            "topic": "Human Body Systems and Their Functions"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "History Request",
                        value = """
                        {
                            "topic": "World War II: Causes, Events, and Consequences"
                        }
                        """
                    )
                }
            )
        )
        @Valid @RequestBody MindMapRequest request) {
        log.info("Generating mind map for topic: {}", request.getTopic());
        String mermaid = mindMapService.generateMindMapMermaid(request.getTopic());
        MindMapResponse response = new MindMapResponse(mermaid);
        log.info("Mind map generated successfully");
        return ResponseEntity.ok(response);
    }
}
