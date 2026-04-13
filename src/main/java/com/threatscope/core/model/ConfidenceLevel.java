package com.threatscope.core.model;

/**
 * Confidence levels for threat detection.
 * 
 * Confidence depends on:
 * - Number of events
 * - Duration of observation
 * - Consistency of behavior
 */
public enum ConfidenceLevel {
    LOW, // Weak evidence, single observation
    MEDIUM, // Moderate evidence, repeated behavior
    HIGH // Strong evidence, sustained pattern
}
