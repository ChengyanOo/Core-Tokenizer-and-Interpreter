package org.yann.coretokenizor;

import java.io.File;
import java.io.IOException;

public class Parser {
    private final CoreScanner coreScanner;

    public Parser(String input) throws IOException {
        this.coreScanner = new CoreScanner(input);
    }

    public void checkToken(int expected) throws IOException {
        int currToken = coreScanner.getToken();

        if (currToken != expected) {
            String currStr = coreScanner.getTokenString(currToken);
            String expectedStr = coreScanner.getTokenString(expected);
            System.out.printf("Error: expected " + expectedStr + " but received " +  currStr+ " at line " + coreScanner.getLineNum());
            System.exit(0);
        }
    }

    public void parseProg(NodeProg nodeProg) throws IOException {
        checkToken(1);
        coreScanner.skipToken(); // Consume "program" token
        nodeProg.declSeq = new NodeDeclSeq();
        parseDeclSeq(nodeProg.declSeq);
        checkToken(2);
        coreScanner.skipToken(); // Consume "begin" token
        nodeProg.stmtSeq = new NodeStmtSeq();
        parseStmtSeq(nodeProg.stmtSeq);
        checkToken(3);
        coreScanner.skipToken(); // Consume "end" token
    }

    public void parseDeclSeq(NodeDeclSeq declSeq) throws IOException {
        declSeq.type=1;
        declSeq.d=new NodeDecl();
        parseDecl(declSeq.d);
        if (coreScanner.getToken()!= 2){
            declSeq.type=2;
            declSeq.ds= new NodeDeclSeq();
            parseDeclSeq(declSeq.ds);
        }

    }

    public void parseStmtSeq(NodeStmtSeq stmtSeq) throws IOException {
        stmtSeq.type =1;
        stmtSeq.stmt= new NodeStmt();
        parseStmt(stmtSeq.stmt);
//        System.out.println("Here: " + coreScanner.getLineNum() + " with name: ");
        if((coreScanner.getToken()!= 3 && coreScanner.getToken()!=7)){
            stmtSeq.type =2;
            stmtSeq.ss= new NodeStmtSeq();
            parseStmtSeq(stmtSeq.ss);
        }

    }

    public void parseDecl(NodeDecl d) throws IOException {
        checkToken(4); // "int"
        coreScanner.skipToken(); // Consume "int" token

        d.idList = new NodeIdList();
        parseIdList(d.idList);

        checkToken(12); // ";"
        coreScanner.skipToken(); // Consume ";" token
    }

    public void parseIdList(NodeIdList idList) throws IOException {
        idList.type = 1;
        checkToken(32); // Identifier
        idList.id = new NodeId();
        idList.id.value = coreScanner.idName();
        coreScanner.skipToken(); // Consume identifier token
        if (coreScanner.getToken() == 13) { // ","
            idList.type = 2;
            idList.idList = new NodeIdList();
            coreScanner.skipToken(); // Consume "," token
            parseIdList(idList.idList);
        } else {
            idList.type = 1;
        }
    }

    public void parseStmt(NodeStmt stmt) throws IOException {
        int currentToken = coreScanner.getToken();
        switch (currentToken) {
            case 32: // Identifier (assign)
                stmt.type = 1;
                stmt.assign = new NodeAssign();
                parseAssign(stmt.assign);
                break;
            case 5: // "if"
                stmt.type = 2;
                stmt.ifStmt = new NodeIf();
                parseIf(stmt.ifStmt);
                break;
            case 8: // "while"
                stmt.type = 3;
                stmt.loop = new NodeLoop();
                parseLoop(stmt.loop);
                break;
            case 10: // "read"
                stmt.type = 4;
                stmt.in = new NodeIn();
                parseIn(stmt.in);
                break;
            case 11: // "write"
                stmt.type = 5;
                stmt.out = new NodeOut();
                parseOut(stmt.out);
                break;
            default:
                System.out.println("Error: unexpected token in <stmt>" + coreScanner.getTokenString(coreScanner.getToken())+ coreScanner.getLineNum());
                System.exit(0);
                break;
        }
    }

