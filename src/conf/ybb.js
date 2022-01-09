var defaultWidth = 640;
var defaultHeight = 520;
var reApplet = /\[gameapplet((?:\s+\w+\s*=\s*[\-\:\d\w]+)*)\]([^'"]*)\[\/gameapplet\]/ig;
var reParam = /(\w+)\s*=\s*([\-\:\d\w]+)/ig;
function daoqiapplet(input) {
var appletResult = reApplet.exec(input);
while (appletResult) {
var replaceResult = "";
var paramsResult = "";
var width = defaultWidth;
var height = defaultHeight;
var params = appletResult[1];
var game=appletResult[2];
var paramResult = reParam.exec(params);
while (paramResult) {
  var paramName = paramResult[1];
  var paramValue = paramResult[2];
  if (paramName == 'width') width = paramValue;
  else if (paramName == 'height') height = paramValue;
  else {
  paramsResult += "<param name="+paramName+" value='"+paramValue+"'/>";
  }
  paramResult = reParam.exec(params);
}
paramsResult += "<param name='game' value='"+game+"'/>";
replaceResult = "<!--[if !IE]>--><object classid='java:GoApplet.class' type='application/x-java-applet' archive='http://www.daoqigame.com/daoqi/applet.jar' width='"+width+"' height='"+height+"'>"+paramsResult;
replaceResult += "<!--<![endif]--><object classid='clsid:8AD9C840-044E-11D1-B3E9-00805F499D93' codebase='http://java.sun.com/update/1.5.0/jinstall-1_5_0-windows-i586.cab' width='"+width+"' height='"+height+"'><param name='code' value='GoApplet'/><param name='archive' value='http://www.daoqigame.com/daoqi/applet.jar'/>"+paramsResult;
replaceResult += "<strong>This browser does not have a Java Plug-in.<br/><a href='http://java.sun.com/products/plugin/downloads/index.html'>Get the latest Java Plug-in here.</a></strong></object><!--[if !IE]>--></object><!--<![endif]-->";
input=input.substring(0,appletResult.index)+replaceResult+input.substring(appletResult.index+appletResult[0].length);
reApplet.lastIndex=appletResult.index+replaceResult.length;
appletResult=reApplet.exec(input);
}
reApplet.lastIndex=0;
return input;
}

function ybbcode(temp) {

if (getCookie('ybbcode')!='0'){ //是否打开YBB代码
temp = temp.replace(/&amp;/ig,"&");
temp = temp.replace(/\[b\]/ig,"<b>");
temp = temp.replace(/\[\/b\]/ig,"<\/b>");
temp = temp.replace(/\[i\]/ig,"<i>");
temp = temp.replace(/\[\/i\]/ig,"<\/i>");
temp = temp.replace(/\[u\]/ig,"<u>");
temp = temp.replace(/\[\/u\]/ig,"<\/u>");
temp = temp.replace(/\[strike\]/ig,"<strike>");
temp = temp.replace(/\[\/strike\]/ig,"<\/strike>");
temp = temp.replace(/\[center\]/ig,"<center>");
temp = temp.replace(/\[\/center\]/ig,"<\/center>");
temp = temp.replace(/(\[sgffile\])(http:\/\/.[^\[]*)(\[\/sgffile\])/ig,"<applet code=\"GoViewer.class\" width=\"400\" height=\"430\"><param name=\"mode\" value=\"url\"><param name=\"record\" value=\"$2\"></applet>");
temp = temp.replace(/(\[sgfviewer\])([^\[]*)(\[\/sgfviewer\])/ig,"<applet code=\"wqp12.class\"  archive=\"http://www.daoqigame.com/weiqi/applet/2000d101.jar\" width=\"660\" height=\"480\" bgcolor=\"gray\"><param name=\"qptype\" value=\"pcx\"><param name=\"bcolor\" value=\"E1E1E1,8c786e,ffc365,beb4a0\"><param name=\"fcolor\" value=\"000000,ffffff,643232,643232\"><param name=\"filename\" value=\"$2\"></applet>");
temp = temp.replace(/\[marquee\]/ig,"<marquee>");
temp = temp.replace(/\[\/marquee\]/ig,"<\/marquee>");
temp = temp.replace(/\[QUOTE\]/ig,"<BLOCKQUOTE><strong>引用</strong>：<HR Size=1>");
temp = temp.replace(/\[\/QUOTE\]/ig,"<HR SIZE=1><\/BLOCKQUOTE>");
temp = temp.replace(/(\[font=)([^.:;`'"=\]]*)(\])/ig,"<FONT face='$2'>");
temp = temp.replace(/\[\/font\]/ig,"<\/FONT>");
temp = temp.replace(/(\[COLOR=)([^.:;`'"=\]]*)(\])/ig,"<FONT COLOR='$2'>");
temp = temp.replace(/\[\/COLOR\]/ig,"<\/FONT>");
temp = temp.replace(/(\[size=)([0-9]*)(\])/ig,"<FONT size='$2'>");
temp = temp.replace(/\[\/size\]/ig,"<\/FONT>");
temp = temp.replace(/(\[URL\])([^]]*)(\[\/URL\])/ig,"<A TARGET=_blank HREF='$2'>$2</A>");
temp = temp.replace(/(\[URL=)([^]]*)(\])/ig,"<A TARGET=_blank HREF='$2'>");
temp = temp.replace(/\[\/URL\]/ig,"<\/A>");
temp = temp.replace(/(\[EMAIL\])(\S+\@[^]]*)(\[\/EMAIL\])/ig,"<a href=mailto:$2>$2</a>");
temp = temp.replace(/(\[RM=([0-9]*)\,([0-9]*)\,([A-Z]*)\])([^]]*)(\[\/RM\])/ig,"<OBJECT classid=clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA width=$2 height=$3><PARAM NAME=SRC VALUE=$5><PARAM NAME=CONSOLE VALUE=Clip1><PARAM NAME=CONTROLS VALUE=imagewindow><PARAM NAME=AUTOSTART VALUE=$4><\/OBJECT><br><OBJECT classid=CLSID:CFCDAA03-8BE4-11CF-B84B-0020AFBBCCFA height=60 width=$2><PARAM NAME=CONTROLS VALUE=ControlPanel,StatusBar><PARAM NAME=CONSOLE VALUE=Clip1><\/OBJECT>");
temp = temp.replace(/(\[MP=([0-9]*)\,([0-9]*)\])([^]]*)(\[\/MP\])/ig,"<object classid=CLSID:22d6f312-b0f6-11d0-94ab-0080c74c7e95 width=$2 height=$3><param name=ShowStatusBar value=-1><param name=Filename value=$4><\/object>");
temp = daoqiapplet(temp);
}

if (getCookie('ybbflash')!='0'){  //是否打开[FLASH]代码
temp = temp.replace(/(\[FLASH=([0-9]*)\,([0-9]*)\])([^]]*)(\[\/FLASH\])/ig,"<embed width=$2 height=$3 src='$4' wmode=transparent>");
}

if (getCookie('ybbimg')!='0'){  //是否打开[IMG]代码
temp = temp.replace(/(\[IMG\])([^];]*)(\[\/IMG\])/ig,"<a target=_blank href='showpic.asp?picurl=$2'><img border=0 onmousewheel='return yuzi_img(event,this)' src=$2 onload=\"javascript:if(this.width>body.clientHeight)this.width=body.clientHeight\"></a>");
}

if (getCookie('ybbbrow')!='0'){  //是否打开表情代码
temp = temp.replace(/(\[em)([0-9]+)(\])/ig,"<IMG border=0 SRC=images/Emotions/$2.gif>");
}

return (temp);
}


function level(experience,membercode,username,moderated)
{
if (membercode=='0'){levelname="尚未激活";levelimage="<img src=images/level/1.gif>";}
else
if (membercode=='4'){levelname="管 理 员";levelimage="<img src=images/level/19.gif>";}
else
if (membercode=='5'){levelname="社区区长";levelimage="<img src=images/level/20.gif>";}
else
if(moderated.indexOf("|"+username+"|") != -1 && moderated!=""){levelname="论坛版主";levelimage="<img src=images/level/18.gif>";}
else
if (experience<= 50){levelname="小野人";levelimage="<img src=images/level/1.gif>";}
else
if (experience<= 150){levelname="野人";levelimage="<img src=images/level/2.gif>";}
else
if (experience<= 500){levelname="土人";levelimage="<img src=images/level/3.gif>";}
else
if (experience<= 1000){levelname="咕噜";levelimage="<img src=images/level/4.gif>";}
else
if (experience<= 2000){levelname="大咕噜";levelimage="<img src=images/level/5.gif>";}
else
if (experience<= 4000){levelname="咕噜队长";levelimage="<img src=images/level/6.gif>";}
else
if (experience<= 8000){levelname="初级守卫";levelimage="<img src=images/level/7.gif>";}
else
if (experience<= 12000){levelname="守卫者";levelimage="<img src=images/level/8.gif>";}
else
if (experience<= 20000){levelname="守卫队长";levelimage="<img src=images/level/9.gif>";}
else
if (experience<= 30000){levelname="斗士";levelimage="<img src=images/level/10.gif>";}
else
if (experience<= 50000){levelname="大斗士";levelimage="<img src=images/level/11.gif>";}
else
if (experience<= 70000){levelname="斗士长";levelimage="<img src=images/level/12.gif>";}
else
if (experience<= 100000){levelname="骑士";levelimage="<img src=images/level/13.gif>";}
else
if (experience<= 130000){levelname="青铜骑士";levelimage="<img src=images/level/14.gif>";}
else
if (experience<= 160000){levelname="白银骑士";levelimage="<img src=images/level/15.gif>";}
else
if (experience<= 200000){levelname="黄金骑士";levelimage="<img src=images/level/16.gif>";}
else
if (experience> 200000){levelname="圣骑士";levelimage="<img src=images/level/17.gif>";}
return('');
}

//放大缩小图片
function yuzi_img(e, o)
{
var zoom = parseInt(o.style.zoom, 10) || 100;
zoom += event.wheelDelta / 12;
if (zoom > 0) o.style.zoom = zoom + '%';
return false;
}