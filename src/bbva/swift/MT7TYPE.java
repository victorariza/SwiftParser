package bbva.swift;

public enum MT7TYPE{
	/**
	 * Issue of a Documentary Credit
	 */
	MT700(700),
	/**
	 * Issue of a Documentary Credit
	 */
	MT701(701),
	/**
	 * Pre-Advice of a Documentary Credit
	 */
	MT705(705),
	/**
	 * Amendment to a Documentary Credit
	 */
	MT707(707),
	/**
	 * Advice of a Third Banks Documentary Credit
	 */
	MT710(710),
	/**
	 * Advice of a Third Banks Documentary Credit
	 */
	MT711(711),
	/**
	 * Transfer of Documentary Credit
	 */
	MT720(720),
	/**
	 * Transfer of Documentary Credit
	 */
	MT721(721),
	/**
	 * Acknowledgement
	 */
	MT730(730),
	/**
	 * Advice of Discharge
	 */
	MT732(732),
	/**
	 * Advice of Refusal
	 */
	MT734(734),
	/**
	 * Authorization to Reimburse
	 */
	MT740(740),
	/**
	 * Reimbursement Claim
	 */
	MT742(742),
	/**
	 * Amendment to an Authorisation to Reimburse
	 */
	MT747(747),
	/**
	 * Advice of Discrepancy
	 */
	MT750(750),
	/**
	 * Authorisation to Pay, Accept or Negotiate
	 */
	MT752(752),
	/**
	 * Advice of Payment/Acceptance/Negotiation
	 */
	MT754(754),
	/**
	 * Advice of Reimbursement or Payment
	 */
	MT756(756),
	/**
	 * 
	 */
	MT760(760),
	/**
	 * 
	 */
	MT767(767),
	/**
	 * 
	 */
	MT769(769),
	/**
	 * 
	 */
	MT791(791),
	/**
	 * 
	 */
	MT795(795),
	/**
	 * 
	 */
	MT799(799),
	/**
	 * 
	 */
	NONE(000);		
	
	private final int value;
	
	MT7TYPE(final int newValue){
		value = newValue;
	}
	
	public int getValue(){ return value;}
	
	public boolean Compare(int i){return value == i;}
	
	public static MT7TYPE getValue(int iType){
		
		MT7TYPE[] values = MT7TYPE.values();
		
		for(int i = 0; i < values.length; i++)
        {
            if(values[i].Compare(iType))
                return values[i];
        }				
		
		return MT7TYPE.NONE;
	}	
	
}
