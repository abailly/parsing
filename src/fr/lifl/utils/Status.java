/**
 * The Status object is made of a List of messages <b>added</b>,
 * a boolean for the state (ok or not), and a String for his message.
 * This object is also thrownable as a RuntimeException.
 *
 * @author Arnaud FONTAINE
 * @author Remi DESSY
 * @see RuntimeException
 */
package fr.lifl.utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Status extends RuntimeException {
    /**
     * List of <b>ALL</b> messages (including this Status message)
     * <b>ONLY</b> if messages have been added by the
     * method append(Status).
     *
     * @see #append(Status)
     * @see #getMessages()
     */
    protected List list;

    /**
     * State of the current object.
     *
     * @see #isOk()
     * @see #setNotOk()
     */
    protected boolean ok;

    /**
     * Message of this status
     *
     * @see #getMsg()
     */
    protected String msg;
    
    /**
     * current throwable for this status
     */
    protected Throwable t;
    
    /**
     * Exception list for this status
     */
    protected List exceptions =  new ArrayList();
    
    /**
	 * Constructor accepting an initial state and a message
	 *
	 * @param ok state of the status
	 * @param msg cause for this state
	 * @return Status
	 */
    public Status(boolean ok, String msg) {
	this.ok = ok;
	this.msg = msg;
	if(!ok)
	    this.msg = "==> " + this.msg;
    }
    /**
	 * Constructor with an additional Throwable argument
	 * 
	 * @param ok state of the status
	 * @param msg cause for this state
	 * @return Status
	 */
    public Status(boolean ok, String msg, Throwable t) {
	this(ok,msg);
	exceptions.add(t);
		
    }						
	
    /**
	 * Returns the list of messages added
	 * by the method append.
	 *
	 * @see #list
	 * @see #append(Status)
	 * @return List
	 */
    public List getMessages() {
	return list;
    }
    
    /**
	 * Returns the message of this Status
	 *
	 * @see #msg
	 * @return String
	 */
    public String getMsg() {
	return msg;
    }
    
    
    /**
	 * Returns an array of all messages of the current object.
	 * If no messages have been added, it returns an array of one element,
	 * the message of the current object.
	 *
	 * @return String[]
	 */
    public String[] trace() {
	String[] res;
	// if there's no list, returns array with one element:the message
	if(list == null) {
	    res = new String[1];
	    res[0] = msg;
	}
	else {
	    Iterator it = list.iterator();
	    //list exists and contains this message
	    res = new String[list.size()];
	    int i=0;
	    //transfer the list to the returned array
	    while(it.hasNext()) {
		res[i] = (String)(it.next());
		i++;
	    }
	}
	return res;
    }
    
    /**
	 * Switch the state of this status to false (not ok)
	 *
	 * @see #isok()
	 */
    public void setNotOk() {
	ok = false;
    }
    
    /**
	 * Return the current state of this status
	 *
	 * @return boolean
	 */
    public boolean isOk() {
	return ok;
    }
	
    /**
	 * Gets and adds the list of messages
	 * after his own message.
	 *
	 * @param s the Status that contains the messages to be added
	 * @see #getMessages()
	 */
    public void append(Status s) {
	//if there's no list, creates one and add it the message
	if(list == null) {
	    list = new ArrayList();
	    list.add(getMsg());
	}
	// add messages	
	List child = s.getMessages();
	if(child != null) {
	    Iterator it = child.iterator();
	    //transfers the list of the status s to the list
	    while(it.hasNext())
		list.add((String)(it.next()));
	}
	else
	    list.add(s.getMsg());
	// add exception list
	List excs = s.getExceptions();
	exceptions.addAll(excs);
    }
    /**
	 * Returns the exceptions.
	 * @return List
	 */
    public List getExceptions() {
	return exceptions;
    }

	/**
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return msg;
	}

	public String toString() {
		String message = "";
		String[] msg = trace();
	
		for(int i=0;i<msg.length;i++) message += ((i==0)?(""):("\n")) + msg[i];
	
		return message;
	}
}
