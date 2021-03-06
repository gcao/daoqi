package jagoclient.board;

import rene.util.list.ListClass;
import rene.util.list.ListElement;
import rene.util.list.Tree;
import rene.util.xml.XmlWriter;

import java.io.PrintWriter;

/**
 * A node has
 * <UL>
 * <LI> a list of actions and a number counter (the number is the number
 * of the next expected move in the game tree),
 * <LI> a flag, if the node is in the main game tree,
 * <LI> a list of changes in this node to be able to undo the node,
 * <LI> the changes in the prisoner count in this node.
 * </UL>
 *
 * @see Action
 * @see Change
 */

class Node {
// ------------------------------ FIELDS ------------------------------

	public int Pw, Pb; // changes in prisoners in this node
	ListClass Actions; // actions and variations
	int N; // next exptected number
	boolean Main; // belongs to main variation
	ListClass Changes;

// --------------------------- CONSTRUCTORS ---------------------------

	/**
	 * initialize with the expected number
	 */
	public Node(int n) {
		Actions = new ListClass();
		N = n;
		Main = false;
		Changes = new ListClass();
		Pw = Pb = 0;
	}

// ------------------------ CANONICAL METHODS ------------------------

	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("Node");
		sb.append("{N=").append(N);
		sb.append(", Main=").append(Main);
		sb.append(", Pw=").append(Pw);
		sb.append(", Pb=").append(Pb);
		sb.append(", Changes=").append(Changes);
		sb.append(", Actions=").append(Actions);
		sb.append('}');
		return sb.toString();
	}

// -------------------------- OTHER METHODS --------------------------

	// access methods:
	public ListElement actions() {
		return Actions.first();
	}

	/**
	 * add a new change to this node
	 */
	public void addchange(Change c) {
		Changes.append(new ListElement(c));
	}

	public ListElement changes() {
		return Changes.first();
	}

	/**
	 * clear the list of changes
	 */
	public void clearchanges() {
		Changes.removeall();
	}

	/**
	 * find the action and a specified tag
	 */
	public boolean contains(String s, String argument) {
		ListElement p = find(s);
		if (p == null) return false;
		Action a = (Action) p.content();
		return a.contains(argument);
	}

	/**
	 * find the list element containing the action of type s
	 */
	ListElement find(String s) {
		ListElement p = Actions.first();
		while (p != null) {
			Action a = (Action) p.content();
			if (a.type().equals(s)) return p;
			p = p.next();
		}
		return null;
	}

	/**
	 * Copy an action from another node.
	 */
	public void copyAction(Node n, String action) {
		if (n.contains(action)) {
			expandaction(new Action(action, n.getaction(action)));
		}
	}

	/**
	 * see if the list contains an action of type s
	 */
	public boolean contains(String s) {
		return find(s) != null;
	}

	/**
	 * expand an action of the same type as a, else generate a new action
	 */
	public void expandaction(Action a) {
		ListElement p = find(a.type());
		if (p == null) addaction(a);
		else {
			Action pa = (Action) p.content();
			pa.addargument(a.argument());
		}
	}

	/**
	 * add an action (at end)
	 */
	public void addaction(Action a) {
		Actions.append(new ListElement(a));
	}

	/**
	 * get the argument of this action (or "")
	 */
	public String getaction(String type) {
		ListElement l = Actions.first();
		while (l != null) {
			Action a = (Action) l.content();
			if (a.type().equals(type)) {
				ListElement la = a.arguments();
				if (la != null) return (String) la.content();
				else return "";
			}
			l = l.next();
		}
		return "";
	}

	/**
	 * Insert an action after p.
	 * p <b>must</b> have content type action.
	 */
	public void insertaction(Action a, ListElement p) {
		Actions.insert(new ListElement(a), p);
	}

	public ListElement lastaction() {
		return Actions.last();
	}

	public ListElement lastchange() {
		return Changes.last();
	}

	public int number() {
		return N;
	}

	public void number(int n) {
		N = n;
	}

	/**
	 * Print the node in SGF.
	 *
	 * @see Action#print
	 */
	public void print(PrintWriter o) {
		o.print(";");
		ListElement p = Actions.first();
		Action a;
		while (p != null) {
			a = (Action) p.content();
			a.print(o);
			p = p.next();
		}
		o.println("");
	}

	public void print(XmlWriter xml, int size) {
		int count = 0;
		Action ra = null, a;
		ListElement p = Actions.first();
		while (p != null) {
			a = (Action) p.content();
			if (a.isRelevant()) {
				count++;
				ra = a;
			}
			p = p.next();
		}
		if (count == 0 && !contains("C")) {
			xml.finishTagNewLine("Node");
			return;
		}
		int number = N - 1;
		if (count == 1) {
			if (ra.type().equals("B") || ra.type().equals("W")) {
				ra.printMove(xml, size, number, this);
				number++;
				if (contains("C")) {
					a = ((Action) find("C").content());
					a.print(xml, size, number);
				}
				return;
			}
		}
		xml.startTagStart("Node");
		if (contains("N")) xml.printArg("name", getaction("N"));
		if (contains("BL")) xml.printArg("blacktime", getaction("BL"));
		if (contains("WL")) xml.printArg("whitetime", getaction("WL"));
		xml.startTagEndNewLine();
		p = Actions.first();
		while (p != null) {
			a = (Action) p.content();
			a.print(xml, size, number);
			if (a.type().equals("B") || a.type().equals("W")) number++;
			p = p.next();
		}
		xml.endTagNewLine("Node");
	}

	/**
	 * remove an action
	 */
	public void removeaction(ListElement la) {
		Actions.remove(la);
	}

	/**
	 * remove all actions
	 */
	public void removeactions() {
		Actions = new ListClass();
	}

	/**
	 * set the action of this type to this argument
	 */
	public void setaction(String type, String arg) {
		setaction(type, arg, false);
	}

	/**
	 * If there is an action of the type:
	 * Remove it, if arg is "", else set its argument to arg.
	 * Else add a new action in front (if it is true)
	 */
	public void setaction(String type, String arg, boolean front) {
		ListElement l = Actions.first();
		while (l != null) {
			Action a = (Action) l.content();
			if (a.type().equals(type)) {
				if (arg.equals("")) {
					Actions.remove(l);
					return;
				} else {
					ListElement la = a.arguments();
					if (la != null) la.content(arg);
					else a.addargument(arg);
				}
				return;
			}
			l = l.next();
		}
		if (front) prependaction(new Action(type, arg));
		else addaction(new Action(type, arg));
	}

	/**
	 * add an action (at front)
	 */
	public void prependaction(Action a) {
		Actions.prepend(new ListElement(a));
	}

	/**
	 * Expand an action of the same type as a, else generate a new action.
	 * If the action is already present with the same argument, delete
	 * that argument from the action.
	 */
	public void toggleaction(Action a) {
		ListElement p = find(a.type());
		if (p == null) addaction(a);
		else {
			Action pa = (Action) p.content();
			pa.toggleargument(a.argument());
		}
	}

// --------------------------- main() method ---------------------------

	public boolean main() {
		return Main;
	}

	// modification methods:
	public void main(boolean m) {
		Main = m;
	}

	/**
	 * Set the Main flag
	 *
	 * @param p is the tree, which contains this node on root.
	 */
	public void main(Tree p) {
		Main = false;
		try {
			if (((Node) p.content()).main()) {
				Main = (this == ((Node) p.firstchild().content()));
			} else if (p.parent() == null) Main = true;
		}
		catch (Exception e) {
		}
	}
}
