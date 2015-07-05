package com.jpmc.am.scheduler.messaging.client;

import com.jpmc.am.scheduler.algs.priority.GroupPriorityAlogorithm;
import com.jpmc.am.scheduler.messaging.AMGateway;
import com.jpmc.am.scheduler.messaging.AMMessageFactory;
import com.jpmc.am.scheduler.messaging.AMMessageGroup;
import com.jpmc.am.scheduler.messaging.AMMessageGroup.GroupStatus;
import com.jpmc.am.scheduler.messaging.AMResourceManager;
import com.jpmc.am.scheduler.messaging.exceptions.TerminationException;
import com.jpmc.am.scheduler.messaging.impl.AMGatewayImpl;
import com.jpmc.am.scheduler.messaging.impl.AMResourceManagerImpl;
import com.jpmc.am.scheduler.messaging.impl.AMSchedulerImpl;
/**
 * This is the main class of application.
 * Here we construct Implementation of the scheduler and start it. Once the Scheduler thread is
 * started it looks out for messages and Resources.Once they are available it tries to send t
 * to the Gateway.A Default algorithm Implementation of Message Priority i.e Group Priority 
 * Algorithm is sent along with input.
 * @author Suryasatish
 *
 */
public class AMSchedulerClient {
	public static void main(String[] args) throws InterruptedException {
		AMGateway aMGateway = new AMGatewayImpl();
		AMResourceManager resManager = new AMResourceManagerImpl();
		
		
		
		AMSchedulerImpl rs = new AMSchedulerImpl(resManager,
				aMGateway,new GroupPriorityAlogorithm());
		
		
		// Start the scheduler
		
		rs.start();//The run method of this thread runs continuously in search of messages and resources.
		
		
		AMMessageGroup grp3 = new AMMessageGroup("Group3");
		AMMessageGroup grp1 = new AMMessageGroup("Group1");
		grp1.setGrpStatus(GroupStatus.CANCELLED);
		
		
		try
		{
		
		
		// Adding one Resource.
		resManager.addResource("1");
		//resManager.addResource("2");
		
		rs.addMessage(AMMessageFactory.createMessage("Surya",grp1));
		rs.addMessage(AMMessageFactory.createMessage("Satish",grp3));
		rs.addMessage(AMMessageFactory.createMessage("Hero",grp1));
		rs.addMessage(AMMessageFactory.createMessage("Hero1",grp3));
		}
		catch(TerminationException tge)
		{
			tge.printStackTrace();
		}
		
	}
}
