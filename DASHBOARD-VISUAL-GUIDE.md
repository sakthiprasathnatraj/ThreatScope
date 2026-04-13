# 🎨 Professional Dashboard - Visual Layout Guide

## Dashboard Layout Overview

```
┌─────────────────────────────────────────────────────────────────────────────┐
│ TOP BAR                                                                     │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ ThreatScope v2.0 Professional Edition    [SYSTEM: SAFE]  [Session Panel]│ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│ LIVE ACTIVITY INDICATORS (1-sec refresh)                                   │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │  Packets Analyzed  │  Packets/sec  │  Active Connections  │  Last Packet││ │
│ │       12,543       │      87       │         15           │   124 ms ago││ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                             │
│ HEARTBEAT MESSAGE (10-sec rotation)                                        │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ System Status                                                           │ │
│ │ Monitoring network traffic normally                                     │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                             │
│ CONTROL PANEL                                                              │
│ ┌──────────────────────────────────┬──────────────────────────────────────┐ │
│ │ Interface: eth0                  │ Mode: [Simple Mode / Expert Mode]   │ │
│ │ Status: ● Enabled                │                                      │ │
│ │ [Enable Monitoring]              │                                      │ │
│ └──────────────────────────────────┴──────────────────────────────────────┘ │
│                                                                             │
│ SECURITY EVENT TIMELINE                                                    │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Security Event Timeline                                                 │ │
│ │ ┌─────────┬──────────────┬──────┬──────────────┬────────────────────┐  │ │
│ │ │ Time    │ Threat Type  │ Risk │ Classification│ Summary           │  │ │
│ │ ├─────────┼──────────────┼──────┼──────────────┼────────────────────┤  │ │
│ │ │ 18:45:23│ PORT_SCAN    │ 25   │ BENIGN_NOISE │ External computer...│  │ │
│ │ │ 18:44:10│ PORT_SCAN    │ 30   │ SUSPICIOUS   │ Multiple ports...  │  │ │
│ │ └─────────┴──────────────┴──────┴──────────────┴────────────────────┘  │ │
│ │                                                                         │ │
│ │ [Click row to expand details]                                           │ │
│ │ ┌─────────────────────────────────────────────────────────────────────┐ │ │
│ │ │ ═══ SECURITY EVENT DETAILS ═══                                      │ │ │
│ │ │ WHAT HAPPENED: External computer attempted to connect...            │ │ │
│ │ │ WHY THIS MATTERS: Risk Score: 25/100 (Low)...                       │ │ │
│ │ │ RECOMMENDED ACTION: No action needed...                             │ │ │
│ │ │ REASSURANCE: This is a low-priority event...                        │ │ │
│ │ └─────────────────────────────────────────────────────────────────────┘ │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                             │
│ RAW ACTIVITY (Expert Mode Only - Hidden by default)                        │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Raw Activity (Expert Mode)                                              │ │
│ │ ┌──────────┬─────────────┬─────────────┬──────┬───────────┬──────────┐ │ │
│ │ │Timestamp │ Source IP   │ Threat Type │ Risk │ Confidence│ Class    │ │ │
│ │ ├──────────┼─────────────┼─────────────┼──────┼───────────┼──────────┤ │ │
│ │ │18:45:23  │198.20.69.42 │ PORT_SCAN   │  25  │ MEDIUM    │ BENIGN   │ │ │
│ │ │18:44:10  │203.45.12.88 │ PORT_SCAN   │  30  │ MEDIUM    │ SUSPICIOUS│ │ │
│ │ └──────────┴─────────────┴─────────────┴──────┴───────────┴──────────┘ │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                             │
│ QUICK STATUS                                                               │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Your system is currently SAFE. No threats detected.                     │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Component Details

### 1. Top Bar

```
┌─────────────────────────────────────────────────────────────────────────┐
│ ThreatScope                      [SYSTEM: SAFE]    ┌─────────────────┐ │
│ v2.0 Professional Edition                          │ 👤 Admin        │ │
│                                                     │ Security Analyst│ │
│                                                     │ ● Active        │ │
│                                                     │ [Logout]        │ │
│                                                     └─────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘
```

**Elements:**
- **Left:** App title + version
- **Center:** System state badge (color-coded, pulsing)
- **Right:** Session context panel (always visible)

**Colors:**
- SAFE: Green (#238636)
- OBSERVE: Blue (#1f6feb)
- WARNING: Orange (#d29922)
- CRITICAL: Red (#da3633)

---

### 2. Live Activity Indicators

```
┌─────────────────────────────────────────────────────────────────────────┐
│  Packets Analyzed  │  Packets/sec  │  Active Connections  │  Last Packet│
│       12,543       │      87       │         15           │   124 ms ago│
└─────────────────────────────────────────────────────────────────────────┘
```

**Metrics:**
1. **Packets Analyzed** - Session total (blue)
2. **Packets/sec** - Live rate (green)
3. **Active Connections** - Last 60s (orange)
4. **Last Packet** - Time since last (gray)

**Refresh:** Every 1 second

---

### 3. Heartbeat Message

```
┌─────────────────────────────────────────────────────────────────────────┐
│ System Status                                                           │
│ Monitoring network traffic normally                                     │
└─────────────────────────────────────────────────────────────────────────┘
```

**Messages (SAFE state, rotating every 10 seconds):**
1. "Monitoring network traffic normally"
2. "No suspicious behavior observed"
3. "System operating within normal parameters"
4. "All security checks passing"
5. "Network activity appears normal"
6. "No threats detected in recent traffic"

---

### 4. Control Panel

```
┌──────────────────────────────────┬──────────────────────────────────────┐
│ Interface: eth0                  │ Mode: [Simple Mode]                  │
│ Status: ● Enabled                │       [Expert Mode]                  │
│ [Enable Monitoring]              │                                      │
└──────────────────────────────────┴──────────────────────────────────────┘
```

**Left Side:**
- Network interface name
- Monitoring status (● Enabled / ○ Disabled)
- Enable/Disable Monitoring button

**Right Side:**
- Mode toggle (Simple Mode / Expert Mode)
- Changes button color when toggled

---

### 5. Security Event Timeline

```
┌─────────────────────────────────────────────────────────────────────────┐
│ Security Event Timeline                                                 │
│ ┌─────────┬──────────────┬──────┬──────────────┬────────────────────┐  │
│ │ Time    │ Threat Type  │ Risk │ Classification│ Summary           │  │
│ ├─────────┼──────────────┼──────┼──────────────┼────────────────────┤  │
│ │ 18:45:23│ PORT_SCAN    │ 25   │ BENIGN_NOISE │ External computer...│  │
│ └─────────┴──────────────┴──────┴──────────────┴────────────────────┘  │
│                                                                         │
│ [Click to expand]                                                       │
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │ ═══ SECURITY EVENT DETAILS ═══                                      │ │
│ │                                                                     │ │
│ │ WHAT HAPPENED:                                                      │ │
│ │ An external computer attempted to connect to 12 different services.│ │
│ │ This appears to be automated internet scanning.                    │ │
│ │                                                                     │ │
│ │ WHY THIS MATTERS:                                                   │ │
│ │ Risk Score: 25/100 (Low)                                            │ │
│ │ Classification: BENIGN_NOISE                                        │ │
│ │ Confidence: MEDIUM                                                  │ │
│ │                                                                     │ │
│ │ RECOMMENDED ACTION:                                                 │ │
│ │ No action needed. We are monitoring the situation.                 │ │
│ │                                                                     │ │
│ │ REASSURANCE:                                                        │ │
│ │ This is a low-priority event. Your system remains secure.          │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘
```

**Features:**
- Sortable table columns
- Click row to expand details
- Details show: WHAT, WHY, ACTION, REASSURANCE
- Reuses backend ExplanationEngine output

---

### 6. Raw Activity (Expert Mode Only)

```
┌─────────────────────────────────────────────────────────────────────────┐
│ Raw Activity (Expert Mode)                                              │
│ ┌──────────┬─────────────┬─────────────┬──────┬───────────┬──────────┐ │
│ │Timestamp │ Source IP   │ Threat Type │ Risk │ Confidence│ Class    │ │
│ ├──────────┼─────────────┼─────────────┼──────┼───────────┼──────────┤ │
│ │18:45:23  │198.20.69.42 │ PORT_SCAN   │  25  │ MEDIUM    │ BENIGN   │ │
│ │18:44:10  │203.45.12.88 │ PORT_SCAN   │  30  │ MEDIUM    │ SUSPICIOUS│ │
│ └──────────┴─────────────┴─────────────┴──────┴───────────┴──────────┘ │
└─────────────────────────────────────────────────────────────────────────┘
```

**Visibility:**
- Hidden in Simple Mode (default)
- Visible in Expert Mode
- Shows last 200 packets
- Auto-scroll enabled

**Columns:**
1. Timestamp
2. Source IP
3. Threat Type
4. Risk Score
5. Confidence
6. Classification

---

### 7. Quick Status

```
┌─────────────────────────────────────────────────────────────────────────┐
│ Your system is currently SAFE. No threats detected.                     │
└─────────────────────────────────────────────────────────────────────────┘
```

**Messages by State:**
- **SAFE:** "Your system is currently SAFE. No threats detected."
- **OBSERVE:** "Monitoring network activity. Some events detected but no immediate threat."
- **WARNING:** "Elevated activity detected. Monitoring closely for potential threats."
- **CRITICAL:** "CRITICAL: High-confidence threat detected. Review security event timeline."

---

## Color Palette

### GitHub Dark Theme

```
Background:        #0d1117  ███████
Card Background:   #161b22  ███████
Border:            #30363d  ███████
Text (Primary):    #c9d1d9  ███████
Text (Muted):      #8b949e  ███████
Blue Accent:       #58a6ff  ███████
Green (Safe):      #3fb950  ███████
Orange (Warning):  #d29922  ███████
Red (Critical):    #da3633  ███████
```

---

## Typography

### Font Sizes
- **Title:** 22px, Bold
- **Headings:** 15-16px, Bold
- **Body:** 12-13px, Regular
- **Metrics:** 24px, Bold
- **Small:** 9-11px, Regular

### Font Family
- System font (cross-platform)
- No custom fonts required

---

## Spacing

### Padding
- Card Padding: 18-20px
- Button Padding: 8-16px
- Section Padding: 24px

### Margins
- Between Cards: 20px
- Between Sections: 12-16px

### Border Radius
- Cards: 8px
- Buttons: 6px
- Badges: 6px

### Shadows
- Card Shadow: 12px blur, rgba(0,0,0,0.4)
- Button Shadow: 8-10px blur, color-specific

---

## Animations

### Pulse (State Badge)
- Subtle glow effect
- Only when monitoring is active
- No blinking or aggressive animation

### Fade-In (New Data)
- 300ms smooth transition
- Applied to new event rows
- Subtle, not distracting

### Hover Effects
- Buttons: Color change + shadow increase
- Table Rows: Background color change
- Smooth 200ms transition

---

## Responsive Behavior

### Minimum Window Size
- Width: 1024px
- Height: 600px

### Recommended Window Size
- Width: 1400px
- Height: 900px

### Scaling
- Cards expand to fill available space
- Tables scroll when content exceeds height
- Horizontal layout maintained (no stacking)

---

## State Indicators

### System State Badge

```
SAFE:     [SYSTEM: SAFE]      Green background, white text
OBSERVE:  [SYSTEM: OBSERVE]   Blue background, white text
WARNING:  [SYSTEM: WARNING]   Orange background, white text
CRITICAL: [SYSTEM: CRITICAL]  Red background, white text
```

### Monitoring Status

```
Enabled:  ● Enabled   Green dot + text
Disabled: ○ Disabled  Gray dot + text
```

### Session Status

```
Active:   ● Active    Green dot + text
Inactive: ○ Inactive  Gray dot + text
```

---

## User Interaction

### Clickable Elements
1. **Enable/Disable Monitoring Button**
   - Toggles monitoring state
   - Changes color and text

2. **Expert Mode Toggle**
   - Switches between Simple and Expert mode
   - Shows/hides Raw Activity panel

3. **Event Timeline Rows**
   - Click to expand details
   - Shows full explanation panel

4. **Logout Button**
   - Placeholder (no real auth yet)
   - Hover effect

### Keyboard Shortcuts
- None currently implemented
- Future: Arrow keys for table navigation

---

## Accessibility

### Color Contrast
- All text meets WCAG AA standards
- High contrast between text and background

### Font Sizes
- Minimum 11px for readability
- Clear hierarchy with size differences

### Interactive Elements
- Clear hover states
- Visible focus indicators
- Adequate click targets (min 32px height)

---

## Professional Appearance

### What Makes It Professional

✅ **Clean Layout**
- Consistent spacing
- Clear visual hierarchy
- No clutter

✅ **Subtle Effects**
- Soft shadows
- Smooth transitions
- No aggressive animations

✅ **Professional Colors**
- GitHub dark theme
- Muted, non-distracting
- Color-coded for meaning

✅ **Clear Typography**
- System font
- Bold headings
- Readable body text

✅ **Calm Communication**
- Reassuring messages
- No panic unless critical
- Educational explanations

---

## Comparison to SOC Dashboards

### Splunk Security
- ✅ Dark theme
- ✅ Live metrics
- ✅ Event timeline
- ✅ Professional spacing

### Elastic SIEM
- ✅ Clean layout
- ✅ Color-coded states
- ✅ Expandable details
- ✅ Expert mode

### Security Onion
- ✅ Calm interface
- ✅ Session context
- ✅ Raw data access
- ✅ Professional appearance

---

**This is a production-quality SOC-style dashboard!** 🎉
