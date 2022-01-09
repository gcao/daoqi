
package de.pxlab.pxl;

/** A KeyboardFocusManager which delivers keyboard events to
 components contained in a full screen window. This is necessary
 because starting from Java 1.4 the DefaultKeyboardFocusManager no
 longer delivers keyboard messages to Window objects since these are
 no longer allowed to become focus owner.

 @author H. Irtel
 @version 0.1.0
 @see java.awt.KeyboardFocusManager
 @see java.awt.DefaultKeyboardFocusManager
*/

public class FullScreenKeyboardFocusManager extends java.awt.DefaultKeyboardFocusManager {

    private java.awt.Component target;

    /** Create a KeyboardFocusManager which delivers keyboard events
     to the given target which may be contained in a full screen
     Window.
     @param target the target component for receiving keyboard
     events. */
    public FullScreenKeyboardFocusManager(java.awt.Component target) {
	super();
	this.target = target;
    }


    /** Overrides the parent's dispatchKeyEvent() method to send
	keyboard events to this keyboard manager's target Component
	which usually will be a DisplayPanel object. */
     public boolean dispatchKeyEvent(java.awt.event.KeyEvent e) {

	  //System.out.println("FullScreenKeyboardFocusManager.dispatchKeyEvent(): " + e.getKeyCode());

	redispatchEvent(target, e);
	return true; // super.dispatchKeyEvent(e);
    }
}
