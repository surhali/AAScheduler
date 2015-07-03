package com.jpmc.am.scheduler.messaging;

import java.util.Queue;
/**
 * Any Algorithm which in future deals with the Prioritization of messages
 * needs to implement this interface.
 * @author Suryasatish
 *
 */
public interface MessagePriorityAlgorithm {

	public AMMessage prioritize(Queue<AMMessage> msgList,AMMessage aMMessage,int resourceSize);
	
	
	
	
}
