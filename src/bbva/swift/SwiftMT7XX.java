package bbva.swift;

import java.util.HashMap;

public class SwiftMT7XX {
	
	private MT7TYPE type;
	private String fileName; 
	private HashMap<String, String> fields;	
	
	public SwiftMT7XX(MT7TYPE type, String fileName, HashMap<String, String> fields) {
		this.type = type;			
		this.fields = fields;
		this.fileName = fileName;
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
			

}
