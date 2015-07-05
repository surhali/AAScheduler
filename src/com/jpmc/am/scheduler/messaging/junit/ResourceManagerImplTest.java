package com.jpmc.am.scheduler.messaging.junit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jpmc.am.scheduler.messaging.AMMessage;
import com.jpmc.am.scheduler.messaging.AMMessageFactory;
import com.jpmc.am.scheduler.messaging.AMResource;
import com.jpmc.am.scheduler.messaging.AMResourceManager;
import com.jpmc.am.scheduler.messaging.impl.AMGatewayImpl;
import com.jpmc.am.scheduler.messaging.impl.AMResourceImpl;
import com.jpmc.am.scheduler.messaging.impl.AMResourceManagerImpl;

public class ResourceManagerImplTest {

	private  AMResourceManager mgr = new AMResourceManagerImpl();;
	private  List<AMResource> resourceList = new ArrayList<AMResource>();
	private  int processingTime = 100; 
	
	
	@Before
	public  void setUp() throws Exception
	{
		
		for (int i = 0; i < 10; i++) {
			AMResource res = new AMResourceImpl("a" + i, processingTime);
			res.setPriority(i);
			resourceList.add(res);
		}
		
		mgr.addResources(resourceList);
		
	}

	/**
	 * This method tests the priority of resource that is being returned.
	 */
	public void testPriorityResource()
	{
		
	assertTrue(mgr.getResourceOnPriority().equals(new AMResourceImpl("a"+9)));
		
	}
	
	
	/**
	 * This tests the availability of Resources Iteratively.
	 * @throws InterruptedException
	 */
	@Test
	public void testAvailableResourcesIteratively() throws InterruptedException {

		
		for (int i = 0; i < 10; i++) {
			mgr.processMessage(resourceList.get(i), AMMessageFactory.createMessage("Surya" + i),
					new AMGatewayImpl());
			System.out.println(mgr.getAvailableResources().size());
			System.out.println(resourceList.size());
			assertTrue(mgr.getAvailableResources().size() == resourceList
					.size() - (i + 1));
			if(i==9)
				assertFalse(mgr.hasAvailableResources());

		}

		Thread.sleep(200);
		assertTrue(mgr.getAvailableResources().size() == 10);
		System.out.println("true");

	}
	/**
	 * This tests whether the test is completed or not.
	 * @throws InterruptedException
	 */
	@Test
	public void testIsCompleted() throws InterruptedException
	{
		AMMessage msg = AMMessageFactory.createMessage("Trial");
		msg.setName("Trial");
		mgr.processMessage(mgr.getResourceOnPriority(), msg,
				new AMGatewayImpl());
		Thread.sleep(200);
		assertTrue(msg.isCompleted());
	}
	
	
	
}
