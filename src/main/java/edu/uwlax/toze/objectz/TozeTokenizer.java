package edu.uwlax.toze.objectz;

import java.io.*;
import java.util.*;

public class TozeTokenizer
{
    public static final int TOKEN_EOL = -2;
    public static final int TOKEN_EOS = -1;
    public static final int TOKEN_ERROR = 0;
    public static final int TOKEN_WORD = 1;
    public static final int TOKEN_NUMBER = 2;
    public static final int TOKEN_DEFS = 3;
    public static final int TOKEN_DDEF = 4;
    public static final int TOKEN_DLARROW = 5;
    public static final int TOKEN_DRARROW = 6;
    public static final int TOKEN_LDANGLE = 7;
    public static final int TOKEN_RDANGLE = 8;
    public static final int TOKEN_SDEF = 9;
    public static final int TOKEN_PROJECT = 10;
    public static final int TOKEN_INCREMENT = 11;
    public static final int TOKEN_LPAREN = 12;
    public static final int TOKEN_RPAREN = 13;
    public static final int TOKEN_LAND = 14;
    public static final int TOKEN_BOX = 15;
    public static final int TOKEN_FCMP = 16;
    public static final int TOKEN_PARALLELTO = 17;
    public static final int TOKEN_DOT = 18;
    public static final int TOKEN_LNOT = 19;
    public static final int TOKEN_LOR = 20;
    public static final int TOKEN_RDARROW = 21;
    public static final int TOKEN_LRDRARROW = 22;
    public static final int TOKEN_BSLASH = 23;
    public static final int TOKEN_FSLASH = 24;
    public static final int TOKEN_MUCHGREATERTHAN = 25;
    public static final int TOKEN_ALL = 26;
    public static final int TOKEN_EXI = 27;
    public static final int TOKEN_MEM = 28;
    public static final int TOKEN_PROD = 29;
    public static final int TOKEN_LAMBDA = 30;
    public static final int TOKEN_MU = 31;
    public static final int TOKEN_DSTRUCKP = 32;
    public static final int TOKEN_DOWNARROW = 33;
    public static final int TOKEN_UNI = 34;
    public static final int TOKEN_COPYRIGHT = 35;
    public static final int TOKEN_MLABRACKET = 36;
    public static final int TOKEN_MRABRACKET = 37;
    public static final int TOKEN_MLWSBRACKET = 38;
    public static final int TOKEN_MRWSBRACKET = 39;
    public static final int TOKEN_THETA = 40;
    public static final int TOKEN_MAP = 41;
    public static final int TOKEN_UPTO = 42;
    public static final int TOKEN_PLUS = 43;
    public static final int TOKEN_MINUS = 44;
    public static final int TOKEN_CHARTIE = 45;
    public static final int TOKEN_UPLUS = 46;
    public static final int TOKEN_UNIONMINUS = 47;
    public static final int TOKEN_TIMES = 48;
    public static final int TOKEN_UHARPOONLEFT = 49;
    public static final int TOKEN_UHARPOONRIGHT = 49;
    public static final int TOKEN_RINGOPERATOR = 50;
    public static final int TOKEN_CIRCLEDTIMES = 51;
    public static final int TOKEN_CIRCLEDPLUS = 52;
    public static final int TOKEN_HASH = 53;
    public static final int TOKEN_LWHITEARROW = 54;
    public static final int TOKEN_RWHITEARROW = 55;
    public static final int TOKEN_DRES = 56;
    public static final int TOKEN_RRES = 57;
    public static final int TOKEN_COMMA = 58;
    public static final int TOKEN_APOS = 59;
    public static final int TOKEN_QUESTION = 60;
    public static final int TOKEN_EXCLAMATION = 61;
    //public static final int TOKEN_SUB_1           = 62;
    public static final int TOKEN_WHITESPACE = 63;
    public static final int TOKEN_SEMICOLON = 64;
    public static final int TOKEN_COLON = 65;
    public static final int TOKEN_IF = 66;
    public static final int TOKEN_THEN = 67;
    public static final int TOKEN_ELSE = 68;
    public static final int TOKEN_DLABRACKET = 69;
    public static final int TOKEN_DRABRACKET = 70;
    public static final int TOKEN_PIPE = 71;
    public static final int TOKEN_USCORE = 72;
    public static final int TOKEN_IRSYMBOL = 73;
    public static final int TOKEN_IFSYMBOL = 74;
    public static final int TOKEN_PFSYMBOL = 75;
    public static final int TOKEN_IGSYMBOL = 76;
    public static final int TOKEN_PGSYMBOL = 77;
    public static final int TOKEN_PRSYMBOL = 78;
    public static final int TOKEN_EXIONE = 79;
    public static final int TOKEN_LET = 80;
    //public static final int TOKEN_DOT             = 81;
    public static final int TOKEN_PERIOD = 82;
    public static final int TOKEN_HYPHEN = 83;
    public static final int TOKEN_PSET = 84;
    public static final int TOKEN_SELF = 85;
    public static final int TOKEN_LBRACE = 86;
    public static final int TOKEN_RBRACE = 87;
    public static final int TOKEN_DARROW = 88;
    public static final int TOKEN_LBRACKET = 89;
    public static final int TOKEN_RBRACKET = 90;
    public static final int TOKEN_TRUE = 91;
    public static final int TOKEN_FALSE = 92;
    public static final int TOKEN_EQUAL = 93;
    public static final int TOKEN_PRE = 94;
    public static final int TOKEN_DOTINIT = 95;
    public static final int TOKEN_IMP = 96;
    public static final int TOKEN_IFF = 97;
    public static final int TOKEN_MUCHGT = 98;
    public static final int TOKEN_WHITEBOX = 99;
    public static final int TOKEN_PARALLELTOE = 100;
    public static final int TOKEN_DELTA = 101;
    public static final int TOKEN_NEQ = 102;
    public static final int TOKEN_NEM = 103;
    public static final int TOKEN_SUBSETOREQUAL = 104;
    public static final int TOKEN_SUBS = 105;
    public static final int TOKEN_LESSTHAN = 106;
    public static final int TOKEN_LEQ = 107;
    public static final int TOKEN_GEQ = 108;
    public static final int TOKEN_GREATERTHAN = 109;
    public static final int TOKEN_PREFIX = 110;
    public static final int TOKEN_SUFFIX = 111;
    public static final int TOKEN_INSEQ = 112;
    public static final int TOKEN_BAGMEMBERSHIP = 113;
    public static final int TOKEN_SQUAREIMAGEOREQUAL = 114;
    public static final int TOKEN_PARTITIONS = 115;
    public static final int TOKEN_TILDE = 116;
    public static final int TOKEN_DISJOINT = 117;
    public static final int TOKEN_PSETONE = 118;
    public static final int TOKEN_ID = 119;
    public static final int TOKEN_FSET = 120;
    public static final int TOKEN_FSETONE = 121;
    public static final int TOKEN_SEQ = 122;
    public static final int TOKEN_SEQONE = 123;
    public static final int TOKEN_ISEQ = 124;
    public static final int TOKEN_BAG = 125;
    public static final int TOKEN_NAT = 126;
    public static final int TOKEN_NATONE = 127;
    public static final int TOKEN_BOOL = 128;
    public static final int TOKEN_REAL = 130;
    public static final int TOKEN_SUPER_MINUS_1 = 131;
    public static final int TOKEN_REL = 132;
    public static final int TOKEN_INTEGER = 133;
    public static final int TOKEN_MOD = 134;
    public static final int TOKEN_DIV = 135;
    public static final int TOKEN_PFUN = 136;
    public static final int TOKEN_TFUN = 137;
    public static final int TOKEN_PINJ = 138;
    public static final int TOKEN_TINJ = 139;
    public static final int TOKEN_TSUR = 140;
    public static final int TOKEN_PSUR = 141;
    public static final int TOKEN_BIJ = 142;
    public static final int TOKEN_FFUN = 143;
    public static final int TOKEN_FINJ = 144;
    public static final int TOKEN_PARA_SKIP = 145;
    public static final int TOKEN_EMPTYSET = 146;
    public static final int TOKEN_PSUBS = 147;
    public static final int TOKEN_INT = 148;
    public static final int TOKEN_SETMINUS = 149;
    public static final int TOKEN_BIGCAP = 150;
    public static final int TOKEN_BIGCUP = 151;
    public static final int TOKEN_CIRC = 152;
    public static final int TOKEN_DSUB = 153;
    public static final int TOKEN_RSUB = 154;
    public static final int TOKEN_INV = 155;
    public static final int TOKEN_TCL = 156;
    public static final int TOKEN_RTCL = 157;
    public static final int TOKEN_FOVR = 158;
    public static final int TOKEN_CAT = 159;
    public static final int TOKEN_DCAT = 160;
    public static final int TOKEN_SUBSETEQPLUS = 161;
    public static final int TOKEN_LSEQ = 162;
    public static final int TOKEN_RSEQ = 163;
    public static final int TOKEN_LBAG = 164;
    public static final int TOKEN_RBAG = 165;
    public static final int TOKEN_LIMG = 166;
    public static final int TOKEN_RIMG = 167;
    public static final int TOKEN_LANG = 168;
    public static final int TOKEN_RANG = 169;
    public static final int TOKEN_LEADSTO = 170;
    public static final int TOKEN_XI = 171;
    public static final int TOKEN_DOM = 172;
    public static final int TOKEN_RAN = 173;
    public static final int TOKEN_REV = 174;
    public static final int TOKEN_HEAD = 175;
    public static final int TOKEN_LAST = 176;
    public static final int TOKEN_TAIL = 177;
    public static final int TOKEN_FRONT = 178;
    public static final int TOKEN_DPIPE = 179;
    public static final int TOKEN_FIRST = 180;
    public static final int TOKEN_SECOND = 181;
    public static final int TOKEN_DPIPEBANG = 182;
    public StringBuffer m_current = new StringBuffer(102);
    private Reader m_reader;
    private TreeMap m_singles = new TreeMap();
    private TreeMap m_keywords = new TreeMap();
    private Stack m_lastChars = new Stack();
    private int m_lineNum = 0;
    private int m_lineStart = 0;
    private int m_pos;

