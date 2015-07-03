package com.jpmc.am.scheduler.messaging;

import com.jpmc.am.scheduler.messaging.AMMessageGroup.GroupStatus;

/***
 * Message factory is a  factory method which masks the details of creating of
 * actual AMMessage.
 * @author Suryasatish
 *
 */
public class AMMessageFactory {
	/***
	 * This method returns a new message of the group.
	 * @param group
	 * @return new message
	 */
	public  static AMMessage createMessage(String name)
	
	{
		AMMessage aMMessage = new AMMessage(name);
		return aMMessage;
	}
	
	public static AMMessage createMessage(String name, AMMessageGroup aMMessageGroup)
	{
		AMMessage aMMessage = new AMMessage(name,aMMessageGroup);
		return aMMessage;
	}
	
	public static AMMessage createMessage(String name, AMMessageGroup aMMessageGroup, String data)
	{
		AMMessage aMMessage = new AMMessage(name,aMMessageGroup,data);
		return aMMessage;
	}
	
	/***
	 * This method a new termination message of the group
	 * @param aMMessageGroup
	 * @return new termination message
	 */
	public  static AMMessage createTerminationMessage(AMMessageGroup aMMessageGroup)
	{
		AMMessage aMMessage = new AMMessage();
		aMMessage.setTerminatedMessage(true);
		aMMessage.setGroup(aMMessageGroup);
	    return aMMessage;
	}
}
