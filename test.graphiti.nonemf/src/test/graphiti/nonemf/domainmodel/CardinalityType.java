package test.graphiti.nonemf.domainmodel;

public enum CardinalityType {
	
	ZERO_OR_ONE("0..1"),
	ONE("1"),
	ONE_OR_MANY("1..*"),
	ZERO_OR_MANY("*");
	
	private String cardinalityName;
	
	CardinalityType(String cardinality) {
		this.cardinalityName = cardinality;
	}
	
	public String getCardinalityName() {
		return this.cardinalityName;
	}
	
	public static String[] getCardinalities() {
		CardinalityType[] values = values();
		String [] result = new String[values.length];
		
		for(int i = 0; i<values.length; i++) {
			result[i] = values[i].getCardinalityName();
		}
		
		return result;
	}

	public static CardinalityType findForName(String cardinalityName) {
		for(CardinalityType cardinality : CardinalityType.values()) {
			if(cardinality.getCardinalityName().equals(cardinalityName))
				return cardinality;
		}
		return null;
	}
	
}
