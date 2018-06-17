package agent;
import jade.core.*; 
import jade.core.behaviours.*; 

public class VotingMachine extends Agent{
	
	
	private static final long serialVersionUID = -3657633911205663525L;

	
	  @Override
	  protected void setup() {
	    System.out.println("Initializing Voting Machine.");
	  }
	  
	  @Override
	  protected void takeDown() {
	    System.out.println("Closing Voting Machine.");
	  }
}
