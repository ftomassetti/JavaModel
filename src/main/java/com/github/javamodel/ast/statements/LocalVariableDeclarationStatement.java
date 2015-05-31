package com.github.javamodel.ast.statements;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.ast.*;
import com.github.javamodel.ast.types.TypeRef;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by federico on 31/05/15.
 */
public class LocalVariableDeclarationStatement extends Statement {

    private List<Modifier> modifiers = new ArrayList<>();
    private final MultipleRelation<LocalVariableDeclarationStatement, AnnotationUsage> annotations = new MultipleRelation<>("annotations", this);
    private final SingleRelation<LocalVariableDeclarationStatement, TypeRef> type = new SingleRelation<>("type", this);
    private final MultipleRelation<LocalVariableDeclarationStatement, VariableDeclaration> variableDeclarations = new MultipleRelation<>("variableDeclarations", this);

    public static Statement fromAntlrNode(Java8Parser.LocalVariableDeclarationStatementContext antlrNode) {
        LocalVariableDeclarationStatement instance = new LocalVariableDeclarationStatement();
        antlrNode.localVariableDeclaration().variableModifier().forEach((cm) -> {
            if (null == cm.annotation()) {
                instance.modifiers.add(Modifier.fromAntlrNode(cm));
            } else {
                instance.annotations.add(AnnotationUsage.fromAntlrNode(cm.annotation()));
            }
        });
        instance.type.set(TypeRef.fromAntlrNode(antlrNode.localVariableDeclaration().unannType()));
        antlrNode.localVariableDeclaration().variableDeclaratorList().variableDeclarator().forEach((vd)->
                        instance.variableDeclarations.add(VariableDeclaration.fromAntlrNode(vd))
        );

        return instance;
    }

}
