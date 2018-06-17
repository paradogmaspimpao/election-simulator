package agent;
import jade.core.*; 
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import java.util.*;

public class VotingMachine extends Agent
{	
	
	private Hashtable candidates;
	
	private class Candidates {
		
		Candidates(){
			candidates = new Hashtable();
			candidates.put("Bolsonaro", 0);
			candidates.put("Ciro", 0);
			candidates.put("Meirelles", 0);
		}
	}
	
	private static final long serialVersionUID = -3657633911205663525L;
	
	
	private class receiveVotes extends Behaviour
	{
		private int numberOfMessages = 0;
		
		public receiveVotes(Agent a)
		{
			super(a);
		}
		
		@Override
		public boolean done() {
			int max = 0;
			String vencedor = null;
			if (numberOfMessages == 3) 
			{
				Enumeration<String> enumeration = candidates.keys();
				while(enumeration.hasMoreElements()) {
		            String key = enumeration.nextElement();
		            if((int)candidates.get(key) > max){
		            	vencedor = key;
		            	max = (int)candidates.get(key);
		            }
		            System.out.println("Candidato: " + key + "\tQuantidade de votos: " + candidates.get(key));
		        }
				System.out.println("Vencedor: " + vencedor);
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

				//incrementa votos
				candidates.put(content, (int)candidates.get(content)+1);

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
	
	
  @Override
  protected void setup() 
  {
	  System.out.println("Initializing the system");
	  new Candidates();
	  receiveVotes rv = new receiveVotes(this);
	  addBehaviour(rv);
  }
  
  @Override
  protected void takeDown() 
  {
    System.out.println("Closing Voting Machine...");
  }
}
