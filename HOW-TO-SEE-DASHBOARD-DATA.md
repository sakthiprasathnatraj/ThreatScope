# 🎯 How to See Data in the Dashboard

## ✅ Quick Fix Added!

I've added a **"Generate Test Events"** button to populate the dashboard with sample data.

---

## 🚀 How to Use

### Step 1: Run the Application
```
1. Open ThreatScope in IntelliJ
2. Run MainApp.java
3. Login with: admin / admin
```

### Step 2: Generate Test Events
```
1. Look for the "Generate Test Events" button
   (It's below the "Enable Monitoring" button)
2. Click it once
3. Watch the dashboard populate with data!
```

---

## 📊 What You'll See

After clicking "Generate Test Events", **4 sample security events** will appear:

### Event Timeline Table
- ✅ 4 rows with different threat types
- ✅ Risk scores: 10, 25, 45, 55
- ✅ Classifications: TRUSTED, BENIGN_NOISE, SUSPICIOUS
- ✅ Timestamps showing when events occurred

### Raw Activity Table (Expert Mode)
- ✅ Same 4 events with detailed columns
- ✅ Source IPs: 198.20.69.42, 203.45.12.88, 192.168.1.100, 45.142.212.61
- ✅ Threat Type: PORT_SCAN
- ✅ Confidence levels: LOW, MEDIUM, HIGH

### Click Any Row
- ✅ Expands to show full details
- ✅ Displays: WHAT HAPPENED, WHY THIS MATTERS, RECOMMENDED ACTION, REASSURANCE

---

## 🎨 Sample Events Generated

### Event 1: BENIGN_NOISE (Low Risk)
- **Source IP:** 198.20.69.42
- **Risk:** 25/100
- **Classification:** BENIGN_NOISE
- **Explanation:** "An external computer attempted to connect to 12 different services. This appears to be automated internet scanning, which is very common."
- **Action:** "No action needed. We are monitoring the situation."

### Event 2: SUSPICIOUS (Medium Risk)
- **Source IP:** 203.45.12.88
- **Risk:** 45/100
- **Classification:** SUSPICIOUS
- **Explanation:** "Multiple connection attempts detected from this IP address. Pattern suggests reconnaissance activity."
- **Action:** "Continue monitoring. Consider blocking if activity escalates."

### Event 3: TRUSTED (Very Low Risk)
- **Source IP:** 192.168.1.100
- **Risk:** 10/100
- **Classification:** TRUSTED
- **Explanation:** "Internal network scan from known device. This is normal administrative activity."
- **Action:** "No action required. This is expected behavior."

### Event 4: SUSPICIOUS (Higher Risk)
- **Source IP:** 45.142.212.61
- **Risk:** 55/100
- **Classification:** SUSPICIOUS
- **Explanation:** "Aggressive port scanning detected. Multiple services targeted in rapid succession."
- **Action:** "Review firewall rules. Consider temporary IP blocking."

---

## 🔄 Generate More Events

You can click "Generate Test Events" multiple times to add more sample data. Each click adds 4 new events to the tables.

---

## 🎯 Testing Expert Mode

1. **Start in Simple Mode** (default)
   - Event Timeline is visible
   - Raw Activity is hidden

2. **Toggle to Expert Mode**
   - Click the "Expert Mode" toggle button
   - Raw Activity table appears below Event Timeline
   - Both tables show the same events with different columns

3. **Toggle back to Simple Mode**
   - Raw Activity table disappears
   - Event Timeline remains visible

---

## 📝 What This Demonstrates

### Live Activity Indicators
- ✅ Packets Analyzed (updates when monitoring enabled)
- ✅ Packets/sec (live rate)
- ✅ Active Connections
- ✅ Last Packet time

### Heartbeat Messages
- ✅ Rotates every 10 seconds
- ✅ Shows calm system status

### Session Context
- ✅ Admin user
- ✅ Security Analyst role
- ✅ Active session status

### Event Timeline
- ✅ Populated with 4 diverse events
- ✅ Click to expand details
- ✅ Shows full explanations

### Raw Activity (Expert Mode)
- ✅ Same events with technical details
- ✅ Source IPs, risk scores, confidence levels
- ✅ Professional table layout

### Quick Status
- ✅ Updates based on system state
- ✅ Calm, reassuring messages

---

## 🎉 Result

After clicking "Generate Test Events", your dashboard will look **exactly like a real SOC monitoring interface** with:

- ✅ Populated event tables
- ✅ Diverse risk levels (10, 25, 45, 55)
- ✅ Different classifications (TRUSTED, BENIGN_NOISE, SUSPICIOUS)
- ✅ Expandable event details
- ✅ Expert mode with raw data
- ✅ Professional appearance

---

## 🔧 To Remove Test Button Later

Once you're satisfied with the dashboard, you can hide the test button by:

1. Open `DashboardViewProfessional.java`
2. Find the `createMonitoringControls()` method
3. Comment out or remove the test button code
4. Recompile and run

Or simply leave it for demonstration purposes!

---

**Now restart the app and click "Generate Test Events" to see the dashboard come alive!** 🚀
