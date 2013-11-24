package edu.uwlax.toze.editor;


/*
 * Class to handle converting tokens to LaTeX commands.
 */

import edu.uwlax.toze.domain.*;
import edu.uwlax.toze.objectz.TozeToken;
import edu.uwlax.toze.objectz.TozeTokenizer;

import java.io.StringReader;

public class TozeLatexExporter
{
    String m_spec = "";
    int m_classLevel = 0;

    private Specification specification;

    public TozeLatexExporter(Specification specification)
    {
        this.specification = specification;
        m_spec = "\\documentstyle[11pt,oz]{article}\n\\begin{document}\n";
    }

    static public String getLatex(Specification specification)
    {
        TozeLatexExporter exporter = new TozeLatexExporter(specification);
        return exporter.getLatex();
    }

    public String getLatex()
    {
        if (specification.getPredicate() != null)
            {
            addPredicate(specification.getPredicate());
            }

        for (SpecObject specObject : specification.getSpecObjectList())
            {
            if (specObject instanceof  AxiomaticDef)
                {
                addAxiomatic(((AxiomaticDef)specObject).getDeclaration(),
                             ((AxiomaticDef)specObject).getPredicate());
                }

            if (specObject instanceof AbbreviationDef)
                {
                addAbbreviation(((AbbreviationDef)specObject).getName(),
                                ((AbbreviationDef)specObject).getExpression());
                }

            if (specObject instanceof BasicTypeDef)
                {
                addBasicTypeDefinition(((BasicTypeDef)specObject).getName());
                }

            if (specObject instanceof FreeTypeDef)
                {
                addFreeType(((FreeTypeDef)specObject).getDeclaration(),
                            ((FreeTypeDef)specObject).getPredicate());
                }

            if (specObject instanceof GenericDef)
                {
                addGeneric(((GenericDef)specObject).getFormalParameters(),
                           ((GenericDef)specObject).getDeclaration(),
                           ((GenericDef)specObject).getPredicate());
                }
//            }

//        for (AxiomaticDef axiomaticDef : specification.getAxiomaticDefList())
//            {
//            addAxiomatic(axiomaticDef.getDeclaration(), axiomaticDef.getPredicate());
//            }
//
//        for (AbbreviationDef abbreviationDef : specification.getAbbreviationDefList())
//            {
//            addAbbreviation(abbreviationDef.getName(), abbreviationDef.getExpression());
//            }
//
//        for (BasicTypeDef basicTypeDef : specification.getBasicTypeDefList())
//            {
//            addBasicTypeDefinition(basicTypeDef.getName());
//            }
//
//        for (FreeTypeDef freeTypeDef : specification.getFreeTypeDefList())
//            {
//            addFreeType(freeTypeDef.getDeclaration(), freeTypeDef.getPredicate());
//            }
//
//        for (GenericDef genericDef : specification.getGenericDefList())
//            {
//            addGeneric(genericDef.getFormalParameters(),
//                       genericDef.getDeclaration(),
//                       genericDef.getPredicate());
//            }

            if (specObject instanceof ClassDef)
                {

//                for (ClassDef classDef : specification.getClassDefList())
//                    {
                ClassDef classDef = (ClassDef)specObject;

                startClass(classDef.getName());

                if (classDef.getVisibilityList() != null)
                    {
                    addVisibilityList(classDef.getVisibilityList());
                    }

                if (classDef.getInheritedClass() != null)
                    {
                    addInheritedClass(classDef.getInheritedClass().getName());
                    }

                for (SpecObject classSpecObject : classDef.getSpecObjectList())
                    {
                    if (classSpecObject instanceof  AxiomaticDef)
                        {
                        addAxiomatic(((AxiomaticDef)classSpecObject).getDeclaration(),
                                     ((AxiomaticDef)classSpecObject).getPredicate());
                        }

                    if (classSpecObject instanceof AbbreviationDef)
                        {
                        addAbbreviation(((AbbreviationDef)classSpecObject).getName(),
                                        ((AbbreviationDef)classSpecObject).getExpression());
                        }

                    if (classSpecObject instanceof BasicTypeDef)
                        {
                        addBasicTypeDefinition(((BasicTypeDef)classSpecObject).getName());
                        }

                    if (classSpecObject instanceof FreeTypeDef)
                        {
                        addFreeType(((FreeTypeDef)classSpecObject).getDeclaration(),
                                    ((FreeTypeDef)classSpecObject).getPredicate());
                        }

//
//                    for (AxiomaticDef axiomaticDef : classDef.getAxiomaticDefList())
//                        {
//                        addAxiomatic(axiomaticDef.getDeclaration(), axiomaticDef.getPredicate());
//                        }
//
//                    for (AbbreviationDef abbreviationDef : classDef.getAbbreviationDefList())
//                        {
//                        addAbbreviation(abbreviationDef.getName(), abbreviationDef.getExpression());
//                        }
//
//                    for (BasicTypeDef basicTypeDef : classDef.getBasicTypeDefList())
//                        {
//                        addBasicTypeDefinition(basicTypeDef.getName());
//                        }
//
//                    for (FreeTypeDef freeTypeDef : classDef.getFreeTypeDefList())
//                        {
//                        addFreeType(freeTypeDef.getDeclaration(), freeTypeDef.getPredicate());
//                        }
                    }

                State state = classDef.getState();
                if (state != null)
                    {
                    if (state.getName() != null)
                        {
                        addState(state.getName());
                        }
                    else
                        {
                        addState(state.getDeclaration(), state.getPredicate());
                        }
                    }

                if (classDef.getInitialState() != null)
                    {
                    addInit(classDef.getInitialState().getPredicate());
                    }

                for (Operation operation : classDef.getOperationList())
                    {
                    if (operation.getOperationExpression() == null)
                        {
                        addOperation(operation.getName(),
                                     operation.getDeltaList(),
                                     operation.getDeclaration(),
                                     operation.getPredicate());
                        }
                    else
                        {
    //                    addOperationExpression(operation.getName(), operation.getOperationExpression());
                        }
                    }

                endClass();
                }
            }
        return m_spec + "\n\\end{document}\n";
    }

