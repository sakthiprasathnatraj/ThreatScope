# ThreatScope UI Upgrade - Visual Reference

## 🎨 Before & After Comparison

### Dashboard Layout

#### BEFORE
```
┌─────────────────────────────────────────────────────┐
│ [SAFE] Status Banner (static green)                │
├─────────────────────────────────────────────────────┤
│ ┌──────────────┐ ┌──────────────┐                  │
│ │ What is      │ │ Why this     │                  │
│ │ happening    │ │ matters      │                  │
│ │              │ │              │                  │
│ └──────────────┘ └──────────────┘                  │
│ ┌──────────────┐ ┌──────────────┐                  │
│ │ Confidence   │ │ Recommended  │                  │
│ │ 100%         │ │ Action       │                  │
│ │              │ │              │                  │
│ └──────────────┘ └──────────────┘                  │
│ ┌─────────────────────────────────────────────┐    │
│ │ Incident Timeline                           │    │
│ │ (basic list)                                │    │
│ └─────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────┘
```

#### AFTER
```
┌─────────────────────────────────────────────────────┐
│ [✓ SAFE] Status Banner (reactive gradient + shadow)│
│ SYSTEM STATUS                                       │
├─────────────────────────────────────────────────────┤
│ ┌──────────────┐ ┌──────────────┐                  │
│ │ WHAT IS      │ │ WHY THIS     │                  │
│ │ HAPPENING    │ │ MATTERS      │                  │
│ │ (bound text) │ │ (bound text) │                  │
│ └──────────────┘ └──────────────┘                  │
│ ┌──────────────┐ ┌──────────────┐                  │
│ │ CONFIDENCE   │ │ RECOMMENDED  │                  │
│ │ LEVEL        │ │ ACTION       │                  │
│ │   ⭕ 100%    │ │ (bound text) │                  │
│ │ High Conf.   │ │ [Button?]    │                  │
│ └──────────────┘ └──────────────┘                  │
│ ┌─────────────────────────────────────────────┐    │
│ │ SYSTEM EXPLANATION                          │    │
│ │ ┌─────────────────────────────────────────┐ │    │
│ │ │ Why did the confidence change?          │ │    │
│ │ │ System is operating normally...         │ │    │
│ │ └─────────────────────────────────────────┘ │    │
│ │ ┌─────────────────────────────────────────┐ │    │
│ │ │ Recent incident impact                  │ │    │
│ │ │ No recent incidents affecting...        │ │    │
│ │ └─────────────────────────────────────────┘ │    │
│ └─────────────────────────────────────────────┘    │
│ ┌─────────────────────────────────────────────┐    │
│ │ Incident Timeline                           │    │
│ │ (with impact badges and explanations)       │    │
│ └─────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────┘
```

---

## 🎯 Status Banner States

### SAFE State
```
┌─────────────────────────────────────────────────────┐
│ ✓  SYSTEM STATUS                                    │
│    SAFE                                             │
│ [Green gradient: #10B981 → #059669]                │
│ [Calm shadow: rgba(16, 185, 129, 0.3)]             │
└─────────────────────────────────────────────────────┘
```

### ATTENTION State
```
┌─────────────────────────────────────────────────────┐
│ ⚠  SYSTEM STATUS                                    │
│    ATTENTION                                        │
│ [Orange gradient: #F59E0B → #D97706]               │
│ [Medium shadow: rgba(245, 158, 11, 0.4), 12px]     │
└─────────────────────────────────────────────────────┘
```

### ACTION REQUIRED State
```
┌─────────────────────────────────────────────────────┐
│ !  SYSTEM STATUS                                    │
│    ACTION REQUIRED                                  │
│ [Red gradient: #EF4444 → #B91C1C]                  │
│ [Strong shadow: rgba(239, 68, 68, 0.5), 14px]      │
└─────────────────────────────────────────────────────┘
```

---

## 📊 Incidents Table

### BEFORE
```
┌────────────┬──────────┬─────────────┬────────────┐
│ Timestamp  │ Severity │ Type        │ Source IP  │
├────────────┼──────────┼─────────────┼────────────┤
│ 12:30:45   │ HIGH     │ PORT_SCAN   │ 10.0.0.5   │
│ 12:25:12   │ MEDIUM   │ SUSPICIOUS  │ 10.0.0.8   │
│ 12:20:33   │ LOW      │ INFO        │ localhost  │
└────────────┴──────────┴─────────────┴────────────┘
```

