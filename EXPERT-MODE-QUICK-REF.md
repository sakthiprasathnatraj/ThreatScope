# Expert Mode - Raw Packet Capture Quick Reference

## ✅ What's Implemented

### New Features
- ✅ **Raw packet data model** (`RawPacketData.java`)
- ✅ **Real-time packet capture** in Expert Mode
- ✅ **TCP flags extraction** (SYN, ACK, FIN, RST, PSH, URG)
- ✅ **Hex payload preview** (first 32 bytes)
- ✅ **Millisecond-precision timestamps**
- ✅ **Automatic UI updates** via JavaFX observable lists
- ✅ **Performance optimization** (500 packet limit)

### Modified Files
1. **NEW**: `src/main/java/com/threatscope/ui/model/RawPacketData.java`
2. **UPDATED**: `src/main/java/com/threatscope/ui/service/BackendBridge.java`
3. **UPDATED**: `src/main/java/com/threatscope/core/capture/PacketSniffer.java`
4. **UPDATED**: `src/main/java/com/threatscope/ui/view/DashboardViewV2.java`
5. **UPDATED**: `src/main/java/com/threatscope/ui/controller/DashboardControllerV2.java`

## 🚀 Quick Start

### 1. Compile
```powershell
cd d:\Sakthi\Java\ThreatScope
mvn clean compile
```

### 2. Run
```powershell
mvn exec:java -Dexec.mainClass="com.threatscope.ui.DashboardV2Launcher"
```

### 3. Enable Monitoring
Click **"Enable Monitoring"** button

### 4. Enable Expert Mode
Click **"Expert Mode"** toggle (bottom right)

### 5. View Raw Packets
Scroll down to see the **"Raw Packet Activity"** table

## 📊 Raw Activity Table Columns

| Column | Description | Example |
|--------|-------------|---------|
| **Timestamp** | Capture time with milliseconds | `13:27:45.123` |
| **Source IP** | Origin IP address | `192.168.1.100` |
| **Dest IP** | Destination IP address | `8.8.8.8` |
| **Src Port** | Source port number | `54321` |
| **Dst Port** | Destination port number | `443` |
| **Protocol** | Network protocol | `TCP`, `UDP`, `ICMP` |
| **Size** | Packet size in bytes | `60 B` |
| **TCP Flags** | TCP control flags | `SYN ACK` |
| **Payload (Hex)** | First 32 bytes in hex | `45 00 00 3C...` |

## 🔍 TCP Flags Reference

| Flag | Meaning | When You'll See It |
|------|---------|-------------------|
| **SYN** | Synchronize | Starting a new connection |
| **ACK** | Acknowledgment | Confirming received data |
| **FIN** | Finish | Closing connection gracefully |
| **RST** | Reset | Abruptly terminating connection |
| **PSH** | Push | Immediate data delivery |
| **URG** | Urgent | Priority data |

### Common Flag Combinations
- `SYN` → New connection request
- `SYN ACK` → Connection accepted
- `ACK` → Data acknowledged
- `PSH ACK` → Data being sent
- `FIN ACK` → Closing connection
- `RST` → Connection reset/refused

## 🎯 Use Cases

### Network Debugging
- See exact packet timing
- Identify connection issues
- Analyze TCP handshakes

### Security Analysis
- Inspect packet payloads
- Detect unusual protocols
- Monitor all network activity

### Learning & Research
- Understand network protocols
- Study packet structures
- Analyze traffic patterns

## ⚠️ Important Notes

### Performance
- Only keeps **last 500 packets** (automatic cleanup)
- Older packets are automatically removed
- No impact on packet capture speed

### Data Displayed
- **Expert Mode shows ALL packets** (including local traffic)
- **Timeline shows only security events** (filtered)
- Both tables update in real-time

### Requirements
- **npcap must be installed**
- **Administrator privileges required**
- **Monitoring must be enabled**

## 🐛 Troubleshooting

### Empty Table?
1. ✅ Click "Enable Monitoring"
2. ✅ Generate network traffic (browse web, ping)
3. ✅ Wait a few seconds

### No TCP Flags?
- Only TCP packets have flags
- UDP/ICMP packets show empty flags column

### Payload Shows Nothing?
- Some packets have no payload (e.g., ACK-only)
- Encrypted payloads show encrypted hex data

## 📝 Example Output

```
Timestamp       Source IP      Dest IP        Src Port  Dst Port  Protocol  Size  TCP Flags  Payload (Hex)
13:27:45.123   192.168.1.100  8.8.8.8        54321     443       TCP       60    SYN        45 00 00 3C 1C 46 40 00 40 06...
13:27:45.125   8.8.8.8        192.168.1.100  443       54321     TCP       60    SYN ACK    45 00 00 3C 00 00 40 00 40 06...
13:27:45.126   192.168.1.100  8.8.8.8        54321     443       TCP       52    ACK        45 00 00 34 1C 47 40 00 40 06...
13:27:45.130   192.168.1.100  8.8.8.8        54321     443       TCP       150   PSH ACK    45 00 00 96 1C 48 40 00 40 06...
```

## 🔄 Data Flow

```
Network Interface (npcap)
    ↓
PacketSniffer.processPacket()
    ↓
PacketSniffer.sendRawPacketToUI()
    ↓
BackendBridge.addRawPacket()
    ↓
Observable List (rawPackets)
    ↓
DashboardViewV2 Raw Activity Table
    ↓
USER sees real-time packets!
```

## 💡 Pro Tips

1. **Use monospace font**: The table already uses Consolas/Courier for hex data
2. **Sort by timestamp**: Click column header to sort
3. **Look for patterns**: Watch for repeated SYN packets (port scans)
4. **Compare with Timeline**: See how raw packets become security events
5. **Analyze payloads**: First 32 bytes often show protocol headers

## 📚 Further Reading

- **TCP/IP Protocol**: Understanding packet structures
- **Wireshark**: Similar tool for packet analysis
- **npcap Documentation**: Low-level packet capture
- **JavaFX TableView**: Understanding observable lists

---

**Ready to use!** Just compile, run, and enable Expert Mode to see raw network packets in real-time.