    public void add(String str)
    {
        m_spec += str;
    }

    public void addBreak()
    {
        if (m_classLevel > 0)
            {
            add("\\\\");
            }
        add("\n");
    }

    public String replaceUscore(String str)
    {
        String nstr = "";

        int i = str.indexOf("_");
        while (i > 0)
            {
            nstr = nstr + str.substring(0, i) + "\\_";
            str = str.substring(i + 1);
            i = str.indexOf("_");
            }
        nstr = nstr + str;

        return nstr;
    }

    public void addTokens(String str)
    {
        StringReader reader = new StringReader(str);
        TozeToken token = null;
        TozeTokenizer tokenizer = new TozeTokenizer(reader);

        while (true)
            {
            try
                {
                token = tokenizer.nextToken();
                }
            catch (Exception e)
                {
                System.out.println(e.toString());
                }

            if (token.m_id == TozeTokenizer.TOKEN_EOS)
                {
                break;
                }

            switch (token.m_id)
                {
                case TozeTokenizer.TOKEN_EOL:
                    add("\\\\\n");
                    break;
                case TozeTokenizer.TOKEN_WORD:
                    add(replaceUscore(token.m_value));
                    break;
                case TozeTokenizer.TOKEN_NUMBER:
                    add(token.m_value);
                    break;
                case TozeTokenizer.TOKEN_DEFS:
                    add("\\defs");
                    break;
                case TozeTokenizer.TOKEN_DDEF:
                    add("\\ddef");
                    break;
                case TozeTokenizer.TOKEN_DLARROW:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_DRARROW:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_LDANGLE:
                    add("\\lang");
                    break;
                case TozeTokenizer.TOKEN_RDANGLE:
                    add("\\rang");
                    break;
                case TozeTokenizer.TOKEN_SDEF:
                    add("\\sdef");
                    break;
                case TozeTokenizer.TOKEN_PROJECT:
                    add("\\project");
                    break; // Harpoon
                case TozeTokenizer.TOKEN_INCREMENT:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_LPAREN:
                    add("(");
                    break;
                case TozeTokenizer.TOKEN_RPAREN:
                    add(")");
                    break;
                case TozeTokenizer.TOKEN_LAND:
                    add("\\land");
                    break;
                case TozeTokenizer.TOKEN_BOX:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_FCMP:
                    add("\\fcmp");
                    break; // Big semi colon
                case TozeTokenizer.TOKEN_PARALLELTO:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_DOT:
                    add("\\dot");
                    break;
                case TozeTokenizer.TOKEN_LNOT:
                    add("\\lnot");
                    break;
                case TozeTokenizer.TOKEN_LOR:
                    add("\\lor");
                    break;
                case TozeTokenizer.TOKEN_RDARROW:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_LRDRARROW:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_BSLASH:
                    add("\\zhide");
                    break;
                case TozeTokenizer.TOKEN_FSLASH:
                    add("/");
                    break;
                case TozeTokenizer.TOKEN_MUCHGREATERTHAN:
                    add("\\gg");
                    break;
                case TozeTokenizer.TOKEN_ALL:
                    add("\\all");
                    break;
                case TozeTokenizer.TOKEN_EXI:
                    add("\\exi");
                    break;
                case TozeTokenizer.TOKEN_MEM:
                    add("\\mem");
                    break;
                case TozeTokenizer.TOKEN_PROD:
                    add("\\prod");
                    break;
                case TozeTokenizer.TOKEN_LAMBDA:
                    add("\\lambda");
                    break;
                case TozeTokenizer.TOKEN_MU:
                    add("\\mu");
                    break;
                case TozeTokenizer.TOKEN_DSTRUCKP:
                    add("\\power");
                    break;
                case TozeTokenizer.TOKEN_DOWNARROW:
                    add("\\downarrow");
                    break;
                case TozeTokenizer.TOKEN_UNI:
                    add("\\uni");
                    break;
                case TozeTokenizer.TOKEN_COPYRIGHT:
                    add("\\copyright");
                    break;
                case TozeTokenizer.TOKEN_MLABRACKET:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_MRABRACKET:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_MLWSBRACKET:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_MRWSBRACKET:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_THETA:
                    add("\\theta");
                    break;
                case TozeTokenizer.TOKEN_MAP:
                    add("\\map");
                    break;
                case TozeTokenizer.TOKEN_UPTO:
                    add("\\upto");
                    break;
                case TozeTokenizer.TOKEN_PLUS:
                    add("+");
                    break;
                case TozeTokenizer.TOKEN_MINUS:
                    add("-");
                    break;
                case TozeTokenizer.TOKEN_CHARTIE:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_UPLUS:
                    add("\\uplus");
                    break;
                case TozeTokenizer.TOKEN_UNIONMINUS:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_TIMES:
                    add("*");
                    break;
                case TozeTokenizer.TOKEN_RINGOPERATOR:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_CIRCLEDTIMES:
                    add("\\otimes");
                    break;
                case TozeTokenizer.TOKEN_CIRCLEDPLUS:
                    add("\\fovr");
                    break;
                case TozeTokenizer.TOKEN_HASH:
                    add("\\#");
                    break;
                case TozeTokenizer.TOKEN_LWHITEARROW:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_RWHITEARROW:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_DRES:
                    add("\\dres");
                    break;
                case TozeTokenizer.TOKEN_RRES:
                    add("\\rres");
                    break;
                case TozeTokenizer.TOKEN_COMMA:
                    add(",");
                    break;
                case TozeTokenizer.TOKEN_APOS:
                    add("'");
                    break;
                case TozeTokenizer.TOKEN_QUESTION:
                    add("?");
                    break;
                case TozeTokenizer.TOKEN_EXCLAMATION:
                    add("!");
                    break;
                //case TozeTokenizer.TOKEN_SUB_1              : add(""); break;
                case TozeTokenizer.TOKEN_WHITESPACE:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_SEMICOLON:
                    add(";");
                    break;
                case TozeTokenizer.TOKEN_COLON:
                    add(":");
                    break;
                case TozeTokenizer.TOKEN_IF:
                    add("if");
                    break;
                case TozeTokenizer.TOKEN_THEN:
                    add("then");
                    break;
                case TozeTokenizer.TOKEN_ELSE:
                    add("else");
                    break;
                case TozeTokenizer.TOKEN_DLABRACKET:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_DRABRACKET:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_PIPE:
                    add("|");
                    break;
                case TozeTokenizer.TOKEN_USCORE:
                    add("_");
                    break;
                case TozeTokenizer.TOKEN_EXIONE:
                    add("\\exione");
                    break;
                case TozeTokenizer.TOKEN_LET:
                    add("\\zlet");
                    break;
                case TozeTokenizer.TOKEN_PERIOD:
                    add(".");
                    break;
                case TozeTokenizer.TOKEN_HYPHEN:
                    add("-");
                    break;
                case TozeTokenizer.TOKEN_PSET:
                    add("\\pset");
                    break;
                case TozeTokenizer.TOKEN_SELF:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_LBRACE:
                    add("\\{");
                    break;
                case TozeTokenizer.TOKEN_RBRACE:
                    add("\\}");
                    break;
                case TozeTokenizer.TOKEN_DARROW:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_LBRACKET:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_RBRACKET:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_TRUE:
                    add("\\true");
                    break;
                case TozeTokenizer.TOKEN_FALSE:
                    add("\\false");
                    break;
                case TozeTokenizer.TOKEN_EQUAL:
                    add("=");
                    break;
                case TozeTokenizer.TOKEN_PRE:
                    add("\\pre");
                    break;
                case TozeTokenizer.TOKEN_DOTINIT:
                    add(".\\Init");
                    break;
                case TozeTokenizer.TOKEN_IMP:
                    add("\\imp");
                    break;
                case TozeTokenizer.TOKEN_IFF:
                    add("\\iff");
                    break;
                case TozeTokenizer.TOKEN_MUCHGT:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_WHITEBOX:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_PARALLELTOE:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_DELTA:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_NEQ:
                    add("\\neq");
                    break;
                case TozeTokenizer.TOKEN_NEM:
                    add("\\nem");
                    break;
                case TozeTokenizer.TOKEN_SUBSETOREQUAL:
                    add("\\subseteq");
                    break;
                case TozeTokenizer.TOKEN_SUBS:
                    add("\\subs");
                    break;
                case TozeTokenizer.TOKEN_LESSTHAN:
                    add("<");
                    break;
                case TozeTokenizer.TOKEN_LEQ:
                    add("\\leq");
                    break;
                case TozeTokenizer.TOKEN_GEQ:
                    add("\\geq");
                    break;
                case TozeTokenizer.TOKEN_GREATERTHAN:
                    add(">");
                    break;
                case TozeTokenizer.TOKEN_PREFIX:
                    add("\\prefix");
                    break;
                case TozeTokenizer.TOKEN_SUFFIX:
                    add("\\suffix");
                    break;
                case TozeTokenizer.TOKEN_INSEQ:
                    add("\\inseq");
                    break;
                case TozeTokenizer.TOKEN_BAGMEMBERSHIP:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_SQUAREIMAGEOREQUAL:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_PARTITIONS:
                    add("\\partitions");
                    break;
                case TozeTokenizer.TOKEN_TILDE:
                    add("~");
                    break;
                case TozeTokenizer.TOKEN_DISJOINT:
                    add("\\disjoint");
                    break;
                case TozeTokenizer.TOKEN_PSETONE:
                    add("\\psetone");
                    break;
                case TozeTokenizer.TOKEN_ID:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_FSET:
                    add("\\fset");
                    break;
                case TozeTokenizer.TOKEN_FSETONE:
                    add("\\fsetone");
                    break;
                case TozeTokenizer.TOKEN_SEQ:
                    add("\\seq");
                    break;
                case TozeTokenizer.TOKEN_SEQONE:
                    add("\\seqone");
                    break;
                case TozeTokenizer.TOKEN_ISEQ:
                    add("iseq");
                    break;
                case TozeTokenizer.TOKEN_BAG:
                    add("\\bag");
                    break;
                case TozeTokenizer.TOKEN_NAT:
                    add("\\nat");
                    break;
                case TozeTokenizer.TOKEN_NATONE:
                    add("\\natone");
                    break;
                case TozeTokenizer.TOKEN_BOOL:
                    add("\\bool");
                    break;
                case TozeTokenizer.TOKEN_REAL:
                    add("\\real");
                    break;
                case TozeTokenizer.TOKEN_SUPER_MINUS_1:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_REL:
                    add("\\rel");
                    break;
                case TozeTokenizer.TOKEN_INTEGER:
                    add("\\integer");
                    break;
                case TozeTokenizer.TOKEN_MOD:
                    add("\\mod");
                    break;
                case TozeTokenizer.TOKEN_DIV:
                    add("\\div");
                    break;
                case TozeTokenizer.TOKEN_PFUN:
                    add("\\pfun");
                    break;
                case TozeTokenizer.TOKEN_TFUN:
                    add("\\tfun");
                    break;
                case TozeTokenizer.TOKEN_PINJ:
                    add("\\pinj");
                    break;
                case TozeTokenizer.TOKEN_TINJ:
                    add("\\tinj");
                    break;
                case TozeTokenizer.TOKEN_TSUR:
                    add("\\tsurj");
                    break;
                case TozeTokenizer.TOKEN_PSUR:
                    add("\\psur");
                    break;
                case TozeTokenizer.TOKEN_BIJ:
                    add("\\bij");
                    break;
                case TozeTokenizer.TOKEN_FFUN:
                    add("\\ffun");
                    break;
                case TozeTokenizer.TOKEN_FINJ:
                    add("\\finj");
                    break;
                case TozeTokenizer.TOKEN_PARA_SKIP:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_EMPTYSET:
                    add("\\emptyset");
                    break;
                case TozeTokenizer.TOKEN_PSUBS:
                    add("\\psubs");
                    break;
                case TozeTokenizer.TOKEN_INT:
                    add("\\int");
                    break;
                case TozeTokenizer.TOKEN_SETMINUS:
                    add("\\zhide");
                    break;
                case TozeTokenizer.TOKEN_BIGCAP:
                    add("\\dinter");
                    break;
                case TozeTokenizer.TOKEN_BIGCUP:
                    add("\\dunion");
                    break;
                case TozeTokenizer.TOKEN_CIRC:
                    add("\\cmp");
                    break;
                case TozeTokenizer.TOKEN_DSUB:
                    add("\\dsub");
                    break;
                case TozeTokenizer.TOKEN_RSUB:
                    add("\\rsub");
                    break;
                case TozeTokenizer.TOKEN_INV:
                    add("\\inv");
                    break;
                case TozeTokenizer.TOKEN_TCL:
                    add("\\tcl");
                    break;
                case TozeTokenizer.TOKEN_RTCL:
                    add("\\rtcl");
                    break;
                case TozeTokenizer.TOKEN_FOVR:
                    add("\\fovr");
                    break;
                case TozeTokenizer.TOKEN_CAT:
                    add("\\cat");
                    break;
                case TozeTokenizer.TOKEN_DCAT:
                    add("\\dcat");
                    break;
                case TozeTokenizer.TOKEN_SUBSETEQPLUS:
                    add("");
                    break;
                case TozeTokenizer.TOKEN_LSEQ:
                    add("\\lseq");
                    break;
                case TozeTokenizer.TOKEN_RSEQ:
                    add("\\rseq");
                    break;
                case TozeTokenizer.TOKEN_LBAG:
                    add("\\lbag");
                    break;
                case TozeTokenizer.TOKEN_RBAG:
                    add("\\rbag");
                    break;
                case TozeTokenizer.TOKEN_LIMG:
                    add("\\limg");
                    break;
                case TozeTokenizer.TOKEN_RIMG:
                    add("\\rimg");
                    break;
                case TozeTokenizer.TOKEN_LANG:
                    add("\\lang");
                    break;
                case TozeTokenizer.TOKEN_RANG:
                    add("\\rang");
                    break;
                case TozeTokenizer.TOKEN_LEADSTO:
                    add("\\leadsto");
                    break;
                case TozeTokenizer.TOKEN_XI:
                    add("\\xi");
                    break;
                case TozeTokenizer.TOKEN_DOM:
                    add("\\dom");
                    break;
                case TozeTokenizer.TOKEN_RAN:
                    add("\\ran");
                    break;
                case TozeTokenizer.TOKEN_REV:
                    add("\\rev");
                    break;
                case TozeTokenizer.TOKEN_HEAD:
                    add("\\head");
                    break;
                case TozeTokenizer.TOKEN_LAST:
                    add("\\last");
                    break;
                case TozeTokenizer.TOKEN_TAIL:
                    add("\\tail");
                    break;
                case TozeTokenizer.TOKEN_FRONT:
                    add("\\front");
                    break;
                case TozeTokenizer.TOKEN_DPIPE:
                    add("||");
                    break;
                case TozeTokenizer.TOKEN_FIRST:
                    add("first");
                    break;
                case TozeTokenizer.TOKEN_SECOND:
                    add("second");
                    break;
                case TozeTokenizer.TOKEN_DPIPEBANG:
                    add("");
                    break;
                }

            add(" ");

            }
    }

