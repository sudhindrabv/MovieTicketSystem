package com.walmart.ticketsystem.service;

import com.walmart.ticketsystem.exception.EmailValidationException;

/**
 * This interface added to declare all the user validations
 * @author Sudhindra
 *
 */
public interface ValidationService {

	public boolean validateCustomerEmaildId(String customerEmail) throws EmailValidationException;

}
