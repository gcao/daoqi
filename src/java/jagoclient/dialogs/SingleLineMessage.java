package jagoclient.dialogs;

import jagoclient.Global;
import jagoclient.gui.*;

import java.awt.*;

/**
 * This class is used to display single line message.
 */

public class SingleLineMessage extends CloseDialog {
    public SingleLineMessage(Frame f, String m, boolean modal) {
        this(f, m, 300, 200, modal);
    }

    public SingleLineMessage(Frame f, String m, int width, int height, boolean modal) {
        super(f, Global.resourceString("Message"), false);
        Label label = new Label(m);
        add("North", label);
        label.setFont(Global.Monospaced);
        Panel p = new MyPanel();
        p.add(new ButtonAction(this, Global.resourceString("OK")));
        add("South", new Panel3D(p));
        validate();
        Global.setwindow(this, "singlelinemessage", width, height);
        setVisible(true);
        setModal(modal);
    }

    public void doAction(String o) {
        Global.notewindow(this, "singlelinemessage");
        if (Global.resourceString("OK").equals(o)) {
            setVisible(false);
            dispose();
        } else
            super.doAction(o);
    }
}

