package com.threatscope.ui.model;

import javafx.beans.property.*;

/**
 * UI Security Event Model
 * 
 * JavaFX-friendly model for security events
 * Uses JavaFX properties for automatic UI binding
 * 
 * This is a simplified, UI-focused version of the backend SecurityEvent.
 * It contains only the information needed for display.
 */
public class UiSecurityEvent {

    private final LongProperty timestamp;
    private final StringProperty sourceIp;
    private final StringProperty destinationIp;
    private final IntegerProperty sourcePort;
    private final IntegerProperty destinationPort;
    private final StringProperty protocol;
    private final IntegerProperty packetSize;
    private final StringProperty threatType;
    private final StringProperty classification;
    private final IntegerProperty riskScore;
    private final StringProperty confidence;
    private final StringProperty explanation;
    private final StringProperty recommendation;
    private final ObjectProperty<EventStatus> status;
    private final StringProperty notes;

    /**
     * Constructor
     */
    public UiSecurityEvent(
            long timestamp,
            String sourceIp,
            String destinationIp,
            int sourcePort,
            int destinationPort,
            String protocol,
            int packetSize,
            String threatType,
            String classification,
            int riskScore,
            String confidence,
            String explanation,
            String recommendation) {
        this.timestamp = new SimpleLongProperty(timestamp);
        this.sourceIp = new SimpleStringProperty(sourceIp);
        this.destinationIp = new SimpleStringProperty(destinationIp != null ? destinationIp : "N/A");
        this.sourcePort = new SimpleIntegerProperty(sourcePort);
        this.destinationPort = new SimpleIntegerProperty(destinationPort);
        this.protocol = new SimpleStringProperty(protocol != null ? protocol : "TCP");
        this.packetSize = new SimpleIntegerProperty(packetSize);
        this.threatType = new SimpleStringProperty(threatType);
        this.classification = new SimpleStringProperty(classification);
        this.riskScore = new SimpleIntegerProperty(riskScore);
        this.confidence = new SimpleStringProperty(confidence);
        this.explanation = new SimpleStringProperty(explanation);
        this.recommendation = new SimpleStringProperty(recommendation);

        // precise init
        this.status = new SimpleObjectProperty<>(EventStatus.NEW);
        this.notes = new SimpleStringProperty("");
    }

    // ===== PROPERTY GETTERS (for JavaFX binding) =====

    public LongProperty timestampProperty() {
        return timestamp;
    }

    public StringProperty sourceIpProperty() {
        return sourceIp;
    }

    public StringProperty threatTypeProperty() {
        return threatType;
    }

    public StringProperty classificationProperty() {
        return classification;
    }

    public IntegerProperty riskScoreProperty() {
        return riskScore;
    }

    public StringProperty confidenceProperty() {
        return confidence;
    }

    public StringProperty explanationProperty() {
        return explanation;
    }

    public StringProperty recommendationProperty() {
        return recommendation;
    }

    // ===== VALUE GETTERS =====

    public long getTimestamp() {
        return timestamp.get();
    }

    public String getSourceIp() {
        return sourceIp.get();
    }

    public String getThreatType() {
        return threatType.get();
    }

    public String getClassification() {
        return classification.get();
    }

    public int getRiskScore() {
        return riskScore.get();
    }

    public String getConfidence() {
        return confidence.get();
    }

    public String getDestinationIp() {
        return destinationIp.get();
    }

    public int getSourcePort() {
        return sourcePort.get();
    }

    public int getDestinationPort() {
        return destinationPort.get();
    }

    public String getProtocol() {
        return protocol.get();
    }

    public int getPacketSize() {
        return packetSize.get();
    }

    public String getExplanation() {
        return explanation.get();
    }

    public String getRecommendation() {
        return recommendation.get();
    }

    public EventStatus getStatus() {
        return status.get();
    }

    public String getNotes() {
        return notes.get();
    }

    // ===== VALUE SETTERS =====

    public void setTimestamp(long timestamp) {
        this.timestamp.set(timestamp);
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp.set(sourceIp);
    }

    public void setThreatType(String threatType) {
        this.threatType.set(threatType);
    }

    public void setClassification(String classification) {
        this.classification.set(classification);
    }

    public void setRiskScore(int riskScore) {
        this.riskScore.set(riskScore);
    }

    public void setConfidence(String confidence) {
        this.confidence.set(confidence);
    }

    public void setExplanation(String explanation) {
        this.explanation.set(explanation);
    }

    public void setRecommendation(String recommendation) {
        this.recommendation.set(recommendation);
    }

    public void setStatus(EventStatus status) {
        this.status.set(status);
    }

    public void setNotes(String notes) {
        this.notes.set(notes);
    }

    public ObjectProperty<EventStatus> statusProperty() {
        return status;
    }

    public StringProperty notesProperty() {
        return notes;
    }

    /**
     * Gets formatted time string
     */
    public String getFormattedTime() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
        return sdf.format(new java.util.Date(timestamp.get()));
    }

    /**
     * Gets timestamp as formatted string for table display
     */
    public String getFormattedTimestamp() {
        return getFormattedTime();
    }

    /**
     * Gets summary text (combination of threat type and source)
     */
    public String getSummary() {
        return threatType.get() + " from " + sourceIp.get();
    }

    /**
     * Gets recommended action (alias for recommendation)
     */
    public String getRecommendedAction() {
        return recommendation.get();
    }

    /**
     * Gets reassurance message based on risk level
     */
    public String getReassurance() {
        int risk = riskScore.get();
        if (risk < 30) {
            return "This is a low-risk event. Your system remains secure. Continue normal operations.";
        } else if (risk < 70) {
            return "This event requires attention but is not critical. Monitor the situation and follow recommended actions.";
        } else {
            return "This is a high-risk event. Take immediate action as recommended to secure your system.";
        }
    }

    /**
     * Gets risk level description
     */
    public String getRiskLevel() {
        int risk = riskScore.get();
        if (risk < 30)
            return "Low";
        if (risk < 50)
            return "Moderate";
        if (risk < 70)
            return "High";
        return "Critical";
    }

    /**
     * Gets classification color
     */
    public String getClassificationColor() {
        String classification = this.classification.get();
        if (classification == null)
            return "#888888";

        switch (classification) {
            case "TRUSTED":
                return "#4caf50"; // Green
            case "BENIGN_NOISE":
                return "#2196f3"; // Blue
            case "SUSPICIOUS":
                return "#ff9800"; // Orange
            case "CONFIRMED_THREAT":
                return "#f44336"; // Red
            default:
                return "#888888"; // Gray
        }
    }

    @Override
    public String toString() {
        return "UiSecurityEvent{" +
                "timestamp=" + getFormattedTime() +
                ", sourceIp='" + sourceIp.get() + '\'' +
                ", threatType='" + threatType.get() + '\'' +
                ", classification='" + classification.get() + '\'' +
                ", riskScore=" + riskScore.get() +
                ", confidence='" + confidence.get() + '\'' +
                '}';
    }
}
