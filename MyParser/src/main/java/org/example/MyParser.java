package org.example;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class MyParser {
    enum TYPE {INTDATATYPE}
    private Map<String, SymbolTableItem> table = new HashMap<>();
    private MyScanner scanner;
    private MyScanner.TOKEN nextToken;
    private PushbackReader pbr;

    // SymbolTableItem + its constructor
    class SymbolTableItem {
        String name;
        TYPE type;


        SymbolTableItem(String name, TYPE type) {
            this.name = name;
            this.type = type;
        }
    }

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
            nextToken = scanner.scan();
            return programParse();
        } catch (Exception e) {
            System.out.println("Scanner error: " + e.getMessage());
            return false;
        }
    }

    // Parsing Methods
    // Check for: <Decls> <Stmts> $
    public boolean programParse() throws IOException {
        if (!declsParse()) {
            return false;
        }
        if (!stmtsParse()) {
            System.out.println("Parse Error: Expected statements");
            return false;
        }
        if (!match(MyScanner.TOKEN.SCANEOF)) {
            System.out.println("Parse Error: Expected end of file ($)");
            return false;
        } else {
            System.out.println("Program parsed successfully!\n");
            return true;
        }
    }


    // Check for: <Decl> <Decls>
    public boolean declsParse() throws IOException {
        if (nextToken == MyScanner.TOKEN.DECLARE ) {
            return declParse() && declsParse();
        }
        return true;
    }

    // Check for: declare id
    public boolean declParse() throws IOException {
        if (!match(MyScanner.TOKEN.DECLARE)) {
            System.out.println("Parse Error: Expected 'declare'");
            return false;
        }
        String id = scanner.getTokenBufferString();
        if (!match(MyScanner.TOKEN.ID)) {
            System.out.println("Parse Error: Expected an identifier after 'declare'");
            return false;
        }
        // Check if id is intliteral and its not in table before adding
        if (!match(MyScanner.TOKEN.INTDATATYPE)) {
            System.out.println("Parse Error: Expected 'int' after identifier");
            return false;
        }
        if (table.containsKey(id)) {
            System.out.println("Parse Error: '" + id + "' already declared.");
            return false;
        }
        table.put(id, new SymbolTableItem(id, TYPE.INTDATATYPE));
        return true;
    }

    // Check for: <Stmt><Stmts>
    public boolean stmtsParse() throws IOException {
        if (nextToken == MyScanner.TOKEN.PRINT || nextToken == MyScanner.TOKEN.SET || nextToken == MyScanner.TOKEN.IF || nextToken == MyScanner.TOKEN.CALC) {
            if (!stmtParse()) {
                return false;
            }
            return stmtsParse();  // Recursive call to look for more <Stmts>
        }
        return true;
    }

    // Check for all 4 possible <stmt>, each have their method
    public boolean stmtParse() throws IOException {
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
                return false;
        }
    }

    // Check for: print id
    public boolean parsePrintStmt() throws IOException {
        if (!match(MyScanner.TOKEN.PRINT)) {
            System.out.println("Parse Error: Expected 'print'");
            return false;
        }
        String id = scanner.getTokenBufferString();
        if (!match(MyScanner.TOKEN.ID)) {
            System.out.println("Parse Error: Expected an identifier after 'print'");
            return false;
        }
        return checkVariableDeclared(id);
    }

    // Check for: set id = intliteral
    public boolean parseSetStmt() throws IOException {
        if (!match(MyScanner.TOKEN.SET)) {
            System.out.println("Parse Error: Expected 'set'");
            return false;
        }
        String id = scanner.getTokenBufferString();
        if (!match(MyScanner.TOKEN.ID)) {
            System.out.println("Parse Error: Expected an identifier after 'set'");
            return false;
        }
        if (!checkVariableDeclared(id)) {
            return false;
        }
        if (!match(MyScanner.TOKEN.EQUALS)) {
            System.out.println("Parse Error: Expected '='");
            return false;
        }
        if (!match(MyScanner.TOKEN.INTLITERAL)) {
            System.out.println("Parse Error: Expected an intliteral after '='");
            return false;
        }
        return true;
    }

    // Check for: if id = id then <Stmts> endif
    public boolean parseIfStmt() throws IOException {
        if (match(MyScanner.TOKEN.IF)) {
            // Check first id
           String first_id = scanner.getTokenBufferString();
           if (!match(MyScanner.TOKEN.ID)) {
               System.out.println("Parse Error: Expected an identifier after 'if'");
               return false;
           }
           if (!checkVariableDeclared(first_id)) {
               return false;
           }
           if (!match(MyScanner.TOKEN.EQUALS)) {
               System.out.println("Parse Error: Expected '='");
               return false;
           }
           // Check second id
           String second_id = scanner.getTokenBufferString();
           if (!match(MyScanner.TOKEN.ID)) {
               System.out.println("Parse Error: Expected an identifier after '='");
               return false;
           }
           if (!checkVariableDeclared(second_id)) {
               return false;
           }
           if (!match(MyScanner.TOKEN.THEN)) {
               System.out.println("Parse Error: Expected 'then'");
               return false;
           }
           // Check for other statements
           if (!stmtsParse()) {
               return false;
           }
           if (!match(MyScanner.TOKEN.ENDIF)) {
               System.out.println("Parse Error: Expected 'endif'");
               return false;
           }
           return true;
        }  else {
            System.out.println("Parse Error: Expected 'if id = id then <Stmts> endif'");
            return false;
        }
    }

    // Check for: calc id = <Sum>
    public boolean parseCalcStmt() throws IOException {
        if (!match(MyScanner.TOKEN.CALC)) {
            System.out.println("Parse Error: Expected 'calc'");
            return false;
        }
        String id = scanner.getTokenBufferString();
        if (!match(MyScanner.TOKEN.ID)) {
            System.out.println("Parse Error: Expected an identifier after 'calc'");
            return false;
        }
        if (!checkVariableDeclared(id)) {
            return false;
        }
        if (!match(MyScanner.TOKEN.EQUALS)) {
            System.out.println("Parse Error: Expected '='");
            return false;
        }
        return sumParse();
    }

    // Check for: <Value> <SumEnd>
    public boolean sumParse() throws IOException {
        if (!valueParse()) {
            System.out.println("Parse Error: Expected a value (like id or intliteral) in sum");
            return false;
        }
        return sumEndParse();
    }

    // Check for: + <Value> <SumEnd>
    public boolean sumEndParse() throws IOException {
        if (nextToken == MyScanner.TOKEN.PLUS) {
            if (!match(MyScanner.TOKEN.PLUS)) {
                System.out.println("Parse Error: Expected '+' in sum");
                return false;
            }
            if (!valueParse()) {
                return false;
            }
            return sumEndParse();
        }
        return true;
    }

    // Check if value is either ID or number
    public boolean valueParse() throws IOException {
        if (nextToken == MyScanner.TOKEN.ID) {
            return match(MyScanner.TOKEN.ID);
        } else if (nextToken == MyScanner.TOKEN.INTLITERAL) {
            return match(MyScanner.TOKEN.INTLITERAL);
        }
        System.out.println("Parse Error: Expected a value (like id or intliteral)");
        return false;
    }

    // Helper method to check if variable is declared in table
    private boolean checkVariableDeclared(String id) throws IOException {
        if (!table.containsKey(id)) {
            System.out.println("Parse Error: Variable '" + id + "' is undeclared.");
            return false;
        }
        return true;
    }

}
