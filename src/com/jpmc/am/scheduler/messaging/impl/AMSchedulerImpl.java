package com.jpmc.am.scheduler.messaging.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.jpmc.am.scheduler.messaging.AMGateway;
import com.jpmc.am.scheduler.messaging.AMMessage;
import com.jpmc.am.scheduler.messaging.AMMessageGroup;
import com.jpmc.am.scheduler.messaging.AMResourceManager;
import com.jpmc.am.scheduler.messaging.AMScheduler;
import com.jpmc.am.scheduler.messaging.MessagePriorityAlgorithm;
import com.jpmc.am.scheduler.messaging.AMMessageGroup.GroupStatus;
import com.jpmc.am.scheduler.messaging.exceptions.TerminationException;

/***
 * This method is the Scheduler implementation of AMScheduler.
 * This class has methods which are responsible for adding the message and sending them
 * to the gateway.
 * @author Suryasatish
 *
 */
public  class AMSchedulerImpl extends AMScheduler {
	private AMResourceManager resManager;
	private Queue<AMMessage> aMMessages;
	private Thread mainGatewayThread;
	private AMGateway aMGateway;

	private List<AMMessageGroup> cancelledGroup;
	private List<AMMessageGroup> terminatedGroup;
	private MessagePriorityAlgorithm alg; 
    public List<AMMessage> cancelledMessages;
	public AMSchedulerImpl(AMResourceManager resManager, AMGateway aMGateway,MessagePriorityAlgorithm alg) {
		this.resManager = resManager;
		this.aMGateway = aMGateway;
		aMMessages = new ConcurrentLinkedQueue<AMMessage>();
		cancelledGroup = new ArrayList<AMMessageGroup>();
		terminatedGroup = new ArrayList<AMMessageGroup>();
		cancelledMessages = Collections.synchronizedList(new ArrayList<AMMessage>());
		this.alg = alg;
	}

	@Override
	public void start() {
		if (mainGatewayThread == null)
			mainGatewayThread = new Thread(new GatewayThreadd(resManager,
					aMMessages));
		if (!mainGatewayThread.isAlive())
			mainGatewayThread.start();
	}
	
	/***
	 * This implementation deals with both adding the message to the message queue 
	 * and termination aMMessages
	 * @throws TerminationException 
	 */
	@Override
	public synchronized void addMessage(AMMessage msg) throws TerminationException {
		
		synchronized (aMMessages) {
			createCancelledGroup(msg);
			if (terminatedGroup.contains(msg.getGroup())) {
				
				throw new TerminationException();
			} else if (msg.isTerminatedMessage()) {
				if(!terminatedGroup.contains(msg.getGroup()))
				{
					//when we encounter a terminated message, that particular message should be
					//sent to aMGateway but further aMMessages needs to be curtailed.This step is ensure this
					//case.
					System.out.println("--New Message is added :"+msg.getName());
					aMMessages.add(msg);
				}
				terminatedGroup.add(msg.getGroup());
			}
			else 
				aMMessages.add(msg);
			System.out.println("--New Message is added :"+msg.getName());
		}
		notify();
	}
	/**
	 * This is an additional method takes List of  message arguments 
	 * and iterates over the List of Messages 
	 * to call the original addMessage(AMMessage message) method. 
	 * @param messageList
	 * @throws TerminationException
	 */
	public synchronized void addMessages(List<AMMessage> messageList) throws TerminationException
	{
      int size = messageList!=null?messageList.size():0;
      
      for(int i=0;i<size;i++)
      {
    	  addMessage(messageList.get(i));
      }
   
	}
	
	/**
	 * This method is used to create a cancelled group.
	 * @param msg
	 */
	public void createCancelledGroup(AMMessage msg)
	{
		if (msg.getGroup().getGrpStatus() == GroupStatus.CANCELLED) {
			cancelledGroup.add(msg.getGroup());

		}

	}
	/**
	 * This method is used to create cancelled group
	 * @param group
	 */
	public void createCancelledGroup(AMMessageGroup group)
	{
		cancelledGroup.add(group);
	}
	
	

	/***
	 * This method should be called by another thread, in order for the thread to wait until aMMessages 
	 * arrive in the message queue of the resource scheduler.
	 * @throws InterruptedException
	 */
	public synchronized void waitForMessages() throws InterruptedException {
		wait();
	}

	@Override
	public synchronized void cancelGroup(AMMessageGroup group) {
		cancelledGroup.add(group);
	}

	/***
	 * This class is the main thread of the scheduler. Its main loop continuously waits for aMMessages to 
	 * arrive, calls the prioritising algorithm, and deals with sending aMMessages using the provided 
	 * resource manager
	 * @author Suryasatish
	 *
	 */
	private class GatewayThreadd implements Runnable {
		private AMResourceManager acmResManager;
		private Queue<AMMessage> aMMessages;

		public GatewayThreadd(AMResourceManager resManager,
				Queue<AMMessage> aMMessages) {
			this.acmResManager = resManager;
			this.aMMessages = aMMessages;
		}

		public void run() {
			/**
			 * Infinite Loop always this continuously looks for Messages and Resources
			 * to send to aMGateway
			 */
			
			AMMessage prevMsg = null;
			
			for (;;) {
				try {
					if(aMMessages.isEmpty()){
					System.out
							.println("......Input aMMessages yet to be received.....\n");
					waitForMessages();
					}
					synchronized (aMMessages) {
                    
						while (!aMMessages.isEmpty()) {
							
							while (!acmResManager.hasAvailableResources()) {
								System.out.println(".....Waiting for Resources........\n");
								
								acmResManager.waitForResources();
								break;
							}

							// The priority algorithm takes place in this
							// method call
							AMMessage gatewayMsg = alg.prioritize(aMMessages,prevMsg,acmResManager.getAvailableResources().size());
                            prevMsg = gatewayMsg;//This helps in knowing what shall be the likelihood of next messesgae.
                            //We need to send the message of same group for default Algorithm.
                            
							
							if (gatewayMsg != null) {
								if (!cancelledGroup.contains(gatewayMsg.getGroup())) {
									acmResManager.processMessage(gatewayMsg,
											aMGateway);
								}
								else
								{
									cancelledMessages.add(gatewayMsg);
								}
							}

						}
					}
				
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
/**
 * 
 * @return Queue.
 */
		public Queue<AMMessage> getMessages() {
		return aMMessages;
	}
/**
 * 
 * @param aMMessages
 */
	public void setMessages(Queue<AMMessage> aMMessages) {
		this.aMMessages = aMMessages;
	}
/**
 * 
 * @return List:AMMessages.
 */
	public List<AMMessage> getCancelledMessages() {
		return cancelledMessages;
	}




}
