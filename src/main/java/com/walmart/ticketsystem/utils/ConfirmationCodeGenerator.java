package com.walmart.ticketsystem.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class ConfirmationCodeGenerator {
	private static AtomicInteger idCounter = new AtomicInteger(1);

    public synchronized static String generateId() {
        return String.valueOf(idCounter.getAndIncrement()); 
    }
}