    public TozeTokenizer()
    {
    }

    public TozeTokenizer(Reader reader)
    {
        m_pos = 0;
        m_lineNum = 0;

        m_reader = reader;

        m_keywords.put("if", new Integer(TOKEN_IF));
        m_keywords.put("then", new Integer(TOKEN_THEN));
        m_keywords.put("else", new Integer(TOKEN_ELSE));
        m_keywords.put("let", new Integer(TOKEN_LET));
        m_keywords.put("self", new Integer(TOKEN_SELF));
        m_keywords.put("true", new Integer(TOKEN_TRUE));
        m_keywords.put("false", new Integer(TOKEN_FALSE));
        m_keywords.put("PRE", new Integer(TOKEN_PRE));
        m_keywords.put("Init", new Integer(TOKEN_DOTINIT));
        m_keywords.put("prefix", new Integer(TOKEN_PREFIX));
        m_keywords.put("suffix", new Integer(TOKEN_SUFFIX));
        m_keywords.put("in", new Integer(TOKEN_INSEQ));
        m_keywords.put("partition", new Integer(TOKEN_PARTITIONS));
        m_keywords.put("disjoint", new Integer(TOKEN_DISJOINT));
        //m_keywords.put("and",                new Integer(TOKEN_AND));
        //m_keywords.put("or",                 new Integer(TOKEN_OR));
        m_keywords.put("mod", new Integer(TOKEN_MOD));
        m_keywords.put("div", new Integer(TOKEN_DIV));
        m_keywords.put("seq", new Integer(TOKEN_SEQ));
        m_keywords.put("seq" + TozeFontMap.CHAR_SUB_1, new Integer(TOKEN_SEQONE));
        m_keywords.put("iseq", new Integer(TOKEN_ISEQ));
        m_keywords.put("dom", new Integer(TOKEN_DOM));
        m_keywords.put("ran", new Integer(TOKEN_RAN));
        m_keywords.put("||", new Integer(TOKEN_DPIPE));
        m_keywords.put("||" + TozeFontMap.CHAR_SMALLBANG, new Integer(TOKEN_DPIPEBANG));
        m_keywords.put("first", new Integer(TOKEN_FIRST));
        m_keywords.put("second", new Integer(TOKEN_SECOND));
        m_keywords.put("[]", new Integer(TOKEN_BOX));
        m_keywords.put("head", new Integer(TOKEN_HEAD));
        m_keywords.put("last", new Integer(TOKEN_LAST));
        m_keywords.put("front", new Integer(TOKEN_FRONT));
        m_keywords.put("tail", new Integer(TOKEN_TAIL));
        m_keywords.put("bag", new Integer(TOKEN_BAG));

        // leqslant
        // geqslant
        // nexi

        m_keywords.put("%sminus1", new Integer(TOKEN_SUPER_MINUS_1));
        m_keywords.put("%pset", new Integer(TOKEN_PSET));
        m_keywords.put("%fset", new Integer(TOKEN_FSET));
        m_keywords.put("%nat", new Integer(TOKEN_NAT));
        m_keywords.put("%integer", new Integer(TOKEN_INTEGER));
        m_keywords.put("%real", new Integer(TOKEN_REAL));
        m_keywords.put("%bool", new Integer(TOKEN_BOOL));
        //m_keywords.put("%sub1", new Integer(TOKEN_SUB_1));
        m_keywords.put("%map", new Integer(TOKEN_MAP));
        m_keywords.put("%rel", new Integer(TOKEN_REL));
        m_keywords.put("%pfun", new Integer(TOKEN_PFUN));
        m_keywords.put("%tfun", new Integer(TOKEN_TFUN));
        m_keywords.put("%pinj", new Integer(TOKEN_PINJ));
        m_keywords.put("%tinj", new Integer(TOKEN_TINJ));
        m_keywords.put("%tsur", new Integer(TOKEN_TSUR));
        m_keywords.put("%psur", new Integer(TOKEN_PSUR));
        m_keywords.put("%bij", new Integer(TOKEN_BIJ));
        m_keywords.put("%ffun", new Integer(TOKEN_FFUN));
        m_keywords.put("%finj", new Integer(TOKEN_FINJ));
        m_keywords.put("%prod", new Integer(TOKEN_PROD));
        m_keywords.put("%inseq", new Integer(TOKEN_INSEQ));
        m_keywords.put("%nem", new Integer(TOKEN_NEM));
        m_keywords.put("%neq", new Integer(TOKEN_NEQ));
        m_keywords.put("%skip", new Integer(TOKEN_PARA_SKIP));
        m_keywords.put("%empyset", new Integer(TOKEN_EMPTYSET));
        m_keywords.put("%subs", new Integer(TOKEN_SUBS));
        m_keywords.put("%psubs", new Integer(TOKEN_PSUBS));
        m_keywords.put("%int", new Integer(TOKEN_INT));
        m_keywords.put("%uni", new Integer(TOKEN_UNI));
        m_keywords.put("%setminus", new Integer(TOKEN_SETMINUS));
        m_keywords.put("%bigcap", new Integer(TOKEN_BIGCAP));
        m_keywords.put("%bigcup", new Integer(TOKEN_BIGCUP));
        m_keywords.put("%fcmp", new Integer(TOKEN_FCMP));
        m_keywords.put("%circ", new Integer(TOKEN_CIRC));
        m_keywords.put("%dres", new Integer(TOKEN_DRES));
        m_keywords.put("%rres", new Integer(TOKEN_RRES));
        m_keywords.put("%dsub", new Integer(TOKEN_DSUB));
        m_keywords.put("%rsub", new Integer(TOKEN_RSUB));
        m_keywords.put("%inv", new Integer(TOKEN_INV));
        m_keywords.put("%tcl", new Integer(TOKEN_TCL));
        m_keywords.put("%rtcl", new Integer(TOKEN_RTCL));
        m_keywords.put("%fovr", new Integer(TOKEN_FOVR));
        m_keywords.put("%cat", new Integer(TOKEN_CAT));
        m_keywords.put("%dcat", new Integer(TOKEN_DCAT));
        m_keywords.put("%project", new Integer(TOKEN_PROJECT));
        m_keywords.put("%uplus", new Integer(TOKEN_UPLUS));
        m_keywords.put("%subseteq", new Integer(TOKEN_SUBSETEQPLUS));
        m_keywords.put("%lseq", new Integer(TOKEN_LSEQ));
        m_keywords.put("%rseq", new Integer(TOKEN_RSEQ));
        m_keywords.put("%lbag", new Integer(TOKEN_LBAG));
        m_keywords.put("%rbag", new Integer(TOKEN_RBAG));
        m_keywords.put("%limg", new Integer(TOKEN_LIMG));
        m_keywords.put("%rimg", new Integer(TOKEN_RIMG));
        m_keywords.put("%all", new Integer(TOKEN_ALL));
        m_keywords.put("%exi", new Integer(TOKEN_EXI));
        m_keywords.put("%dot", new Integer(TOKEN_DOT));
        m_keywords.put("%lnot", new Integer(TOKEN_LNOT));
        m_keywords.put("%land", new Integer(TOKEN_LAND));
        m_keywords.put("%lor", new Integer(TOKEN_LOR));
        m_keywords.put("%imp", new Integer(TOKEN_IMP));
        m_keywords.put("%iff", new Integer(TOKEN_IFF));
        m_keywords.put("%lang", new Integer(TOKEN_LANG));
        m_keywords.put("%rang", new Integer(TOKEN_RANG));
        m_keywords.put("%skip", new Integer(TOKEN_ERROR));
        m_keywords.put("%skip", new Integer(TOKEN_ERROR));
        m_keywords.put("%sdef", new Integer(TOKEN_SDEF));
        m_keywords.put("%leadsto", new Integer(TOKEN_LEADSTO));
        m_keywords.put("%leq", new Integer(TOKEN_LEQ));
        m_keywords.put("%geq", new Integer(TOKEN_GEQ));
        m_keywords.put("%delta", new Integer(TOKEN_DELTA));
        m_keywords.put("%xi", new Integer(TOKEN_XI));
        m_keywords.put("%lambda", new Integer(TOKEN_LAMBDA));
        m_keywords.put("%mu", new Integer(TOKEN_MU));
        m_keywords.put("%theta", new Integer(TOKEN_THETA));

        /*
         * m_singles.put(new Integer(0x27EA), new Integer(TOKEN_LDANGLE)); //
         * left double angle
         * m_singles.put(new Integer(0x27EB), new Integer(TOKEN_RDANGLE)); //
         * right double angle
         * m_singles.put(new Integer(0x2259), new Integer(TOKEN_ESTIMATES));
         * m_singles.put(new Integer(0x2A21), new Integer(TOKEN_PROJECTION)); //
         * Harpoon
         * m_singles.put(new Integer(0x2206), new Integer(TOKEN_INCREMENT));
         * m_singles.put(new Integer(0x2227), new Integer(TOKEN_AND));
         * m_singles.put(new Integer(0x2AFF), new Integer(TOKEN_BOX));
         * m_singles.put(new Integer(0x2A1F), new Integer(TOKEN_COMPOSITION));
         * // Big semi colon
         * m_singles.put(new Integer(0x2225), new Integer(TOKEN_PARALLELTO));
         * m_singles.put(new Integer(0x2022), new Integer(TOKEN_SOLIDDOT));
         * m_singles.put(new Integer(0x0), new Integer(TOKEN_NOT));
         * m_singles.put(new Integer(0x2228), new Integer(TOKEN_OR));
         * m_singles.put(new Integer(0x21D2), new Integer(TOKEN_RDARROW)); //
         * right double arrow
         * m_singles.put(new Integer(0x21D4), new Integer(TOKEN_LRDRARROW)); //
         * left-right double arrow
         * m_singles.put(new Integer(0x2A20), new
         * Integer(TOKEN_MUCHGREATERTHAN));
         * m_singles.put(new Integer(0x2200), new Integer(TOKEN_FORALL));
         * m_singles.put(new Integer(0x2203), new Integer(TOKEN_THEREEXISTS));
         * m_singles.put(new Integer(0x2208), new Integer(TOKEN_ELEMENTOF));
         * m_singles.put(new Integer(0x2A2F), new Integer(TOKEN_CROSSPRODUCT));
         * m_singles.put(new Integer(0x03BB), new Integer(TOKEN_LAMBDA));
         * m_singles.put(new Integer(0x02BC), new Integer(TOKEN_MU));
         * m_singles.put(new Integer(0x2119), new Integer(TOKEN_DSTRUCKP)); //
         * double struck P
         * m_singles.put(new Integer(0x2193), new Integer(TOKEN_DOWNARROW));
         * m_singles.put(new Integer(0x222A), new Integer(TOKEN_UNION));
         * m_singles.put(new Integer(0x00A9), new Integer(TOKEN_COPYRIGHT));
         * m_singles.put(new Integer(0x27E8), new Integer(TOKEN_MLABRACKET)); //
         * mathematical left angle bracket
         * m_singles.put(new Integer(0x27E9), new Integer(TOKEN_MRABRACKET)); //
         * mathematical right angle bracket
         * m_singles.put(new Integer(0x27E6), new Integer(TOKEN_MLWSBRACKET));
         * // mathematical left white square bracket
         * m_singles.put(new Integer(0x27E7), new Integer(TOKEN_MRWSBRACKET));
         * // mathematical right white square bracket
         * m_singles.put(new Integer(0x0398), new Integer(TOKEN_THETA));
         * m_singles.put(new Integer(0x21A6), new Integer(TOKEN_RARROWBAR)); //
         * rightward arrow from bar
         * m_singles.put(new Integer(0x2025), new Integer(TOKEN_TWODOTS)); //
         * two dot leader
         * m_singles.put(new Integer(0x2040), new Integer(TOKEN_CHARTIE)); //
         * character tie
         * m_singles.put(new Integer(0x228E), new Integer(TOKEN_MULTISETUNION));
         * m_singles.put(new Integer(0x2A41), new Integer(TOKEN_UNIONMINUS)); //
         * union with minus sign
         * m_singles.put(new Integer(0x21BF), new Integer(TOKEN_UHARPOONLEFT));
         * m_singles.put(new Integer(0x21BE), new Integer(TOKEN_UHARPOONRIGHT));
         * m_singles.put(new Integer(0x2218), new Integer(TOKEN_RINGOPERATOR));
         * m_singles.put(new Integer(0x2297), new Integer(TOKEN_CIRCLEDTIMES));
         * m_singles.put(new Integer(0x2295), new Integer(TOKEN_CIRCLEDPLUS));
         * m_singles.put(new Integer(0x0), new Integer(TOKEN_LWHITEARROW));
         * m_singles.put(new Integer(0x0), new Integer(TOKEN_RWHITEARROW));
         * m_singles.put(new Integer(0x2A64), new
         * Integer(TOKEN_DOMAINARESTRICT)); // domain antirestriction
         * m_singles.put(new Integer(0x2A65), new
         * Integer(TOKEN_RANGEARESTRICT)); // range antirestriction
         */

        m_singles.put(new Integer(TozeFontMap.CHAR_SUPER_MINUS_1), new Integer(TOKEN_SUPER_MINUS_1));
        m_singles.put(new Integer(TozeFontMap.CHAR_POWER), new Integer(TOKEN_PSET));
        m_singles.put(new Integer(TozeFontMap.CHAR_FINSET), new Integer(TOKEN_FSET));
        m_singles.put(new Integer(TozeFontMap.CHAR_NAT), new Integer(TOKEN_NAT));
        m_singles.put(new Integer(TozeFontMap.CHAR_NUM), new Integer(TOKEN_INTEGER));
        m_singles.put(new Integer(TozeFontMap.CHAR_R), new Integer(TOKEN_REAL));
        //m_singles.put(new Integer(TozeFontMap.CHAR_SUB_1),         new Integer(TOKEN_SUB_1));
        m_singles.put(new Integer(TozeFontMap.CHAR_MAPSTO), new Integer(TOKEN_MAP));
        m_singles.put(new Integer(TozeFontMap.CHAR_REL), new Integer(TOKEN_REL));
        m_singles.put(new Integer(TozeFontMap.CHAR_PFUN), new Integer(TOKEN_PFUN));
        m_singles.put(new Integer(TozeFontMap.CHAR_FUN), new Integer(TOKEN_TFUN));
        m_singles.put(new Integer(TozeFontMap.CHAR_PINJ), new Integer(TOKEN_PINJ));
        m_singles.put(new Integer(TozeFontMap.CHAR_INJ), new Integer(TOKEN_TINJ));
        m_singles.put(new Integer(TozeFontMap.CHAR_SURJ), new Integer(TOKEN_TSUR));
        m_singles.put(new Integer(TozeFontMap.CHAR_PSURJ), new Integer(TOKEN_PSUR));
        m_singles.put(new Integer(TozeFontMap.CHAR_BIJ), new Integer(TOKEN_BIJ));
        m_singles.put(new Integer(TozeFontMap.CHAR_FFUN), new Integer(TOKEN_FFUN));
        m_singles.put(new Integer(TozeFontMap.CHAR_FINJ), new Integer(TOKEN_FINJ));
        m_singles.put(new Integer(TozeFontMap.CHAR_CROSS), new Integer(TOKEN_PROD));
        m_singles.put(new Integer(TozeFontMap.CHAR_IN), new Integer(TOKEN_MEM));
        m_singles.put(new Integer(TozeFontMap.CHAR_NOTIN), new Integer(TOKEN_NEM));
        m_singles.put(new Integer(TozeFontMap.CHAR_NEQ), new Integer(TOKEN_NEQ));
        m_singles.put(new Integer(TozeFontMap.CHAR_EMPTYSET), new Integer(TOKEN_EMPTYSET));
        m_singles.put(new Integer(TozeFontMap.CHAR_SUBSET), new Integer(TOKEN_SUBS));
        m_singles.put(new Integer(TozeFontMap.CHAR_PROPSUBSET), new Integer(TOKEN_PSUBS));
        m_singles.put(new Integer(TozeFontMap.CHAR_CAP), new Integer(TOKEN_INT));
        m_singles.put(new Integer(TozeFontMap.CHAR_CUP), new Integer(TOKEN_UNI));
        m_singles.put(new Integer(TozeFontMap.CHAR_SETMINUS), new Integer(TOKEN_SETMINUS));
        m_singles.put(new Integer(TozeFontMap.CHAR_BIGCAP), new Integer(TOKEN_BIGCAP));
        m_singles.put(new Integer(TozeFontMap.CHAR_BIGCUP), new Integer(TOKEN_BIGCUP));
        m_singles.put(new Integer(TozeFontMap.CHAR_COMP), new Integer(TOKEN_FCMP));
        m_singles.put(new Integer(TozeFontMap.CHAR_CIRC), new Integer(TOKEN_CIRC));
        m_singles.put(new Integer(TozeFontMap.CHAR_DRES), new Integer(TOKEN_DRES));
        m_singles.put(new Integer(TozeFontMap.CHAR_RRES), new Integer(TOKEN_RRES));
        m_singles.put(new Integer(TozeFontMap.CHAR_NDRES), new Integer(TOKEN_DSUB));
        m_singles.put(new Integer(TozeFontMap.CHAR_NRRES), new Integer(TOKEN_RSUB));
        m_singles.put(new Integer(TozeFontMap.CHAR_INV), new Integer(TOKEN_INV));
        m_singles.put(new Integer(TozeFontMap.CHAR_PLUS_POST), new Integer(TOKEN_TCL));
        m_singles.put(new Integer(TozeFontMap.CHAR_TIMES_POST), new Integer(TOKEN_RTCL));
        m_singles.put(new Integer(TozeFontMap.CHAR_OPLUS), new Integer(TOKEN_FOVR));
        m_singles.put(new Integer(TozeFontMap.CHAR_CAT), new Integer(TOKEN_CAT));
        m_singles.put(new Integer(TozeFontMap.CHAR_DCAT), new Integer(TOKEN_DCAT));
        m_singles.put(new Integer(TozeFontMap.CHAR_PROJECT), new Integer(TOKEN_PROJECT));
        m_singles.put(new Integer(TozeFontMap.CHAR_UPLUS), new Integer(TOKEN_UPLUS));
        m_singles.put(new Integer(TozeFontMap.CHAR_LANGLE), new Integer(TOKEN_LSEQ));
        m_singles.put(new Integer(TozeFontMap.CHAR_RANGLE), new Integer(TOKEN_RSEQ));
        m_singles.put(new Integer(TozeFontMap.CHAR_LBAG), new Integer(TOKEN_LBAG));
        m_singles.put(new Integer(TozeFontMap.CHAR_RBAG), new Integer(TOKEN_RBAG));
        m_singles.put(new Integer(TozeFontMap.CHAR_LIMG), new Integer(TOKEN_LIMG));
        m_singles.put(new Integer(TozeFontMap.CHAR_RIMG), new Integer(TOKEN_RIMG));
        m_singles.put(new Integer(TozeFontMap.CHAR_FORALL), new Integer(TOKEN_ALL));
        m_singles.put(new Integer(TozeFontMap.CHAR_EXISTS), new Integer(TOKEN_EXI));
        m_singles.put(new Integer(TozeFontMap.CHAR_SPOT), new Integer(TOKEN_DOT));
        m_singles.put(new Integer(TozeFontMap.CHAR_NOT), new Integer(TOKEN_LNOT));
        m_singles.put(new Integer(TozeFontMap.CHAR_AND), new Integer(TOKEN_LAND));
        m_singles.put(new Integer(TozeFontMap.CHAR_OR), new Integer(TOKEN_LOR));
        m_singles.put(new Integer(TozeFontMap.CHAR_IMPLIES), new Integer(TOKEN_IMP));
        m_singles.put(new Integer(TozeFontMap.CHAR_IFF), new Integer(TOKEN_IFF));
        m_singles.put(new Integer(TozeFontMap.CHAR_LDATA), new Integer(TOKEN_LANG));
        m_singles.put(new Integer(TozeFontMap.CHAR_RDATA), new Integer(TOKEN_RANG));
        m_singles.put(new Integer(TozeFontMap.CHAR_DEFS), new Integer(TOKEN_SDEF));
        m_singles.put(new Integer(TozeFontMap.CHAR_CURVEY), new Integer(TOKEN_LEADSTO));
        m_singles.put(new Integer(TozeFontMap.CHAR_LEQ), new Integer(TOKEN_LEQ));
        m_singles.put(new Integer(TozeFontMap.CHAR_GEQ), new Integer(TOKEN_GEQ));
        m_singles.put(new Integer(TozeFontMap.CHAR_DELTA), new Integer(TOKEN_DELTA));
        m_singles.put(new Integer(TozeFontMap.CHAR_XI), new Integer(TOKEN_XI));
        m_singles.put(new Integer(TozeFontMap.CHAR_LAMBDA), new Integer(TOKEN_LAMBDA));
        m_singles.put(new Integer(TozeFontMap.CHAR_MU), new Integer(TOKEN_MU));
        m_singles.put(new Integer(TozeFontMap.CHAR_THETA), new Integer(TOKEN_THETA));
        m_singles.put(new Integer(TozeFontMap.CHAR_DARROW), new Integer(TOKEN_DARROW));
        m_singles.put(new Integer(TozeFontMap.CHAR_BOOL), new Integer(TOKEN_BOOL));
        m_singles.put(new Integer(TozeFontMap.CHAR_BOX), new Integer(TOKEN_BOX));

        m_singles.put(new Integer('\\'), new Integer(TOKEN_BSLASH));    // backward slash
        m_singles.put(new Integer('/'), new Integer(TOKEN_FSLASH));    // forward slash
        m_singles.put(new Integer('*'), new Integer(TOKEN_TIMES));
        m_singles.put(new Integer('+'), new Integer(TOKEN_PLUS));
        m_singles.put(new Integer('-'), new Integer(TOKEN_MINUS));
        m_singles.put(new Integer('#'), new Integer(TOKEN_HASH));
        m_singles.put(new Integer('('), new Integer(TOKEN_LPAREN));     // left parenthesis
        m_singles.put(new Integer(')'), new Integer(TOKEN_RPAREN));     // right parenthesis
        m_singles.put(new Integer(','), new Integer(TOKEN_COMMA));
        m_singles.put(new Integer('\''), new Integer(TOKEN_APOS));
        m_singles.put(new Integer('?'), new Integer(TOKEN_QUESTION));
        m_singles.put(new Integer('!'), new Integer(TOKEN_EXCLAMATION));
        m_singles.put(new Integer(';'), new Integer(TOKEN_SEMICOLON));
        m_singles.put(new Integer(':'), new Integer(TOKEN_COLON));
        m_singles.put(new Integer('|'), new Integer(TOKEN_PIPE));
        m_singles.put(new Integer('_'), new Integer(TOKEN_USCORE));
        m_singles.put(new Integer('.'), new Integer(TOKEN_PERIOD));
//      m_singles.put(new Integer('-'),    new Integer(TOKEN_HYPHEN));
        m_singles.put(new Integer('{'), new Integer(TOKEN_LBRACE));
        m_singles.put(new Integer('}'), new Integer(TOKEN_RBRACE));
        m_singles.put(new Integer('['), new Integer(TOKEN_LBRACKET));
        m_singles.put(new Integer(']'), new Integer(TOKEN_RBRACKET));
        m_singles.put(new Integer('='), new Integer(TOKEN_EQUAL));
        m_singles.put(new Integer('<'), new Integer(TOKEN_LESSTHAN));
        m_singles.put(new Integer('>'), new Integer(TOKEN_GREATERTHAN));
        m_singles.put(new Integer('~'), new Integer(TOKEN_TILDE));
    }

