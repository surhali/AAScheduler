package com.jpmc.am.scheduler.messaging;

/***
 * The AMMessage interface represents a single message to be sent to the AMGateway
 * @author Suryasatish
 *
 */
public class AMMessage extends AMMessageFactory {
	
	
	
	private volatile boolean completed = false;
	private static final String DEFAULT_NAME = "unnamed";
	private AMMessageGroup aMMessageGroup;
	private String name;
	private String data;
	private boolean isTerminatedMessage = false;
	
	
	

	 AMMessage() {
		name = DEFAULT_NAME;
		aMMessageGroup = new AMMessageGroup();
	}
	
	 AMMessage(String name, AMMessageGroup aMMessageGroup) {
		this.name = name;
		this.aMMessageGroup = aMMessageGroup;
	}
	
	 AMMessage(String message)
	{
		name = message;
		aMMessageGroup = new AMMessageGroup();
	}
	 AMMessage(String name, AMMessageGroup aMMessageGroup, String data) {
		this.name = name;
		this.aMMessageGroup = aMMessageGroup;
		this.data = data;
	}

	
	
	
	/***
	 * This method is called when the message has been sent to the aMGateway.
	 */
	public  void completed()
	{
		completed = true;
	}
	
	/***
	 * This method returns true if the complete() method of this message is already called.
	 * @return
	 */
	public  boolean isCompleted()
	{
		return completed;
	}
	
	/**
	 * @return the name
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
	
	/**
	 * @return the aMMessageGroup
	 */
	public AMMessageGroup getGroup() {
		return aMMessageGroup;
	}

	/**
	 * @param aMMessageGroup the aMMessageGroup to set
	 */
	public void setGroup(AMMessageGroup aMMessageGroup) {
		this.aMMessageGroup = aMMessageGroup;
	}
	
	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
	
	public AMMessageGroup getMessageGroup() {
		return aMMessageGroup;
	}

	public void setMessageGroup(AMMessageGroup aMMessageGroup) {
		this.aMMessageGroup = aMMessageGroup;
	}

	public boolean isTerminatedMessage() {
		return isTerminatedMessage;
	}

	public void setTerminatedMessage(boolean isTerminatedMessage) {
		this.isTerminatedMessage = isTerminatedMessage;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	@Override
	public String toString() {
		return name + " - " + aMMessageGroup;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		AMMessage other = (AMMessage) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}




}
