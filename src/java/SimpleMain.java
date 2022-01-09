import jagoclient.Defaults;
import jagoclient.Global;
import jagoclient.ServerTypeUtil;
import jagoclient.board.GoFrame;
import jagoclient.gui.ButtonAction;
import jagoclient.gui.DoActionListener;
import jagoclient.igs.Connect;
import jagoclient.igs.ConnectionFrame;
import jagoclient.igs.connection.Connection;
import rene.gui.CloseFrame;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2005-11-1
 * Time: 11:54:04
 * To change this template use File | Settings | File Templates.
 */
public class SimpleMain extends CloseFrame implements DoActionListener {

// ------------------------------ FIELDS ------------------------------
    public static final String WINDOW_NAME_KEY = "simplemain";
    public static final String SERVER_TYPE_KEY = "simplemainservertype";
    public static final String HOST_KEY = "simplemainhost";
    public static final String PORT_KEY = "simplemainport";
    public static final String USERNAME_KEY = "simplemainuser";
    public static final String PASSWORD_KEY = "simplemainpass";
    private Choice serverTypeChoice;
    private TextField hostField;
    private TextField portField;
    private TextField usernameField;
    private TextField passwordField;
    private Checkbox savePasswordBox;
// --------------------------- CONSTRUCTORS ---------------------------

    public SimpleMain() {
        seticon("ijago.gif");
        setTitle(Global.resourceString("Daoqi") + " " + Global.Version);
        setBackground(Global.ControlBackground);
        setLayout(new BorderLayout());
        Panel fieldsPanel = new Panel(new GridBagLayout());
        add("Center", fieldsPanel);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 2;
        c.ipady = 1;
        c.gridx = 0;
        c.gridy = 0;
        fieldsPanel.add(new Label(Global.resourceString("Server_Type")), c);
        c.gridx = 1;
        fieldsPanel.add(serverTypeChoice = ServerTypeUtil.createServerTypeChoice(), c);
        serverTypeChoice.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                int serverType = ServerTypeUtil.getServerType(serverTypeChoice);
                setServerType(serverType);
            }
        });
        c.gridx = 0;
        c.gridy++;
        fieldsPanel.add(new Label(Global.resourceString("Server")), c);
        hostField = new TextField(25);
        c.gridx = 1;
        fieldsPanel.add(hostField, c);
        c.gridx = 0;
        c.gridy++;
        fieldsPanel.add(new Label(Global.resourceString("Port")), c);
        portField = new TextField(25);
        c.gridx = 1;
        fieldsPanel.add(portField, c);
        c.gridx = 0;
        c.gridy++;
        fieldsPanel.add(new Label(Global.resourceString("Username")), c);
        usernameField = new TextField(25);
        c.gridx = 1;
        fieldsPanel.add(usernameField, c);
        c.gridx = 0;
        c.gridy++;
        fieldsPanel.add(new Label(Global.resourceString("Password")), c);
        passwordField = new TextField(25);
        passwordField.setEchoChar('*');
        c.gridx = 1;
        fieldsPanel.add(passwordField, c);
        c.gridx = 0;
        c.gridy++;
        fieldsPanel.add(new Label(Global.resourceString("Save_Password")), c);
        savePasswordBox = new Checkbox("");
        c.gridx = 1;
        fieldsPanel.add(savePasswordBox, c);
        Panel buttonsPanel = new Panel(new FlowLayout());
        add("South", buttonsPanel);
        ButtonAction localBoardBtn = new ButtonAction(this, Global.resourceString("Local_Board"));
        buttonsPanel.add(localBoardBtn);
        ButtonAction loginBtn = new ButtonAction(this, Global.resourceString("Login"));
        buttonsPanel.add(loginBtn);
        ButtonAction guestBtn = new ButtonAction(this, Global.resourceString("Guest"));
        buttonsPanel.add(guestBtn);
        Global.setwindow(this, WINDOW_NAME_KEY, 300, 200);
        int serverType = Global.getParameter(SERVER_TYPE_KEY, Global.IGS);
        ServerTypeUtil.setServerType(serverTypeChoice, serverType);
        setServerType(serverType);
        usernameField.setText(Global.getParameter(USERNAME_KEY, ""));
        passwordField.setText(getPassword());
        savePasswordBox.setState(Global.haveParameter(PASSWORD_KEY));
        setVisible(true);
    }

    private static String getPassword() {
        if (Global.haveParameter(PASSWORD_KEY)) {
            return Global.decrypt(Global.getParameter(PASSWORD_KEY, ""));
        } else {
            return "";
        }
    }

    private void setServerType(int serverType) {
        if (serverType == Global.IGS) {
            hostField.setText(Defaults.IGS_HOST);
            hostField.setEditable(false);
            portField.setText(Defaults.IGS_PORT_S);
            portField.setEditable(false);
        } else {
            hostField.setEditable(true);
            portField.setEditable(true);
        }
    }

    public SimpleMain(int serverType, String host, int port) {
        this();
        ServerTypeUtil.setServerType(serverTypeChoice, serverType);
        hostField.setText(host.trim());
        portField.setText(String.valueOf(port));
    }
// ------------------------ INTERFACE METHODS ------------------------

