# ThreatScope v2.0 - Quick Start Guide

## 🚀 **PHASE 1 & 2 COMPLETE!**

**Status:** Login screen and foundation ready ✅

---

## 📦 **COMPILATION**

### **From Project Root:**

```bash
# Navigate to project root
cd d:\Sakthi\Java\ThreatScope

# Clean and compile
mvn clean compile
```

---

## 🎯 **RUNNING THE APPLICATION**

### **Option 1: Console Mode (Backend Only)**

```bash
mvn exec:java -Dexec.mainClass="com.threatscope.Main"
```

**What you'll see:**
- Network interface selection
- Packet capture monitoring
- Console-based security observations
- Real-time threat detection

---

### **Option 2: JavaFX GUI Mode** ✨

```bash
mvn exec:java -Dexec.mainClass="com.threatscope.ui.MainApp"
```

**What you'll see:**
1. **Professional login screen** (dark SOC-style)
   - Username: `admin`
   - Password: `admin123`
   
2. **Dashboard** (placeholder - full implementation next)
   - Top bar with system state
   - Welcome message
   - Status indicators

---

## 🎨 **WHAT'S BEEN BUILT**

### **✅ Completed (Phase 1 & 2)**

**UI Components:**
- ✅ Professional dark theme CSS
- ✅ Login screen with authentication
- ✅ Smooth fade transitions
- ✅ Dashboard placeholder
- ✅ Top bar with branding

**Architecture:**
- ✅ Clean MVC pattern
- ✅ Dual entry points (Console + GUI)
- ✅ Service layer (AuthService)
- ✅ Controller layer (LoginController)
- ✅ View layer (LoginView, DashboardView)

---

## 📋 **NEXT PHASE (Phase 3)**

### **Full Dashboard Implementation**

Will include:
1. **System Overview Panel**
   - Current network interface
   - Monitoring status
   - Packet rate

2. **Latest Security Observation**
   - Explanation text from ExplanationEngine
   - Risk score display
   - Confidence level

3. **System State Badge**
   - SAFE (green)
   - OBSERVE (blue)
   - WARNING (orange)
   - CRITICAL (red)

4. **Backend Integration**
   - BackendBridge service
   - Connect to OutputGateway
   - Real-time event streaming

---

## 🔧 **REQUIREMENTS**

- ✅ Java 8 (JavaFX bundled)
- ✅ Maven
- ✅ Npcap (for packet capture)
- ⚠️ Admin privileges (for packet capture)

---

## 📁 **PROJECT STRUCTURE**

```
ThreatScope/
├── src/main/java/com/threatscope/
│   ├── Main.java                    ← Console entry point
│   ├── core/                        ← Backend (9 layers)
│   │   ├── capture/
│   │   ├── decode/
│   │   ├── classify/
│   │   ├── detect/
│   │   ├── risk/
│   │   ├── explanation/
│   │   ├── state/
│   │   ├── output/
│   │   └── model/
│   └── ui/                          ← JavaFX Frontend ✨
│       ├── MainApp.java             ← GUI entry point
│       ├── view/
│       │   ├── LoginView.java
│       │   └── DashboardView.java
│       ├── controller/
│       │   └── LoginController.java
│       └── service/
│           └── AuthService.java
└── src/main/resources/
    └── theme/
        └── dark-theme.css           ← Professional theme
```

---

## 🎓 **FOR DEMONSTRATION**

### **Login Screen Features:**
- Professional dark SOC-style design
- Centered card layout
- Inline error messages (no popups)
- Smooth fade transition
- Enter key support

### **Design Philosophy:**
- Calm, non-alarming colors
- Professional security tool appearance
- User-friendly for non-technical users
- Technical depth for experts

---

## ✅ **VERIFICATION CHECKLIST**

Before running:
- [ ] Java 8 installed
- [ ] Maven installed
- [ ] Project compiled successfully
- [ ] Npcap installed (for packet capture)
- [ ] Admin privileges available

---

## 🏆 **STATUS**

**Backend:** ✅ COMPLETE (v2.0 Professional)  
**Frontend Phase 1 & 2:** ✅ COMPLETE  
**Frontend Phase 3:** ⏳ IN PROGRESS  

**Next:** Full dashboard with backend integration

---

**ThreatScope v2.0 - Professional Desktop Security Monitoring**

Built by: Antigravity AI  
Date: February 9, 2026
