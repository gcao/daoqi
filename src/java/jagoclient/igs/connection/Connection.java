package jagoclient.igs.connection;

import jagoclient.Global;
import rene.util.parser.StringParser;

import java.io.PrintWriter;

/**
A class, which holds a connection and can be initialized with
a string from "server.cfg".
*/
public class Connection
{	public String Name="", Server="", User="", Password="";
	public int ServerType;
	public int Port;
	public boolean Valid,Trying;
	public static final int MOVE=0,MOVE_TIME=1,MOVE_N_TIME=2;
	public int MoveStyle=MOVE;
	public String Encoding;
	public Connection (String line)
	{	if (!Global.isApplet()) Encoding=System.getProperty("file.encoding");
		if (Encoding==null) Encoding=""; 
		StringParser p=new StringParser(line);
		Valid=true;
		Trying=false;
		p.skip("["); Name=p.upto(']');
		p.skip("]"); p.skipblanks(); if (p.error()) return;
		p.skip("["); if (p.error()) return;
		String s =p.parseword(']');
		if (s.matches("\\d")) {
			ServerType = Integer.parseInt(s);
			p.skip("]"); p.skipblanks(); if (p.error()) return;
			p.skip("["); if (p.error()) return; Server=p.parseword(']');
		} else {
			ServerType = Global.NNGS_DAOQI;
			Server = s;
		}
		p.skip("]"); p.skipblanks(); if (p.error()) return;
		p.skip("["); if (p.error()) return; Port=p.parseint(']');
		p.skip("]"); p.skipblanks(); if (p.error()) return;
		p.skip("["); if (p.error()) return; User=p.parseword(']');
		p.skip("]"); p.skipblanks(); if (p.error()) return;
		p.skip("["); if (p.error()) return; Password=p.parseword(']');
		p.skip("]"); p.skipblanks(); if (p.error()) return;
		p.skip("["); if (p.error()) return; MoveStyle=p.parseint(']');
		p.skip("]"); p.skipblanks(); if (p.error()) return;
		p.skip("["); if (p.error()) return; Encoding=p.parseword(']');
	}
	public void write (PrintWriter out)
	{	out.println("["+Name+"] ["+ServerType+"] ["+Server+"] ["+
					Port+"] ["+User+"] ["+Password+"] ["+
					MoveStyle+"] ["+Encoding+"]");
	}
	public boolean valid () { return Valid; }
}

