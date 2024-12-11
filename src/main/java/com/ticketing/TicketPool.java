package main.java.com.ticketing;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPool {
    private final AtomicInteger availableTickets;
    private final int maxTicketCapacity;
    private final ReentrantLock lock;

    public TicketPool(int maxCapacity) {
        this.maxTicketCapacity = maxCapacity;
        this.availableTickets = new AtomicInteger(0);
        this.lock = new ReentrantLock();
    }

    public boolean addTickets(int count) {
        lock.lock();
        try {
            int current = availableTickets.get();
            if (current + count <= maxTicketCapacity) {
                availableTickets.addAndGet(count);
                System.out.println("Added " + count + " tickets. Total available: " + availableTickets.get());
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public boolean removeTicket() {
        lock.lock();
        try {
            if (availableTickets.get() > 0) {
                availableTickets.decrementAndGet();
                System.out.println("Ticket purchased. Remaining tickets: " + availableTickets.get());
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public int getAvailableTickets() {
        return availableTickets.get();
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }
} 