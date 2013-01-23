package test.graphiti.nonemf.diagram.utils;



public class GraphitiPictogramElementProperties {
	
	public enum StatementRulePropertyType {
		
		FROM("fromCardinality"),
		TO("toCardinality"),
		PREDICATE_NAME("predicateName");
		
		public static String StatementRulePropertyKey = "statementRuleToTextProperty";
		
		private String propertyValue;
		
		StatementRulePropertyType(String propertyValue) {
			this.propertyValue = propertyValue;
		}
		
		public String getPropertyValue() {
			return this.propertyValue;
		}
		
		public static StatementRulePropertyType findByValue(String value){
			for (StatementRulePropertyType property : StatementRulePropertyType.values()) {
				if(property.getPropertyValue().equals(value))
					return property;
			}
			
			return null;
		}
	}
	
}
