package edu.uwlax.toze.objectz;


/*
 * Symbol Table
 * 
 * This file implements the symbol table. It is used to store types
 * and variables.
 */

import edu.uwlax.toze.editor.TozeFontMap;

import java.util.*;

public class AstSymbolTable
{
    final static int ADDTYPE_TYPES = 0;
    final static int ADDTYPE_SYMBOLS = 1;

    AstSymbolTable m_parent = null;
    TreeMap m_symbols = new TreeMap();
    TreeMap m_types = new TreeMap();
    List m_kids = new ArrayList();
    boolean m_undefinedSymbolOccurred = false;
    boolean m_undefinedTypeOccurred = false;
    boolean m_newSymbolSet = false;
    List m_visible = new ArrayList();
    TreeMap m_refs = new TreeMap();
    String m_name = "";
    boolean m_isOperation = false;
    boolean m_noAddLocal = false;

    public AstSymbolTable()
    {
        AstType type;
        AstType setType;
      
      /*
       * Predefined types.
       */

        type = new AstType();
        type.m_type = AstType.TYPE_SET;
        type.m_setType = new AstType(AstType.TYPE_NATURAL);
        m_types.put(Character.toString(TozeFontMap.CHAR_NAT), type);

        type = new AstType();
        type.m_type = AstType.TYPE_SET;
        type.m_setType = new AstType(AstType.TYPE_NATURAL1);
        m_types.put(Character.toString(TozeFontMap.CHAR_NAT) + Character.toString(TozeFontMap.CHAR_SUB_1), type);

        type = new AstType();
        type.m_type = AstType.TYPE_SET;
        type.m_setType = new AstType(AstType.TYPE_NUMBER);
        m_types.put(Character.toString(TozeFontMap.CHAR_NUM), type);

        type = new AstType();
        type.m_type = AstType.TYPE_SET;
        type.m_setType = new AstType(AstType.TYPE_REAL);
        m_types.put(Character.toString(TozeFontMap.CHAR_R), type);

        type = new AstType();
        type.m_type = AstType.TYPE_SET;
        type.m_setType = new AstType(AstType.TYPE_BOOL);
        m_types.put(Character.toString(TozeFontMap.CHAR_BOOL), type);
    }

    public AstSymbolTable(AstSymbolTable parent)
    {
      /*
       * The predefined types do not need to be redefined when a child
       * symbol table is created since types are looked for in parents.
       */
        m_parent = parent;
    }

    public void reset()
    {
        if (m_parent != null)
            {
            m_parent.reset();
            }
        else
            {
            m_undefinedSymbolOccurred = false;
            m_undefinedTypeOccurred = false;
            m_newSymbolSet = false;
            }
    }

    public void setNoAddLocal()
    {
        m_noAddLocal = true;
    }

    public void setUndefinedSymbolOccurred()
    {
      /*
       * Set undefined symbol occurred in the root symbol table.
       */

        if (m_parent != null)
            {
            m_parent.setUndefinedSymbolOccurred();
            }
        else
            {
            m_undefinedSymbolOccurred = true;
            }
    }

    public void setUndefinedTypeOccurred()
    {
      /*
       * Set undefined type occurred in the root symbol table.
       */

        if (m_parent != null)
            {
            m_parent.setUndefinedTypeOccurred();
            }
        else
            {
            m_undefinedTypeOccurred = true;
            }
    }

    public void addKid(AstSymbolTable kid)
    {
        m_kids.add(kid);
    }
   
   /*
    * Add a symbol of the specified type.
    */

    public boolean add(String name, AstType type)
    {
        if (m_noAddLocal)
            {
            return m_parent.add(name, type);
            }
        else
            {
            if (m_symbols.get(name) == null)
                {
                m_symbols.put(name, type);
                return true;
                }
            }
        return false;
    }
   
   /*
    * Add a type with its associated name.
    */

    public boolean addType(String name, AstType type)
    {
        if (m_types.get(name) == null)
            {
            m_types.put(name, type);
            return true;
            }
        return false;
    }
   
   /*
    * Get the type of a symbol.
    */

    public AstType getType(String name)
    {
      /*
       * By default, search any higher scopes for the symbol.
       */
        return getType(name, 1);
    }

    public AstType getType(String name, int fromParent)
    {
        int i;
        AstType type;
      
      /*
       * Check the local scope.
       */

        type = (AstType) m_symbols.get(name);
        if (type != null)
            {
            return type;
            }
      
      /*
       * If this is an operation then check to see if the same operation
       * in superclasses have this symbol.
       */

        if (m_isOperation)
            {
            type = getTypeFrom(m_name, name);
            if (type != null)
                {
                return type;
                }
            }
      
      /*
       * First check if the value is in any referenced schemas
       * or superclasses.
       */

        Collection values = m_refs.values();
        Iterator it = values.iterator();
        while (it.hasNext())
            {
            type = ((AstSymbolTable) it.next()).getType(name, 0);
            if (type != null)
                {
                return type;
                }
            }

      /*
       * If we have not found it at this point and the flag
       * indicates that we should also look in parents,
       * search the parents to find the type.
       */

        if ((fromParent == 1) && (m_parent != null))
            {
            type = m_parent.getType(name);
            if (type != null)
                {
                return type;
                }
            }
      
      /*
       * If we are at this point the symbol was not found so we will
       * set a flag to indicate this.
       */

        setUndefinedSymbolOccurred();
        return null;
    }

