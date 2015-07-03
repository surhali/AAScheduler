package com.jpmc.am.scheduler.algs.priority;
/**
 * This is a default Group Priority Algorithm.
 * This groups all the messages according to the like groups
 *  @author Suryasatish
 */
import java.util.List;
import java.util.Queue;

import com.jpmc.am.scheduler.messaging.AMMessage;
import com.jpmc.am.scheduler.messaging.AMMessageGroup;
import com.jpmc.am.scheduler.messaging.MessagePriorityAlgorithm;

public class GroupPriorityAlogorithm implements MessagePriorityAlgorithm {

	private List<AMMessage> msgList=null;
	/**
	 * This method priortizes the messages in such a way that all the messages belonging to the
	 * same group are sent sequentially when one resource is available.
	 */
	@Override
	public AMMessage prioritize(Queue<AMMessage> msgList,AMMessage prevMsg,int size) {
		// TODO Auto-generated method stub
    
		AMMessage msg = msgList.peek();
		if(prevMsg==null)
		{
			msgList.poll();//This means this message is the first message in the Queue.
			return msg;
		}
		else if(size==1)//When the number of available resources are one, the similar grouping
			           //logic is applied
		{
			for(AMMessage msge:msgList)
			{
				if(msge.getGroup().equals(prevMsg.getGroup()))
				{
					msgList.remove(msge);
					return msge;
				}
			}
		}
		    msgList.poll();
			return msg;//if more than one resources are there this messages can be sent randomly.
		
	}

}
