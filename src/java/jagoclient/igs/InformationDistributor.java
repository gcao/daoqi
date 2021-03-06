package jagoclient.igs;

import jagoclient.Global;

import java.io.PrintWriter;

/**
A Distributor to display informations from the server (type 9).
It will open a new InformationDialog or append to an old one.
*/

public class InformationDistributor extends Distributor
{	ConnectionFrame CF;
	PrintWriter Out;
	String S;
	public InformationDialog infodialog;
	int Lines;
	public InformationDistributor
		(ConnectionFrame cf, IgsStream in, PrintWriter out)
	{	super(in,9,0,false);
		CF=cf; Out=out;
		S= ""; Lines=0;
		infodialog=null;
	}
	public void send (String C)
	{	if (Lines>0) S=S+"\n"+C;
		else S=S+C;
		Lines++;
	}
	public void allsended ()
	{	if (S.equals("")) return;
		if (S.startsWith("Match") && S.indexOf("requested")>0)
		{
			//if (CF.getServerType() == Global.IGS && S.indexOf("dqdq") < 0)  // only accept Daoqi request.
			//{   //CF.append("Match request from non-Daoqi user is ignored!");
			//    S=""; Lines=0;
			//    return;
			//}
			new MatchDialog(CF,S,Out,this);
			S=""; Lines=0; return;
		}
		if (Global.blocks(S)!=MessageFilter.BLOCK_COMPLETE)
			CF.append(S);
		if ((Global.blocks(S)==0 && CF.wantsinformation())
		    || Global.posfilter(S))
		{	if (infodialog==null)
				infodialog=new InformationDialog(CF,S+"\n",Out,this);
			else
				infodialog.append(S+"\n");
		}
		S=""; Lines=0;
	}
	public void remove ()
	{	infodialog=null;
	}
}
