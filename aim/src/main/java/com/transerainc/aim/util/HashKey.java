package com.transerainc.aim.util;

public class HashKey {
	
	private String attribute1;
	private String attribute2;
	
	public HashKey(String mem1, String mem2){
		attribute1 = mem1;
		attribute2 = mem2;
	}
	
	/**
	 * @return the attribute1
	 */
	public String getattribute1() {
		return attribute1;
	}

	/**
	 * @param attribute1 the attribute1 to set
	 */
	public void setattribute1(String attribute1) {
		this.attribute1 = attribute1;
	}

	/**
	 * @return the attribute2
	 */
	public String getattribute2() {
		return attribute2;
	}

	/**
	 * @param attribute2 the attribute2 to set
	 */
	public void setattribute2(String attribute2) {
		this.attribute2 = attribute2;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attribute1 == null) ? 0 : attribute1.hashCode());
		result = prime * result + ((attribute2 == null) ? 0 : attribute2.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HashKey other = (HashKey) obj;
		if (attribute1 == null) {
			if (other.attribute1 != null)
				return false;
		} else if (!attribute1.equals(other.attribute1))
			return false;
		if (attribute2 == null) {
			if (other.attribute2 != null)
				return false;
		} else if (!attribute2.equals(other.attribute2))
			return false;
		return true;
	}

}