    public void addBasicTypeDefinition(String defs)
    {
        add("\\begin{zpar}\n[ ");
        addTokens(defs);
        add("]\\end{zpar}\n");
        addBreak();
    }

    public void addFreeType(String identifier,
                            String branch)
    {
        add("\\begin{zpar}\n$");
        add(identifier);
        add("\\ddef");
        addTokens(branch);
        add("$\\end{zpar}");
        addBreak();
    }

    public void addInheritedClass(String inherited)
    {
        add("\\begin{zpar}\n$");
        addTokens(inherited);
        add("$\\end{zpar}");
        addBreak();
    }

    public void addPredicate(String predicate)
    {
        add("\\begin{zpar}\n$");
        addTokens(predicate);
        add("$\\end{zpar}");
        addBreak();
    }

    public void addSchema(String schemaName,
                          String decls,
                          String preds)
    {
        add("\\begin{schema}{" + schemaName + "}\n");
        if (decls != null)
            {
            addTokens(decls);
            }
        if (preds != null)
            {
            if (preds.length() > 0)
                {
                add("\\ST\n");
                addTokens(preds);
                }
            }
        add("\\end{schema}");
        addBreak();
    }

    public void addSchemaExpression(String schemaName,
                                    String expr)
    {
        add("\\begin{zpar}\n$" + schemaName + " \\sdef ");
        addTokens(expr);
        add("$\\end{zpar}");
        addBreak();
    }

