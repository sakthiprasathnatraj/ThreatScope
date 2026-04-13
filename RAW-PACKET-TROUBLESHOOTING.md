# Raw Packet Capture - Troubleshooting Guide

## Current Status
You've enabled Expert Mode but the Raw Packet Activity table is empty.

## Diagnostic Steps

### Step 1: Check if Monitoring is Running
Look at the button - it should say:
- ✅ **"Disable Monitoring"** (monitoring is ON)
- ❌ **"Enable Monitoring"** (monitoring is OFF)

**If it says "Enable Monitoring"**, click it to start packet capture!

### Step 2: Check Console Output
After clicking "Enable Monitoring", look for these messages in the console:

#### Expected Messages:
```
🚀 Starting packet capture on interface 0
Monitoring: [Your Network Interface Name]
📦 Raw packet #1 sent to UI: 192.168.1.100:54321 → 8.8.8.8:443 [TCP]
📦 Raw packet #2 sent to UI: 8.8.8.8:443 → 192.168.1.100:54321 [TCP]
...
```

#### Possible Error Messages:
```
❌ No network interfaces found.
   → Solution: Install npcap and run as administrator

❌ Pcap error: Access is denied
   → Solution: Run IntelliJ as administrator

⚠️ UI classes not found - running in console-only mode
   → Solution: Rebuild the project (Ctrl+F9)

❌ Error sending packet to UI: [error message]
   → Solution: Check the error and report it
```

### Step 3: Generate Network Traffic
If monitoring is running but no packets appear:

1. **Open a web browser** and visit a website
2. **Ping a server**: Open CMD and run `ping 8.8.8.8`
3. **Click "Generate Test Events"** button

### Step 4: Verify Expert Mode is Enabled
- The "Expert Mode" button should be **blue/highlighted**
- The "Raw Packet Activity" section should be **visible**
- If not visible, click the "Expert Mode" button again

## Quick Fix Checklist

### ✅ Before Running:
1. [ ] Rebuild project in IntelliJ (Ctrl+F9 or Build → Build Project)
2. [ ] Run IntelliJ as Administrator (required for npcap)
3. [ ] Verify npcap is installed

### ✅ After Starting Application:
1. [ ] Click "Enable Monitoring" button
2. [ ] Wait 2-3 seconds
3. [ ] Click "Expert Mode" toggle
4. [ ] Generate network traffic (browse web, ping)
5. [ ] Check console for debug messages

## Common Issues

### Issue 1: "Enable Monitoring" button doesn't work
**Symptoms**: Button doesn't change to "Disable Monitoring"
**Solutions**:
- Run as administrator
- Check if npcap is installed
- Look for error messages in console

### Issue 2: Monitoring works but table is empty
**Symptoms**: Button says "Disable Monitoring" but table shows placeholder
**Solutions**:
- Check console for "📦 Raw packet" messages
- If you see the messages, it's a UI binding issue
- If you don't see messages, packets aren't being captured

### Issue 3: Console shows errors
**Symptoms**: Red error messages in console
**Solutions**:
- **"ClassNotFoundException"**: Rebuild project
- **"Access denied"**: Run as administrator
- **"No interfaces found"**: Install npcap

## Debug Commands

### Check if npcap is installed:
```powershell
Get-Service npcap
```
Should show "Running" status

### List network interfaces:
```powershell
ipconfig /all
```
Should show your active network adapters

### Test packet capture (from project root):
```powershell
mvn clean compile
mvn exec:java -Dexec.mainClass="com.threatscope.ui.DashboardV2Launcher"
```

## Expected Behavior

### When Working Correctly:
1. Click "Enable Monitoring"
   - Button changes to "Disable Monitoring"
   - Console shows: "🚀 Starting packet capture on interface 0"
   
2. Generate traffic (browse web)
   - Console shows: "📦 Raw packet #1 sent to UI..."
   - Console shows: "📦 Raw packet #2 sent to UI..."
   
3. Click "Expert Mode"
   - Raw Packet Activity section appears
   - Table fills with packet data in real-time
   
4. Table shows:
   - Timestamps with milliseconds
   - Source and destination IPs
   - Port numbers
   - Protocol (TCP/UDP/ICMP)
   - Packet sizes
   - TCP flags (SYN, ACK, etc.)
   - Hex payload preview

## Still Not Working?

### Collect Diagnostic Information:
1. **Console output** - Copy all messages
2. **Button states** - Screenshot of the dashboard
3. **Network interfaces** - Run `ipconfig /all`
4. **Npcap status** - Run `Get-Service npcap`

### Then check:
- Is monitoring actually enabled? (button text)
- Are packets being captured? (console messages)
- Is Expert Mode enabled? (button highlighted)
- Is the table visible? (scroll down)

## Next Steps

1. **Rebuild the project** (Ctrl+F9)
2. **Run as administrator**
3. **Click "Enable Monitoring"**
4. **Watch the console** for debug messages
5. **Click "Expert Mode"**
6. **Generate network traffic**

The debug messages I added will tell us exactly where the problem is!
