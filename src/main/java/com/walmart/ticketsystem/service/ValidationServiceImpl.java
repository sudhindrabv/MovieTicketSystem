package com.walmart.ticketsystem.service;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.walmart.ticketsystem.exception.EmailValidationException;

public class ValidationServiceImpl implements ValidationService {
	private static ValidationService validationService;
	
	// static method to create instance of Singleton ValidationService class
	public static ValidationService getValidationServiceInstance() {
		if (validationService == null) {
			synchronized (ValidationService.class) {
				if (validationService == null) {
					validationService = new ValidationServiceImpl();
				}
			}
		}
		return validationService;
	}
	
	@Override
	public boolean validateCustomerEmaildId(String customerEmail) throws EmailValidationException {
		Pattern EMAIL_PATTERN = Pattern.compile(
				"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		boolean result = false;
		String msg = "";
		if (StringUtils.isEmpty(customerEmail)) {
			msg = "Customer Email value is required";
		} else if (!EMAIL_PATTERN.matcher(customerEmail).matches()) {
			msg = "Customer Email is not valid";
		} else {
			result = true;
		}
		if (!result) {
			throw new EmailValidationException(msg);
		}
		return true;
	}

}
