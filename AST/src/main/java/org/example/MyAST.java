package org.example;

import java.util.ArrayList;
import java.util.List;


public class MyAST {

    // NodeBase and Root
    class NodeProgram extends NodeBase {
        private NodeDecls nodeDecls;
        private NodeStmts nodeStmts;

        public NodeProgram(NodeDecls nodeDecls, NodeStmts nodeStmts) {
            this.nodeDecls = nodeDecls;
            this.nodeStmts = nodeStmts;
        }

        @Override
        public void display() {
            System.out.println("AST Declarations");
            nodeDecls.display();
            System.out.println();
            System.out.println("AST Statements");
            nodeStmts.display();
            System.out.println();
        }
    }

    private NodeProgram root;

    public NodeProgram getRoot() {
        return root;
    }

    public void setRoot(NodeProgram root) {
        this.root = root;
    }

    public void display() {
        if (root != null) {
            root.display();
        } else {
            System.out.println("AST root is empty");
        }
    }

    // AST Classes

    // Base Node class and Abstract Node classes
    public abstract class NodeBase {
        public abstract void display();
    }

    public abstract class NodeExpr extends NodeBase {
        // EMPTY
    }

    abstract class NodeStmt extends NodeBase {
        // EMPTY
    }

    // All AST node classes
    class NodeId extends NodeExpr {
        private String variableName;

        public NodeId(String variableName) {
            this.variableName = variableName;
        }

        @Override
        public void display() {
            System.out.println("AST id " + variableName);
        }
    }

    class NodeIntLiteral extends NodeExpr {
        private int value;

        public NodeIntLiteral(int value) {
            this.value = value;
        }

        @Override
        public void display() {
            System.out.println("AST int literal " + value);
        }
    }

    class NodePlus extends NodeExpr {
        private NodeExpr lhs;
        private NodeExpr rhs;

        public NodePlus(NodeExpr lhs, NodeExpr rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public void display() {
            System.out.println("AST sum");
            System.out.print("LHS: ");
            lhs.display();
            System.out.print("RHS: ");
            rhs.display();
        }
    }



    class NodePrint extends NodeStmt {
        private NodeId nodeId;

        public NodePrint(NodeId nodeId) {
            this.nodeId = nodeId;
        }

        @Override
        public void display() {
            System.out.println("AST print");
            nodeId.display();
            System.out.println();
        }
    }

    class NodeSet extends NodeStmt {
        private NodeId nodeId;
        private NodeIntLiteral nodeIntLiteral;

        public NodeSet(NodeId nodeId, NodeIntLiteral nodeIntLiteral) {
            this.nodeId = nodeId;
            this.nodeIntLiteral = nodeIntLiteral;
        }

        @Override
        public void display() {
            System.out.println("AST set");
            nodeId.display();
            nodeIntLiteral.display();
            System.out.println();
        }
    }

    class NodeCalc extends NodeStmt {
        private NodeId nodeId;
        private NodeExpr nodeExpr;

        public NodeCalc(NodeId nodeId, NodeExpr nodeExpr) {
            this.nodeId = nodeId;
            this.nodeExpr = nodeExpr;
        }

        @Override
        public void display() {
            System.out.println("AST calc");
            nodeId.display();
            nodeExpr.display();
            System.out.println();
        }
    }

    class NodeStmts extends NodeStmt {
        private List<NodeStmt> stmts;

        public NodeStmts(List<NodeStmt> stmts) {
            this.stmts = stmts;
        }

        @Override
        public void display() {
            for (NodeStmt stmt : stmts) {
                stmt.display();
            }
        }
    }

    class NodeIf extends NodeStmt {
        private NodeId lhs;
        private NodeId rhs;
        private NodeStmts stmts;

        public NodeIf(NodeId lhs, NodeId rhs, NodeStmts stmts) {
            this.lhs = lhs;
            this.rhs = rhs;
            this.stmts = stmts;
        }

        @Override
        public void display() {
            System.out.println("AST if");
            System.out.print("LHS: ");
            lhs.display();
            System.out.print("RHS: ");
            rhs.display();
            System.out.println();
            stmts.display();
            System.out.println("AST endif");
        }
    }

    class NodeDecls extends NodeBase {
        private List<NodeId> decls;

        public NodeDecls(List<NodeId> decls) {
            this.decls = decls;
        }

        @Override
        public void display() {
            for (NodeId decl : decls) {
                decl.display();
            }
        }
    }
}
