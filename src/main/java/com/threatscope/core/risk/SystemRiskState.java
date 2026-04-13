package com.threatscope.core.risk;

public enum SystemRiskState {

    SAFE("System operating normally"),
    ATTENTION("Suspicious activity detected"),
    CRITICAL("Immediate action required");

    private final String description;

    SystemRiskState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // 🔥 CORE MAPPING LOGIC
    public static SystemRiskState fromScore(int riskScore) {
        if (riskScore <= 30) {
            return SAFE;
        } else if (riskScore <= 60) {
            return ATTENTION;
        } else {
            return CRITICAL;
        }
    }
}
