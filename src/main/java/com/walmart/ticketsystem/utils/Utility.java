package com.walmart.ticketsystem.utils;

import java.util.Scanner;

public class Utility {
	public static boolean isValidNumber(String no) {
		if(no == null){
			return false;
		}
		try {
			Integer.parseInt(no);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	public static boolean isValidRowAndColumn(Scanner sc, String rows, String clsPerRow) {
		boolean isvalidRow = isValidNumber(rows);
		if(!isvalidRow){
			while(!isvalidRow){
				System.out.println("Invalid Row.");
				System.out.println("Enter valid number:");
				rows = sc.next();
				isvalidRow = isValidNumber(rows);
			}
		}
		
		boolean isValidColsPerRow = isValidNumber(clsPerRow);
		if(!isValidColsPerRow){
			while(!isValidColsPerRow){
				System.out.println("Enter valid number of seats per row.");
				clsPerRow = sc.next();
				isValidColsPerRow = isValidNumber(clsPerRow);
			}
		}
		if(isvalidRow && isValidColsPerRow) {
			return true;
		}
		return false;
	}
}
