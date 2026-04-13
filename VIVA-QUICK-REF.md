# 🎓 ThreatScope - Viva Quick Reference

## ARCHITECTURE (5 LAYERS)

```
CAPTURE → DETECTION → CORRELATION → RISK/STATE → MODEL
```

1. **CAPTURE:** Extract IP + Port, filter local traffic
2. **DETECTION:** Pattern detection with time windows
3. **CORRELATION:** Combine evidence, single output
4. **RISK/STATE:** Calculate risk, manage states
5. **MODEL:** Immutable data structures

---

## DETECTION RULE

**PORT SCAN:**
- ≥ 10 unique ports
- Within 5 seconds
- 60-second cooldown

---

## FORMULAS

**Risk:**
```
Risk = 60 + (PortCount × 2)
Capped at 100
```

**Confidence:**
- HIGH: Risk ≥ 80 AND Evidence ≥ 10
- MEDIUM: Risk ≥ 50 OR Evidence ≥ 5
- LOW: Everything else

---

## STATES

```
SAFE (risk < 40)
  ↓
OBSERVE (risk ≥ 40)
  ↓
WARNING (risk ≥ 60)
  ↓
CRITICAL (risk ≥ 80 + HIGH confidence)
```

**Anti-Flapping:**
- Upward: Immediate
- Downward: 30-second cooldown

---

## FALSE POSITIVE CONTROL

1. Local traffic filtering
2. Time-window patterns (5 sec)
3. Alert cooldown (60 sec)
4. Pattern vs. single packet

---

## KEY VIVA ANSWERS

**Q: Why time-window?**  
A: Prevents false positives, realistic detection, auto-cleanup

**Q: Why CRITICAL needs HIGH confidence?**  
A: Prevent panic, require strong evidence (risk ≥ 80 + evidence ≥ 10)

**Q: How prevent state flapping?**  
A: Upward immediate, downward 30-sec cooldown

**Q: How prevent false positives?**  
A: Multi-layer: packet filter, local traffic, time-window, cooldown

---

## COMPILATION

```
[INFO] BUILD SUCCESS
[INFO] Compiling 20 source files
```

---

## RUN COMMAND

```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.threatscope.Main"
```

---

**Status:** ✅ DEMO-READY