    public void parseAssign(NodeAssign assign) throws IOException {
        checkToken(32); // Identifier
        assign.id = new NodeId();
        assign.id.value = coreScanner.idName();
        coreScanner.skipToken(); // Consume identifier token

        checkToken(14); // "="
        coreScanner.skipToken(); // Consume "=" token

        assign.exp = new NodeExp();
        parseExp(assign.exp);

        checkToken(12); // ";"
        coreScanner.skipToken(); // Consume ";" token
    }

    public void parseIf(NodeIf ifStmt) throws IOException {
        checkToken(5); // "if"
        coreScanner.skipToken(); // Consume "if" token
        ifStmt.cond = new NodeCond();
        parseCond(ifStmt.cond);

        checkToken(6); // "then"
        coreScanner.skipToken(); // Consume "then" token
        ifStmt.thenStmtSeq = new NodeStmtSeq();

        parseStmtSeq(ifStmt.thenStmtSeq);
//        System.out.println("Here" + coreScanner.idName());
        if (coreScanner.getToken() == 7) { // "else"
            ifStmt.type = 2;
            coreScanner.skipToken(); // Consume "else" token

            ifStmt.elseStmtSeq = new NodeStmtSeq();
            parseStmtSeq(ifStmt.elseStmtSeq);

        } else if (coreScanner.getToken() == 3){
            ifStmt.type = 1;

        } else{
            System.out.println("Error: unexpected token in <if>");
            System.exit(0);
        }

        checkToken(3); // "end"
        coreScanner.skipToken(); // Consume "end" token

        checkToken(12); // ";"
        coreScanner.skipToken(); // Consume ";" token
    }

    public void parseLoop(NodeLoop loop) throws IOException {
        checkToken(8); // "while"
        coreScanner.skipToken(); // Consume "while" token

        loop.cond = new NodeCond();
        parseCond(loop.cond);

        checkToken(9); // "loop"
        coreScanner.skipToken(); // Consume "loop" token

        loop.stmtSeq = new NodeStmtSeq();
        parseStmtSeq(loop.stmtSeq);

        checkToken(3); // "end"
        coreScanner.skipToken(); // Consume "end" token

        checkToken(12); // ";"
        coreScanner.skipToken(); // Consume ";" token
    }


    public void parseIn(NodeIn in) throws IOException {
        checkToken(10); // "read"
        coreScanner.skipToken(); // Consume "read" token

        in.idList = new NodeIdList();
        parseIdList(in.idList);

        checkToken(12); // ";"
        coreScanner.skipToken(); // Consume ";" token
    }

    public void parseOut(NodeOut out) throws IOException {
        checkToken(11); // "write"
        coreScanner.skipToken(); // Consume "write" token

        out.idList = new NodeIdList();
        parseIdList(out.idList);

        checkToken(12); // ";"
        coreScanner.skipToken(); // Consume ";" token
    }

    public void parseCond(NodeCond cond) throws IOException {
        int currToken = coreScanner.getToken();
        if (currToken == 15) { // "!"
            cond.type = 2;
            coreScanner.skipToken(); // Consume "!" token

            cond.left = new NodeCond();
            parseCond(cond.left);
        } else if (currToken == 16) { // "["
            coreScanner.skipToken(); // Consume "[" token

            cond.left = new NodeCond();
            parseCond(cond.left);
            currToken = coreScanner.getToken(); // Update currToken
            if (currToken == 18) {
                coreScanner.skipToken(); // Consume "&&" token
                cond.type = 3;
            } else if (currToken == 19) {
                coreScanner.skipToken(); // Consume "||" token
                cond.type = 4;
            } else{
                System.out.println("Error: unexpected token in <cond>");
                System.exit(0);
            }

            cond.right = new NodeCond();
            parseCond(cond.right);

            checkToken(17); // "]"
            coreScanner.skipToken(); // Consume "]" token
        } else {
            cond.type = 1;
            cond.comp = new NodeComp();
            parseComp(cond.comp);
        }
    }

