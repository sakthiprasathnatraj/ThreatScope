# ThreatScope v2.0 Diagrams

Since you need high-quality diagrams for your IEEE paper, you can use these **MermaidJS** definitions. You can view these in a Markdown editor that supports Mermaid (like VS Code or GitHub) and take screenshots, or use an online editor like [mermaid.live](https://mermaid.live).

## Figure 1: High-Level System Architecture Diagram

```mermaid
graph TD
    subgraph "User Interface Layer"
        UI[<b>DashboardViewV2</b><br/>JavaFX UI]
        VA[<b>Visual Analytics</b><br/>LineChart, PieChart]
        Alerts[<b>Alert Manager</b><br/>Notifications]
    end

    subgraph "Risk & State Layer"
        Risk[<b>Risk Engine</b><br/>Score: 0-100]
        State[<b>State Manager</b><br/>SAFE / WARNING / CRITICAL]
        Class[<b>Traffic Classifier</b><br/>Trusted / Benign / Threat]
    end

    subgraph "Detection Layer"
        DDoS[<b>DDoS Detector</b><br/>Volumetric Analysis]
        Scan[<b>Port Scan Detector</b><br/>Time-Window Logic]
        Pattern[<b>Pattern Detector</b><br/>Signature Matching]
    end

    subgraph "Capture Layer"
        Pcap[<b>Pcap4J Wrapper</b><br/>Libpcap/WinPcap]
        NIC[<b>Network Interface</b><br/>Promiscuous Mode]
        Buffer[<b>Ring Buffer</b><br/>Raw Packets]
    end

    NIC --> Pcap
    Pcap --> Buffer
    Buffer --> DDoS & Scan & Pattern
    DDoS & Scan & Pattern --> Class
    Class --> Risk
    Risk --> State
    State --> UI
    VA -.-> Buffer
```

## Figure 2: Detection Engine Logic Flow

```mermaid
flowchart TD
    Start([Packet Received]) --> Filter{IPv4 & Non-Empty?}
    Filter -- No --> Drop([Drop Packet])
    Filter -- Yes --> Decode[Decode Packet Headers]
    
    Decode --> Parallel{Parallel Analysis}
    
    Parallel --> D1[<b>DDoS Detector</b><br/>Check Packet Rate]
    Parallel --> D2[<b>Pattern Detector</b><br/>Check Signatures]
    Parallel --> D3[<b>Scan Detector</b><br/>Check Port Count]
    
    D1 --> C1{Rate > Threshold?}
    D2 --> C2{Match Found?}
    D3 --> C3{Count > 10?}
    
    C1 -- Yes --> Event[Create Security Event]
    C2 -- Yes --> Event
    C3 -- Yes --> Event
    
    C1 & C2 & C3 -- No --> Stats[Update Statistics]
    
    Event --> Risk[Calculate Risk Score]
    Risk --> UI[Update Dashboard]
```

## Figure 3: Concurrency Model (Producer-Consumer)

```mermaid
sequenceDiagram
    participant NIC as Network Interface
    participant Cap as <b>Capture Thread</b>
    participant Q as <b>BlockingQueue</b>
    participant W as <b>Worker Threads</b>
    participant UI as <b>JavaFX UI Thread</b>

    loop Every Packet
        NIC->>Cap: Raw Packet
        Cap->>Q: Offer(Packet)
    end

    loop Async Processing
        W->>Q: Poll()
        Q-->>W: Packet
        W->>W: Analyze(Packet)
        W-->>UI: Platform.runLater(Update)
    end

    UI->>UI: Render Charts
```

## Figure 6: Detection Accuracy (Bar Chart Data)

```mermaid
xychart-beta
    title "Detection Accuracy by Attack Vector"
    x-axis ["SYN Flood", "UDP Flood", "Intense Scan", "Stealth Scan", "Backdoor"]
    y-axis "Accuracy (%)" 0 --> 100
    bar [100, 98, 100, 88, 100]
```