// --------------------- Interface DoActionListener ---------------------
    public void doAction(String o) {
        if (Global.resourceString("Local_Board").equals(o)) {
            GoFrame gf = new GoFrame(new Frame(), Global.resourceString("Local_Viewer"));
            saveForm();
            gf.setQuitOnClose(true);
            this.setVisible(false);
            Global.notewindow(this, WINDOW_NAME_KEY);
            Global.writeparameter("go.cfg");
        } else if (Global.resourceString("Login").equals(o)) {
            Connection conn = new Connection(constructConnectionLine());
            // create a connection frame and connect via
            // the connect class
            ConnectionFrame cf = new ConnectionFrame(Global.resourceString("Connection_to_") + hostField.getText() + Global.resourceString("_as_") + usernameField.getText(), usernameField.getText().trim(), conn.Encoding);
            Global.setwindow(cf, "connection", 500, 400);
            new Connect(conn, cf);
            saveForm();
            cf.setQuitOnClose(true);
            this.setVisible(false);
            Global.notewindow(this, WINDOW_NAME_KEY);
            Global.writeparameter("go.cfg");
        } else if (Global.resourceString("Guest").equals(o)) {
            Connection conn = new Connection(constructGuestConnectionLine());
            // create a connection frame and connect via
            // the connect class
            ConnectionFrame cf = new ConnectionFrame(Global.resourceString("Connection_to_") + hostField.getText() + Global.resourceString("_as_") + usernameField.getText(), usernameField.getText().trim(), conn.Encoding);
            Global.setwindow(cf, "connection", 500, 400);
            new Connect(conn, cf);
            saveForm();
            cf.setQuitOnClose(true);
            this.setVisible(false);
            Global.notewindow(this, WINDOW_NAME_KEY);
            Global.writeparameter("go.cfg");
        } else {
            super.doAction(o);
        }
    }
// -------------------------- OTHER METHODS --------------------------

    private String constructConnectionLine() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append(hostField.getText().trim()); // name of the connection
        sb.append("][");
        sb.append(String.valueOf(ServerTypeUtil.getServerType(serverTypeChoice)));
        sb.append("][");
        sb.append(hostField.getText().trim());
        sb.append("][");
        sb.append(portField.getText().trim());
        sb.append("][");
        sb.append(usernameField.getText().trim());
        sb.append("][");
        sb.append(passwordField.getText().trim());
        sb.append("][1][GBK]");
        return sb.toString();
    }

    private String constructGuestConnectionLine() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append(hostField.getText().trim()); // name of the connection
        sb.append("][");
        sb.append(String.valueOf(ServerTypeUtil.getServerType(serverTypeChoice)));
        sb.append("][");
        sb.append(hostField.getText().trim());
        sb.append("][");
        sb.append(portField.getText().trim());
        sb.append("][");
        String guestLogin = "guest" + (int) (Math.random() * 10000);
        sb.append(guestLogin);
        sb.append("][guest");
        sb.append("][1][GBK]");
        return sb.toString();
    }

    public void doclose() {
        Global.notewindow(this, WINDOW_NAME_KEY);
        Global.writeparameter("go.cfg");
        super.doclose();
        System.exit(0);
    }

    private void saveForm() {
        Global.setParameter(HOST_KEY, hostField.getText().trim());
        Global.setParameter(SERVER_TYPE_KEY, String.valueOf(ServerTypeUtil.getServerType(serverTypeChoice)));
        Global.setParameter(PORT_KEY, portField.getText().trim());
        Global.setParameter(USERNAME_KEY, usernameField.getText().trim());
        if (savePasswordBox.getState()) {
            setPassword(passwordField.getText());
        } else {
            Global.removeParameter(PASSWORD_KEY);
        }
    }

    public static void setPassword(String password) {
        Global.setParameter(PASSWORD_KEY, Global.encrypt(password));
    }
// --------------------------- main() method ---------------------------

    public static void main(String[] args) throws Exception {
        String host = null;
        int serverType = Global.NNGS_DAOQI;
        int port = Defaults.DEFAULT_PORT;
        try {
            String portStr = Global.getParameter(PORT_KEY, Defaults.DEFAULT_PORT_S);
            port = Integer.parseInt(portStr);
        } catch (Exception e) {
            // do nothing
        }
        int na = 0;
        while (args.length > na) {
            if (args.length - na >= 2 && args[na].startsWith("-l")) {
                if (args.length > na + 1) {
                    String lang = args[na + 1];
                    if ("chinese".equalsIgnoreCase(lang)) {
                        Locale.setDefault(Locale.CHINESE);
                    } else if ("english".equalsIgnoreCase(lang)) {
                        Locale.setDefault(Locale.ENGLISH);
                    }
                }
                na += 2;
            } else if (args.length - na >= 2 && args[na].startsWith("-t")) {
                try {
                    serverType = Integer.parseInt(args[na + 1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                na += 2;
            } else if (args.length - na >= 2 && args[na].startsWith("-h")) {
                host = args[na + 1];
                na += 2;
            } else if (args.length - na >= 2 && args[na].startsWith("-p")) {
                try {
                    port = Integer.parseInt(args[na + 1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                na += 2;
            }
        }
        initSettings();
        if (host == null) {
            new SimpleMain();
        } else {
            new SimpleMain(serverType, host, port);
        }
    }

    public static void initSettings() {
        Global.home(System.getProperty("user.home"));
        Global.readparameter("go.cfg");
        Global.makeColors();
        Global.createfonts();
        Global.loadmessagefilter(); // load message filters, if available
    }
}