    public void parseComp(NodeComp comp) throws IOException {
        checkToken(20); // "("
        coreScanner.skipToken(); // Consume "(" token

        comp.left = new NodeOp();
        parseOp(comp.left);

        comp.compOp = new NodeCompOp();
        parseCompOp(comp.compOp);

        comp.right = new NodeOp();
        parseOp(comp.right);

        checkToken(21); // ")"
        coreScanner.skipToken(); // Consume ")" token
    }

    public void parseExp(NodeExp exp) throws IOException {
        exp.fac = new NodeFac();
        parseFac(exp.fac);

        int currToken = coreScanner.getToken();
        if (currToken == 22 || currToken == 23) { // "+" or "-"
            exp.type = currToken == 22 ? 2 : 3;
            coreScanner.skipToken(); // Consume "+" or "-" token

            exp.exp = new NodeExp();
            parseExp(exp.exp);
        } else {
            exp.type = 1;
        }
    }

    public void parseFac(NodeFac fac) throws IOException {
        fac.op = new NodeOp();
        parseOp(fac.op);

        if (coreScanner.getToken() == 24) { // "*"
            fac.type = 2;
            coreScanner.skipToken(); // Consume "*" token

            fac.fac = new NodeFac();
            parseFac(fac.fac);
        } else {
            fac.type = 1;
        }
    }

    public void parseOp(NodeOp op) throws IOException {
        int currToken = coreScanner.getToken();
        if (currToken == 20) { // "("
            op.type = 3;
            coreScanner.skipToken(); // Consume "(" token

            op.exp = new NodeExp();
            parseExp(op.exp);

            checkToken(21); // ")"
            coreScanner.skipToken(); // Consume ")" token
        } else if (currToken == 32) { // identifier
            op.type = 2;
            op.id = new NodeId();
            op.id.value = coreScanner.idName();
            coreScanner.skipToken(); // Consume identifier token
        } else if (currToken == 31) {
            op.type = 1;
            op.intValue = new NodeInt();
            op.intValue.value = coreScanner.intVal();
            coreScanner.skipToken(); //
        }
    }

    public void parseCompOp(NodeCompOp compOp) {
        int currToken = coreScanner.getToken();


        switch (currToken) {
            case 25: // "!="
                coreScanner.skipToken(); // Consume "!=" token
                compOp.type = 1;
                break;
            case 26: // "=="
                coreScanner.skipToken(); // Consume "==" token
                compOp.type = 2;
                break;
            case 27: // "<"
                coreScanner.skipToken(); // Consume "<" token
                compOp.type = 3;
                break;
            case 28: // ">"
                coreScanner.skipToken(); // Consume ">" token
                compOp.type = 4;
                break;
            case 29: // "<="
                coreScanner.skipToken(); // Consume "<=" token
                compOp.type = 5;
                break;
            case 30: // ">="
                coreScanner.skipToken(); // Consume ">=" token
                compOp.type = 6;
                break;
            default:
                System.out.println("Error: Invalid comp op token");
                System.exit(0);
        }
    }

    public static void main(String[] args) throws IOException {
//        String input = "input.txt";
//        Parser parser = new Parser(input);
//        NodeProg nodeProg = new NodeProg();
//        parser.parseProg(nodeProg);
//        Printer printer = new Printer();
//        System.out.println();
//        System.out.println("*********The following is the pretty printing output*********");
//        printer.printProg(nodeProg);
//
//        File file = new File("data.txt");
//        Executer executer = new Executer(file);
//        System.out.println();
//        System.out.println("*********The following is the execution output*********");
//        executer.executeProg(nodeProg);
//        System.out.println();
    }
}
