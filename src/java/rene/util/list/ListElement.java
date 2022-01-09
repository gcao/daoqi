package rene.util.list;

/**
 * The nodes of a list.
 */

public class ListElement
// A list node with pointers to previous and next element
// and with a content of type Object.
{
// ------------------------------ FIELDS ------------------------------

	ListElement Next, Previous; // the chain pointers
	Object Content; // the content of the node
	ListClass L; // Belongs to this list

// --------------------------- CONSTRUCTORS ---------------------------

	public ListElement(Object content)
	// get a new Element with the content and null pointers
	{
		Content = content;
		Next = Previous = null;
		L = null;
	}

// ------------------------ CANONICAL METHODS ------------------------

	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("ListElement");
		sb.append("{Content=").append(Content);
		sb.append(", Next=").append(Next);
		sb.append('}');
		return sb.toString();
	}

// -------------------------- OTHER METHODS --------------------------

	// access methods:
	public Object content() {
		return Content;
	}

	// modifying methods:
	public void content(Object o) {
		Content = o;
	}

	public ListClass list() {
		return L;
	}

	public void list(ListClass l) {
		L = l;
	}

	public ListElement next() {
		return Next;
	}

	public void next(ListElement o) {
		Next = o;
	}

	public ListElement previous() {
		return Previous;
	}

	public void previous(ListElement o) {
		Previous = o;
	}
}


