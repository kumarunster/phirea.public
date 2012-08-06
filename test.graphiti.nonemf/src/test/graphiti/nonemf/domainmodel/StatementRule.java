package test.graphiti.nonemf.domainmodel;

public class StatementRule extends NonEmfDomainObject {
	
	private TermClass fromClass;
	
	private TermClass toClass;
	
	private Predicate predicate;
	
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
}
