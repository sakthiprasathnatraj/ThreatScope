# Fix: JavaFX Runtime Components Missing

## ❌ Error You're Seeing
```
Error: JavaFX runtime components are missing, and are required to run this application
```

This happens because IntelliJ doesn't automatically add JavaFX modules when running directly.

---

## ✅ Solution: Use Maven to Run

Instead of clicking "Run" directly in IntelliJ, use Maven which handles JavaFX properly.

### **Option 1: Run with Maven (RECOMMENDED)**

1. Open **Terminal** in IntelliJ (bottom panel)
2. Run this command:

```bash
mvn clean javafx:run
```

This will:
- Clean old compiled files
- Compile the project
- Run with JavaFX properly configured

---

### **Option 2: Add VM Options to IntelliJ Run Configuration**

If you want to run directly from IntelliJ:

1. **Click** the dropdown next to the Run button (top right)
2. Select **"Edit Configurations..."**
3. Find **"DashboardV2Launcher"** in the list
4. In **"VM options"** field, add:

```
--module-path "C:\Users\YOUR_USERNAME\.m2\repository\org\openjfx" --add-modules javafx.controls,javafx.graphics,javafx.base
```

**Replace `YOUR_USERNAME`** with your actual Windows username.

5. Click **"Apply"** then **"OK"**
6. Now you can click Run normally

---

### **Option 3: Run from Command Line (EASIEST)**

Open Command Prompt or PowerShell:

```bash
cd d:\Sakthi\Java\ThreatScope
mvn clean javafx:run
```

This is the simplest and most reliable method.

---

## 🎯 Recommended Approach

**Use Maven to run:**

```bash
# In IntelliJ Terminal (bottom panel):
mvn clean javafx:run
```

**Why?**
- ✅ Automatically handles JavaFX modules
- ✅ No VM options needed
- ✅ Always uses latest compiled code
- ✅ Works every time

---

## 📝 Step-by-Step (Easiest Method)

1. **Stop** any running ThreatScope windows
2. In **IntelliJ**, click **Terminal** tab (bottom)
3. Type: `mvn clean javafx:run`
4. Press **Enter**
5. Wait for compilation and launch
6. Dashboard window will open with new layout!

---

## ⚠️ Important Notes

- **Don't use** the green Run button in IntelliJ (causes JavaFX error)
- **DO use** Maven command: `mvn clean javafx:run`
- This ensures JavaFX modules are loaded correctly

---

## 🚀 Quick Commands

### **Run Dashboard:**
```bash
mvn clean javafx:run
```

### **Just Compile:**
```bash
mvn clean compile
```

### **Run Console Mode (No UI):**
```bash
mvn exec:java -Dexec.mainClass="com.threatscope.Main"
```

---

## ✅ After Running with Maven

You should see:
1. Compilation messages
2. "Building jar: ..." message
3. Dashboard window opens
4. **Larger window** (1600×1000)
5. **Scrollbar** on the right
6. **Proper spacing** between sections

---

**TL;DR: Use `mvn clean javafx:run` instead of IntelliJ's Run button!**