    public void addAbbreviation(String abbr,
                                String expr)
    {
        add("\\begin{zpar}\n$" + abbr + " \\defs ");
        addTokens(expr);
        add("$\\end{zpar}");
        addBreak();
    }

    public void addGeneric(String formal,
                           String decls,
                           String preds)
    {
        boolean gen = false;

        if (formal != null)
            {
            if (formal.length() > 0)
                {
                gen = true;
                }
            }
        if (gen)
            {
            int s = 0;
            int l = formal.length() - 1;
            if (formal.charAt(s) == '[')
                {
                s++;
                }
            if (formal.charAt(l) != ']')
                {
                l++;
                }
            if (l < s)
                {
                l = s + 1;
                }
            add("\\begin{gendef}{" + formal.substring(s, l) + "}\n");
            }
        else
            {
            add("\\begin{uniqdef}\n");
            }

        if (decls != null)
            {
            addTokens(decls);
            }
        if (preds != null)
            {
            if (preds.length() > 0)
                {
                add("\\ST\n");
                addTokens(preds);
                }
            }

        if (gen)
            {
            add("\\end{gendef}");
            }
        else
            {
            add("\\end{uniqdef}");
            }
        addBreak();
    }

    public void addAxiomatic(String decls,
                             String preds)
    {
        add("\\begin{axdef}\n");
        addTokens(decls);
        if (preds != null)
            {
            if (preds.length() > 0)
                {
                add("\\ST\n");
                addTokens(preds);
                }
            }
        add("\\end{axdef}");
        addBreak();
    }

