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
	private  AMResourceManager mgr = new AMResourceManagerImpl();
	
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


@Test
public void testGroupedMessages() throws TerminationException
{
	AMMessageGroup aMMessageGroup1 = new AMMessageGroup("Group1");
	AMMessageGroup aMMessageGroup2 = new AMMessageGroup("Group2");
	
	AMMessage msg1 = AMMessageFactory.createMessage("Test1", aMMessageGroup1);
	AMMessage msg2 = AMMessageFactory.createMessage("Test2", aMMessageGroup2);
	AMMessage msg3 = AMMessageFactory.createMessage("Test3", aMMessageGroup1);
	mgr.addResource("1");//Only one resource is available
	AMSchedulerImpl rs = new AMSchedulerImpl(mgr,
			gateway,new GroupPriorityAlogorithm());
	rs.addMessage(msg1);
	rs.addMessage(msg2);
	rs.addMessage(msg3);
	
	
}










}
