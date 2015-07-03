package com.jpmc.am.scheduler.messaging.impl;

import com.jpmc.am.scheduler.messaging.AMGateway;
import com.jpmc.am.scheduler.messaging.AMMessage;

/***
 *  Implementation of the AMGateway interface.
 * @author  @author Suryasatish 
 *
 */
public class AMGatewayImpl implements AMGateway {

	/***
	 * Very simple implementation of send().
	 */
	@Override
	public void send(AMMessage msg) {
		if (!msg.isCompleted())
			msg.completed();
	}

}
