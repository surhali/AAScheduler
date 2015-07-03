package com.jpmc.am.scheduler.messaging.impl;

import java.util.concurrent.CountDownLatch;

import com.jpmc.am.scheduler.messaging.AMGateway;
import com.jpmc.am.scheduler.messaging.AMMessage;
import com.jpmc.am.scheduler.messaging.AMResource;


public class AMResourceImpl extends AMResource {

	/**
	 * A random value initialized. This can be passed as an argument from user or 
	 * external system.
	 */
	private int processingTime = 100;
	
	
	private volatile boolean available = true;

	public AMResourceImpl(String name) {
		
		super(name);
		
	}

	public AMResourceImpl(String name, int processingTime) {
		super(name);
		this.processingTime = processingTime;
	}

	@Override
	public void start(AMMessage msg, AMGateway aMGateway, CountDownLatch resourceEnds) throws InterruptedException {
		CountDownLatch resourceStarts = new CountDownLatch(1);
		Thread resThread = null;
		if (resThread == null) {
			resThread = new Thread(new ResourceThread(msg, aMGateway, resourceStarts, resourceEnds));
		}
		if (!resThread.isAlive()) {
			resThread.start();
			// Wait for the thread to actually do work, then proceed
			resourceStarts.await();
		}
	}
	
	
	
	/**
	 * @return the availability of the resource
	 */
	@Override
	public boolean isAvailable() {
		return available;
	}

	
	private class ResourceThread implements Runnable {
		private final CountDownLatch resourceStarts;
		private final CountDownLatch resourceEnds;
		
		private AMMessage msg;
		private AMGateway aMGateway;
		
		public ResourceThread(AMMessage msg, AMGateway aMGateway, CountDownLatch resourceStarts, CountDownLatch resourceEnds) {
			this.resourceStarts = resourceStarts;
			this.resourceEnds = resourceEnds;
			this.msg = msg;
			this.aMGateway = aMGateway;
		}

		public void run() {
			available = false;
			
			resourceStarts.countDown();
			try {
				
				Thread.sleep(processingTime);
				
				if (!msg.isCompleted())
					aMGateway.send(msg);
				System.out.println("------The message being sent to AMGateway is "
						+ msg.getName() + " Grouup is "
						+ msg.getGroup().getGroupID()+"------\n");
				;
				    msg.setCompleted(true);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				available = true;
				
				resourceEnds.countDown();
			}
		}

		
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof AMResource) {
			return ((AMResource) o).getName().equals(this.getName());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.getName().hashCode() * 123;
	}
	
}
