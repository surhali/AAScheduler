package com.jpmc.am.scheduler.messaging;

/**
 * A default gateway class to send the messages to gateway.
 * @author Suryasatish
 *
 */
public interface AMGateway {
	/**
	 * 
	 * @param msg
	 */
	public void send(AMMessage msg);
}
