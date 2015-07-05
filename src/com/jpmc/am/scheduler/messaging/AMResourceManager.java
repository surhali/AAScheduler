package com.jpmc.am.scheduler.messaging;

import java.util.ArrayList;
import java.util.List;

/***
 * AMResourceManager has the responsibility to manage resources. It supports operations such as 
 * add/remove resources or start a resource.
 * @author Suryasatish
 *
 */
public abstract class AMResourceManager {
	protected List<AMResource> resourceList;
	protected volatile boolean hasAvailableResources = false;
	
	public AMResourceManager() {
		resourceList = new ArrayList<AMResource>();
	}
	
	public AMResourceManager(List<AMResource> resourceList) {
		this.resourceList = resourceList;
	}
	
	
	/***
	 * This method is called by the user to provide resources to the resource manager.
	 * A new Resource with the given name is created.It has default values of processing time and priority.
	 * @param name
	 */
	public abstract void addResource(String name);
	/**
	 * This method takes a List of Resources as Input.
	 * @param resourceList
	 */
	public abstract void addResources(List<AMResource> resourceList);
	/**
	 * A resource created is added to existing set of resources.
	 * @param resource
	 */
	public abstract  void addResource(AMResource resource);
	
	/**
	 * This method is used to process Messages.
	 * @param msg
	 * @param aMGateway
	 * @throws InterruptedException
	 */
	public abstract void processMessage(AMMessage msg, AMGateway aMGateway) throws InterruptedException;
	
	/**
	 * This is an Overloaded version of processMessage
	 * @param res
	 * @param msg
	 * @param aMGateway
	 * @throws InterruptedException
	 */
	public abstract void processMessage(AMResource res, AMMessage msg, AMGateway aMGateway) throws InterruptedException;
	
	/***
	 * This method removes a resource from the resource manager.
	 * @param res - The resource to remove
	 * @return The result of the remove process
	 */
	public boolean removeResource(AMResource res) {
		return resourceList.remove(res);
	}
	
	/***
	 * This method returns true if there is at least one idle resource. A resource is idle when it is not doing 
	 * any processing.
	 * @return
	 */
	public boolean hasAvailableResources() {
		return hasAvailableResources;
	}
	
	/**
	 * This method is used to update the resources and notify 
	 * the threads which are waiting.
	 */
	public synchronized void updateAvailableResource() {
		if (getAvailableResources().size() == 0)
			hasAvailableResources = false;
		else {
			hasAvailableResources = true;
			notify();
		}
	}
	
	/**
	 * This Synchronized method is called when the schedulerImpl is waiting for 
	 * Resources.
	 * @throws InterruptedException
	 */
	public synchronized void waitForResources() throws InterruptedException {
		wait();
	}
	
	/***
	 * This method returns a list of immediate available resources.
	 * @return a list of immediate available resources
	 */
	public List<AMResource> getAvailableResources() {
		List<AMResource> availableResources = new ArrayList<AMResource>();

		for (AMResource aMResource : resourceList) {
			if (aMResource.isAvailable()) {
				availableResources.add(aMResource);
			}
		}

		return availableResources;
	}
	/**
	 * This is a generic Implementation of fetching AMResource on Priority.
	 * When multiple Resources are available user can set priority and
	 * depending on Priority a AMResource is returned.
	 * Priority of AMResource is increases with the value.
	 * Ex: If two resources are having say priorities
	 * -1 and 0, 0 has More Priority
	 * 1 and 6, 6 has more Priority.
	 * for -10 and -5, -5 has more priority.
	 * @return AMResource
	 */
	public AMResource getResourceOnPriority()
	{
		AMResource priorityResource = null;
		int priority = 0;
		if(resourceList!=null && resourceList.size()==1)
		{
			return resourceList.get(0);
		}
	
		else
		{
			for (AMResource aMResource : resourceList) {
			if(aMResource.getPriority()>=priority)
			{
				priority = aMResource.getPriority();
				priorityResource = aMResource;
			}
		
			}
			return 	priorityResource;
		}
	
	
	}
}
