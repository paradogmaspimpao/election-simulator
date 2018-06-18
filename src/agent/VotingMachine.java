package agent;
import jade.core.*; 
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import java.util.*;

public class VotingMachine extends Agent
{	
	private static final long serialVersionUID = -3657633911205663525L;
	
	private AID[] listaCandi;
	
	private Hashtable<String, Integer> candidateList;
	
	private class peneira extends OneShotBehaviour{

		public peneira(VotingMachine votingMachine) {
			super(votingMachine);
		}

		@Override
		public void action() {
			
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("Candidate ");
			template.addServices(sd);
			try {
				 DFAgentDescription[] result = DFService.search(myAgent, template);
				 listaCandi = new AID[result.length];
				 for (int i = 0; i < result.length; ++i) {
					 listaCandi[i] = result[i].getName();
					 System.out.println(listaCandi[i].getName());
				 }
				 }
				 catch (FIPAException fe) {
				 fe.printStackTrace();
				 }
			
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
			
			if (numberOfMessages == 3) 
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
				System.out.println("I received a message!");
				String content = message.getContent();

				// as Hashtable values need to be wrapped, Integer needs to be redeclared with the incremented value
				try
				{
					candidateList.put(content, candidateList.get(content) + 1);
				}catch(NullPointerException e)
				{
					System.out.println("Candidate not found");
				}

				System.out.println("CONTENT: " +  content);
				System.out.println("REPLY: " + message.getReplyWith());
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
		
		Hashtable<String, Integer> _candidates = new Hashtable<String, Integer>();
		_candidates.put("Jair", 0);
		_candidates.put("Andre", 0);
		_candidates.put("Henrique", 0);
		
		return _candidates;
	}
	
	
  @Override
  protected void setup() 
  {
	  System.out.println("Initializing the system");
	  
	  candidateList = populateCandidates();
	  
	  receiveVotes rv = new receiveVotes(this);
	  addBehaviour(rv);
	  
	  peneira lol = new peneira(this);
	  addBehaviour(lol);
  }
  
  @Override
  protected void takeDown() 
  {
    System.out.println("Closing Voting Machine...");
  }
}
