package com.jpmc.am.scheduler.messaging;

import com.jpmc.am.scheduler.messaging.exceptions.TerminationException;

/**
 * This scheduler is an abstract class with methods that are used to
 * schedule the messages to a resource to send them to the gateway.
 * @author suryasatish
 *
 */
public abstract class AMScheduler {
	
	/***
	 * As the name suggests this method is used to add message to the Queue.
	 * @param msg
	 * @throws TerminationException 
	 */
	public abstract void addMessage(AMMessage msg) throws TerminationException;
	
	/***
	 * This method starts the main thread of AMScheduler. Once started, the scheduler will wait for 
	 * messages to arrive.
	 * Note:This thread runs continuously in an infinite loop always looking for Messages 
	 * and sending them to gateway.
	 */
	public abstract void start();
	
	/***
	 * Cancels a group of message. Once cancelled, no more message of that group will be sent to the aMGateway.
	 * @param group
	 * @Return void
	 */
	public abstract void cancelGroup(AMMessageGroup group);
	
	
}
