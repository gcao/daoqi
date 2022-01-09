package jagoclient.board;

/**
 * Holds position changes at one field.
 */

public class Change {
// ------------------------------ FIELDS ------------------------------

	public int I, J, C;
	public int N;

// --------------------------- CONSTRUCTORS ---------------------------

	public Change(int i, int j, int c) {
		this(i, j, c, 0);
	}

	/**
	 * Board position i,j changed from c.
	 */
	public Change(int i, int j, int c, int n) {
		I = i;
		J = j;
		C = c;
		N = n;
	}

// ------------------------ CANONICAL METHODS ------------------------

	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("Change");
		sb.append("{N=").append(N);
		sb.append(", C=").append(C);
		sb.append(", I=").append(I);
		sb.append(", J=").append(J);
		sb.append('}');
		return sb.toString();
	}
}
