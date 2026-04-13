# 🔧 QUICK FIX - List All Network Interfaces

## What I Just Did

I updated `InterfaceSelector.java` to:
- ❌ **Exclude Bluetooth** adapters (like "Bluetooth Device (Personal Area Network)")
- ❌ **Exclude VMware/VirtualBox** adapters
- ✅ **Prioritize Ethernet/Wi-Fi** adapters (Realtek, Intel, Broadcom, etc.)

## Next Step: Find Your Real Network Adapter

### Option 1: Run Interface Lister (RECOMMENDED)

1. In IntelliJ, open: `src/main/java/com/threatscope/core/capture/InterfaceSelector.java`
2. Right-click anywhere in the file
3. Select **"Run 'InterfaceSelector.main()'"**
4. Check the console output

This will show you ALL available network interfaces and which one will be selected.

### Option 2: Rebuild and Restart

1. **Rebuild**: Press **Ctrl+F9**
2. **Restart** the application
3. **Click "Enable Monitoring"**
4. **Check console** to see which interface is selected

## What You Should See

After running InterfaceSelector.main(), you'll see something like:

```
=== Available Network Interfaces ===

[0] WAN Miniport (Network Monitor)
    Description: WAN Miniport (Network Monitor)
    ⚠️  Virtual/Loopback interface - may not capture real traffic

[1] WAN Miniport (IP)
    Description: WAN Miniport (IP)
    ⚠️  Virtual/Loopback interface - may not capture real traffic

[2] WAN Miniport (IPv6)
    Description: WAN Miniport (IPv6)
    ⚠️  Virtual/Loopback interface - may not capture real traffic

[3] Bluetooth Device (Personal Area Network)
    Description: Bluetooth Device (Personal Area Network)
    ⚠️  Virtual/Loopback interface - may not capture real traffic

[4] Realtek PCIe GBE Family Controller
    Description: Realtek PCIe GBE Family Controller
    Addresses: [192.168.1.100/24]
    ✅ RECOMMENDED - This looks like a real network interface

[5] Wi-Fi Adapter
    Description: Intel(R) Wi-Fi 6 AX201 160MHz
    Addresses: [192.168.1.101/24]
    ✅ RECOMMENDED - This looks like a real network interface

====================================

✅ Auto-selected interface [4]: Realtek PCIe GBE Family Controller
```

## Expected Result

The selector should now skip:
- ❌ WAN Miniport interfaces
- ❌ Bluetooth adapters
- ❌ Virtual adapters

And select:
- ✅ Ethernet adapter (Realtek, Intel, Broadcom, etc.)
- ✅ Wi-Fi adapter (if Ethernet not available)

## If It Still Selects Bluetooth

If it still selects Bluetooth, that means you might not have an active Ethernet or Wi-Fi connection. In that case:

1. **Check your network connection**:
   - Are you connected to the internet?
   - Via Ethernet cable or Wi-Fi?

2. **Run this in CMD**:
   ```cmd
   ipconfig /all
   ```
   
3. **Look for**:
   - "Ethernet adapter"
   - "Wireless LAN adapter Wi-Fi"
   - Check which one has an IP address (192.168.x.x or similar)

4. **Tell me**:
   - Which adapter shows an IP address?
   - What's the adapter name?

## Quick Test

**Please run InterfaceSelector.main() and paste the console output here!**

This will show me exactly which interfaces are available on your system, and I can tell you which one should be selected.

---

**After you run the test, we'll know exactly which interface to use!** 🎯
