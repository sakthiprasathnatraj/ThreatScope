# ThreatScope v2.0 - Professional Dashboard Layout Rebuild

## 🎯 Overview

This document describes the **FINAL** professional SOC-level dashboard layout for ThreatScope v2.0, optimized for **1920×1080** resolution with no unnecessary scrolling.

---

## 📐 Layout Architecture

### Root Structure: BorderPane

```
┌─────────────────────────────────────────────────────────────┐
│ TOP: Header Bar (70px fixed)                                 │
├─────────────────────────────────────────────────────────────┤
│ CENTER: Main Grid (2×2 layout)                               │
│ ┌──────────────────────┬──────────────┐                     │
│ │ Live Monitoring (65%)│ Risk (35%)   │ Row 1 (45%)         │
│ ├──────────────────────┼──────────────┤                     │
│ │ Timeline Table (65%) │ Explanation  │ Row 2 (55%)         │
│ │                      │ (Expandable) │                     │
│ └──────────────────────┴──────────────┘                     │
├─────────────────────────────────────────────────────────────┤
│ BOTTOM: Control Bar (80px fixed)                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 🧱 Component Breakdown

### 1. TOP SECTION - Header Bar (70px)

**Purpose**: Compact branding and system status

**Layout**: HBox with LEFT and RIGHT sections

#### LEFT Side:
- **ThreatScope** logo (22px, bold, cyan #00d4ff)
- **v2.0 Professional** version text (11px, muted #666666)

#### RIGHT Side:
- **SYSTEM STATE** badge (color-coded pill)
  - SAFE: Green (#4ade80) with glow
  - SUSPICIOUS: Orange (#fb923c) with glow
  - THREAT DETECTED: Red (#f87171) with glow
- **User Card**: Admin | Logout

**Design Notes**:
- Fixed height prevents layout shift
- Clean, minimal, professional
- State badge has subtle glow effect

---

### 2. CENTER SECTION - Main Grid (2×2)

**Grid Configuration**:
- **Columns**: 65% (left) | 35% (right)
- **Rows**: 45% (top) | 55% (bottom)
- **Spacing**: 20px gaps, 20px padding

---

#### ROW 1, LEFT - Live Monitoring Panel (65% width, 45% height)

**Components**:

1. **Statistics Row** (4 horizontal cards)
   - Packets Analyzed
   - Packets/sec
   - Active Connections
   - Last Packet
   
   **Card Specs**:
   - Max height: 90px
   - Equal width distribution
   - Large number (24px, bold, cyan)
   - Small label (11px, gray)
   - Dark background (#252525) with border

2. **System Status Card**
   - Max height: 110px
   - Short explanation text
   - Wraps properly
   - No excessive empty space

**Design Notes**:
- VBox.setVgrow = NEVER (prevents vertical expansion)
- Compact, information-dense
- Professional card styling

---

#### ROW 1, RIGHT - Risk Overview Panel (35% width, 45% height)

**Components**:

1. **Risk Level Progress Bar**
   - Large horizontal bar (35px height)
   - Percentage label overlaid
   - Color changes based on risk:
     - 0-29%: Green (#4ade80)
     - 30-69%: Orange (#fb923c)
     - 70-100%: Red (#f87171)

2. **Risk Distribution Chart**
   - Compact bar chart (max 200px height)
   - Shows Low/Med/High event counts
   - Minimal styling, no legend
   - Fits without pushing layout

**Design Notes**:
- VBox.setVgrow = NEVER
- Charts are compact, not oversized
- No unnecessary vertical stretching

---

#### ROW 2, LEFT - Security Event Timeline (65% width, 55% height)

**Components**:

1. **TableView** (fixed 300px height)
   
   **Columns**:
   - Time (120px)
   - Threat (150px)
   - Risk (80px)
   - Classification (120px)
   - Summary (300px)
   
   **Features**:
   - Column auto-resize enabled
   - Horizontal stretch
   - Clean row hover highlight
   - Dark theme (#252525)
   - Placeholder: "No security events detected"

**Design Notes**:
- VBox.setVgrow(table, Priority.ALWAYS) - allows table to use available space
- Fixed height prevents overflow
- Clearly readable on 1080p
- Professional data table styling

---

#### ROW 2, RIGHT - Expandable Explanation Panel (35% width, 55% height)

**Components**:

1. **TitledPane** (collapsible)
   - Title: "Security Explanation"
   - Initially collapsed
   - Expands to show details

2. **ScrollPane** (max 300px height)
   
   **Sections**:
   - WHAT HAPPENED
   - WHY THIS MATTERS
   - CLASSIFICATION
   - RISK LEVEL
   - CONFIDENCE
   - RECOMMENDED ACTION
   - REASSURANCE
   
   **Features**:
   - Text wraps properly
   - Scrollable when expanded
   - Fits screen without pushing layout
   - Dark background (#1f1f1f)

**Design Notes**:
- When collapsed: minimal space
- When expanded: readable but contained
- No layout overflow

---

### 3. BOTTOM SECTION - Control Bar (80px)

**Purpose**: Action buttons and mode toggles

**Layout**: HBox with LEFT and RIGHT sections

#### LEFT Side:
- **Enable/Disable Monitoring** button (180px × 40px)
  - Blue (#0078d4) when inactive
  - Red (#dc2626) when active
- **Generate Test Events** button (180px × 40px)
  - Gray (#444444)

#### RIGHT Side:
- **Expert Mode** toggle button (150px × 40px)
  - Gray (#2a2a2a) when off
  - Blue (#0078d4) when on

**Design Notes**:
- Fixed height: 80px
- Buttons aligned properly
- Same height for consistency
- No floating in large empty area

---

## 🎨 Visual Design System

### Color Palette

**Backgrounds**:
- Root: #0f0f0f (very dark)
- Panels: #1a1a1a (dark)
- Cards: #252525 (medium dark)
- Borders: #2a2a2a, #333333

**Text**:
- Primary: #ffffff (white)
- Secondary: #cccccc (light gray)
- Muted: #888888 (gray)
- Disabled: #666666 (dark gray)

**Accents**:
- Primary: #00d4ff (cyan)
- Success: #4ade80 (green)
- Warning: #fb923c (orange)
- Danger: #f87171 (red)
- Info: #0078d4 (blue)

### Typography

**Font Family**: Segoe UI (fallback: System)

**Sizes**:
- Logo: 22px bold
- Section titles: 16px bold
- Card titles: 13px semi-bold
- Large numbers: 24px bold
- Body text: 12-13px regular
- Small labels: 11px regular

### Spacing

**Grid**:
- All spacing aligns to 20px grid
- Padding: 20px (panels), 15px (cards)
- Gaps: 20px (grid), 15px (internal)

**Component Heights**:
- Header: 70px (fixed)
- Control bar: 80px (fixed)
- Stat cards: max 90px
- Status card: max 110px
- Timeline table: 300px
- Risk chart: max 200px
- Explanation scroll: max 300px

### Effects

**Shadows**:
- State badge: Subtle glow matching color
- Cards: None (clean, flat design)

**Borders**:
- Radius: 6-8px (rounded corners)
- Width: 1px
- Color: #2a2a2a, #333333

---

## 🔄 Responsive Behavior

### Window Sizing
- **Default**: Maximized (1920×1080)
- **Minimum**: Not enforced (assumes 1080p)
- **Resize**: Grid percentages maintain proportions

### Component Growth Rules

**NEVER Grow Vertically**:
- Live Monitoring Panel
- Risk Overview Panel
- All stat cards
- Risk chart

**ALWAYS Grow (within container)**:
- Timeline table (Priority.ALWAYS)

**Controlled Growth**:
- Explanation panel (max 300px scroll)

---

## 📊 Data Flow

### Controller → View Updates

**DashboardControllerV2** updates every 1 second:

1. **System State Badge**
   - Reads: `backendBridge.getCurrentSystemState()`
   - Updates: Badge text and color

2. **Live Monitoring Stats**
   - Reads: `getTotalPacketsAnalyzed()`, `getCurrentPacketRate()`, etc.
   - Updates: Stat card labels

3. **Risk Overview**
   - Reads: `getCurrentRiskScore()`
   - Updates: Progress bar, percentage, chart

4. **Timeline Table**
   - Reads: `getRecentSecurityEvents(100)`
   - Updates: Observable list (auto-updates table)

5. **Explanation Panel**
   - Reads: Selected event from table
   - Updates: All explanation sections

---

## 🧪 Validation Checklist

After implementation, verify:

- ☑ **No scrolling required** on 1920×1080 screen
- ☑ **Timeline clearly readable** with all columns visible
- ☑ **Explanation readable** when expanded
- ☑ **Charts not oversized** - fit within allocated space
- ☑ **No clipping** of any components
- ☑ **Window resize behaves properly** - grid maintains proportions
- ☑ **Professional appearance** - SOC-level quality
- ☑ **Proper alignment** - all edges align to 20px grid
- ☑ **Equal spacing** between cards
- ☑ **State badge updates** correctly with color changes
- ☑ **Buttons functional** - monitoring toggle works
- ☑ **Table updates** with real-time data
- ☑ **Expert mode toggle** changes styling

---

## 🚀 Running the Dashboard

### Option 1: Direct Launcher (Recommended for Testing)

```bash
mvn exec:java -Dexec.mainClass="com.threatscope.ui.DashboardV2Launcher"
```

Or from IntelliJ:
1. Open `DashboardV2Launcher.java`
2. Right-click → Run 'DashboardV2Launcher.main()'

### Option 2: Full Application Flow

```bash
mvn exec:java -Dexec.mainClass="com.threatscope.ui.MainApp"
```

Then login and navigate to dashboard.

---

## 📁 File Structure

```
src/main/java/com/threatscope/ui/
├── view/
│   └── DashboardViewV2.java          # New professional layout
├── controller/
│   └── DashboardControllerV2.java    # Controller with real-time updates
├── model/
│   └── UiSecurityEvent.java          # Enhanced with helper methods
└── DashboardV2Launcher.java          # Test launcher
```

---

## 🎯 Design Philosophy

This dashboard follows **SOC-level professional standards**:

1. **Information Density**: Maximum relevant data, minimal waste
2. **Visual Hierarchy**: Important info (state, timeline) dominates
3. **No Scrolling**: Everything fits on 1080p without vertical scroll
4. **Responsive**: Grid layout adapts to window size
5. **Clean**: No excessive padding, margins, or empty space
6. **Professional**: Dark theme, consistent spacing, proper typography
7. **Functional**: Real-time updates, interactive elements, clear actions

**Inspiration**: CrowdStrike Falcon, SentinelOne Console, Splunk Security

---

## 🔧 Customization Points

### To Adjust Layout Proportions:

**In `DashboardViewV2.createMainGrid()`**:

```java
// Column widths (currently 65/35)
col1.setPercentWidth(65);  // Left column
col2.setPercentWidth(35);  // Right column

