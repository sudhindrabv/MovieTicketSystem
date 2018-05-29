package com.walmart.ticketsystem.utils;

import java.util.UUID;

public class ConfirmationCodeGenerator {

	public synchronized static String generateId() {
		return String.valueOf(UUID.randomUUID());
	}
}
