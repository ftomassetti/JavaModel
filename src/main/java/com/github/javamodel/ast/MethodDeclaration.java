package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;
import com.github.javamodel.ast.statements.BlockStatement;
import com.github.javamodel.ast.types.TypeRef;
import com.github.javamodel.ast.types.VoidTypeRef;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ftomassetti on 27/05/15.
 */
public class MethodDeclaration extends Node {
    @Override
    public String toString() {
        return "MethodDeclaration{" +
                "name='" + name + '\'' +
                ", modifiers=" + modifiers +
                '}';
    }

    private List<Modifier> modifiers = new ArrayList<>();
    private String name;
    private final MultipleRelation<MethodDeclaration, AnnotationUsage> annotations = new MultipleRelation<>("annotations", this);
    private final MultipleRelation<MethodDeclaration, FormalParameter> formalParameters = new MultipleRelation<>("formalParameters", this);
    private final SingleRelation<MethodDeclaration, TypeRef> returnType = new SingleRelation<>("returnType", this);
    private final SingleRelation<MethodDeclaration, BlockStatement> block = new SingleRelation<MethodDeclaration, BlockStatement>("block", this);

    public static MethodDeclaration fromAntlrNode(Java8Parser.MethodDeclarationContext antlrNode){
        MethodDeclaration instance = new MethodDeclaration();
        antlrNode.methodModifier().forEach((cm)-> {
            if (null == cm.annotation()){
                instance.modifiers.add(Modifier.fromAntlrNode(cm));
            } else {
                instance.annotations.add(AnnotationUsage.fromAntlrNode(cm.annotation()));
            }
        });
        if (antlrNode.methodHeader().typeParameters()!=null){
            throw new UnsupportedOperationException();
        }
        if (antlrNode.methodHeader().throws_() != null){
            throw new UnsupportedOperationException();
        }
        if (antlrNode.methodHeader().annotation()!=null){
            antlrNode.methodHeader().annotation().forEach((an)->instance.annotations.add(AnnotationUsage.fromAntlrNode(an)));
        }
        if (antlrNode.methodHeader().result().getText().equals("void")){
            instance.returnType.set(new VoidTypeRef());
        } else {
            instance.returnType.set(TypeRef.fromAntlrNode(antlrNode.methodHeader().result().unannType()));
        }
        if (antlrNode.methodHeader().methodDeclarator().dims() != null){
            throw new UnsupportedOperationException();
        }
        if (antlrNode.methodHeader().methodDeclarator().formalParameterList() != null){
            if (antlrNode.methodHeader().methodDeclarator().formalParameterList().formalParameters() != null) {
                antlrNode.methodHeader().methodDeclarator().formalParameterList().formalParameters().formalParameter().forEach((an) ->
                        instance.formalParameters.add(FormalParameter.fromAntlrNode(an)));
            }
        }
        if (antlrNode.methodBody() != null){
            instance.block.set(BlockStatement.fromAntlrNode(antlrNode.methodBody().block()));
        }

        instance.name = antlrNode.methodHeader().methodDeclarator().Identifier().getText();

        return instance;
    }
}
