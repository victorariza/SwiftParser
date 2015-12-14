package bbva.swift;

import java.util.HashMap;

public class SwiftMT7XX {
	
	private MT7TYPE type;
	private String inputOutput;
	private String fileName; 
	private String sellerBank;
	private String buyerBank;
	private HashMap<String, String> fields;	
	
	public SwiftMT7XX(MT7TYPE type, String inputOutput, String fileName, HashMap<String, String> fields, String buyerBank, String sellerBank) {
		this.type = type;			
		this.inputOutput = inputOutput;
		this.fields = fields;
		this.fileName = fileName;
		this.sellerBank = sellerBank;
		this.buyerBank = buyerBank;
	}
	
	public SwiftMT7XX(MT7TYPE type) {
		this.type = type;			
	}
	
	public MT7TYPE getType() {
		return type;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String getFieldValue(String field){
		return fields.get(field);
	}
		
	public String getApplicant() {
		return fields.get("50");
	}
	
	public String getBeneficiary() {
		return fields.get("59");
	}	

	public String getCreditNumber() {
		return fields.get("20");
	}

	public String getSellerBank() {
		return sellerBank;
	}

	public String getBuyerBank() {
		return buyerBank;
	}

	public String getInputOutput() {
		return inputOutput;
	}

	public void setInputOutput(String inputOutput) {
		this.inputOutput = inputOutput;
	}
	
			

}