    private void push(int c)
    {
        m_lastChars.push(new Integer(c));
    }

    private void addChar(int c)
    {
        m_current.append((char) c);
    }

    private int pop() throws IOException
    {
        Integer i;
        int c;

        if (m_lastChars.size() > 0)
            {
            i = (Integer) m_lastChars.pop();
            c = i.intValue();
            m_pos++;
            }
        else
            {
            c = m_reader.read();
            m_pos++;
            }
        return c;
    }

    public TozeToken nextToken() throws IOException
    {
        int c;
        int tokenId = TOKEN_ERROR;
        int pos = m_pos;
        boolean isUnNat = false;

        m_current.setLength(0);

        /*
         * Get either the last pushed char or the next char from the stream.
         */
        /*
         * if (m_lastChars.size() > 0)
         * {
         * i = (Integer)m_lastChars.pop();
         * c = i.intValue();
         * m_pos++;
         * }
         * else
         * {
         * c = m_reader.read();
         * m_pos++;
         * }
         */
        c = pop();

        /*
         * End of stream.
         */

        if (c == -1)
            {
            return new TozeToken(TOKEN_EOS, "", pos - m_lineStart, m_lineNum);
            }
        else if (c == 10)
            {
            TozeToken tt = new TozeToken(TOKEN_EOL, "", pos - m_lineStart - 1, m_lineNum);
            m_lineNum++;
            m_lineStart = pos;
            return tt;
            }

        /*
         * Whitespace
         */
        else if (Character.isWhitespace((char) c))
            {
            while (Character.isWhitespace((char) c))
                {
                if (c == 10)
                    {
                    break;
                    }

                addChar(c);
                c = pop();
//            c = m_reader.read();
//            m_pos++;
                }

            push(c);
            m_pos--;

            tokenId = TOKEN_WHITESPACE;
            }

        /*
         * Word
         */
        else if (((char) c >= 'a' && (char) c <= 'z')
                 || ((char) c >= 'A' && (char) c <= 'Z'))
            {
            while (((char) c >= 'a' && (char) c <= 'z')
                   || ((char) c >= 'A' && (char) c <= 'Z')
                   || ((char) c >= '0' && (char) c <= '9')
                   || ((char) c == '_'))
                {
                addChar(c);
                c = pop();
//            c = m_reader.read();
//            m_pos++;
                }

            push(c);
            m_pos--;

            Integer val = (Integer) m_keywords.get(m_current.toString());

            if (val == null)
                {
                tokenId = TOKEN_WORD;
                }
            else
                {
                tokenId = val.intValue();
                }
            }
        else if ((char) c == '|')
            {
            addChar(c);
            c = pop();
            if ((char) c == '|')
                {
                addChar(c);
                return new TozeToken(TOKEN_DPIPE, m_current.toString(), pos - m_lineStart, m_lineNum);
                }
            push(c);
            return new TozeToken(TOKEN_PIPE, m_current.toString(), pos - m_lineStart, m_lineNum);
            }

        /*
         * Number
         */
        else if (Character.isDigit((char) c))
            {
            while (Character.isDigit((char) c)
                   || c == '.')
                {
                if (c == '.')
                    {
                    if (isUnNat)
                        {
                        break;
                        }
                    isUnNat = true;
                    }
                addChar(c);
                c = pop();
                }

            push(c);
            m_pos--;

            // Don't let numbers end with a period.
            if (m_current.charAt(m_current.length() - 1) == '.')
                {
                push('.');
                m_pos--;
                m_current.setLength(m_current.length() - 1);
                }

            tokenId = TOKEN_NUMBER;
            }
        /*
         * Subscript Number
         */
        /*
         * else if ((c >= 0x2080) && (c <= 0x2089))
         * {
         * while ((c >= 0x2080) && (c <= 0x2089))
         * {
         * addChar(c);
         * c = pop();
         * // c = m_reader.read();
         * // m_pos++;
         * }
         *
         * push(c);
         * m_pos--;
         *
         * tokenId = TOKEN_SUBSCRIPTNUM;
         * }
         */
        /*
         * N1
         */
        else if (c == TozeFontMap.CHAR_NAT)
            {
            addChar(c);
            c = pop();
            if (c == TozeFontMap.CHAR_SUB_1)
                {
                addChar(c);
                return new TozeToken(TOKEN_NATONE, m_current.toString(), pos - m_lineStart, m_lineNum);
                }
            else
                {
                push(c);
                m_pos--;
                }
            return new TozeToken(TOKEN_NAT, m_current.toString(), pos - m_lineStart, m_lineNum);
            }
        /*
         * E1
         */
        else if (c == TozeFontMap.CHAR_EXISTS)
            {
            addChar(c);
            c = pop();
            if (c == TozeFontMap.CHAR_SUB_1)
                {
                addChar(c);
                return new TozeToken(TOKEN_EXIONE, m_current.toString(), pos - m_lineStart, m_lineNum);
                }
            else
                {
                push(c);
                m_pos--;
                }
            return new TozeToken(TOKEN_EXI, m_current.toString(), pos - m_lineStart, m_lineNum);
            }
        /*
         * P1
         */
        else if (c == TozeFontMap.CHAR_POWER)
            {
            addChar(c);
            c = pop();
            if (c == TozeFontMap.CHAR_SUB_1)
                {
                addChar(c);
                return new TozeToken(TOKEN_PSETONE, m_current.toString(), pos - m_lineStart, m_lineNum);
                }
            else
                {
                push(c);
                m_pos--;
                }
            return new TozeToken(TOKEN_PSET, m_current.toString(), pos - m_lineStart, m_lineNum);
            }
        /*
         * F1
         */
        else if (c == TozeFontMap.CHAR_FINSET)
            {
            addChar(c);
            c = pop();
            if (c == TozeFontMap.CHAR_SUB_1)
                {
                addChar(c);
                return new TozeToken(TOKEN_FSETONE, m_current.toString(), pos - m_lineStart, m_lineNum);
                }
            else
                {
                push(c);
                m_pos--;
                }
            return new TozeToken(TOKEN_FSET, m_current.toString(), pos - m_lineStart, m_lineNum);
            }
        /*
         * Double equals
         */
        else if (c == '=')
            {
            m_current.append((char) c);
            c = pop();
//         c = m_reader.read();
//         m_pos++;
            if (c == '=')
                {
                m_current.append((char) c);
                return new TozeToken(TOKEN_DEFS, "==", pos - m_lineStart, m_lineNum);
                }
            else
                {
                push(c);
                m_pos--;
                return new TozeToken(TOKEN_EQUAL, "=", pos - m_lineStart, m_lineNum);
                }
            }
        /*
         * Double dots
         */
        else if (c == '.')
            {
            m_current.append((char) c);
            c = pop();
            if (c == '.')
                {
                m_current.append((char) c);
                return new TozeToken(TOKEN_UPTO, "..", pos - m_lineStart, m_lineNum);
                }
            else
                {
                push(c);
                m_pos--;
                return new TozeToken(TOKEN_PERIOD, ".", pos - m_lineStart, m_lineNum);
                }
            }
        /*
         * Assign (::=)
         * Handle the case when there is only a colon (:).
         */
        else if (c == ':')
            {
            addChar(c);
            c = pop();
//         c = m_reader.read();
//         m_pos++;
            if (c != ':')
                {
                push(c);
                m_pos--;
                return new TozeToken(TOKEN_COLON, ":", pos - m_lineStart, m_lineNum);
                }
            addChar(c);
            c = pop();
//         c = m_reader.read();
//         m_pos++;
            if (c != '=')
                {
                return new TozeToken(TOKEN_ERROR, "", pos - m_lineStart, m_lineNum);
                }
            addChar(c);

            return new TozeToken(TOKEN_DDEF, "::=", pos - m_lineStart, m_lineNum);
            }
        /*
         * Special characters spelled out.
         */
        else if (c == '%')
            {
            addChar(c);
            c = pop();
//         c = m_reader.read();
//         m_pos++;
            while ((c != '%') && (Character.isLetter((char) c) || Character.isDigit((char) c)))
                {
                addChar(c);
                c = pop();
//            c = m_reader.read();
//            m_pos++;
                }
            if (c != 0)
                {
                push(c);
                m_pos--;
                }

            Integer tok;

            tok = (Integer) m_keywords.get(m_current.toString());

            if (tok == null)
                {
                return new TozeToken(TOKEN_ERROR, "", pos - m_lineStart, m_lineNum);
                }
            else
                {
                return new TozeToken(tok.intValue(), m_current.toString(), pos - m_lineStart, m_lineNum);
                }
            }
        else
            {
            Integer tok;

            tok = (Integer) m_singles.get(new Integer(c));

            if (tok == null)
                {
                return new TozeToken(TOKEN_ERROR, "", pos - m_lineStart, m_lineNum);
                }
            else
                {
                addChar(c);
                return new TozeToken(tok.intValue(), Character.toString((char) c), pos - m_lineStart, m_lineNum);
                }
            }

        return new TozeToken(tokenId, m_current.toString(), pos - m_lineStart, m_lineNum);
    }
};
