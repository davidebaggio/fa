import java.util.Scanner;

public class Transition {
	public State prevState;
	public String label;
	public State nextState;

	public Transition(State prevState, String label, State nextState) {
		this.prevState = prevState;
		this.label = label;
		this.nextState = nextState;
	}

	public Transition(String t) {
		Scanner scanner = new Scanner(t).useDelimiter("->");
		if (scanner.hasNext()) {
			String prevState = scanner.next().strip();
			this.prevState = new State(prevState);
		} else {
			System.out.println("Invalid transition");
			scanner.close();
			System.exit(1);
		}
		if (scanner.hasNext()) {
			String label = scanner.next().strip();
			this.label = label;
		} else {
			System.out.println("Invalid transition");
			scanner.close();
			System.exit(1);
		}
		if (scanner.hasNext()) {
			String nextState = scanner.next().strip();
			this.nextState = new State(nextState);
		} else {
			System.out.println("Invalid transition");
			scanner.close();
			System.exit(1);
		}
	}

	public String printTransition() {
		return prevState.label + " -> " + label + " -> " + nextState.label;
	}
}
