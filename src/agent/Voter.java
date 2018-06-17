package agent;
import jade.core.*; 
import jade.core.behaviours.*; 
import jade.lang.acl.ACLMessage;

public class Voter extends Agent{

private static final long serialVersionUID = -6461105609727551278L;

	class Vote extends OneShotBehaviour {
	
		private static final long serialVersionUID = -1447880119921839792L;
		
		private String targetCandidate;
		
		public void action() {
			System.out.println("Votando");
			ACLMessage vote = new ACLMessage(ACLMessage.INFORM);
			vote.addReceiver(new AID("VotingMachine", AID.ISLOCALNAME));
			vote.setContent(targetCandidate);
			vote.setConversationId("voto");
			vote.setReplyWith("Voto em " + targetCandidate + System.currentTimeMillis());
			myAgent.send(vote);
		}
	}
	
	@Override
	protected void setup() {
		System.out.println("Initializing Voting Machine.");
	}
  
	@Override
	protected void takeDown() {
		System.out.println("Closing Voting Machine.");
	}
}
