package org.yann.coretokenizor;

public class Tree {
}

class NodeProg {
    NodeDeclSeq declSeq;
    NodeStmtSeq stmtSeq;
}

class NodeDeclSeq {
    int type;
    NodeDecl d;
    NodeDeclSeq ds;
}

class NodeStmtSeq {
    int type;
    NodeStmt stmt;
    NodeStmtSeq ss;
}

class NodeDecl {
    NodeIdList idList;
}

class NodeIdList {
    int type;
    NodeId id;
    NodeIdList idList;
}

class NodeStmt {
    int type;
    NodeAssign assign;
    NodeIf ifStmt;
    NodeLoop loop;
    NodeIn in;
    NodeOut out;
}

class NodeAssign {
    NodeId id;
    NodeExp exp;
}

class NodeIf {
    int type;
    NodeCond cond;
    NodeStmtSeq thenStmtSeq;
    NodeStmtSeq elseStmtSeq;
}

class NodeLoop {
    NodeCond cond;
    NodeStmtSeq stmtSeq;
}

class NodeIn {
    NodeIdList idList;
}

class NodeOut {
    NodeIdList idList;
}

class NodeCond {
    int type;
    NodeComp comp;
    NodeCond left;
    NodeCond right;
}

class NodeComp {
    NodeOp left;
    NodeOp right;
    NodeCompOp compOp;
}

class NodeExp {
    int type;
    NodeFac fac;
    NodeExp exp;
}

class NodeFac {
    int type;
    NodeOp op;
    NodeFac fac;
}

class NodeOp {
    int type;
    NodeInt intValue;
    NodeId id;
    NodeExp exp;
}
class NodeCompOp {
    int type;
}
class NodeId {
    String value;
}

class NodeInt {
    int value;
}
