package com.jpmc.am.scheduler.messaging.junit;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jpmc.am.scheduler.algs.priority.GroupPriorityAlogorithm;
import com.jpmc.am.scheduler.messaging.AMMessage;
import com.jpmc.am.scheduler.messaging.AMMessageFactory;
import com.jpmc.am.scheduler.messaging.AMMessageGroup;
import com.jpmc.am.scheduler.messaging.AMResourceManager;
import com.jpmc.am.scheduler.messaging.exceptions.TerminationException;
import com.jpmc.am.scheduler.messaging.impl.AMGatewayImpl;
import com.jpmc.am.scheduler.messaging.impl.AMResourceManagerImpl;
import com.jpmc.am.scheduler.messaging.impl.AMSchedulerImpl;

public class ACMGatewayTest {
	
	private AMGatewayImpl gateway;
	
	
	@Before
	public void setUp() throws Exception {
		gateway = new AMGatewayImpl();
	}

	@After
	public void tearDown() throws Exception {
	}

	/***
	 * Test the send() method of AMGatewayImpl. The messasge's 
	 * isCompleted() method should return true afterwards.
	 */
	@Test
	public void testSend() {
		AMMessageGroup aMMessageGroup = new AMMessageGroup();
		AMMessage msg = AMMessageFactory.createMessage("Test", aMMessageGroup);
		gateway.send(msg);
		assertTrue(msg.isCompleted());
	}

/**
 * This test primarily tests whether all the messages of the same group are delivered 
 * as a group when available resources are 1. The output is validated by looking at the
 * Console.
 * Sample output for the given input looks like this
 * 
 * @throws TerminationException
 */
@Test
public void testGroupedMessages() throws TerminationException,InterruptedException
{
	  AMResourceManager mgr = new AMResourceManagerImpl();
	AMMessageGroup aMMessageGroup1 = new AMMessageGroup("Group1");
	AMMessageGroup aMMessageGroup2 = new AMMessageGroup("Group2");
	
	AMMessage msg1 = AMMessageFactory.createMessage("Test1", aMMessageGroup1);
	AMMessage msg2 = AMMessageFactory.createMessage("Test2", aMMessageGroup2);
	AMMessage msg3 = AMMessageFactory.createMessage("Test3", aMMessageGroup1);
	
	
	AMSchedulerImpl asi = new AMSchedulerImpl(mgr,
			gateway,new GroupPriorityAlogorithm());
	asi.addMessage(msg1);
	asi.addMessage(msg2);
	asi.addMessage(msg3);
	mgr.addResource("2");//Only one resource is available
	asi.start();
	Thread.sleep(2000);/**If we donot let the Thread to sleep then, the process would be completed
	very quickly i.e in lesser time than the Scheduler Thread takes to send the message to gateway.
	Hence if we dont specify the sleeping time(it should be greater than the Processing time of Resource
	i.e AMResourceImpl..the default time hardcoded is 100 milliseconds.) then the messages sent to the gateway are not printed
	on the cosole.Can check the behaviour by removing this sleepingTime**/
	
	/**
	 * Messages sent to the gateway are grouped by the GroupID.
	 */
	
}


}
