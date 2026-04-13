# Running ThreatScope from IntelliJ IDEA

**Date:** 2026-02-09  
**Purpose:** Configure IntelliJ to run JavaFX application directly  
**Java Version:** 21 (Liberica JDK 21.0.7)

---

## 🎯 **SETUP INTELLIJ RUN CONFIGURATION**

### **Method 1: Maven Run Configuration (RECOMMENDED)**

This is the easiest and most reliable method!

#### **Step 1: Open Run Configurations**
1. Click **Run** menu → **Edit Configurations...**
2. Or click the dropdown next to the Run button → **Edit Configurations...**

#### **Step 2: Add New Maven Configuration**
1. Click the **+** button (top left)
2. Select **Maven**

#### **Step 3: Configure Maven Run**
Fill in these details:

**Name:** `ThreatScope Enhanced Dashboard`

**Working directory:** `$ProjectFileDir$`
(Or manually: `d:\Sakthi\Java\ThreatScope`)

**Command line:** `javafx:run`

**Optional - Before launch:**
- Click **+** → **Run Maven Goal**
- Goal: `compile`
- This ensures compilation before running

#### **Step 4: Apply and Run**
1. Click **Apply**
2. Click **OK**
3. Select **ThreatScope Enhanced Dashboard** from dropdown
4. Click the green **Run** button (▶)

---

### **Method 2: Application Run Configuration**

This method runs the Java class directly but requires VM options.

#### **Step 1: Open Run Configurations**
1. Click **Run** → **Edit Configurations...**

#### **Step 2: Add New Application Configuration**
1. Click **+** button
2. Select **Application**

#### **Step 3: Configure Application**

**Name:** `ThreatScope JavaFX`

**Main class:** `com.threatscope.ui.MainApp`
- Click **...** button to browse
- Type `MainApp` and select `com.threatscope.ui.MainApp`

**VM options:** (IMPORTANT!)
```
--module-path "C:\Users\YOUR_USERNAME\.m2\repository\org\openjfx" --add-modules javafx.controls,javafx.graphics,javafx.base
```

**Note:** Replace `YOUR_USERNAME` with your actual Windows username

**Or use this simpler approach:**
```
--module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.graphics,javafx.base
```

**Working directory:** `$ProjectFileDir$`

**Use classpath of module:** `ThreatScope`

#### **Step 4: Apply and Run**
1. Click **Apply**
2. Click **OK**
3. Run the configuration

---

### **Method 3: Maven Tool Window (QUICKEST)**

This is the fastest way if you don't want to create a configuration!

#### **Step 1: Open Maven Tool Window**
1. Click **View** → **Tool Windows** → **Maven**
2. Or click the **Maven** tab on the right side

#### **Step 2: Expand Plugins**
1. Expand **ThreatScope**
2. Expand **Plugins**
3. Expand **javafx**

#### **Step 3: Run**
1. **Double-click** `javafx:run`
2. Application will compile and launch!

**To make it easier:**
- Right-click `javafx:run`
- Select **Create 'ThreatScope [javafx:run]'...**
- Click **OK**
- Now it appears in your Run configurations dropdown!

---

## ✅ **RECOMMENDED: Method 3 (Maven Tool Window)**

**This is the easiest!**

1. Open **Maven** tool window (right side)
2. Expand **ThreatScope → Plugins → javafx**
3. Double-click **javafx:run**
4. Done! 🎉

---

## 🔧 **TROUBLESHOOTING**

### **Problem: "Error: JavaFX runtime components are missing"**

**Solution:** You're using Application configuration without VM options.

**Fix:** Use Maven configuration (Method 1 or 3) instead!

---

### **Problem: "Module not found: javafx.controls"**

**Solution:** VM options are incorrect or missing.

**Fix:**
1. Use Maven configuration (recommended)
2. Or add correct VM options to Application configuration

---

### **Problem: "Cannot find main class"**

**Solution:** IntelliJ hasn't indexed the project.

**Fix:**
1. Right-click `pom.xml`
2. Select **Maven → Reload Project**
3. Wait for indexing to complete
4. Try running again

---

### **Problem: Compilation errors in IntelliJ**

**Solution:** Project SDK mismatch.

**Fix:**
1. Press `Ctrl + Alt + Shift + S` (Project Structure)
2. Click **Project**
3. Set **SDK** to `liberica-21`
4. Set **Language level** to `21`
5. Click **Apply** and **OK**

---

## 🎯 **QUICK START GUIDE**

### **For First-Time Setup:**

1. **Reload Maven Project:**
   - Right-click `pom.xml`
   - Select **Maven → Reload Project**
   - Wait for dependencies to download

2. **Open Maven Tool Window:**
   - Click **View → Tool Windows → Maven**

3. **Run Application:**
   - Expand **ThreatScope → Plugins → javafx**
   - Double-click **javafx:run**

4. **Login:**
   - Username: `admin`
   - Password: `admin123`

5. **Enjoy Enhanced Dashboard!** 🎉

---

## 📊 **WHAT EACH METHOD DOES**

### **Method 1: Maven Run Configuration**
- ✅ Handles JavaFX modules automatically
- ✅ Compiles before running
- ✅ Most reliable
- ✅ Reusable configuration

### **Method 2: Application Configuration**
- ⚠️ Requires manual VM options
- ⚠️ More complex setup
- ✅ Faster startup (no Maven overhead)
- ⚠️ Not recommended for JavaFX

### **Method 3: Maven Tool Window**
- ✅ No configuration needed
- ✅ Quick and easy
- ✅ Handles modules automatically
- ✅ **RECOMMENDED!**

---

## 🎨 **AFTER RUNNING**

You'll see:
- ✅ Professional login screen
- ✅ Enhanced dashboard with 3 charts
- ✅ Real-time updates
- ✅ Event statistics
- ✅ SOC-level appearance

---

## 📝 **SUMMARY**

**Easiest Way to Run from IntelliJ:**

1. Open **Maven** tool window (right side)
2. Expand **ThreatScope → Plugins → javafx**
3. Double-click **javafx:run**

**That's it!** No configuration needed! 🚀

---

## 🏆 **BEST PRACTICE**

**Use Maven Tool Window (Method 3):**
- Fastest setup
- No configuration needed
- Always works
- Handles JavaFX modules automatically

**Avoid Application configuration:**
- Requires complex VM options
- Easy to misconfigure
- Not recommended for JavaFX apps

---

**Built by:** Antigravity AI  
**Date:** February 9, 2026  
**Version:** ThreatScope v2.0 - IntelliJ Setup Guide
