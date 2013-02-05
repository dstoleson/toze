package edu.uwlax.toze.objectz;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;

public class TozeFontMap
{
    static private Font m_font = null;

    static public Font getFont()
    {
        if (m_font != null)
            {
            return m_font;
            }

        try
            {
            InputStream fontStream = TozeFontMap.class.getResourceAsStream("zfont.ttf");
            m_font = Font.createFont(Font.TRUETYPE_FONT, fontStream);

            AffineTransform at = new AffineTransform();
            at.setToScale(20.0, 20.0);
            AffineTransform at2 = new AffineTransform();
            at2.setToTranslation(0.0, 0.15);
            at.concatenate(at2);

            m_font = m_font.deriveFont(at);
            }
        catch (Exception e)
            {
            System.out.println(e.toString());
            }

        return m_font;
    }
    
    public static final char CHAR_SUPER_MINUS_1 = 160;
    public static final char CHAR_POWER = 161;
    public static final char CHAR_FINSET = 162;
    public static final char CHAR_NAT = 163;
    public static final char CHAR_NUM = 164;
    public static final char CHAR_R = 165;
    public static final char CHAR_SUB_1 = 166;
    public static final char CHAR_MAPSTO = 167;
    public static final char CHAR_REL = 168;
    public static final char CHAR_PFUN = 169;
    public static final char CHAR_FUN = 170;
    public static final char CHAR_PINJ = 171;
    public static final char CHAR_INJ = 172;
    public static final char CHAR_SURJ = 173;
    public static final char CHAR_PSURJ = 174;
    public static final char CHAR_BIJ = 175;
    public static final char CHAR_FFUN = 176;
    public static final char CHAR_FINJ = 177;
    public static final char CHAR_CROSS = 178;
    public static final char CHAR_IN = 179;
    public static final char CHAR_NOTIN = 180;
    public static final char CHAR_NEQ = 181;
    public static final char CHAR_PARA_SKIP = 182;
    public static final char CHAR_EMPTYSET = 184;
    public static final char CHAR_PROPSUBSET = 185;
    public static final char CHAR_SUBSET = 186;
    public static final char CHAR_CAP = 187;
    public static final char CHAR_CUP = 188;
    public static final char CHAR_SETMINUS = 189;
    public static final char CHAR_BIGCAP = 190;
    public static final char CHAR_BIGCUP = 191;
    public static final char CHAR_COMP = 192; // Big semi colon
    public static final char CHAR_CIRC = 193;
    public static final char CHAR_DRES = 194;
    public static final char CHAR_RRES = 195;
    public static final char CHAR_NDRES = 196;
    public static final char CHAR_NRRES = 197;
    public static final char CHAR_INV = 198;
    public static final char CHAR_PLUS_POST = 199;
    public static final char CHAR_TIMES_POST = 200;
    public static final char CHAR_OPLUS = 201;
    public static final char CHAR_CAT = 202;
    public static final char CHAR_DCAT = 203;
    public static final char CHAR_PROJECT = 204; // Harpoon
    public static final char CHAR_UPLUS = 205;
    public static final char CHAR_SUBSETEQPLUS = 206;
    public static final char CHAR_LANGLE = 207;
    public static final char CHAR_RANGLE = 208;
    public static final char CHAR_LBAG = 209;
    public static final char CHAR_RBAG = 210;
    public static final char CHAR_LIMG = 211;
    public static final char CHAR_RIMG = 212;
    public static final char CHAR_FORALL = 213;
    public static final char CHAR_EXISTS = 214;
    public static final char CHAR_SPOT = 215;
    public static final char CHAR_NOT = 216;
    public static final char CHAR_AND = 217;
    public static final char CHAR_OR = 218;
    public static final char CHAR_IMPLIES = 219;
    public static final char CHAR_IFF = 220;
    public static final char CHAR_LDATA = 221;
    public static final char CHAR_RDATA = 222;
    public static final char CHAR_UK1 = 223;
    public static final char CHAR_UK2 = 224;
    public static final char CHAR_DEFS = 225;
    public static final char CHAR_CURVEY = 226;
    public static final char CHAR_LEQ = 227;
    public static final char CHAR_GEQ = 228;
    public static final char CHAR_DELTA = 243;
    public static final char CHAR_XI = 244;
    public static final char CHAR_LAMBDA = 245;
    public static final char CHAR_MU = 246;
    public static final char CHAR_THETA = 247;
    public static final char CHAR_DARROW = 1;
    public static final char CHAR_BOOL = 353;
    public static final char CHAR_BOX = 339;
    public static final char CHAR_RHARPOON = 204;
    public static final char CHAR_SMALLBANG = 8250;
}
