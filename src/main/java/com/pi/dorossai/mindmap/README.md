# Mind Map Generation API

The Mind Map API provides endpoints for generating hierarchical mind maps in Mermaid format from any topic. These mind maps can be used for visual learning, concept organization, note-taking, and knowledge representation.

## What is a Mind Map?

A mind map is a diagram that visually organizes information, showing relationships among pieces of the whole. It is often created around a single concept, drawn as an image in the center of a blank page, to which associated representations of ideas such as images, words, and parts of words are added.

## Mermaid Format

The API returns mind maps in [Mermaid](https://mermaid-js.github.io/mermaid/#/) format, which is a simple markdown-like script language for generating charts from text. Mermaid is widely supported in various documentation tools and can be rendered directly in GitHub markdown, documentation platforms, and dedicated visualization tools.

## Endpoint

### `POST /api/generate-mindmap`

Generates a mind map in Mermaid format for a specified topic.

#### Request Format

```json
{
  "topic": "String (3-200 characters)"
}
```

#### Response Format

```json
{
  "mindmap": "String (Mermaid format graph)"
}
```

## Example Usage

### Example 1: Computer Science

#### Request

```json
{
  "topic": "Computer Science"
}
```

#### Response

```json
{
  "mindmap": "graph TD;\\nA[Computer Science] --> B[Programming]\\nA --> C[Data Structures]\\nA --> D[Algorithms]\\nA --> E[Databases]\\nA --> F[Artificial Intelligence]\\nA --> G[Computer Networks]\\nA --> H[Operating Systems]\\nA --> I[Software Engineering]\\nA --> J[Cybersecurity]\\nB --> B1[Languages]\\nB --> B2[Paradigms]\\nB1 --> B1a[Python]\\nB1 --> B1b[Java]\\nB1 --> B1c[C++]\\nB1 --> B1d[JavaScript]\\nB2 --> B2a[Object-Oriented]\\nB2 --> B2b[Functional]\\nB2 --> B2c[Procedural]\\nC --> C1[Arrays]\\nC --> C2[Linked Lists]\\nC --> C3[Trees]\\nC --> C4[Graphs]\\nD --> D1[Sorting Algorithms]\\nD --> D2[Search Algorithms]\\nD --> D3[Dynamic Programming]\\nD --> D4[Greedy Algorithms]\\nE --> E1[SQL]\\nE --> E2[NoSQL]\\nF --> F1[Machine Learning]\\nF --> F2[Deep Learning]\\nF --> F3[Natural Language Processing]\\nF --> F4[Computer Vision]\\nG --> G1[OSI Model]\\nG --> G2[TCP/IP]\\nG --> G3[Network Security]\\nH --> H1[Process Management]\\nH --> H2[Memory Management]\\nH --> H3[File Systems]\\nH --> H4[Concurrency]\\nI --> I1[Agile Development]\\nI --> I2[Version Control]\\nI --> I3[Testing]\\nJ --> J1[Encryption]\\nJ --> J2[Ethical Hacking]\\nJ --> J3[Network Security]"
}
```

### Example 2: Renewable Energy

#### Request

```json
{
  "topic": "Renewable Energy Sources"
}
```

#### Response

```json
{
  "mindmap": "graph TD;\\nRenewableEnergy[Renewable Energy] --> Solar[Solar Energy]\\nRenewableEnergy --> Wind[Wind Energy]\\nRenewableEnergy --> Hydro[Hydroelectric Energy]\\nRenewableEnergy --> Geothermal[Geothermal Energy]\\nRenewableEnergy --> Biomass[Biomass Energy]\\nSolar --> PhotovoltaicCells[Photovoltaic Cells]\\nSolar --> SolarThermal[Solar Thermal]\\nWind --> OnshoreWind[Onshore Wind Farms]\\nWind --> OffshoreWind[Offshore Wind Farms]\\nHydro --> Dams[Hydroelectric Dams]\\nHydro --> RunOfRiver[Run-of-River]\\nHydro --> TidalEnergy[Tidal Energy]\\nGeothermal --> GeothermalPlants[Geothermal Power Plants]\\nGeothermal --> GeothermalHeating[Geothermal Heating]\\nBiomass --> BiofuelProduction[Biofuel Production]\\nBiomass --> BiomassPlants[Biomass Power Plants]"
}
```

### Rendering the Mind Map

The Mermaid format can be rendered in many markdown editors, documentation platforms, or using the [Mermaid Live Editor](https://mermaid-js.github.io/mermaid-live-editor/).

## Best Practices for Topic Selection

1. **Be specific**: "Machine Learning Algorithms" will generate a better mind map than just "Machine Learning"
2. **Use descriptive topics**: "Climate Change: Causes and Effects" is better than "Climate"
3. **Consider complexity**: Very broad topics will generate large mind maps; very narrow topics might not have enough content

## Integration Examples

### HTML Embedding

```html
<script src="https://cdn.jsdelivr.net/npm/mermaid/dist/mermaid.min.js"></script>
<script>mermaid.initialize({startOnLoad:true});</script>

<div class="mermaid">
  <!-- Insert mindmap string here -->
  graph TD;
  A[Main Topic] --> B[Subtopic 1]
  A --> C[Subtopic 2]
</div>
```

### React Component

```jsx
import mermaid from 'mermaid';

const MindMapViewer = ({ mindmapString }) => {
  const elementId = 'mindmap-' + Math.random().toString(36).substr(2, 9);
  
  useEffect(() => {
    mermaid.initialize({ startOnLoad: false });
    mermaid.render(elementId, mindmapString, (svg) => {
      document.getElementById(elementId + '-container').innerHTML = svg;
    });
  }, [mindmapString, elementId]);
  
  return <div id={elementId + '-container'} />;
}
```