// Row heights (currently 45/55)
row1.setPercentHeight(45);  // Top row
row2.setPercentHeight(55);  // Bottom row
```

### To Change Component Heights:

```java
// Header
header.setPrefHeight(70);  // Adjust header height

// Control bar
controlBar.setPrefHeight(80);  // Adjust control bar height

// Timeline table
timelineTable.setPrefHeight(300);  // Adjust table height

// Stat cards
card.setMaxHeight(90);  // Adjust stat card height
```

### To Modify Colors:

See the **Color Palette** section above and update inline styles.

---

## ✅ Success Criteria

The dashboard is considered **production-ready** when:

1. ✓ Fits perfectly on 1920×1080 without scrolling
2. ✓ All data clearly visible and readable
3. ✓ Professional SOC-level appearance
4. ✓ Real-time updates working smoothly
5. ✓ No layout bugs or clipping
6. ✓ Responsive to window resizing
7. ✓ Clean, consistent styling throughout
8. ✓ Functional monitoring controls
9. ✓ Timeline table populated with events
10. ✓ Explanation panel shows event details

---

## 📝 Notes

- This is a **structural redesign**, not cosmetic tweaking
- Layout uses **GridPane** for precise 2×2 control
- **VBox.setVgrow(Priority.NEVER)** prevents unwanted expansion
- **Fixed heights** on header/footer ensure stability
- **Percentage-based grid** maintains proportions on resize
- **Dark theme** (#0f0f0f base) for professional SOC look
- **Compact design** maximizes screen real estate

---

**Last Updated**: 2026-02-13  
**Version**: 2.0 Final  
**Status**: ✅ Implementation Complete
