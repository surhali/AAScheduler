package com.jpmc.am.scheduler.messaging;
/**
 * This class has the Group Information for every message.The AMMessageGroup is
 * represented by a GroupID which also acts as name of the Group with which a 
 * Group is represented.
 * @author Suryasatish
 *
 */
public class AMMessageGroup {
	public enum GroupStatus
	{
		DEFAULT,
		CANCELLED;
	}
	public String groupID;
	public GroupStatus grpStatus;
	private static final String NO_NAME="NoName";
	public AMMessageGroup()
	{
		this.groupID=NO_NAME;
		this.grpStatus = GroupStatus.DEFAULT;
	}
	
	public AMMessageGroup(String groupName)
	{
		this.groupID=groupName;
		this.grpStatus = GroupStatus.DEFAULT;
	}
	
	public AMMessageGroup(String groupName,GroupStatus grpType)
	{
		this.groupID=groupName;
		this.grpStatus = grpType;
	}
	
	public String getGroupID() {
		return groupID;
	}
	public void setGroupName(String groupID) {
		this.groupID = groupID;
	}

	public GroupStatus getGrpStatus() {
		return grpStatus;
	}

	public void setGrpStatus(GroupStatus grpType) {
		this.grpStatus = grpType;
	}

	/**
	 * hashCode() 
	 * @return  hashCode Representation.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupID == null) ? 0 : groupID.hashCode());
		return result;
	}

	/**
	 * Equals method which checks the equality of two groups
	 * Note:We here use GroupID to check the equality.
	 * @param Object
	 * @return boolean
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AMMessageGroup)) {
			return false;
		}
		AMMessageGroup other = (AMMessageGroup) obj;
		if (groupID == null) {
			if (other.groupID != null) {
				return false;
			}
		} else if (!groupID.equals(other.groupID)) {
			return false;
		}
		return true;
	}
	
	
	
	
}
