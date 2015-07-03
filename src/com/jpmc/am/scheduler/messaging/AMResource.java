package com.jpmc.am.scheduler.messaging;

import java.util.concurrent.CountDownLatch;
/**
 * This class represents the AMResource.
 * @author suryasatish
 *
 */
public abstract class AMResource {

	protected String name;
	protected int priority=0;
	
	public AMResource(String name) {
		this.name = name;
	}
	
	/**
	 * This method starts the resource, changes status to not available. This method is not supposed to be called
	 * outside of a AMResourceManager object
	 * @throws InterruptedException 
	 */
	public abstract void start(AMMessage msg, AMGateway aMGateway, CountDownLatch resourceEnds) throws InterruptedException;
	
	
	
	/**
	 * @return the availability of the resource
	 */
	public abstract boolean isAvailable();

	/**
	 * @return the name of the resource
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
/**
 * A Priority for the resource is set so incase when two resources are available
 * then the resource with higher priority will be choosen to handle the Message Queue.
 * @return
 */
	public int getPriority() {
		return priority;
	}
/**
 * 
 * @param priority
 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

/* (non-Javadoc)
 * @see java.lang.Object#hashCode()
 */
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	return result;
}

/* (non-Javadoc)
 * @see java.lang.Object#equals(java.lang.Object)
 */
@Override
public boolean equals(Object obj) {
	if (this == obj) {
		return true;
	}
	if (obj == null) {
		return false;
	}
	if (!(obj instanceof AMResource)) {
		return false;
	}
	AMResource other = (AMResource) obj;
	if (name == null) {
		if (other.name != null) {
			return false;
		}
	} else if (!name.equals(other.name)) {
		return false;
	}
	return true;
}





}
