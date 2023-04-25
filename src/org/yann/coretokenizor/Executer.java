package org.yann.coretokenizor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Executer {
    private Memory memory; // Used for storing and accessing variable values
    private Scanner dataScanner; // Scanner object for reading input data from a file

    // Constructor initializes the memory and dataScanner
    public Executer(File file) throws FileNotFoundException {
        this.memory = new Memory();
        dataScanner = new Scanner(file);
    }

    // Executes the entire program, including declarations and statements
    public void executeProg(NodeProg prog) {
        executeDeclSeq(prog.declSeq);
        executeStmtSeq(prog.stmtSeq);
    }

    // Executes a sequence of declarations
    public void executeDeclSeq(NodeDeclSeq declSeq) {
        if (declSeq.type == 2) {
            executeDecl(declSeq.d);
            executeDeclSeq(declSeq.ds);
        } else{
            executeDecl(declSeq.d);
        }
    }

    // Executes a single declaration
    public void executeDecl(NodeDecl decl) {
        executeIdList(decl.idList);
    }

    // Initializes the variables in the identifier list with a value of 0
    public void executeIdList(NodeIdList idList) {
        memory.set(idList.id.value, 0);
        if (idList.type == 2) {
            executeIdList(idList.idList);
        }
    }

    // Executes a sequence of statements
    public void executeStmtSeq(NodeStmtSeq stmtSeq) {
        if (stmtSeq.type == 2) {
            executeStmt(stmtSeq.stmt);
            executeStmtSeq(stmtSeq.ss);
        }else{
            executeStmt(stmtSeq.stmt);
        }
    }

    // Executes a single statement, depending on the statement type
    public void executeStmt(NodeStmt stmt) {
        if (stmt.type == 1) {
            executeAssign(stmt.assign);
        } else if (stmt.type == 2) {
            executeIf(stmt.ifStmt);
        } else if (stmt.type == 3) {
            executeLoop(stmt.loop);
        } else if (stmt.type == 4) {
            executeIn(stmt.in);
        } else if (stmt.type == 5) {
            executeOut(stmt.out);
        }
    }

    // Executes an assignment statement, storing the result in memory
    public void executeAssign(NodeAssign assign) {
        int value = executeExp(assign.exp);
        memory.set(assign.id.value, value);
    }

    // Executes an if statement, including condition evaluation and then/else branches
    public void executeIf(NodeIf ifStmt) {
        boolean condition = executeCond(ifStmt.cond);
        if (condition) {
            executeStmtSeq(ifStmt.thenStmtSeq);
        } else if (ifStmt.type == 2) {
            executeStmtSeq(ifStmt.elseStmtSeq);
        }
    }

    // Executes a loop statement, including condition evaluation and loop body
    public void executeLoop(NodeLoop loop) {
        while (executeCond(loop.cond)) {
            executeStmtSeq(loop.stmtSeq);
        }
    }

    // Executes an input statement, reading values from dataScanner and storing them in memory
    public void executeIn(NodeIn in) {
        executeIdListIn(in.idList, dataScanner);
    }

    // Reads values from the scanner and stores them in memory for each identifier in the list
    public void executeIdListIn(NodeIdList idList, Scanner scanner) {
        int value = scanner.nextInt();
        memory.set(idList.id.value, value);
        if (idList.type == 2) {
            executeIdListIn(idList.idList, scanner);
        }
    }

    // Executes an output statement, printing values from memory for each identifier in the list
    public void executeOut(NodeOut out) {
        executeIdListOut(out.idList);
    }

    // Prints values from memory for each identifier in the list
    public void executeIdListOut(NodeIdList idList) {
        if(!memory.has(idList.id.value)){
            System.out.println("Error: id " + idList.id.value + " was not initialized");
            System.exit(0);
        }
        System.out.println(idList.id.value + " = " + memory.get(idList.id.value));
        if (idList.type == 2) {
            executeIdListOut(idList.idList);
        }
    }

    // Evaluates a condition and returns a boolean result
    public boolean executeCond(NodeCond cond) {
        if (cond.type == 1) {
            return executeComp(cond.comp);
        } else if (cond.type == 2) {
            return !executeCond(cond.left);
        } else {
            boolean leftCond = executeCond(cond.left);
            boolean rightCond = executeCond(cond.right);

            if (cond.type == 3) {
                return leftCond && rightCond;
            } else {
                return leftCond || rightCond;
            }
        }
    }

    // Evaluates a comparison and returns a boolean result
    public boolean executeComp(NodeComp comp) {
        int leftOp = executeOp(comp.left);
        int rightOp = executeOp(comp.right);
        switch (comp.compOp.type) {
            case 1:
                return leftOp != rightOp;
            case 2:
                return leftOp == rightOp;
            case 3:
                return leftOp < rightOp;
            case 4:
                return leftOp > rightOp;
            case 5:
                return leftOp <= rightOp;
            default:
                return leftOp >= rightOp;
        }
    }

    // Evaluates an expression and returns an integer result
    public int executeExp(NodeExp exp) {
        int leftFac = executeFac(exp.fac);

        if (exp.type == 2) {
            int rightExp = executeExp(exp.exp);
            return leftFac + rightExp;
        } else if (exp.type == 3) {
            int rightExp = executeExp(exp.exp);
            return leftFac - rightExp;
        } else {
            return leftFac;
        }
    }

    // Evaluates a factor and returns an integer result
    public int executeFac(NodeFac fac) {
        int leftOp = executeOp(fac.op);
        if (fac.type == 2) {
            int rightFac = executeFac(fac.fac);
            return leftOp * rightFac;
        } else {
            return leftOp;
        }
    }

    // Evaluates an operand and returns an integer result
    public int executeOp(NodeOp op) {
        if (op.type == 1) {
            return op.intValue.value;
        } else if (op.type == 2) {
            if(!memory.has(op.id.value)){
                System.out.println("Error: id " + op.id.value + " was not initialized");
                System.exit(0);
            }
            return memory.get(op.id.value);
        } else {
            return executeExp(op.exp);
        }
    }
}