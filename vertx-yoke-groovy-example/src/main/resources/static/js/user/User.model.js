var GenderType = stjs.enumeration2( 
	{
		"MALE": {code: "M", translationCode: "male"}, 
		"FEMALE": {code: "F", translationCode: "female"}
	}
	,
	{
		"getCode" : function() {
			return this.code;
		},
		
		"getTranslationCode" : function() {
			return this.translationCode;
		}
		
	},
	{
		"findByCode" : function(code) {
			for(i in GenderType.values()) {
				console.log("in findByCode, i is: " + i);
				if(GenderType.values()[i].code === code)
					return GenderType.values()[i];
			}
		}
	}
);

/*
var GenderType = stjs.enumeration(
    "MALE", 
    "FEMALE"
);
*/

var User = function(){};
stjs.extend(User, null, [], function(constructor, prototype){
    prototype.fName = null;
    prototype.lName = null;
    prototype.email = null;
    prototype.gender = null;
    prototype.passwd = null;
    prototype.getfName = function() {
        return this.fName;
    };
    prototype.setfName = function(fName) {
        this.fName = fName;
    };
    prototype.getlName = function() {
        return this.lName;
    };
    prototype.setlName = function(lName) {
        this.lName = lName;
    };
    prototype.getEmail = function() {
        return this.email;
    };
    prototype.setEmail = function(email) {
        this.email = email;
    };
    prototype.getGender = function() {
        return this.gender;
    };
    prototype.setGender = function(gender) {
        this.gender = gender;
    };
    prototype.getPasswd = function() {
        return this.passwd;
    };
    prototype.setPasswd = function(passwd) {
        this.passwd = passwd;
    };
	constructor.createFromJSON = function(json) {
        return stjs.parseJSON(json, User);
    };
}, {"gender":{name:"Enum", arguments:["GenderType"]}});



