package com.ticketsystem;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TicketAnalytics {
    private final ConcurrentHashMap<String, AtomicInteger> vendorStats;
    private final ConcurrentHashMap<String, AtomicInteger> customerStats;
    private final ConcurrentHashMap<LocalDateTime, Integer> timeSeriesData;
    private final ReentrantReadWriteLock lock;
    private int peakTicketCount;
    private LocalDateTime peakTime;
    private final int updateIntervalSeconds;

    public TicketAnalytics(int updateIntervalSeconds) {
        this.vendorStats = new ConcurrentHashMap<>();
        this.customerStats = new ConcurrentHashMap<>();
        this.timeSeriesData = new ConcurrentHashMap<>();
        this.lock = new ReentrantReadWriteLock();
        this.peakTicketCount = 0;
        this.updateIntervalSeconds = updateIntervalSeconds;
    }

    public void recordVendorActivity(String vendorName, int ticketsAdded) {
        vendorStats.computeIfAbsent(vendorName, k -> new AtomicInteger(0))
                  .addAndGet(ticketsAdded);
    }

    public void recordCustomerPurchase(String customerName) {
        customerStats.computeIfAbsent(customerName, k -> new AtomicInteger(0))
                    .incrementAndGet();
    }

    public void updateTimeSeriesData(int currentTicketCount) {
        LocalDateTime now = LocalDateTime.now();
        timeSeriesData.put(now, currentTicketCount);

        lock.writeLock().lock();
        try {
            if (currentTicketCount > peakTicketCount) {
                peakTicketCount = currentTicketCount;
                peakTime = now;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Map<String, Integer> getVendorStats() {
        Map<String, Integer> stats = new HashMap<>();
        vendorStats.forEach((key, value) -> stats.put(key, value.get()));
        return stats;
    }

    public Map<String, Integer> getCustomerStats() {
        Map<String, Integer> stats = new HashMap<>();
        customerStats.forEach((key, value) -> stats.put(key, value.get()));
        return stats;
    }

    public List<Map.Entry<LocalDateTime, Integer>> getTimeSeriesData() {
        return new ArrayList<>(timeSeriesData.entrySet());
    }

    public Map<String, Object> getPeakStats() {
        lock.readLock().lock();
        try {
            Map<String, Object> peakStats = new HashMap<>();
            peakStats.put("count", peakTicketCount);
            peakStats.put("time", peakTime);
            return peakStats;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void clearStats() {
        vendorStats.clear();
        customerStats.clear();
        timeSeriesData.clear();
        lock.writeLock().lock();
        try {
            peakTicketCount = 0;
            peakTime = null;
        } finally {
            lock.writeLock().unlock();
        }
    }
} 