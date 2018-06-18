package agent;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class CandidateAgent extends Agent
{
	private static final long serialVersionUID = 7951150000544730740L;
	
	@Override
	protected void setup() 
	{
		System.out.println("My name is " + getAID().getName());
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType("candidate");
		sd.setName(getLocalName() + "-candidate");
		
		dfd.addServices(sd);
		
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}

	}
	
	@Override
	protected void takeDown() 
	{
		try {
			DFService.deregister(this);
			System.out.println("I'm removed from the DF");
		}catch(FIPAException fe) {
			fe.printStackTrace();
		}
	}

}
