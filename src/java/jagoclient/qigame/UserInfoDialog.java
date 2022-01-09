package jagoclient.qigame;

import jagoclient.Global;
import jagoclient.gui.*;
import net.lemurnetworks.util.StringUtil;

import java.awt.*;

public class UserInfoDialog extends CloseDialog {
    TextField User, Password;
    Frame F;

    public UserInfoDialog(CloseFrame f) {
        super(f, Global.resourceString("Enter_Qigame_User_Info"), true);
        F = f;
        Panel p1 = new MyPanel();
        p1.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 2;
        c.ipady = 1;
        c.gridx = 0; c.gridy = 0; c.weightx = 0.5;
        p1.add(new MyLabel(Global.resourceString("Username")), c);
        c.gridx = 1; c.gridy = 0;
        p1.add(User = new FormTextField(Util.getQigameUsername()), c);
        c.gridx = 0; c.gridy = 1;
        p1.add(new MyLabel(Global.resourceString("Password")), c);
        c.gridx = 1; c.gridy = 1;
        p1.add(Password = new FormTextField(Util.getQigamePassword()), c);
        add("Center", new Panel3D(p1));
        Password.setEchoChar('*');
        Panel panel = new MyPanel();
        panel.add(new ButtonAction(this, Global.resourceString("OK")));
        panel.add(new ButtonAction(this, Global.resourceString("Cancel")));
        add("South", new Panel3D(panel));
        Global.setpacked(this, "edit", 380, 260, F);
        validate();
        setVisible(true);
        User.requestFocus();
    }

    public void doAction(String o) {
        Global.notewindow(this, "edit");
        if (Global.resourceString("OK").equals(o)) {
            Util.setQigameUsername(User.getText().trim());
            Util.setQigamePassword(Password.getText());
            setVisible(false);
            dispose();
        } else if (Global.resourceString("Cancel").equals(o)) {
            setVisible(false);
            dispose();
        } else
            super.doAction(o);
    }
}

