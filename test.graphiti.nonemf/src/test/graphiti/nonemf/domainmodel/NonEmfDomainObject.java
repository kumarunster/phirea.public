package test.graphiti.nonemf.domainmodel;

import java.util.UUID;

public class NonEmfDomainObject implements INonEmfDomainObject {

	private String id;
	
	private String name;
	
	public NonEmfDomainObject() {
		if(id == null) {
			id = UUID.randomUUID().toString();
		}
			
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}
}
