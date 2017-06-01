package parser;

public class AppleScriptEnumeration {
	private String id;
	
	public AppleScriptEnumeration(String id) {
	    this.id = id;	
	}
	
	public String getId() {
		return id;
	}
	
	public boolean equals(Object o) {
		if (o instanceof AppleScriptEnumeration) {
			AppleScriptEnumeration ase = (AppleScriptEnumeration) o;
			if (ase.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}
	
	public String toString() {
		return getId();
	}
}
