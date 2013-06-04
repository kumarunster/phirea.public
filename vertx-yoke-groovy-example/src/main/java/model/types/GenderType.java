package model.types;

public enum GenderType {
	
  MALE("M", "male"),
  FEMALE("F", "female");
  
  String code;
  String translationCode;
  
  private GenderType(String code, String translationCode)
  {
    this.code = code;
    this.translationCode = translationCode;
  }

  public String getCode()
  {
    return code;
  }

  public String getTranslationCode()
  {
    return translationCode;
  }
  
  public static GenderType findByCode(String code) {
	  for(GenderType type : GenderType.values()) {
		  if(type.getCode().equals(code))
			  return type;
	  }
	  
	  return null;
  }
	

}
