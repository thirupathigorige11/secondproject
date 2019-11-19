package com.sumadhura.bean;

import java.util.List;

public class WDRateAnalysis {
	private String workDescription;
	private String wdMaterialUOM;
	private double wdMaterialAmountPerUnit;
	private List<MaterialDetails> materialsList;
	
	
	
	public String getWorkDescription() {
		return workDescription;
	}
	public void setWorkDescription(String workDescription) {
		this.workDescription = workDescription;
	}
	public String getWdMaterialUOM() {
		return wdMaterialUOM;
	}
	public void setWdMaterialUOM(String wdMaterialUOM) {
		this.wdMaterialUOM = wdMaterialUOM;
	}
	
	public double getWdMaterialAmountPerUnit() {
		return wdMaterialAmountPerUnit;
	}
	public void setWdMaterialAmountPerUnit(double wdMaterialAmountPerUnit) {
		this.wdMaterialAmountPerUnit = wdMaterialAmountPerUnit;
	}
	public List<MaterialDetails> getMaterialsList() {
		return materialsList;
	}
	public void setMaterialsList(List<MaterialDetails> materialsList) {
		this.materialsList = materialsList;
	}
	
    
}
