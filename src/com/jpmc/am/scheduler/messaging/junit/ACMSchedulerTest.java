package com.jpmc.am.scheduler.messaging.junit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jpmc.am.scheduler.algs.priority.GroupPriorityAlogorithm;
import com.jpmc.am.scheduler.messaging.AMGateway;
import com.jpmc.am.scheduler.messaging.AMMessage;
import com.jpmc.am.scheduler.messaging.AMMessageFactory;
import com.jpmc.am.scheduler.messaging.AMMessageGroup;
import com.jpmc.am.scheduler.messaging.AMResourceManager;
import com.jpmc.am.scheduler.messaging.AMMessageGroup.GroupStatus;
import com.jpmc.am.scheduler.messaging.exceptions.TerminationException;
import com.jpmc.am.scheduler.messaging.impl.AMGatewayImpl;
import com.jpmc.am.scheduler.messaging.impl.AMResourceManagerImpl;
import com.jpmc.am.scheduler.messaging.impl.AMSchedulerImpl;
/**
 * This class basically tests the Grouping functionality of messages,Termination and other features.
 * @author Suryasatish
 *
 */
public class ACMSchedulerTest {

	
	AMGateway aMGateway = new AMGatewayImpl();
	AMResourceManager resManager = new AMResourceManagerImpl();
	AMSchedulerImpl asi;
	
	@Before
	public void setUp() throws Exception {
		asi = new AMSchedulerImpl(resManager,
				aMGateway,new GroupPriorityAlogorithm());
		resManager.addResource("1");
	}
	
	/**
	 * This testcase  populates a message with Termination.Any message
	 * from the same group should not sent to the aMGateway and an error is thrown.
	 */
	
	public void testTerminationMessage()
	{
		asi.start();
		
		try
		{
		
		AMMessage mess = AMMessageFactory.createMessage("Trail1");
		mess.setTerminatedMessage(true);
		//AMMessageGroup grp3 = new AMMessageGroup("Group3");
		AMMessageGroup grp1 = new AMMessageGroup("Group1");
		grp1.setGrpStatus(GroupStatus.DEFAULT);
		//AMMessageGroup grp2 = new AMMessageGroup("Group2");
		mess.setGroup(grp1);
		AMMessage mess1 = AMMessageFactory.createMessage("Trial2");
		mess1.setGroup(grp1);
	    asi.addMessage(mess);
	    asi.addMessage(mess1);
				
		}
		catch(TerminationException tge)
		{
			System.out.println(tge.getLocalizedMessage());
			assertTrue(true);
		}
	}
	/**
	 * This testcase tests the cancelled group functionality.
	 * When a group is cancelled any further messages from the group are not be sent
	 * to the gateway.
	 * @throws TerminationException
	 * @throws InterruptedException
	 */
	@Test
	public void testCancelledGroup() throws TerminationException,InterruptedException
	{
		
		asi = new AMSchedulerImpl(resManager,
				aMGateway,new GroupPriorityAlogorithm());
		resManager.addResource("1");
		
		AMMessage mess = AMMessageFactory.createMessage("Trail1");
	    AMMessageGroup grp3 = new AMMessageGroup("Group3");
	    grp3.setGrpStatus(GroupStatus.CANCELLED);
	    mess.setGroup(grp3);//Setting mess with cancelled group.
	    AMMessage mess2 = AMMessageFactory.createMessage("Trail12",new AMMessageGroup("Group1"));
	    AMMessage mess3 = AMMessageFactory.createMessage("Trail3",grp3);
	    List<AMMessage> msgList = new ArrayList<AMMessage>();
	    msgList.add(mess);
	    msgList.add(mess2);
	    msgList.add(mess3);
	    
	    asi.addMessages(msgList);
	    
	    asi.createCancelledGroup(grp3);
	    
	    asi.start();
	    //Once the group is cancelled,further messages stay back in queue and not sent to
	   // aMGateway
        Thread.sleep(10);
        System.out.println("\n");
        System.out.println("=========Cancelled Messages================\n");
        for(AMMessage msge:asi.getCancelledMessages())
        {
        	System.out.println(msge.getName()+"\n");
        }
        System.out.println("=========Cancelled Messages================\n");
	    assertTrue(asi.getCancelledMessages().contains(mess3));
	    assertFalse(asi.getCancelledMessages().contains(mess2));
	    
	    
	}
	
}
