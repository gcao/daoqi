import jagoclient.Global;

import javax.swing.*;

public class GoApplet extends JApplet {
// -------------------------- OTHER METHODS --------------------------

	synchronized public void init() {
		Global.setApplet(true);
		getContentPane().add(new AppletComponent(this));
	}
}