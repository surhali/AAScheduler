package com.jpmc.am.scheduler.messaging.impl;

import java.util.concurrent.CountDownLatch;

import com.jpmc.am.scheduler.messaging.AMGateway;
import com.jpmc.am.scheduler.messaging.AMMessage;
import com.jpmc.am.scheduler.messaging.AMResource;


public class AMResourceImpl extends AMResource {

	/**
	 * A random Processing Time value initialized. This can be passed as an argument from user or 
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
				System.out.println("------The message sent to Gateway is : "
						+ msg.getName() + " of Group : "
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AMResourceImpl)) {
			return false;
		}
		AMResourceImpl other = (AMResourceImpl) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	
}
