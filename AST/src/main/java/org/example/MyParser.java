package org.example;

import org.example.MyScanner;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyParser {
    enum TYPE {INTDATATYPE}

    private Map<String, SymbolTableItem> table = new HashMap<>();
    private MyScanner scanner;
    private MyScanner.TOKEN nextToken;
    private PushbackReader pbr;

    // Code for AST
    private MyAST ast;

    public MyParser() {
        ast = new MyAST();
    }

    public MyAST getAst() {
        return ast;
    }

    // SymbolTableItem + constructor
    class SymbolTableItem {
        String name;
        TYPE type;


        SymbolTableItem(String name, TYPE type) {
            this.name = name;
            this.type = type;
        }
    }

    // MyParse code
    boolean match(MyScanner.TOKEN expectedToken) throws IOException {
        if (nextToken != expectedToken) {
            System.out.println("Error: Expected " + expectedToken + " but got " + nextToken + " instead.");
            return false;
        } else {
            System.out.println("Matched: " + expectedToken + " = " + scanner.getTokenBufferString());
            nextToken = scanner.scan();
            return true;
        }
    }

    public boolean parse(String program) throws IOException {
        StringReader fileReader = new StringReader(program);
        pbr = new PushbackReader(fileReader);
        scanner = new MyScanner(pbr);
        try {
            // Begin parse
            nextToken = scanner.scan();
            MyAST.NodeProgram nodeProgram = programParse();
            // Set root
            if (nodeProgram == null) {
                return false;
            } else {
                ast.setRoot(nodeProgram);
                return true;
            }
        } catch (Exception e) {
            System.out.println("Scanner error: " + e.getMessage());
            return false;
        }
    }

    // Parsing Methods modified for AST
    // Check for: <Decls> <Stmts> $
    public MyAST.NodeProgram programParse() throws IOException {
        MyAST.NodeDecls declsNode = declsParse();
        MyAST.NodeStmts stmtsNode = stmtsParse();
        // Check null for both
        if (declsNode == null) {
            System.out.println("Parse Error: Expected declarations");
            return null;
        }
        if (stmtsNode == null) {
            System.out.println("Parse Error: Expected statements");
            return null;
        }

        if (!match(MyScanner.TOKEN.SCANEOF)) {
            System.out.println("Parse Error: Expected end of file ($)");
            return null;
        } else {
            System.out.println("The program has parsed successfully!\n");
            return ast.new NodeProgram(declsNode, stmtsNode);
        }
    }


    // Check for: <Decl> <Decls>
    public MyAST.NodeDecls declsParse() throws IOException {
        ArrayList<MyAST.NodeId> decls = new ArrayList<>();
        while (nextToken == MyScanner.TOKEN.DECLARE) {
            MyAST.NodeId declNode = declParse();
            if (declNode == null) {
                System.out.println("Parse Error: Expected declaration");
                return null;
            } else {
                decls.add(declNode);
            }
        }
        return ast.new NodeDecls((decls));
    }

    // Check for: declare id
    public MyAST.NodeId declParse() throws IOException {
        if (!match(MyScanner.TOKEN.DECLARE)) {
            System.out.println("Parse Error: Expected 'declare'");
            return null;
        }
        String id = scanner.getTokenBufferString();
        if (!match(MyScanner.TOKEN.ID)) {
            System.out.println("Parse Error: Expected an identifier after 'declare'");
            return null;
        }
        if (table.containsKey(id)) {
            System.out.println("Parse Error: '" + id + "' already declared.");
            return null;
        }
        table.put(id, new SymbolTableItem(id, TYPE.INTDATATYPE));
        return ast.new NodeId(id);
    }

    // Check for: <Stmt><Stmts>
    public MyAST.NodeStmts stmtsParse() throws IOException {
        ArrayList<MyAST.NodeStmt> stmts = new ArrayList<>();
        while (nextToken == MyScanner.TOKEN.PRINT || nextToken == MyScanner.TOKEN.SET || nextToken == MyScanner.TOKEN.IF || nextToken == MyScanner.TOKEN.CALC) {
            MyAST.NodeStmt stmtNode = stmtParse();
            if (stmtNode == null) {
                System.out.println("Parse Error: Expected statement");
                return null;
            }
            stmts.add(stmtNode);
        }
        return ast.new NodeStmts(stmts);
    }

    // Check for all 4 possible <stmt>, each have their method
    public MyAST.NodeStmt stmtParse() throws IOException {
        switch (nextToken) {
            case PRINT:
                return parsePrintStmt();
            case SET:
                return parseSetStmt();
            case IF:
                return parseIfStmt();
            case CALC:
                return parseCalcStmt();
            default:
                System.out.println("Parse Error: Unexpected token '" + nextToken + "' in statement");
                return null;
        }
    }

    // Check for: print id
    public MyAST.NodePrint parsePrintStmt() throws IOException {
        if (!match(MyScanner.TOKEN.PRINT)) {
            System.out.println("Parse Error: Expected 'print'");
            return null;
        }
        String id = scanner.getTokenBufferString();
        if (!match(MyScanner.TOKEN.ID)) {
            System.out.println("Parse Error: Expected an identifier after 'print'");
            return null;
        }
        // Check var declaration
        if (!checkVariableDeclared(id)) {
            System.out.println("Parse Error: Variable '" + id + "' has not been declared.");
            return null;
        }
        return ast.new NodePrint(ast.new NodeId(id));
    }

    // Check for: set id = intliteral
    public MyAST.NodeSet parseSetStmt() throws IOException {
        if (!match(MyScanner.TOKEN.SET)) {
            System.out.println("Parse Error: Expected 'set'");
            return null;
        }
        String id = scanner.getTokenBufferString();
        if (!match(MyScanner.TOKEN.ID)) {
            System.out.println("Parse Error: Expected an identifier after 'set'");
            return null;
        }
        // Check var declaration
        if (!checkVariableDeclared(id)) {
            System.out.println("Parse Error: Variable '" + id + "' has not been declared.");
            return null;
        }
        if (!match(MyScanner.TOKEN.EQUALS)) {
            System.out.println("Parse Error: Expected '='");
            return null;
        }
        int value = Integer.parseInt(scanner.getTokenBufferString());
        if (!match(MyScanner.TOKEN.INTLITERAL)) {
            System.out.println("Parse Error: Expected an intliteral after '='");
            return null;
        }
        return ast.new NodeSet(ast.new NodeId(id), ast.new NodeIntLiteral(value));
    }

    // Check for: if id = id then <Stmts> endif
    public MyAST.NodeIf parseIfStmt() throws IOException {
        if (!match(MyScanner.TOKEN.IF)) {
           System.out.println("Parse Error: Expected 'if'");
           return null;
        }
        // Check First id
        String first_id = scanner.getTokenBufferString();
        if (!match(MyScanner.TOKEN.ID)) {
            System.out.println("Parse Error: Expected an identifier after 'if'");
            return null;
        }
        // Check var declaration
        if (!checkVariableDeclared(first_id)) {
            System.out.println("Parse Error: Variable '" + first_id + "' has not been declared.");
            return null;
        }
        if (!match(MyScanner.TOKEN.EQUALS)) {
            System.out.println("Parse Error: Expected '='");
            return null;
        }
        // Check Second id
        String second_id = scanner.getTokenBufferString();
        if (!match(MyScanner.TOKEN.ID)) {
            System.out.println("Parse Error: Expected an identifier after '='");
            return null;
        }
        // Check var declaration
        if (!checkVariableDeclared(second_id)) {
            System.out.println("Parse Error: Variable '" + second_id + "' has not been declared.");
            return null;
        }
        if (!match(MyScanner.TOKEN.THEN)) {
            System.out.println("Parse Error: Expected 'then'");
            return null;
        }
        // Check for other statements
        MyAST.NodeStmts stmtsNode = stmtsParse();
        if (stmtsNode == null) {
            return null;
        }
        if (!match(MyScanner.TOKEN.ENDIF)) {
            System.out.println("Parse Error: Expected 'endif'");
            return null;
        }
        return ast.new NodeIf(ast.new NodeId(first_id), ast.new NodeId(second_id), stmtsNode);
    }



    // Check for: calc id = <Sum>
    public MyAST.NodeCalc parseCalcStmt() throws IOException {
        if (!match(MyScanner.TOKEN.CALC)) {
            System.out.println("Parse Error: Expected 'calc'");
            return null;
        }
        String id = scanner.getTokenBufferString();
        if (!match(MyScanner.TOKEN.ID)) {
            System.out.println("Parse Error: Expected an identifier after 'calc'");
            return null;
        }
        // Check var declaration
        if (!checkVariableDeclared(id)) {
            System.out.println("Parse Error: Variable '" + id + "' has not been declared.");
            return null;
        }
        if (!match(MyScanner.TOKEN.EQUALS)) {
            System.out.println("Parse Error: Expected '='");
            return null;
        }
        MyAST.NodeExpr exprNode = sumParse();
        if (exprNode == null) {
            return null;
        }
        return ast.new NodeCalc(ast.new NodeId(id), exprNode);
    }

    // Check for: <Value> <SumEnd>
    public MyAST.NodeExpr sumParse() throws IOException {
        MyAST.NodeExpr valueNode = valueParse();
        if (valueNode == null) {
            return null;
        }
        return sumEndParse(valueNode);
    }

    // Check for: + <Value> <SumEnd>
    public MyAST.NodeExpr sumEndParse(MyAST.NodeExpr left) throws IOException {
        if (nextToken == MyScanner.TOKEN.PLUS) {
            if (!match(MyScanner.TOKEN.PLUS)) {
                System.out.println("Parse Error: Expected '+' in sum");
                return null;
            }
            MyAST.NodeExpr valueNode = valueParse();
            if (valueNode == null) {
                System.out.println("Parse Error: Right side is null");
                return null;
            }
            MyAST.NodeExpr rightNode = sumEndParse(valueNode);
            if (rightNode == null) {
                return null;
            }
            return ast.new NodePlus(left, rightNode);
        }
        return left;
    }

    // Check if value is either ID or number
    public MyAST.NodeExpr valueParse() throws IOException {
        if (nextToken == MyScanner.TOKEN.ID) {
            String id = scanner.getTokenBufferString();
            if (!match(MyScanner.TOKEN.ID)) {
                System.out.println("Parse Error: Expected an identifier after 'id'");
                return null;
            } else {
                return ast.new NodeId(id);
            }
        } else if (nextToken == MyScanner.TOKEN.INTLITERAL) {
            int value = Integer.parseInt(scanner.getTokenBufferString());
            if (!match(MyScanner.TOKEN.INTLITERAL)) {
                System.out.println("Parse Error: Expected an intliteral after 'int'");
                return null;
            } else {
                return ast.new NodeIntLiteral(value);
            }
        }
        System.out.println("Parse Error: Expected a value (like id or intliteral)");
        return null;
    }

    // This Helper method is used to check if a var has been declared or not
    private boolean checkVariableDeclared(String id) throws IOException {
        if (!table.containsKey(id)) {
            System.out.println("Parse Error: Variable '" + id + "' is undeclared.");
            return false;
        }
        return true;
    }

}
