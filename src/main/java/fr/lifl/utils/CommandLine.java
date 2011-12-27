/*
 * Created on Mar 28, 2004
 * $Log: CommandLine.java,v $
 * Revision 1.3  2005/04/25 11:17:21  bailly
 * run commit
 *
 * Revision 1.2  2004/05/10 12:41:05  bailly
 * Added option arguments retrieval methods
 *
 */
package fr.lifl.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A general purpose class to handle command line attributes
 * <p>
 * Instances of this class are used to parse commandline arguments according to
 * certain options.
 * 
 * @author nono
 * @version $Id$
 */
public class CommandLine {

    public static interface IOption {
        public Object getArgument();

    }

    /*
     * Basic option class. Contains a boolean flag
     */
    class Option implements IOption {
        boolean set = false;

        /**
         * @return
         */
        public boolean isSet() {
            return set;

        }

        /**
         * @param b
         */
        public void setSet(boolean b) {
            set = b;
        }

        /**
         * Allows option a chance to get parameters
         * 
         * @param it
         */
        public void handleParameters(Iterator it) {
            /* DO NOTHING */
        }

        /*
         * (non-Javadoc)
         * 
         * @see fr.lifl.utils.CommandLine.IOption#getArgument()
         */
        public Object getArgument() {
            return null;
        }

    }

    /*
     * option class with one argument
     */
    class OptionSingle extends Option {
        private Object argument;

        /**
         * @return
         */
        public Object getArgument() {
            return argument;
        }

        /**
         * @param object
         */
        public void setArgument(Object object) {
            argument = object;
        }

        /*
         * (non-Javadoc)
         * 
         * @see fr.lifl.utils.CommandLine.Option#handleParameters(java.util.Iterator)
         */
        public void handleParameters(Iterator it) {
            argument = it.next();
        }

    }

    /*
     * An option class to store multiple values
     */
    class OptionMultiple extends OptionSingle {

        String separator = ":";

        OptionMultiple(char sep) {
            setArgument(new LinkedList());
            this.separator = "" + sep;
        }

        OptionMultiple() {
            setArgument(new LinkedList());
        }

        public void addArgument(Object o) {
            ((List) getArgument()).add(o);
        }

        /*
         * (non-Javadoc) Parse the given argument as a colon separated list of
         * elements
         * 
         * @see fr.lifl.utils.CommandLine.Option#handleParameters(java.util.Iterator)
         */
        public void handleParameters(Iterator it) {
            String fularg = (String) it.next();
            StringTokenizer stok = new StringTokenizer(fularg, separator);
            while (stok.hasMoreElements())
                addArgument(stok.nextElement());
        }
    }

    /* defined options */
    private Option[] opts = new Option[256];

    /* non option arguments */
    private List arguments = new LinkedList();

    /**
     * Adds a new letter denoting a binary flag option to this command line
     * handler.
     * 
     * @param letter
     *          the letter denoting flag option
     * @return this commandline object for further processing
     */
    public CommandLine addOption(char letter) {
        if (letter <= 255)
            opts[letter] = new Option();
        return this;
    }

    /**
     * Adds a new option denoted by a letter and a single argument.
     * 
     * @param letter
     * @return this commandline object for further processing
     */
    public CommandLine addOptionSingle(char letter) {
        if (letter <= 255)
            opts[letter] = new OptionSingle();
        return this;
    }

    /**
     * Adds a new letter denoting an option which takes multiple arguments
     * separated by second character.
     * 
     * @param letter
     *          option letter
     * @param sep
     *          separator for multiple options
     * @return this commandline object for further processing
     */
    public CommandLine addOptionMultiple(char letter, char sep) {
        if (letter <= 255)
            opts[letter] = new OptionMultiple(sep);
        return this;
    }

    /**
     * Adds a new letter denoting an option which takes multiple arguments
     * separated by a colon (':').
     * 
     * @param letter
     *          option letter
     * @return this commandline object for further processing
     */
    public CommandLine addOptionMultiple(char letter) {
        if (letter <= 255)
            opts[letter] = new OptionMultiple();
        return this;
    }

    /*
     * static java.util.Map createParameters(String plist) { java.util.Map params =
     * new java.util.HashMap(); java.util.StringTokenizer st = new
     * java.util.StringTokenizer(plist, ","); while (st.hasMoreTokens()) { try {
     * String tok = st.nextToken(); String pname = tok.substring(0,
     * tok.indexOf("=")); String pval = tok.substring(tok.indexOf("=") + 1);
     * params.put(pname, pval); } catch (Exception ex) { System.err.println("Bad
     * parameter list :" + ex); return null; } } return params; }
     * 
     * static void applyParameters( java.util.Map params) {
     * java.beans.PropertyDescriptor[] props; // retrieve property descriptors
     * from visitor bean try { props = java .beans .Introspector
     * .getBeanInfo(v.getClass()) .getPropertyDescriptors(); } catch (Exception
     * ex) { System.err.println( "Unable to get property descriptions from " +
     * v.getClass().getName() + " : " + ex.getMessage()); return; } // apply
     * properties given by user for (int i = 0; i < props.length; i++) { // get
     * property info String pname = props[i].getName(); Class cls =
     * props[i].getPropertyType(); java.lang.reflect.Method meth =
     * props[i].getWriteMethod(); // find property in parameters map String strval =
     * (String) params.get(pname); if (strval == null) continue; try { Object val =
     * fr.lifl.fidl.util.TypeHelper.convert(cls, strval); meth.invoke(v, new
     * Object[] { val }); } catch (Exception ex) { System.err.println( "Error
     * while setting parameter " + pname + " to " + strval); } } }
     */
    public void parseOptions(String argv[]) {
        Iterator it = Arrays.asList(argv).iterator();

        // parse command line
        while (it.hasNext()) {
            String arg = (String) it.next();
            if (arg.startsWith("-")) {
                // command-line option
                char optn = arg.charAt(1);
                Option opt = opts[optn];
                /* check option is valid */
                if (opt == null)
                    throw new IllegalArgumentException("Unknown option " + optn);
                /* setoption and give it a chance to handle parameters */
                opt.setSet(true);
                opt.handleParameters(it);
            } else {
                /* cl argument */
                arguments.add(arg);
            }
        }
    }

    /**
     * @return
     */
    public List getArguments() {
        return arguments;
    }

    /**
     * @param c
     * @return
     */
    public boolean isSet(char c) {
        Option opt = opts[c];
        if (opt == null)
            return false;
        return opt.isSet();
    }

    /**
     * @param c
     */
    public IOption getOption(char c) {

        return opts[c];
    }

}