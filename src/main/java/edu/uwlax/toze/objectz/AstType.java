package edu.uwlax.toze.objectz;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class AstType
{
   static final int TYPE_UNDEFINED    = 0;
   static final int TYPE_NATURAL      = 1;
   static final int TYPE_NATURAL1     = 2;
   static final int TYPE_NUMBER       = 3;
   static final int TYPE_BOOL         = 4;
   static final int TYPE_USER_DEFINED = 5;
   static final int TYPE_BASIC        = 6;
   static final int TYPE_FREE         = 7;
   static final int TYPE_CLASS        = 8;
   static final int TYPE_REAL         = 9;
   static final int TYPE_SET          = 10;
   static final int TYPE_TUPLE        = 11;
   static final int TYPE_FUNCTION     = 12;
   static final int TYPE_SEQUENCE     = 13;
   static final int TYPE_SCHEMA       = 14;
   static final int TYPE_GENERIC      = 15;
   static final int TYPE_OPERATION    = 16;
   static final int TYPE_EMPTY        = 17;
   static final int TYPE_BAG          = 18;
   static final int TYPE_EMPTYSEQ     = 19;
   
   AstType m_setType = null;
   List m_tupleTypes = new ArrayList();
   
   AstSymbolTable m_classMembers = null;
   
   int m_type = TYPE_UNDEFINED;
   String m_name = null;
   
   boolean m_tupleIsSeq = false;
   
   int m_numClassFormalParamList = 0;
   
   /*
    * This will get set when checking generic parameters. It will
    * get set the first time the type is checked and then simply
    * referenced after that.
    */
   int m_genericType = TYPE_UNDEFINED;
   
   public AstType()
   {
      m_type = TYPE_UNDEFINED;
   }
   
   public AstType(int type)
   {
      m_type = type;
   }
   
   public boolean isSet()
   {
      if (m_type == TYPE_SET) return true;
      
      if (m_type == TYPE_GENERIC)
      {
         if (m_genericType == TYPE_UNDEFINED)
         {
            m_genericType = TYPE_SET;
         }
         return m_genericType == TYPE_SET;
      }
      
      /*
       * This is used in case it is a tuple with one value.
       */
      if (getSetType() != null) return true;
      
      return false;
   }
   
   public int getType()
   {
      if ((m_type == TYPE_TUPLE) &&
          (m_tupleTypes.size() == 1))
      {
         return ((AstType)m_tupleTypes.get(0)).getType();
      }
      
      return m_type;
   }
   
   public String getName()
   {
      if ((m_type == TYPE_TUPLE) &&
            (m_tupleTypes.size() == 1))
        {
           return ((AstType)m_tupleTypes.get(0)).getName();
        }
        
        return m_name;
   }
   
   public AstType getSetType2()
   {
      if ((m_type == TYPE_TUPLE) &&
          (m_tupleTypes.size() == 1))
      {
         return ((AstType)m_tupleTypes.get(0)).getSetType2();
      }
      
      return m_setType;
   }
   
   public AstType getSetType()
   {
      if (getType() == TYPE_SET) return getSetType2();
      if (getType() == TYPE_SEQUENCE) return getSetType2();
      if (getType() == TYPE_BAG) return getSetType2();
      if (getType() == TYPE_GENERIC) return this;
      if (getType() == TYPE_TUPLE) return getSetType2();
      return null;
   }
   
   public AstSymbolTable getClassMembers()
   {
      if ((m_type == TYPE_TUPLE) &&
          (m_tupleTypes.size() == 1))
      {
         return ((AstType)m_tupleTypes.get(0)).getClassMembers();
      }
      
      return m_classMembers;
   }
   
   public boolean isSequence()
   {
      return ((getType() == TYPE_SEQUENCE) ||
              (getType() == TYPE_EMPTYSEQ));
   }
   
   public boolean isBag()
   {
      return (getType() == TYPE_BAG);
   }
   
   public boolean isRelation()
   {
      if (getType() == TYPE_SEQUENCE) return true;
      if (getType() == TYPE_BAG) return true;
      if (getType() != TYPE_SET) return false;
      if (m_setType.getType() != TYPE_TUPLE) return false;
      if (m_setType.m_tupleTypes.size() != 2) return false;
      return true;
   }
   
   public boolean isANumber()
   {
      if (getType() == TYPE_NATURAL  ||
          getType() == TYPE_NATURAL1 ||
          getType() == TYPE_NUMBER   ||
          getType() == TYPE_REAL     ||
          getType() == TYPE_GENERIC)
         return true;
      
      if (getType() == TYPE_GENERIC)
      {
         if (m_genericType == TYPE_UNDEFINED)
         {
            m_genericType = TYPE_REAL;
         }
         if (m_genericType == TYPE_NATURAL ||
             m_genericType == TYPE_NATURAL1 ||
             m_genericType == TYPE_NUMBER ||
             m_genericType == TYPE_REAL)
            return true;
      }
      return false;
   }
   
   public boolean isTuple()
   {
      return (m_type == TYPE_TUPLE);
   }
   
   /*
    * Determine if the specified type is compatible with the one
    * represented by the current object.
    */
   
   public boolean isCompatible(AstType type)
   {
      if (type.isANumber())
      {
         AstType newType = resultantType(type);
         return !newType.isUndefined();
      }
      
      if (isSet() && type.isSet())
      {
         if ((getSetType().getType() == TYPE_EMPTY) ||
             (type.getSetType().getType() == TYPE_EMPTY)) return true;
         return getSetType().isCompatible(type.getSetType());
      }

      if (isSequence() && type.isSequence())
      {
         return getSetType().isCompatible(type.getSetType());
      }
      
      if (getType() == type.getType()) return isEqual(type);
      return false;
   }
   
   public boolean isEqual(AstType type)
   {
      if (type == null) return false;
      
      if (getType() == TYPE_GENERIC) return true;
      if (type.getType() == TYPE_GENERIC) return true;
      
      switch (getType())
      {
         case TYPE_NATURAL:
         case TYPE_NATURAL1:
         case TYPE_NUMBER:
         case TYPE_REAL:
            return isCompatible(type);
         case TYPE_BOOL:
            return getType() == type.getType();
         case TYPE_SET:
         case TYPE_SEQUENCE:
         case TYPE_BAG:
            return getSetType().isEqual(type.getSetType());
         case TYPE_TUPLE:
            int i;
            if (m_tupleTypes.size() != type.m_tupleTypes.size()) return false;
            for (i=0; i<m_tupleTypes.size(); i++)
            {
               AstType t1 = (AstType)m_tupleTypes.get(i);
               AstType t2 = (AstType)type.m_tupleTypes.get(i);
               if (!t1.isCompatible(t2)) return false;
            }
            return true;
         case TYPE_FREE:
         case TYPE_BASIC:
         case TYPE_SCHEMA:
            return getName().equals(type.getName());
            
         case TYPE_CLASS:
            if (isSuperClass(type.m_name) ||
                type.isSuperClass(m_name))
               return true;
            break;
      }
      return false;
   }
   
   public boolean isSuperClass(String name)
   {
      if (name == null) return false;
      if (m_classMembers != null) return m_classMembers.isSuperClass(name);
      return false;
   }
   
   public boolean isClassDef()
   {
      if (getType() != TYPE_SET) return false;
      if (m_setType.getType() != TYPE_CLASS) return false;
      return true;
   }
   
   public boolean isSchemaDef()
   {
      if (getType() != TYPE_SET) return false;
      if (m_setType.getType() != TYPE_SCHEMA) return false;
      return true;
   }
   
   public boolean isOperation()
   {
      return getType() == TYPE_OPERATION;
   }

   /*
    * Given the specified type, determine what the resultant type
    * would be when combined with the one represented by the current
    * object.
    */
   
   public AstType resultantType(AstType type)
   {
      int newType = TYPE_UNDEFINED;
      
      if (type.getType() == TYPE_GENERIC)
      {
         return this;
      }
      
      if (getType() == TYPE_GENERIC)
      {
         return type;
      }
      
      if (getType() == TYPE_NATURAL)
      {
         if (type.getType() == TYPE_NATURAL)       newType = TYPE_NATURAL;
         else if (type.getType() == TYPE_NATURAL1) newType = TYPE_NATURAL;
         else if (type.getType() == TYPE_NUMBER)   newType = TYPE_NUMBER;
         else if (type.getType() == TYPE_REAL)     newType = TYPE_REAL;
      }
      else if (getType() == TYPE_NATURAL1)
      {
         if (type.getType() == TYPE_NATURAL)       newType = TYPE_NATURAL;
         else if (type.getType() == TYPE_NATURAL1) newType = TYPE_NATURAL1;
         else if (type.getType() == TYPE_NUMBER)   newType = TYPE_NUMBER;
         else if (type.getType() == TYPE_REAL)     newType = TYPE_REAL;
      }
      else if (getType() == TYPE_NUMBER)
      {
         if (type.getType() == TYPE_NATURAL)       newType = TYPE_NUMBER;
         else if (type.getType() == TYPE_NATURAL1) newType = TYPE_NUMBER;
         else if (type.getType() == TYPE_NUMBER)   newType = TYPE_NUMBER;
         else if (type.getType() == TYPE_REAL)     newType = TYPE_REAL;
      }
      else if (getType() == TYPE_REAL)
      {
         newType = TYPE_REAL;
      }
      else if (getType() == TYPE_BOOL)
      {
         if (type.getType() == TYPE_BOOL) newType = TYPE_BOOL;
      }
      return new AstType(newType);
   }
   
   public boolean isUndefined()
   {
      if (getType() == TYPE_UNDEFINED) return true;
      
      if ((getType() == TYPE_SET)      ||
          (getType() == TYPE_SEQUENCE) ||
          (getType() == TYPE_BAG))
      {
         if (getSetType() == null) return true;
         return getSetType().isUndefined();
      }
      
      if (getType() == TYPE_TUPLE)
      {
         int i;
         for (i=0; i<m_tupleTypes.size(); i++)
         {
            if (((AstType)m_tupleTypes.get(i)).isUndefined()) return true;
         }
      }
      
      return false;
   }
   
   /*
    * Create a tuple from the provided types. If one of the types is
    * a tuple, then use the types of the tuple and not the tuple
    * itself.
    */
   
   public void newTuple(AstType typeL, AstType typeR)
   {
      int i;
      m_type = TYPE_TUPLE;
      
      if (typeL.getType() == AstType.TYPE_TUPLE)
      {
         for (i=0; i<typeL.m_tupleTypes.size(); i++)
         {
            m_tupleTypes.add((AstType)typeL.m_tupleTypes.get(i));
         }
      }
      else
      {
         m_tupleTypes.add(typeL);
      }
      
      if (typeR.getType() == AstType.TYPE_TUPLE)
      {
         for (i=0; i<typeR.m_tupleTypes.size(); i++)
         {
            m_tupleTypes.add((AstType)typeR.m_tupleTypes.get(i));
         }
      }
      else
      {
         m_tupleTypes.add(typeR);
      }
   }
   
   /*
    * For debugging
    */
   
   public String toString()
   {
      String str = new String();
      
      if (getType() == TYPE_SET)
      {
         return "{" + m_setType.toString() + "}";
      }
      if (getType() == TYPE_SEQUENCE)
      {
         return "<" + m_setType.toString() + ">";
      }
      if (getType() == TYPE_BAG)
      {
         return "[[" + m_setType.toString() + "]]";
      }
      switch (getType())
      {
         case TYPE_NATURAL: str = "NAT"; break;
         case TYPE_NATURAL1: str = "NAT1"; break;
         case TYPE_NUMBER: str = "NUMBER"; break;
         case TYPE_REAL: str = "REAL"; break;
         case TYPE_BOOL: str = "BOOL"; break;
         case TYPE_TUPLE:
             int i;
             str = "(";
             for (i=0; i<m_tupleTypes.size(); i++)
             {
                if (i > 0) str += ",";
                str += ((AstType)m_tupleTypes.get(i)).toString();
             }
             str += ")";
             break;
         case TYPE_FREE:
            str = "FREE (" + m_name + ")";
            break;
         case TYPE_CLASS:
            str = "CLASS (" + m_name + ")";
            break;
         case TYPE_SCHEMA:
            str = "SCHEMA (" + m_name + ")";
            break;
         default: return "undefined";
      }
      return str;
   }
}

