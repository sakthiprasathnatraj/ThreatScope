# How to Restart Dashboard with New Changes

## ⚠️ IMPORTANT: You Must Restart!

The dashboard you're seeing is the **OLD VERSION** running from old compiled code. You need to **STOP and RESTART** to see the new scrollable layout.

---

## 🛑 Step 1: Stop the Running Application

### **In IntelliJ:**
1. Look at the bottom of IntelliJ window
2. Find the "Run" panel (should be open)
3. Click the **RED STOP BUTTON** (■) to stop the application
4. Wait for it to fully stop

### **Or Close the Window:**
- Simply close the ThreatScope window (X button)
- This will stop the application

---

## 🔄 Step 2: Rebuild the Project

### **In IntelliJ:**
1. Go to **Build** menu → **Rebuild Project**
2. Wait for compilation to complete
3. Check for any errors in the "Build" panel

### **Or from Command Line:**
```bash
cd d:\Sakthi\Java\ThreatScope
mvn clean compile
```

---

## 🚀 Step 3: Run the New Version

### **In IntelliJ:**
1. Open `src/main/java/com/threatscope/ui/DashboardV2Launcher.java`
2. Right-click on the file
3. Select **"Run 'DashboardV2Launcher.main()'"**
4. Wait for the window to open

---

## ✅ How to Know It's the New Version

### **Old Version (What You're Seeing Now):**
- ❌ Window is cramped
- ❌ Everything stacked vertically with no space
- ❌ No scrollbar visible
- ❌ Content cut off at bottom
- ❌ Window size: smaller

### **New Version (What You Should See):**
- ✅ Window is **1600×1000** (larger)
- ✅ Sections have **proper spacing** (25px gaps)
- ✅ **Scrollbar appears** on the right side
- ✅ **Larger fonts** (18px titles, 32px stat values)
- ✅ **Stat cards are 120px tall** (not cramped)
- ✅ Can **scroll down** to see all sections
- ✅ All text is **clearly readable**

---

## 🎯 Quick Test

After restarting, try this:

1. **Check window size** - Should be noticeably larger (1600×1000)
2. **Look for scrollbar** - Should appear on the right side
3. **Scroll down** - Use mouse wheel to scroll
4. **Check spacing** - Sections should have generous gaps between them
5. **Check fonts** - Stat numbers should be large (32px)

---

## 🐛 If Still Having Issues

### **Clear IntelliJ Cache:**
1. **File** → **Invalidate Caches**
2. Select **"Invalidate and Restart"**
3. Wait for IntelliJ to restart
4. Run the dashboard again

### **Clean Rebuild:**
```bash
cd d:\Sakthi\Java\ThreatScope
mvn clean
mvn compile
```

Then run from IntelliJ again.

---

## 📊 What the New Layout Looks Like

```
┌─────────────────────────────────────────────┐
│ HEADER (70px) - Logo | Badge | User        │ ← Fixed
├─────────────────────────────────────────────┤
│ ╔═══════════════════════════════════════╗  │
│ ║ SCROLLABLE CONTENT                    ║  │
│ ║                                       ║  │
│ ║ [Stat] [Stat] [Stat] [Stat]          ║  │ ← 120px tall
│ ║ ↕ 25px gap                            ║  │
│ ║ System Status                         ║  │ ← 100px
│ ║ ↕ 25px gap                            ║  │
│ ║ Risk Progress Bar                     ║  │ ← 40px
│ ║ Risk Chart                            ║  │ ← 250px
│ ║ ↕ 25px gap                            ║  │
│ ║ Timeline Table                        ║  │ ← 400px
│ ║ ↕ 25px gap                            ║  │
│ ║ Explanation (7 sections)              ║  │
│ ║                                       ║  │
│ ╚═══════════════════════════════════════╝  │
│ ↕ SCROLL HERE ↕                             │ ← Scrollbar
├─────────────────────────────────────────────┤
│ CONTROL BAR (80px) - Buttons            │ ← Fixed
└─────────────────────────────────────────────┘
```

---

## 🎨 Visual Differences

| Feature | Old (Current) | New (After Restart) |
|---------|---------------|---------------------|
| Window Size | 1400×900 | **1600×1000** |
| Stat Cards | Cramped | **120px tall** |
| Stat Values | Small | **32px bold** |
| Section Titles | Missing | **18px bold** |
| Spacing | Tight | **25px gaps** |
| Scrollbar | Hidden | **Visible on right** |
| Scrolling | Broken | **Smooth scrolling** |

---

## ⚡ Quick Restart Steps

1. **STOP** the running app (red stop button)
2. **BUILD** → Rebuild Project
3. **RUN** DashboardV2Launcher.java
4. **CHECK** - Window should be larger with scrollbar

---

**The changes are in the code, but you're running the OLD compiled version. You MUST restart to see the new layout!**
