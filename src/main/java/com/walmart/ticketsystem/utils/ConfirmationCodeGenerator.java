package com.walmart.ticketsystem.utils;

import java.util.UUID;

/**
 * Class used to handle ConfirmationCodeGenerator related functionalities
 * @author Sudhindra
 *
 */
public class ConfirmationCodeGenerator {

	public synchronized static String generateId() {
		return String.valueOf(UUID.randomUUID());
	}
}
