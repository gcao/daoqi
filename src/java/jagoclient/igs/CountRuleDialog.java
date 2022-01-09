package jagoclient.igs;

import jagoclient.Global;
import jagoclient.Rule;
import jagoclient.igs.oob.OOBCommand;
import jagoclient.igs.oob.OOBCountRule;
import jagoclient.gui.ButtonAction;
import jagoclient.gui.CloseDialog;
import jagoclient.gui.MyLabel;
import jagoclient.gui.MyPanel;
import jagoclient.sound.JagoSound;

import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * This dialog is opened, when a count rule is suggested.
 */

public class CountRuleDialog extends CloseDialog {
    int countRule;
    ConnectionFrame connectionFrame;
    ButtonAction acceptButton, declineButton;

    public CountRuleDialog(ConnectionFrame connectionFrame, int countRule) {
        super(connectionFrame, Global.resourceString("Message"), false);
        this.connectionFrame = connectionFrame;
        this.countRule = countRule;
        add("North", new MyLabel(Global.resourceString("Message")));
        Panel pm = new MyPanel();
        pm.setLayout(new BorderLayout());
        Label content;
        pm.add("Center", content = new Label());
        content.setFont(Global.SansSerif);
        if (Global.Background != null) content.setBackground(Global.Background);
        String ruleName = Rule.getRule(countRule).toString();
        content.setText(Global.resourceString("Your_Opponent_Suggested_To_Use_Rule")+ruleName);
        add("Center", pm);
        Panel p = new MyPanel();
        p.add(acceptButton = new ButtonAction(this, Global.resourceString("Accept")));
        p.add(declineButton = new ButtonAction(this, Global.resourceString("Decline")));
        add("South", p);
        validate();
        Global.setwindow(this, "countruledialog", 300, 100);
        setVisible(true);
        JagoSound.play("game", "wip", true);
    }

    public void windowOpened(WindowEvent e) {
        acceptButton.requestFocus();
    }

    public void doAction(String o) {
        Global.notewindow(this, "countruledialog");
        if (Global.resourceString("Accept").equals(o)) {
            IgsGoFrame goFrame = connectionFrame.getGoFrame(connectionFrame.getGameInPlay());
            goFrame.setCountRule(countRule);
            OOBCountRule ruleCommand = new OOBCountRule();
            ruleCommand.setCommandType(OOBCommand.COUNT_RULE_ACCEPT);
            ruleCommand.setCountRule(countRule);
            connectionFrame.oobSendCommand(OOBCommand.IGNORE_ME_COMMAND);
            connectionFrame.oobSendCommand(ruleCommand);
            setVisible(false);
            dispose();
        } else if (Global.resourceString("Decline").equals(o)) {
            OOBCountRule ruleCommand = new OOBCountRule();
            ruleCommand.setCommandType(OOBCommand.COUNT_RULE_DECLINE);
            ruleCommand.setCountRule(countRule);
            connectionFrame.oobSendCommand(OOBCommand.IGNORE_ME_COMMAND);
            connectionFrame.oobSendCommand(ruleCommand);
            setVisible(false);
            dispose();
        }
    }

    public boolean close() {
        return true;
    }
}

