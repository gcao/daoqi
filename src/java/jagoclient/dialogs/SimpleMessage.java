package jagoclient.dialogs;

import jagoclient.Global;
import jagoclient.gui.*;

import java.awt.*;

import rene.viewer.Viewer;

/**
 * This class is used to display single line message.
 */

public class SimpleMessage extends CloseDialog {
    public SimpleMessage(Frame f, String m, boolean modal) {
        this(f, m, 300, 200, modal);
    }

    public SimpleMessage(Frame f, String m, int width, int height, boolean modal) {
        super(f, Global.resourceString("Message"), false);
        Viewer content = new Viewer();
        add("North", content);
        content.setText(m);
        //content.setFont(Global.Monospaced);
        Panel p = new MyPanel();
        p.add(new ButtonAction(this, Global.resourceString("OK")));
        add("South", new Panel3D(p));
        validate();
        Global.setwindow(this, "simplemessage", width, height);
        setVisible(true);
        setModal(modal);
    }

    public void doAction(String o) {
        Global.notewindow(this, "simplemessage");
        if (Global.resourceString("OK").equals(o)) {
            setVisible(false);
            dispose();
        } else
            super.doAction(o);
    }
}