### AFTER
```
┌────────────┬──────────┬────────┬─────────────┬────────────┬──────────────────────────┐
│ Timestamp  │ Severity │ Impact │ Type        │ Source IP  │ Explanation              │
├────────────┼──────────┼────────┼─────────────┼────────────┼──────────────────────────┤
│ 12:30:45   │ HIGH     │ [high] │ PORT_SCAN   │ 10.0.0.5   │ Detected port scan...    │
│ 12:25:12   │ MEDIUM   │ [med]  │ SUSPICIOUS  │ 10.0.0.8   │ Unusual traffic pattern  │
│ 12:20:33   │ LOW      │ [low]  │ INFO        │ localhost  │ System ready event       │
└────────────┴──────────┴────────┴─────────────┴────────────┴──────────────────────────┘

Impact Badges:
[low]  = Green background (#DCFCE7), dark green text (#166534)
[med]  = Yellow background (#FEF3C7), dark yellow text (#92400E)
[high] = Red background (#FEE2E2), dark red text (#991B1B)
```

### EMPTY STATE
```
┌─────────────────────────────────────────────────────┐
│                                                     │
│                      📋                             │
│                                                     │
│              No Incidents Detected                  │
│                                                     │
│   System monitoring is active. No security         │
│   incidents have been detected yet.                │
│                                                     │
└─────────────────────────────────────────────────────┘
```

---

## ⚙️ Settings Screen

### BEFORE
```
┌─────────────────────────────────────────────────────┐
│ Settings                                            │
│                                                     │
│ Scan Interval: [Dropdown ▼]                        │
│ Alert Threshold: [Dropdown ▼]                      │
│ Data Retention: [Dropdown ▼]                       │
│                                                     │
│ [Save] [Cancel]                                     │
└─────────────────────────────────────────────────────┘
```

### AFTER
```
┌─────────────────────────────────────────────────────┐
│ Settings                                            │
│ Configure ThreatScope monitoring and alert prefs    │
│                                                     │
│ ┌─────────────────────────────────────────────┐    │
│ │ MONITORING SETTINGS                         │    │
│ │                                             │    │
│ │ Scan Interval                               │    │
│ │ How often to refresh network and process... │    │
│ │ [Select interval ▼]                         │    │
│ │ ─────────────────────────────────────────── │    │
│ │ Alert Threshold                             │    │
│ │ Minimum severity level for notifications... │    │
│ │ [Select threshold ▼]                        │    │
│ │ ─────────────────────────────────────────── │    │
│ │ Data Retention                              │    │
│ │ How long to keep incident history...        │    │
│ │ [Select retention period ▼]                 │    │
│ └─────────────────────────────────────────────┘    │
│                                                     │
│ ┌─────────────────────────────────────────────┐    │
│ │ ALERT PREFERENCES                           │    │
│ │                                             │    │
│ │ ☑ Enable desktop notifications              │    │
│ │   Show system notifications for high...     │    │
│ │                                             │    │
│ │ ☑ Enable sound alerts                       │    │
│ │   Play an alert sound when critical...      │    │
│ │                                             │    │
│ │ ☐ Automatic threat response                 │    │
│ │   Allow ThreatScope to automatically...     │    │
│ └─────────────────────────────────────────────┘    │
│                                                     │
│ [Save Changes] [Reset to Defaults]                 │
│                                                     │
│ ┌─────────────────────────────────────────────┐    │
│ │ 💡 Settings Information                     │    │
│ │ Changes take effect immediately. Some...    │    │
│ └─────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────┘
```

---

## 🎨 Color Palette Reference

### Status Colors
```
SAFE:
  Primary: #10B981 (Emerald 500)
  Dark:    #059669 (Emerald 600)
  Shadow:  rgba(16, 185, 129, 0.3)

ATTENTION:
  Primary: #F59E0B (Amber 500)
  Dark:    #D97706 (Amber 600)
  Shadow:  rgba(245, 158, 11, 0.4)

ACTION:
  Primary: #EF4444 (Red 500)
  Dark:    #B91C1C (Red 700)
  Shadow:  rgba(239, 68, 68, 0.5)
```

### Text Colors
```
Primary:   #1E293B (Slate 800)
Secondary: #334155 (Slate 700)
Muted:     #64748B (Slate 500)
Light:     #94A3B8 (Slate 400)
```

