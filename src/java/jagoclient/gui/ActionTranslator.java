package jagoclient.gui;

import java.awt.event.*;

public class ActionTranslator implements ActionListener
{   String Name;
    DoActionListener C;
    public ActionTranslator (DoActionListener c, String name)
    {   Name=name; C=c;
    }
    public void actionPerformed (ActionEvent e)
    {   C.doAction(Name);
    }
    public void setString (String s)
    {	Name=s;
    }
}