    public void startClass(String className)
    {
        m_classLevel++;
        add("\\begin{class}{" + className + "}\n");
    }

    public void endClass()
    {
        add("\\end{class}");
        addBreak();
        m_classLevel--;
    }

    public void addVisibilityList(String list)
    {
        add("\\begin{zpar}\n$ \\filter (");
        addTokens(list);
        add(") $\\end{zpar}");
        addBreak();
    }

    public void addState(String decls,
                         String preds)
    {
        add("\\begin{state}\n");
        if (decls != null)
            {
            addTokens(decls);
            }
        if (preds != null)
            {
            add("\\ST\n");
            addTokens(preds);
            }
        add("\\end{state}");
        addBreak();
    }

    public void addState(String state)
    {
        add("[" + state + "]\n");
        addBreak();
    }

    public void addInit(String preds)
    {
        add("\\begin{init}\n");
        if (preds != null)
            {
            addTokens(preds);
            }
        add("\\end{init}");
        addBreak();
    }

    public void addOperation(String name,
                             String delta,
                             String decls,
                             String preds)
    {
        add("\\begin{op}{" + name + "}\n");
        if (delta != null)
            {
            if (delta.length() > 0)
                {
                add("\\Delta( ");
                addTokens(delta);
                add(" )\\\\\n");
                }
            }
        if (decls != null)
            {
            if (decls.length() > 0)
                {
                addTokens(decls);
                }
            }
        if (preds != null)
            {
            if (preds.length() > 0)
                {
                add("\\ST\n");
                addTokens(preds);
                }
            }
        add("\\end{op}\\\\\n");
    }
}
