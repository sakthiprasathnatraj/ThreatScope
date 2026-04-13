package com.threatscope.core.risk;

import com.threatscope.core.model.SecurityEvent;

public class RiskStateEngine {

    public static SystemRiskState evaluate(SecurityEvent event, int riskScore) {

        SystemRiskState state = SystemRiskState.fromScore(riskScore);

        System.out.println("🧠 SYSTEM STATE → " + state.name());
        System.out.println("📖 STATE MEANING → " + state.getDescription());

        // Optional future hook
        // saveState(event, state);

        return state;
    }
}
