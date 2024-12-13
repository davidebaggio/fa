import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Automata {
	public ArrayList<String> alphabet = new ArrayList<String>();
	public ArrayList<State> states = new ArrayList<State>();
	public ArrayList<Transition> transitions = new ArrayList<Transition>();
	public State initialState;
	public ArrayList<State> finalStates = new ArrayList<State>();
	public State currentState;

	public Automata(ArrayList<String> alphabet, ArrayList<State> states, ArrayList<Transition> transitions,
			State initialState,
			ArrayList<State> finalStates) {
		this.alphabet = alphabet;
		this.states = states;
		this.transitions = transitions;
		this.initialState = initialState;
		this.finalStates = finalStates;
		this.currentState = initialState;
	}

	public static void main(String[] args) throws FileNotFoundException {
		if (args.length <= 0) {
			System.out.println("Please provide a file path as an argument and the string to recognize.");
			System.exit(1);
		}
		Automata automata = constructAutomataFromFile(args[0]);
		printAutomata(automata);
		System.out.println("--------------------------------");

		String input = args[1];
		if (recognizeString(automata, input)) {
			System.out.println("Current state: " + automata.currentState.label);
			System.out.println("String [ " + input + " ] is recognized by the automata\n");
		} else {
			System.out.println("Current state: " + automata.currentState.label);
			System.out.println("String [ " + input + " ] is not recognized by the automata\n");
		}
	}

	public static boolean recognizeString(Automata automata, String input) {
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			for (Transition t : automata.transitions) {
				// System.out.println("|" + automata.currentState.label + "| - |" +
				// t.prevState.label + "|");
				if (c == t.label.charAt(0) && automata.currentState.label.strip().equals(t.prevState.label.strip())) {
					if (automata.contains(t.nextState)) {
						//printOp(automata.currentState, c, t.nextState);
						automata.currentState = t.nextState;
						break;
					} else {
						System.out.println("ERROR: State \" " + t.nextState.label + " \" is not in the automata");
						return false;
					}
				}
			}
		}
		if (automata.isFinal(automata.currentState)) {
			return true;
		}
		return false;
	}

	public static void printAutomata(Automata automata) {
		System.out.print("Alphabet: {");
		for (String el : automata.alphabet) {
			System.out.print(" " + el);
		}
		System.out.println(" }");

		System.out.print("States: {");
		for (State el : automata.states) {
			System.out.print(" " + el.label);
		}
		System.out.println(" }");

		System.out.print("Transition: \n\t{\n");
		for (Transition el : automata.transitions) {
			System.out.println("\t" + el.printTransition());
		}
		System.out.println("\t}");

		System.out.println("Initial State: " + automata.initialState.label);

		System.out.print("Final States: {");
		for (State el : automata.finalStates) {
			System.out.print(" " + el.label);
		}
		System.out.println(" }");

		System.out.println("Current State: " + automata.currentState.label);
	}

	public boolean contains(State s) {
		for (State c : this.states) {
			if (s.label.equals(c.label)) {
				return true;
			}
		}
		return false;
	}

	public boolean isFinal(State s) {
		for (State c : this.finalStates) {
			if (s.label.equals(c.label)) {
				return true;
			}
		}
		return false;
	}

	public static void printOp(State s, char c, State f) {
		System.out.println("Reading: \"" + c + "\"\tFrom " + s.label + " -> " + f.label);
	}

	public static Automata constructAutomataFromFile(String filepath) throws FileNotFoundException {
		File f = new File(filepath);
		if (!f.exists()) {
			System.out.println("File does not exist");
			System.exit(1);
		}

		System.out.println("Loading automata from file.\n--------------------------------");
		ArrayList<String> alphabet = new ArrayList<String>();
		ArrayList<String> states = new ArrayList<String>();
		ArrayList<String> transitions = new ArrayList<String>();
		String initial = null;
		ArrayList<String> finalStates = new ArrayList<String>();

		Scanner scanner = new Scanner(f).useDelimiter("#");
		// read file
		while (scanner.hasNext()) {
			String arg = scanner.next();
			Scanner info = new Scanner(arg).useDelimiter(":");
			if (info.hasNext()) {
				String label = info.next();
				// System.out.println(label);
				String[] rules = info.next().split(",");
				if (label.strip().equals("alphabet")) {
					// System.out.println("Alphabeth ----");
					alphabet = new ArrayList<String>(Arrays.asList(rules));
					for (int i = 0; i < alphabet.size(); i++) {
						alphabet.set(i, alphabet.get(i).strip());
					}
				} else if (label.strip().equals("states")) {
					// System.out.println("States ----");
					states = new ArrayList<String>(Arrays.asList(rules));
					for (int i = 0; i < states.size(); i++) {
						states.set(i, states.get(i).strip());
					}
				} else if (label.strip().equals("transitions")) {
					// System.out.println("Transitions ----");
					transitions = new ArrayList<String>(Arrays.asList(rules));
					for (int i = 0; i < transitions.size(); i++) {
						transitions.set(i, transitions.get(i).strip());
					}
				} else if (label.strip().equals("initial")) {
					// System.out.println("Initial ----");
					initial = rules[0];
				} else if (label.strip().equals("finals")) {
					// System.out.println("Finals ----");
					finalStates = new ArrayList<String>(Arrays.asList(rules));
					for (int i = 0; i < finalStates.size(); i++) {
						finalStates.set(i, finalStates.get(i).strip());
					}
				} else {
					System.out.println("Invalid file");
					System.exit(1);
				}
			}
			info.close();
		}
		scanner.close();
		if (alphabet.isEmpty() || states.isEmpty() || transitions.isEmpty() || initial == null
				|| finalStates.isEmpty()) {
			System.out.println("Invalid file");
			System.exit(1);
		}

		ArrayList<State> s = new ArrayList<State>();
		for (int i = 0; i < states.size(); i++) {
			s.add(new State(states.get(i)));
		}

		ArrayList<Transition> t = new ArrayList<Transition>();
		for (int i = 0; i < transitions.size(); i++) {
			t.add(new Transition(transitions.get(i)));
		}

		State init = new State(initial);

		ArrayList<State> fin = new ArrayList<State>();
		for (int i = 0; i < finalStates.size(); i++) {
			fin.add(new State(finalStates.get(i)));
		}

		Automata automata = new Automata(alphabet, s, t, init, fin);
		return automata;
	}
}