package agent;
import jade.core.*; 
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.*;

public class VotingMachine extends Agent
{	
	private static final long serialVersionUID = -3657633911205663525L;
	
	private AID[] aid_candidates;
	
	private Integer numberOfVoters = null;
	private Hashtable<String, Integer> candidateList;
	private AgentContainer ac = null;
	private AgentController t1, t2, t3 = null;
	private class GetCandidates extends Behaviour{

		DFAgentDescription[] result;
		public GetCandidates(VotingMachine votingMachine) {
			super(votingMachine);
		}

		@Override
		public void action() {
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("candidate");
			template.addServices(sd);
			try {
				 result = DFService.search(myAgent, template);
				 aid_candidates = new AID[result.length];
				 for (int i = 0; i < result.length; ++i) {
					 aid_candidates[i] = result[i].getName();
					 //System.out.println("--" + listaCandi[i].getLocalName());
				 	}
				 }
				 catch (FIPAException fe) {
					 fe.printStackTrace();
				 }
			
			DFAgentDescription template_v = new DFAgentDescription();
			ServiceDescription sd_v = new ServiceDescription();
			sd_v.setType("voter");
			template_v.addServices(sd_v);
			
			DFAgentDescription[] voters = null;
			try
			{
				voters = DFService.search(myAgent, template_v);
			}catch(FIPAException fe) {
				fe.printStackTrace();
			}
			
			numberOfVoters = voters.length;
			
		}
		
		@Override
		public boolean done() {
			if (numberOfVoters > 0)
				return true;
			else
				return false;
		}
		
	}
	
	private class receiveVotes extends Behaviour
	{
		private static final long serialVersionUID = 8272372925929726772L;
		
		private int numberOfMessages = 0;
		
		public receiveVotes(Agent a)
		{
			super(a);
		}
		
		@Override
		public boolean done() {
			int max = 0;
			String winner = null;
			
			if (numberOfVoters != null && numberOfMessages == numberOfVoters) 
			{
				Enumeration<String> enumeration = candidateList.keys();
				
				while(enumeration.hasMoreElements()) 
				{
		            String key = enumeration.nextElement();
		            int candidate_votes = candidateList.get(key);
		            
		            if(candidate_votes > max){
		            	winner = key;
		            	max = candidate_votes;
		            }
		            System.out.println("Candidato: " + key + "\t\tQuantidade de votos: " + candidateList.get(key));
		        }
				
				System.out.println("Vencedor: " + winner);
				return true;
			}
			return false;
		}
		
		@Override
		public void action() {
			
			
			ACLMessage message = myAgent.receive();
			
			if (message != null)
			{
				String content = message.getContent();
				boolean found = false;
				for (int i = 0; i < aid_candidates.length; i++)
				{
					if (aid_candidates[i].getLocalName().equals(content))
					{
						Integer votes = candidateList.get(content);
						candidateList.put(content, (votes == null ? 0 : votes) + 1);
						found = true;
					}
				}
				
				if (!found)
				{
					System.out.println("Candidato \"" + content + "\" nao existe, voto nulo");
					candidateList.put("Nulo", candidateList.get("Nulo") + 1);
				}
				// as Hashtable values need to be wrapped, Integer needs to be redeclared with the incremented value
				numberOfMessages++;
			}
			else
			{
				block();
			}
			
		}
		
	}
	
	protected Hashtable<String, Integer> populateCandidates()
	{
		try {
			AgentContainer container = (AgentContainer)getContainerController(); // get a container controller for creating new agents
			t1 = container.createNewAgent("Temer", "agent.CandidateAgent", null);
			t2 = container.createNewAgent("Andre", "agent.CandidateAgent", null);
			t3 = container.createNewAgent("Henrique", "agent.CandidateAgent", null);
			t1.start();
			t2.start();
			t3.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
		Hashtable<String, Integer> _candidates = new Hashtable<String, Integer>();
		_candidates.put("Temer", 0);
		_candidates.put("Andre", 0);
		_candidates.put("Henrique", 0);
		_candidates.put("Nulo", 0);
		
		return _candidates;
	}
	
	
  @Override
  protected void setup() 
  {
	  System.out.println("Initializing the system");
	  
	  candidateList = populateCandidates();
	  
	  receiveVotes rv = new receiveVotes(this);
	  addBehaviour(rv);
	  
	  GetCandidates lol = new GetCandidates(this);
	  addBehaviour(lol);
  }
  
  @Override
  protected void takeDown() 
  {
    System.out.println("Closing Voting Machine...");
  }
}
