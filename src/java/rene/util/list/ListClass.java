package rene.util.list;

/**
 * A class for a list of things. The list is forward and backward
 * chained.
 */

public class ListClass {
// ------------------------------ FIELDS ------------------------------
	ListElement First, Last; // Pointer to start and end of list.

// --------------------------- CONSTRUCTORS ---------------------------

	/**
	 * Generate an empty list.
	 */
	public ListClass() {
		First = null;
		Last = null;
	}

// -------------------------- OTHER METHODS --------------------------

	/**
	 * @return First ListElement.
	 */
	public ListElement first() {
		return First;
	}

	/*
	@param l ListElement to be inserted.
	@param after If null, it works like prepend.
	*/
	public void insert(ListElement l, ListElement after) {
		if (after == Last) append(l);
		else if (after == null) prepend(l);
		else {
			after.next().previous(l);
			l.next(after.next());
			after.next(l);
			l.previous(after);
			l.list(this);
		}
	}

	/**
	 * Append a node to the list
	 */
	public void append(ListElement l) {
		if (Last == null) init(l);
		else {
			Last.next(l);
			l.previous(Last);
			Last = l;
			l.next(null);
			l.list(this);
		}
	}

	/**
	 * initialize the list with a single element.
	 */
	public void init(ListElement l) {
		Last = First = l;
		l.previous(null);
		l.next(null);
		l.list(this);
	}

	public void prepend(ListElement l)
	// prepend a node to the list
	{
		if (First == null) init(l);
		else {
			First.previous(l);
			l.next(First);
			First = l;
			l.previous(null);
			l.list(this);
		}
	}

	/**
	 * @return Last ListElement.
	 */
	public ListElement last() {
		return Last;
	}

	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("ListClass");
		sb.append("{First=").append(First);
		sb.append('}');
		return sb.toString();
	}

	/**
	 * Remove a node from the list.
	 * The node really should be in the list, which is not checked.
	 */
	public void remove(ListElement l) {
		if (First == l) {
			First = l.next();
			if (First != null) First.previous(null);
			else Last = null;
		} else if (Last == l) {
			Last = l.previous();
			if (Last != null) Last.next(null);
			else First = null;
		} else {
			l.previous().next(l.next());
			l.next().previous(l.previous());
		}
		l.next(null);
		l.previous(null);
		l.list(null);
	}

	/**
	 * remove everything after e
	 */
	public void removeAfter(ListElement e) {
		e.next(null);
		Last = e;
	}

	/**
	 * Empty the list.
	 */
	public void removeall() {
		First = null;
		Last = null;
	}
}

