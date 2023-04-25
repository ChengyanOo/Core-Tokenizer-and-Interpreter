package org.yann.coretokenizor;

public class Printer {

    // Prints the entire program, including declarations and statements
    public void printProg(NodeProg nodeProg) {
        System.out.println("program ");
        printDeclSeq(nodeProg.declSeq);
        System.out.println("begin ");
        printStmtSeq(nodeProg.stmtSeq);
        System.out.println("end");
    }

    // Prints a sequence of declarations
    public void printDeclSeq(NodeDeclSeq declSeq) {
        if (declSeq.type == 1) {
            printDecl(declSeq.d);
        } else if (declSeq.type == 2) {
            printDecl(declSeq.d);
            printDeclSeq(declSeq.ds);
        }
    }

    // Prints a sequence of statements
    public void printStmtSeq(NodeStmtSeq stmtSeq) {
        if (stmtSeq.type == 1) {
            printStmt(stmtSeq.stmt);
        } else if (stmtSeq.type == 2) {
            printStmt(stmtSeq.stmt);
            printStmtSeq(stmtSeq.ss);
        }
    }

    // Prints a single declaration, including the list of identifiers
    public void printDecl(NodeDecl d) {
        System.out.print("int ");
        printIdList(d.idList);
        System.out.println(";");
    }

    // Prints a list of identifiers (for declarations or input/output)
    public void printIdList(NodeIdList idList) {
        System.out.print(idList.id.value);
        if (idList.type == 2) {
            System.out.print(", ");
            printIdList(idList.idList);
        }
    }

    // Prints a single statement, depending on the statement type
    public void printStmt(NodeStmt stmt) {
        switch (stmt.type) {
            case 1:
                printAssign(stmt.assign);
                break;
            case 2:
                printIf(stmt.ifStmt);
                break;
            case 3:
                printLoop(stmt.loop);
                break;
            case 4:
                printIn(stmt.in);
                break;
            case 5:
                printOut(stmt.out);
                break;
            default:
                System.out.println("Error: Invalid statement type");
                System.exit(0);
        }
    }

    // Prints an assignment statement
    public void printAssign(NodeAssign assign) {
        System.out.print(assign.id.value + " = ");
        printExp(assign.exp);
        System.out.println(";");
    }

    // Prints an if statement, including its condition and then/else branches
    public void printIf(NodeIf ifStmt) {
        System.out.print("    if ");
        printCond(ifStmt.cond);
        System.out.print(" then ");
        printStmtSeq(ifStmt.thenStmtSeq);
        if (ifStmt.type == 2) {
            System.out.print("    else ");
            printStmtSeq(ifStmt.elseStmtSeq);
        }
        System.out.println("    end;");
    }

    // Prints a loop statement, including its condition and body
    public void printLoop(NodeLoop loop) {
        System.out.print("while ");
        printCond(loop.cond);
        System.out.print(" loop ");
        printStmtSeq(loop.stmtSeq);
        System.out.println(" end;");
    }

    // Prints an input statement, including the list of identifiers to read
    public void printIn(NodeIn in) {
        System.out.print("read ");
        printIdList(in.idList);
        System.out.println(";");
    }

    // Prints an output statement, including the list of identifiers to print
    public void printOut(NodeOut out) {
        System.out.print("write ");
        printIdList(out.idList);
        System.out.println(";");
    }

    // Prints a condition, which can be a comparison, negation, or logical operation
    public void printCond(NodeCond cond) {
        if (cond.type == 1) {
            printComp(cond.comp);
        } else if (cond.type == 2) {
            System.out.print("!");
            printCond(cond.left);
        } else if (cond.type == 3 || cond.type == 4) {
            System.out.print("[");
            printCond(cond.left);
            System.out.print(cond.type == 3 ? " && " : " || ");
            printCond(cond.right);
            System.out.print("]");
        }
    }
    // Prints a comparison, including its operands and comparison operator
    public void printComp(NodeComp comp) {
        System.out.print("(");
        printOp(comp.left);
        printCompOp(comp.compOp);
        printOp(comp.right);
        System.out.print(")");
    }

    // Prints an expression, which can be a sum, difference, or single factor
    public void printExp(NodeExp exp) {
        printFac(exp.fac);
        if (exp.type == 2) {
            System.out.print(" + ");
            printExp(exp.exp);
        } else if (exp.type == 3) {
            System.out.print(" - ");
            printExp(exp.exp);
        }
    }

    // Prints a factor, which can be a product or a single operand
    public void printFac(NodeFac fac) {
        printOp(fac.op);
        if (fac.type == 2) {
            System.out.print(" * ");
            printFac(fac.fac);
        }
    }

    // Prints an operand, which can be an integer value, identifier, or expression
    public void printOp(NodeOp op) {
        if (op.type == 1) {
            System.out.print(op.intValue.value);
        } else if (op.type == 2) {
            System.out.print(op.id.value);
        } else if (op.type == 3) {
            System.out.print("(");
            printExp(op.exp);
            System.out.print(")");
        }
    }

    // Prints a comparison operator
    public void printCompOp(NodeCompOp compOp) {
        String[] compOps = {"!=", "==", "<", ">", "<=", ">="};
        System.out.print(compOps[compOp.type - 1]);
    }

}
