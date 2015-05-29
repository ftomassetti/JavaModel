package com.github.javamodel.ast.statements;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;
import com.github.javamodel.ast.MultipleRelation;

/**
 * Created by federico on 29/05/15.
 */
public class BlockStatement extends Statement {
    private MultipleRelation<BlockStatement, Statement> statements = new MultipleRelation<>(this);

    @Override
    public String toString() {
        return "BlockStatement{}";
    }

    public static BlockStatement fromAntlrNode(Java8Parser.BlockContext antlrNode){
        return fromAntlrNode(antlrNode.blockStatements());
    }

    public static BlockStatement fromAntlrNode(Java8Parser.BlockStatementsContext antlrNode){
        BlockStatement instance = new BlockStatement();
        if (antlrNode == null) return instance;
        if (antlrNode.blockStatement() != null) {
            antlrNode.blockStatement().forEach((s) -> instance.statements.add(Statement.fromAntlrNode(s)));
        }
        return instance;
    }
}
