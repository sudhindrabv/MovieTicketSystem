package com.walmart.ticketsystem.service;

import com.walmart.ticketsystem.exception.EmailValidationException;

public interface ValidationService {
	
	public boolean validateCustomerEmaildId(String customerEmail) throws EmailValidationException;
	
	
}