   /*
    * Get the definition of a type.
    */

    public AstType getTypeDef(String name)
    {
        AstType type = (AstType) m_types.get(name);
        if (type != null)
            {
            return type;
            }
      
      /*
       * Search the parents if it was not found in this
       * symbol table.
       */

        if (type == null)
            {
            if (m_parent != null)
                {
                return m_parent.getTypeDef(name);
                }
            }
      
      /*
       * If we are at this point then the type was not found so
       * we will set a flag.
       */

        setUndefinedTypeOccurred();
        return null;
    }
   
   /*
    * Set the type of a symbol.
    */

    public void setSymbol(String name, AstType type)
    {
        AstType t = (AstType) m_symbols.get(name);
      
      /*
       * Only set the type if the symbol was previously
       * undefined.
       */
        if (t == null)
            {
            if (m_parent != null)
                {
                m_parent.setSymbol(name, type);
                }
            }
        else
            {
            if (t.isUndefined())
                {
                m_symbols.put(name, type);
                m_newSymbolSet = true;
                }
            }
    }
   
   /*
    * Set the type of a user defined type.
    */

    public void setType(String name, AstType type)
    {
        m_types.put(name, type);
    }
   
   /*
    * Get the type from a specific class.
    */

    public AstType getTypeFrom(String oname, String name)
    {
        AstType otype;
        int i;

        Collection values = m_parent.m_refs.values();
        Iterator it = values.iterator();
        while (it.hasNext())
            {
            otype = ((AstSymbolTable) it.next()).getType(oname, 0);
            if (otype != null)
                {
                AstType type = otype.m_classMembers.getType(name);
                if (type != null)
                    {
                    return type;
                    }
                }
            }
        return null;
    }
   
   /*
    * Add a symbol to the visibility list.
    */

    public boolean addVisible(String name)
    {
        AstType type = getType(name, 0);
        if (type != null)
            {
            m_visible.add(name);
            return true;
            }
        return false;
    }

    public AstType getTypeVisible(String name)
    {
        int i;
        AstType type = getType(name);
        if (type == null)
            {
            return new AstType();
            }
      
      /*
       * If there is no visibiliy list then all features of
       * a symbol table (class) are visible.
       */
        if (m_visible.isEmpty())
            {
            return type;
            }
      
      /*
       * Search for the symbol name in the visibility list.
       */
        for (i = 0; i < m_visible.size(); i++)
            {
            if (name.equals((String) m_visible.get(i)))
                {
                return type;
                }
            }
        return null;
    }
   
   /*
    * This add is used to allow one symbol table to have access
    * to another. This is done mainly for subclasses where the
    * subclass would have access to each of the classes it is
    * inheriting.
    */

    public void add(String name, AstSymbolTable stable)
    {
        m_refs.put(name, stable);
    }

    public boolean isSuperClass(String name)
    {
        if (name == null)
            {
            return false;
            }
        if (name.equals(m_name))
            {
            return true;
            }
        if (m_refs != null)
            {
            Collection v = m_refs.values();
            Iterator it = v.iterator();
            while (it.hasNext())
                {
                if (((AstSymbolTable) it.next()).isSuperClass(name))
                    {
                    return true;
                    }
                }
            }
        return false;
    }
   
   /*
    * For debugging purposes.
    */

    public void print()
    {
        print(0);
    }

    public void print(int l)
    {
        Set s = m_types.entrySet();
        Iterator iter = s.iterator();
        Map.Entry e;
        AstType t;

        while (iter.hasNext())
            {
            e = (Map.Entry) iter.next();
            t = (AstType) e.getValue();
            print(l, (String) e.getKey() + " = " + t.toString() + " (T)");
            }

        Set set = m_symbols.entrySet();
        Iterator it = set.iterator();
        Map.Entry entry;
        AstType type;
        while (it.hasNext())
            {
            entry = (Map.Entry) it.next();
            type = (AstType) entry.getValue();
            print(l, (String) entry.getKey() + " = " + type.toString());
            }

        int i;
        for (i = 0; i < m_kids.size(); i++)
            {
            print(l + 1, "Symbol Table");
            ((AstSymbolTable) m_kids.get(i)).print(l + 1);
            }
    }

    public void print(int l, String text)
    {
        int i;
        for (i = 0; i < l; i++)
            {
            System.out.print(" ");
            }
        System.out.println(text);
    }
}
