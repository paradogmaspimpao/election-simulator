package agent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import jade.core.*; 
import jade.core.behaviours.*; 
import jade.lang.acl.ACLMessage;

public class Voter extends Agent{

private static final long serialVersionUID = -6461105609727551278L;

	class Vote extends OneShotBehaviour {
		
		public Vote(Agent a)
		{
			super(a);
		}
		
		private String printDate()
		{
			long epoch = System.currentTimeMillis();
			SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			Date r_date = new Date(epoch);
			
			return date.format(r_date);
		}
	
		private static final long serialVersionUID = -1447880119921839792L;
		
		private String targetCandidate;
		private Scanner sc = new Scanner(System.in);
		
		public void action() {
			System.out.println("Insira o seu candidato: ");
			targetCandidate = sc.nextLine();
			
			ACLMessage vote = new ACLMessage(ACLMessage.INFORM);
			
			vote.addReceiver(new AID("urna", AID.ISLOCALNAME));
			vote.setContent(targetCandidate);
			vote.setConversationId("voto");
			vote.setReplyWith("Voto em " + targetCandidate + " " + printDate());
			
			System.out.println("Sending vote...");
			myAgent.send(vote);
		}
	}
	
	@Override
	protected void setup() {
		System.out.println("Initializing Voting Machine.");
		addBehaviour(new Vote(this));
	}
  
	@Override
	protected void takeDown() {
		System.out.println("Closing Voting Machine.");
	}
}
