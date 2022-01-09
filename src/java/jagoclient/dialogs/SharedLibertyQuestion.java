package jagoclient.dialogs;

import jagoclient.Global;
import jagoclient.gui.*;

import java.awt.*;

public class SharedLibertyQuestion extends CloseDialog {
    int sharedLiberties;
    TextField sharedLibertiesField;

    public SharedLibertyQuestion(Frame f) {
        super(f, Global.resourceString("Enter_Shared_Liberties"), true);
        Panel n = new MyPanel();
        n.setLayout(new BorderLayout());
        n.add("North", new MyLabel(Global.resourceString("Enter_Shared_Liberties")));
        n.add("Center", sharedLibertiesField = new TextFieldAction(this, "Input", 25));
        sharedLibertiesField.setText("0");
        add("Center", new Panel3D(n));
        Panel p = new MyPanel();
        p.add(new ButtonAction(this, Global.resourceString("OK")));
        add("South", new Panel3D(p));
        Global.setpacked(this, "getparameter", 300, 150, f);
        validate();
        sharedLibertiesField.addKeyListener(this);
        setVisible(true);
    }

    public void doAction(String o) {
        if (o.equals(Global.resourceString("OK"))) {
            try {
                sharedLiberties = Integer.parseInt(sharedLibertiesField.getText().trim());
            } catch (Exception e) {
                return;
            }
            setVisible(false);
            dispose();
        } else
            super.doAction(o);
    }

    public int getSharedLiberties() {
        return sharedLiberties;
    }
}