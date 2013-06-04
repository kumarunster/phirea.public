package model.types;

public enum GenderType {
	MALE("Male"),
	FEMALE("Female");
	
	private String genderCode;

	private GenderType(String genderCode) {
		this.genderCode = genderCode;
	}
	
	public String getGenderCode() {
		return this.genderCode;
	}
	
	public static GenderType findByValue(String genderCode) {
		for (GenderType genderType : GenderType.values()) {
			if(genderType.getGenderCode().equals(genderCode))
				return genderType;
		}
		
		return null;
	}
	

}
