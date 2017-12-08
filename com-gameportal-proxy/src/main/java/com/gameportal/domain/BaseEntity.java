package com.gameportal.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseEntity extends PageBean implements IEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6451464369400396588L;
	
	protected static Logger logger = LoggerFactory.getLogger(BaseEntity.class);
	
	private String id;
	
	

	public void setId(String id) {
		this.id = id;
	}
	
	public BaseEntity(){}
	public BaseEntity(String id){
		this.id = id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final BaseEntity other = (BaseEntity) obj;
		if (getId() != null && other.getId() != null)
			return getId().equals(other.getId());
		return false;
	}
	
	@Override
	public int compareTo(Object o) {
		if (null == o)
			return 1;
		if (!IEntity.class.isAssignableFrom(o.getClass())) {
			return 1;
		}
		IEntity target = (IEntity) o;
		if (null == this.getId())
			return 1;
		if (null == target.getId())
			return -1;
		return this.getId().toString().compareTo(target.getId().toString());
	}

	@Override
	public String getId() {
		return id;
	}
	
	

}