### Background Colors
```
Page:      #F8FAFC (Slate 50)
Card:      #FFFFFF (White)
Panel:     #F1F5F9 (Slate 100)
Hover:     #F8FAFC (Slate 50)
```

### Impact/Severity Badges
```
Low:
  Background: #DCFCE7 (Green 100)
  Text:       #166534 (Green 800)

Medium:
  Background: #FEF3C7 (Yellow 100)
  Text:       #92400E (Yellow 800)

High:
  Background: #FEE2E2 (Red 100)
  Text:       #991B1B (Red 800)
```

---

## 📏 Spacing & Typography

### Spacing Scale
```
xs:  4px   (tight elements)
sm:  8px   (related items)
md:  16px  (section spacing)
lg:  24px  (card spacing)
xl:  32px  (page padding)
```

### Typography Scale
```
Page Title:      24px, bold, #1E293B
Card Title:      12px, 700, #64748B, letter-spacing: 0.5px
Section Header:  14px, 600, #334155
Body Text:       15px, 400, #334155, line-spacing: 1.5px
Muted Text:      14px, 400, #64748B
Small Text:      13px, 500, #64748B
Badge Text:      11px, 600
```

### Font Stack
```
font-family: 'Segoe UI', 'Inter', -apple-system, sans-serif;
```

---

## 🎭 Hover Effects

### Cards
```
Default:
  shadow: dropshadow(rgba(0,0,0,0.06), 12px, 0, 0, 2)

Hover:
  shadow: dropshadow(rgba(0,0,0,0.1), 16px, 0, 0, 4)
```

### Buttons
```
Primary:
  Default:  #3B82F6, shadow: rgba(59,130,246,0.2)
  Hover:    #2563EB, shadow: rgba(59,130,246,0.3)
  Pressed:  #1D4ED8, shadow: rgba(59,130,246,0.15)

Danger:
  Default:  #DC2626, shadow: rgba(220,38,38,0.2)
  Hover:    #B91C1C, shadow: rgba(220,38,38,0.3)
  Pressed:  #991B1B, shadow: rgba(220,38,38,0.15)
```

### Navigation
```
Default:
  background: transparent
  text: #64748B

Hover:
  background: #F8FAFC
  text: #1E293B

Active:
  background: #EFF6FF
  text: #2563EB
  border-left: 3px solid #2563EB
```

---

## 🔄 Confidence Ring Animation

### Visual Representation
```
100%:  ⭕ (full circle, green)
75%:   ◔  (3/4 circle, green/orange)
50%:   ◑  (1/2 circle, orange)
25%:   ◕  (1/4 circle, red)

Animation:
  - Smooth stroke-dash-offset transition
  - Color changes with security state
  - Subtitle updates automatically:
    > 90%:  "High Confidence"
    60-90%: "Medium Confidence"
    < 60%:  "Low Confidence"
```

---

## 📐 Layout Grid

### Dashboard Grid
```
┌─────────────────────────────────────────────────────┐
│ Status Banner (full width, 120px height)            │
├──────────────────────────┬──────────────────────────┤
│ What is Happening        │ Why This Matters         │
│ (50% width, 200px min)   │ (50% width, 200px min)   │
├──────────────────────────┼──────────────────────────┤
│ Confidence Level         │ Recommended Action       │
│ (50% width, 200px min)   │ (50% width, 200px min)   │
├─────────────────────────────────────────────────────┤
│ Explainability Panel (full width)                   │
├─────────────────────────────────────────────────────┤
│ Incident Timeline (full width)                      │
└─────────────────────────────────────────────────────┘

Grid gaps: 24px horizontal, 24px vertical
Page padding: 32px all sides
```

---

## ✨ Key Visual Improvements Summary

1. **Status Banner:** Static → Reactive gradient with state-based shadows
2. **Confidence Ring:** Static → Animated with auto-updating subtitle
3. **Cards:** Flat → Subtle shadow with hover effect
4. **Typography:** Basic → Clear hierarchy with proper spacing
5. **Buttons:** Simple → Shadow effects with hover/pressed states
6. **Navigation:** Plain → Active state with left border
7. **Tables:** Basic → Hover rows, styled headers
8. **Empty States:** Blank → Helpful messages with icons
9. **Impact Badges:** None → Color-coded, rounded badges
10. **Explainability:** None → Dedicated panel with clear sections

---

*Visual Reference Guide v1.0*  
*ThreatScope UI Upgrade - 2026-01-22*
