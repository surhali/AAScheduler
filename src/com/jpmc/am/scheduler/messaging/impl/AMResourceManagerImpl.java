package com.jpmc.am.scheduler.messaging.impl;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.jpmc.am.scheduler.messaging.AMGateway;
import com.jpmc.am.scheduler.messaging.AMMessage;
import com.jpmc.am.scheduler.messaging.AMResource;
import com.jpmc.am.scheduler.messaging.AMResourceManager;

/**
 * This class has the Implementation for AM Resource Manager.
 * @author Suryasatish
 *
 */
public class AMResourceManagerImpl extends AMResourceManager {

	public AMResourceManagerImpl() {
		super();
	}

	public AMResourceManagerImpl(List<AMResource> resourceList) {
		super(resourceList);
	}

	@Override
	public synchronized void addResource(String name) {
		AMResource res = new AMResourceImpl(name);
		this.resourceList.add(res);
		hasAvailableResources = true;
		System.out.println("--New Resource is added :"+res.getName()+"\n");
        notify();//Other threads will be waiting for resource availablity,once
        //the resource is available the waiting threads are notified.
	}
	
	/**
	 * @param resourceList.
	 */
	public synchronized void addResources(List<AMResource> resourceList)
	{
		this.resourceList.addAll(resourceList);
		hasAvailableResources = true;
		for(AMResource res:resourceList)
		{
			System.out.println("--New Resource is added :"+res.getName()+"\n");
		}
		notify();
	}

	
	/**
	 * @param AMMessage
	 * @param AMGateway
	 */
	
	@Override
	public void processMessage(AMMessage msg, AMGateway aMGateway) throws InterruptedException {
		AMResource availResource = this.getResourceOnPriority();
		if (availResource != null) {
			processMessage(availResource, msg, aMGateway);
		}
	}
	/**
	 * 
	 */
	@Override
	public synchronized void processMessage(AMResource res, AMMessage msg, AMGateway aMGateway) throws InterruptedException {
		
		CountDownLatch resourceEnds = new CountDownLatch(1);
		Thread resManagerThread = new Thread(new FindResourceThread(resourceEnds));
		resManagerThread.start();
		res.start(msg, aMGateway, resourceEnds);
		updateAvailableResource();
		
		
	}

	
	private class FindResourceThread implements Runnable {
		private final CountDownLatch resourceEnds;

		public FindResourceThread(CountDownLatch resourceEnds) {
			this.resourceEnds = resourceEnds;
		}

		@Override
		public void run() {
			try {
				resourceEnds.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				updateAvailableResource();
			}
		}
	}
}
