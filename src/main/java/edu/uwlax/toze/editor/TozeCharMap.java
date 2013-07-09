package edu.uwlax.toze.editor;

import java.util.ArrayList;
import java.util.List;

public class TozeCharMap
{
    private String tozeChar;
    private String mnemonic;
    private String description;

    static final private List<TozeCharMap> allChars;
    
    static
        {
        allChars = new ArrayList<TozeCharMap>();
        
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_SUPER_MINUS_1), "%sminus1%", "-1 Super Script"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_POWER), "%pset%", "Power Set"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_FINSET), "%fset%", "Finite Set"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_NAT), "%nat%", "Natural Numbers"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_NAT) + 
                                        Character.toString(TozeFontMap.CHAR_SUB_1), "%natone%", "Natural Numbers + 1"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_NUM), "%integer%", "Integer Numbers"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_R), "%real%", "Real Numbers"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_SUB_1), "%sub1%", "Subscript 1"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_MAPSTO), "%map%", "Maps To"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_REL), "%rel%", "Relation"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_PFUN), "%pfun%", "Partial Function"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_FUN), "%tfun%", "Total Function"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_PINJ), "%pinj%", "Partial Injection"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_INJ), "%tinj%", "Total Injection"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_SURJ), "%tsur%", "TSUR"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_PSURJ), "%psur%", "PSUR"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_BIJ), "%bij%", "BIJ"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_FFUN), "%ffun%", "FFUN"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_FINJ), "%finj%", "FINJ"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_CROSS), "%prod%", "Product"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_IN), "%mem%", "Membership"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_NOTIN), "%nem%", "Not A Member"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_NEQ), "%neq%", "Not Equal"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_PARA_SKIP), "%skip%", "Skip"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_EMPTYSET), "%emptyset%", "Empty Set"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_PROPSUBSET), "%psubs%", "Proper Subset"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_SUBSET), "%subs%", "Subset"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_CAP), "%int%", "Intersection"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_CUP), "%uni%", "Union"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_SETMINUS), "%setminus%", "Set Minus"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_BIGCAP), "%bigcap%", "Big Cap"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_BIGCUP), "%bigcup%", "Big Cup"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_COMP), "%fcmp%", "FCMP"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_CIRC), "%circ%", "CIRC"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_DRES), "%dres%", "DRES"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_RRES), "%rres%", "RRES"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_NDRES), "%dsub%", "DSUB"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_NDRES), "%rsub%", "RSUB"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_INV), "%inv%", "INV"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_PLUS_POST), "%tcl%", "TCL"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_TIMES_POST), "%rtcl%", "RTCL"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_OPLUS), "%oplus%", "OPLUS"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_CAT), "%cat%", "CAT"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_DCAT), "%dcat%", "DCAT"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_PROJECT), "%project%", "Project"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_UPLUS), "%uplus%", "UPLUS"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_SUBSETEQPLUS), "%subseteqplus%", "SUBSETEQPLUS"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_LANGLE), "%lseq%", "Left Sequence"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_RANGLE), "%rseq%", "Right Sequence"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_LBAG), "%lbag%", "Left Bag"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_RBAG), "%rbag%", "Right Bag"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_LIMG), "%limg%", "Left Image"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_RIMG), "%rimg%", "Right Image"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_FORALL), "%all%", "For All"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_EXISTS), "%exi%", "There Exists"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_SPOT), "%dot%, %suchthat%", "Dot / Such That"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_NOT), "%lnot%", "Not"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_AND), "%land%", "And"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_OR), "%lor%", "Or"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_IMPLIES), "%imp%", "Implies"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_IFF), "%iff%", "If and Only If"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_LDATA), "%lang%", "Left Angle"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_RDATA), "%rang%", "Right Angle"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_UK1), "%skip%", "SKIP1"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_UK2), "%skip%", "SKIP2"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_DEFS), "%sdef%", "SDEF"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_CURVEY), "%leadsto%", "Leads To"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_LEQ), "%leq%", "Less Than or Equal To"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_GEQ), "%geq%", "Greater Than or Equal To"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_DELTA), "%delta%", "Delta"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_XI), "%xi%", "Xi"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_LAMBDA), "%lambda%", "Lambda"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_MU), "%mu%", "Mu"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_MU), "%theta%", "Theta"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_DARROW), "%darrow%", "Down Arrow"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_SMALLBANG), "%!%", "Small !"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_BOOL), "%bool%", "Boolean"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_BOX), "%box%", "Box"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_RHARPOON), "%rharpoon%", "Right Harpoon"));
        allChars.add(new TozeCharMap(Character.toString(TozeFontMap.CHAR_EXTRACT), "%extract%", "Extract"));
        }
    
    public TozeCharMap(String tozeChar, String mnemonic, String description)
    {
        this.tozeChar = tozeChar;
        this.mnemonic = mnemonic;
        this.description = description;
    }

    public String getTozeChar()
    {
        return tozeChar;
    }

    public String getMnemonic()
    {
        return mnemonic;
    }

    public String getDescription()
    {
        return description;
    }
    
    
    @Override
    public String toString()
    {
        return tozeChar + " " + mnemonic + " " + description;
    }
    
    static public List<TozeCharMap> getAllChars()
    {
        return allChars;
    }
}
