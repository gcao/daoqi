package jagoclient.igs;

import jagoclient.GameResult;
import jagoclient.Global;
import jagoclient.gui.ButtonAction;
import jagoclient.gui.CloseDialog;
import jagoclient.gui.MyLabel;
import jagoclient.gui.MyPanel;
import jagoclient.igs.oob.OOBCommand;
import jagoclient.igs.oob.OOBResult;
import jagoclient.sound.JagoSound;
import rene.viewer.Viewer;

import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * This dialog is opened, when a count rule is suggested.
 */

public class ResultSuggestDialog extends CloseDialog {
    GameResult result;
    ConnectionFrame connectionFrame;
    ButtonAction acceptButton, declineButton;

    public ResultSuggestDialog(ConnectionFrame connectionFrame, GameResult result) {
        super(connectionFrame, Global.resourceString("Message"), false);
        this.connectionFrame = connectionFrame;
        this.result = result;
        add("North", new MyLabel(Global.resourceString("Message")));
        Panel pm = new MyPanel();
        pm.setLayout(new BorderLayout());
        Viewer content;
        pm.add("Center", content = new Viewer());
        content.setFont(Global.SansSerif);
        if (Global.Background != null) content.setBackground(Global.Background);
        content.setText(Global.resourceString("Result_")+"\n\n"+result.toLongString());
        add("Center", pm);
        Panel p = new MyPanel();
        p.add(acceptButton = new ButtonAction(this, Global.resourceString("Accept")));
        p.add(declineButton = new ButtonAction(this, Global.resourceString("Decline")));
        add("South", p);
        validate();
        Global.setwindow(this, "resultdialog", 300, 100);
        setVisible(true);
        JagoSound.play("game", "wip", true);
    }

    public void windowOpened(WindowEvent e) {
        acceptButton.requestFocus();
    }

    public void doAction(String o) {
        Global.notewindow(this, "resultdialog");
        if (Global.resourceString("Accept").equals(o)) {
            IgsGoFrame goFrame = connectionFrame.getGoFrame(connectionFrame.getGameInPlay());
            OOBResult resultCommand = new OOBResult();
            resultCommand.setCommandType(OOBCommand.RESULT_ACCEPT);
            resultCommand.setResult(result);
            connectionFrame.oobSendCommand(OOBCommand.IGNORE_ME_COMMAND);
            connectionFrame.oobSendCommand(resultCommand);
            setVisible(false);
            dispose();
            goFrame.finishGame(result);
        } else if (Global.resourceString("Decline").equals(o)) {
            OOBResult resultCommand = new OOBResult();
            resultCommand.setCommandType(OOBCommand.RESULT_DECLINE);
            resultCommand.setResult(result);
            connectionFrame.oobSendCommand(OOBCommand.IGNORE_ME_COMMAND);
            connectionFrame.oobSendCommand(resultCommand);
            setVisible(false);
            dispose();
        }
    }

    public boolean close() {
        return true;
    }
}

