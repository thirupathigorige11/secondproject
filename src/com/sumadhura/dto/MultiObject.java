package com.sumadhura.dto;

import java.util.List;
import java.util.Map;

public class MultiObject { //append new ones by increasing Index of same type, but don't change existing ones.
	
	private Map<String, List<String>> mapOfStringAndList1;
	private Map<String, String> mapOfStrings1;
	private String string1;
	private String string2;
	private String string3;
	
	
	public String getString1() {
		return string1;
	}
	public void setString1(String string1) {
		this.string1 = string1;
	}
	public String getString2() {
		return string2;
	}
	public void setString2(String string2) {
		this.string2 = string2;
	}
	public String getString3() {
		return string3;
	}
	public void setString3(String string3) {
		this.string3 = string3;
	}
	public Map<String, List<String>> getMapOfStringAndList1() {
		return mapOfStringAndList1;
	}
	public void setMapOfStringAndList1(Map<String, List<String>> mapOfStringAndList1) {
		this.mapOfStringAndList1 = mapOfStringAndList1;
	}
	public Map<String, String> getMapOfStrings1() {
		return mapOfStrings1;
	}
	public void setMapOfStrings1(Map<String, String> mapOfStrings1) {
		this.mapOfStrings1 = mapOfStrings1;
	}
	
	
}
