package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;
import com.github.javamodel.ast.types.TypeRef;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ftomassetti on 27/05/15.
 */
public class FieldDeclaration extends Node {
    @Override
    public String toString() {
        return "FieldDeclaration{}";
    }

    private List<Modifier> modifiers = new ArrayList<>();
    private final MultipleRelation<FieldDeclaration, AnnotationUsage> annotations = new MultipleRelation<>("annotations", this);
    private final SingleRelation<FieldDeclaration, TypeRef> type = new SingleRelation<>("type", this);
    private final MultipleRelation<FieldDeclaration, VariableDeclaration> variableDeclarations = new MultipleRelation<>("variableDeclarations", this);

    public static FieldDeclaration fromAntlrNode(Java8Parser.FieldDeclarationContext antlrNode){
        FieldDeclaration instance = new FieldDeclaration();
        antlrNode.fieldModifier().forEach((cm)-> {
            if (null == cm.annotation()){
                instance.modifiers.add(Modifier.fromAntlrNode(cm));
            } else {
                instance.annotations.add(AnnotationUsage.fromAntlrNode(cm.annotation()));
            }
        });
        instance.type.set(TypeRef.fromAntlrNode(antlrNode.unannType()));
        antlrNode.variableDeclaratorList().variableDeclarator().forEach((vd)->
                instance.variableDeclarations.add(VariableDeclaration.fromAntlrNode(vd))
        );
        
        return instance;
    }
}
