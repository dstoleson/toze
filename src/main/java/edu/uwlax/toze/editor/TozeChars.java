package edu.uwlax.toze.editor;

import java.util.*;

public class TozeChars
{
    public static Set<TozeCharMap> all;
    public static TreeMap m_map = null;
    int m_pos;

    public TozeChars()
    {
        if (m_map == null)
            {
            m_map = new TreeMap();

            m_map.put("%integer%", Character.toString(TozeFontMap.CHAR_NUM));
            m_map.put("%sminus1%", Character.toString(TozeFontMap.CHAR_SUPER_MINUS_1));
            m_map.put("%pset%", Character.toString(TozeFontMap.CHAR_POWER));
            m_map.put("%fset%", Character.toString(TozeFontMap.CHAR_FINSET));
            m_map.put("%nat%", Character.toString(TozeFontMap.CHAR_NAT));
            m_map.put("%natone%", Character.toString(TozeFontMap.CHAR_NAT) + Character.toString(TozeFontMap.CHAR_SUB_1));
            m_map.put("%integer%", Character.toString(TozeFontMap.CHAR_NUM));
            m_map.put("%real%", Character.toString(TozeFontMap.CHAR_R));
            m_map.put("%sub1%", Character.toString(TozeFontMap.CHAR_SUB_1));
            m_map.put("%map%", Character.toString(TozeFontMap.CHAR_MAPSTO));
            m_map.put("%rel%", Character.toString(TozeFontMap.CHAR_REL));
            m_map.put("%pfun%", Character.toString(TozeFontMap.CHAR_PFUN));
            m_map.put("%tfun%", Character.toString(TozeFontMap.CHAR_FUN));
            m_map.put("%pinj%", Character.toString(TozeFontMap.CHAR_PINJ));
            m_map.put("%tinj%", Character.toString(TozeFontMap.CHAR_INJ));
            m_map.put("%tsur%", Character.toString(TozeFontMap.CHAR_SURJ));
            m_map.put("%psur%", Character.toString(TozeFontMap.CHAR_PSURJ));
            m_map.put("%bij%", Character.toString(TozeFontMap.CHAR_BIJ));
            m_map.put("%ffun%", Character.toString(TozeFontMap.CHAR_FFUN));
            m_map.put("%finj%", Character.toString(TozeFontMap.CHAR_FINJ));
            m_map.put("%prod%", Character.toString(TozeFontMap.CHAR_CROSS));
            m_map.put("%mem%", Character.toString(TozeFontMap.CHAR_IN));
            m_map.put("%nem%", Character.toString(TozeFontMap.CHAR_NOTIN));
            m_map.put("%neq%", Character.toString(TozeFontMap.CHAR_NEQ));
            m_map.put("%skip%", Character.toString(TozeFontMap.CHAR_PARA_SKIP));
            m_map.put("%emptyset%", Character.toString(TozeFontMap.CHAR_EMPTYSET));
            m_map.put("%psubs%", Character.toString(TozeFontMap.CHAR_PROPSUBSET));
            m_map.put("%subs%", Character.toString(TozeFontMap.CHAR_SUBSET));
            m_map.put("%int%", Character.toString(TozeFontMap.CHAR_CAP));
            m_map.put("%uni%", Character.toString(TozeFontMap.CHAR_CUP));
            m_map.put("%setminus%", Character.toString(TozeFontMap.CHAR_SETMINUS));
            m_map.put("%bigcap%", Character.toString(TozeFontMap.CHAR_BIGCAP));
            m_map.put("%bigcup%", Character.toString(TozeFontMap.CHAR_BIGCUP));
            m_map.put("%fcmp%", Character.toString(TozeFontMap.CHAR_COMP));
            m_map.put("%circ%", Character.toString(TozeFontMap.CHAR_CIRC));
            m_map.put("%dres%", Character.toString(TozeFontMap.CHAR_DRES));
            m_map.put("%rres%", Character.toString(TozeFontMap.CHAR_RRES));
            m_map.put("%dsub%", Character.toString(TozeFontMap.CHAR_NDRES));
            m_map.put("%rsub%", Character.toString(TozeFontMap.CHAR_NRRES));
            m_map.put("%inv%", Character.toString(TozeFontMap.CHAR_INV));
            m_map.put("%tcl%", Character.toString(TozeFontMap.CHAR_PLUS_POST));
            m_map.put("%rtcl%", Character.toString(TozeFontMap.CHAR_TIMES_POST));
            m_map.put("%oplus%", Character.toString(TozeFontMap.CHAR_OPLUS));
            m_map.put("%cat%", Character.toString(TozeFontMap.CHAR_CAT));
            m_map.put("%dcat%", Character.toString(TozeFontMap.CHAR_DCAT));
            m_map.put("%project%", Character.toString(TozeFontMap.CHAR_PROJECT));
            m_map.put("%uplus%", Character.toString(TozeFontMap.CHAR_UPLUS));
            m_map.put("%subseteqplus%", Character.toString(TozeFontMap.CHAR_SUBSETEQPLUS));
            m_map.put("%lseq%", Character.toString(TozeFontMap.CHAR_LANGLE));
            m_map.put("%rseq%", Character.toString(TozeFontMap.CHAR_RANGLE));
            m_map.put("%lbag%", Character.toString(TozeFontMap.CHAR_LBAG));
            m_map.put("%rbag%", Character.toString(TozeFontMap.CHAR_RBAG));
            m_map.put("%limg%", Character.toString(TozeFontMap.CHAR_LIMG));
            m_map.put("%rimg%", Character.toString(TozeFontMap.CHAR_RIMG));
            m_map.put("%all%", Character.toString(TozeFontMap.CHAR_FORALL));
            m_map.put("%exi%", Character.toString(TozeFontMap.CHAR_EXISTS));
            m_map.put("%dot%", Character.toString(TozeFontMap.CHAR_SPOT));
            m_map.put("%suchthat%", Character.toString(TozeFontMap.CHAR_SPOT));
            m_map.put("%lnot%", Character.toString(TozeFontMap.CHAR_NOT));
            m_map.put("%land%", Character.toString(TozeFontMap.CHAR_AND));
            m_map.put("%lor%", Character.toString(TozeFontMap.CHAR_OR));
            m_map.put("%imp%", Character.toString(TozeFontMap.CHAR_IMPLIES));
            m_map.put("%iff%", Character.toString(TozeFontMap.CHAR_IFF));
            m_map.put("%lang%", Character.toString(TozeFontMap.CHAR_LDATA));
            m_map.put("%rang%", Character.toString(TozeFontMap.CHAR_RDATA));
            m_map.put("%skip%", Character.toString(TozeFontMap.CHAR_UK1));
            m_map.put("%skip%", Character.toString(TozeFontMap.CHAR_UK2));
            m_map.put("%sdef%", Character.toString(TozeFontMap.CHAR_DEFS));
            m_map.put("%leadsto%", Character.toString(TozeFontMap.CHAR_CURVEY));
            m_map.put("%leq%", Character.toString(TozeFontMap.CHAR_LEQ));
            m_map.put("%geq%", Character.toString(TozeFontMap.CHAR_GEQ));
            m_map.put("%delta%", Character.toString(TozeFontMap.CHAR_DELTA));
            m_map.put("%xi%", Character.toString(TozeFontMap.CHAR_XI));
            m_map.put("%lambda%", Character.toString(TozeFontMap.CHAR_LAMBDA));
            m_map.put("%mu%", Character.toString(TozeFontMap.CHAR_MU));
            m_map.put("%theta%", Character.toString(TozeFontMap.CHAR_THETA));
            m_map.put("%darrow%", Character.toString(TozeFontMap.CHAR_DARROW));
            m_map.put("%!%", Character.toString(TozeFontMap.CHAR_SMALLBANG));
            m_map.put("%bool%", Character.toString(TozeFontMap.CHAR_BOOL));
            m_map.put("%box%", Character.toString(TozeFontMap.CHAR_BOX));
            m_map.put("%rharpoon%", Character.toString(TozeFontMap.CHAR_RHARPOON));
            m_map.put("%extract%",      Character.toString(TozeFontMap.CHAR_EXTRACT));
            }
    }

    public String mapLast(String str, int pos)
    {
        if (pos > 0)
            {
            if (str.charAt(pos - 1) == '%')
                {
                int idx = str.lastIndexOf("%", pos - 2);
                if (idx > -1)
                    {
                    String tst = str.substring(idx, pos);
                    String c = (String) m_map.get(tst);
                    if (c != null)
                        {
                        String ret = str.substring(0, idx) + c + str.substring(pos);
                        m_pos = idx + c.length();
                        return ret;
                        }
                    }
                }
            }

        return null;
    }

    static public String getMap()
    {
        String ret = new String();
        Collection c = m_map.values();
        Set s = m_map.entrySet();
        Iterator i = s.iterator();
        Map.Entry e;
        while (i.hasNext())
            {
            e = (Map.Entry) i.next();
            ret += " " + (String) e.getKey();
            ret += " ........ ";
            ret += (String) e.getValue();
            ret += "\n";
            }
        return ret;
    }
}
