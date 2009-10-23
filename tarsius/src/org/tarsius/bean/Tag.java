package org.tarsius.bean;

public class Tag {
	
	private Integer id = null;
	private String name = null;
	private Integer idParent = null;
	
	public Tag() {
	}
	
	public Tag(Integer id, String name, Integer idParent) {
		this.id = id;
		this.name = name;
		this.idParent = idParent;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getIdParent() {
		return idParent;
	}
	
	public void setIdParent(Integer idParent) {
		this.idParent = idParent;
	}
	
	@Override
	public boolean equals(Object otherObj) {
		if(otherObj instanceof Tag && otherObj != null){
			Tag otherTag = (Tag)otherObj;
			if(otherTag.id != null && this.id != null){
				return this.id.intValue() == otherTag.id.intValue();
			}
			return false;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.id != null ? this.id.intValue() : 0;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
