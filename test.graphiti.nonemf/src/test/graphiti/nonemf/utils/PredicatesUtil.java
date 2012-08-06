package test.graphiti.nonemf.utils;

import test.graphiti.nonemf.domainmodel.Predicate;

public class PredicatesUtil {
	
	public static String STANDARD_PREDICATE_ID = "90882fb1-265b-4284-9c56-f9551ea4eede"; 
	
	private static Predicate standardPredicate;
	
	public static Predicate getStandardPredicate() {
		
		if(standardPredicate == null) {
			standardPredicate = new Predicate();
			standardPredicate.setId(STANDARD_PREDICATE_ID);
			standardPredicate.setName("Aggregation: enthält");
		}
		
		return standardPredicate;
	}
	
	public static void setStandardPredicate(Predicate predicate) {
		standardPredicate = predicate;
	}

}
