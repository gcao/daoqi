package jagoclient.board;

import rene.util.list.Tree;

/**
 * This is a child class of Tree, with some help functions for
 * the content type Node.
 *
 * @see Node
 */

public class TreeNode extends Tree {
// --------------------------- CONSTRUCTORS ---------------------------

	/**
	 * initialize with a given Node
	 */
	public TreeNode(Node n) {
		super(n);
	}

	/**
	 * initialize with an empty node with the specified number
	 */
	public TreeNode(int number) {
		super(new Node(number));
	}

// -------------------------- OTHER METHODS --------------------------

	/**
	 * add this action to the node
	 */
	public void addaction(Action a) {
		node().addaction(a);
	}

	public TreeNode firstChild() {
		return (TreeNode) firstchild();
	}

	/**
	 * get the value of the action of this type
	 */
	public String getaction(String type) {
		return node().getaction(type);
	}

	/**
	 * @return true if it the last node in the main tree
	 */
	public boolean isLastMain() {
		return !haschildren() && isMain();
	}

	/**
	 * @return true if it is a main node
	 */
	public boolean isMain() {
		return node().main();
	}

	public TreeNode lastChild() {
		return (TreeNode) lastchild();
	}

	public Node node() {
		return ((Node) content());
	}

	public TreeNode parentPos() {
		return (TreeNode) parent();
	}

	public void setaction(String type, String s) {
		node().setaction(type, s);
	}

	/**
	 * Set the action type in the node to the string s.
	 *
	 * @param flag determines, if the action is to be added, even of s is emtpy.
	 */
	public void setaction(String type, String s, boolean flag) {
		node().setaction(type, s, flag);
	}

	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("TreeNode");
		sb.append("{node=").append(node());
		sb.append(", \nfirstChild=").append(firstChild());
		sb.append("\n}");
		return sb.toString();
	}

	// --------------------------- main() method ---------------------------

	/**
	 * set the main flag in the node
	 */
	public void main(boolean flag) {
		node().main(flag);
	}
}
