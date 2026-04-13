package com.threatscope.core.detect;

import java.util.*;

public class TrafficStats {

    public static final Map<String, List<Long>> packetTimes = new HashMap<>();
    public static final Map<String, Set<Integer>> ports = new HashMap<>();
}
