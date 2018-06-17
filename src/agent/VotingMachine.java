package agent;
import jade.core.*; 
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage; 

public class VotingMachine extends Agent
{	
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
			if (numberOfMessages == 3) 
			{
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
	  receiveVotes rv = new receiveVotes(this);
	  addBehaviour(rv);
  }
  
  @Override
  protected void takeDown() 
  {
    System.out.println("Closing Voting Machine...");
  }
}
