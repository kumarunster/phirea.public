package test.graphiti.nonemf.domainmodel;

public class StatementRule extends NonEmfDomainObject {
	
	private TermClass fromClass;
	
	private TermClass toClass;
	
	private Predicate predicate;
	
	private CardinalityType fromCardinality;
	
	private CardinalityType toCardinality;
	
	Object readResolve() {
		if(this.fromCardinality == null)
			this.fromCardinality = CardinalityType.ONE;
		
		if(this.toCardinality == null)
			this.toCardinality = CardinalityType.ZERO_OR_MANY;
		
		return this;
	}
	
	
	public TermClass getFromClass() {
		return fromClass;
	}

	public void setFromClass(TermClass fromClass) {
		this.fromClass = fromClass;
	}

	public TermClass getToClass() {
		return toClass;
	}

	public void setToClass(TermClass toClass) {
		this.toClass = toClass;
	}

	public Predicate getPredicate() {
		return predicate;
	}

	public void setPredicate(Predicate predicate) {
		this.predicate = predicate;
	}

	public CardinalityType getFromCardinality() {
		return fromCardinality;
	}

	public void setFromCardinality(CardinalityType fromCardinality) {
		this.fromCardinality = fromCardinality;
	}

	public CardinalityType getToCardinality() {
		return toCardinality;
	}

	public void setToCardinality(CardinalityType toCardinality) {
		this.toCardinality = toCardinality;
	}
	
	
}
