package com.github.javamodel.ast.typedecls;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.annotations.AttributeMapping;
import com.github.javamodel.annotations.RelationMapping;
import com.github.javamodel.annotations.RuleMapping;
import com.github.javamodel.ast.AstNode;
import com.github.javamodel.ast.common.*;
import com.github.javamodel.ast.reflection.AstNodeType;
import com.github.javamodel.ast.reflection.AstNodeTypeDeriver;
import lombok.Data;

import java.util.List;

/**
 * Created by ftomassetti on 22/05/15.
 */
@Data
@RuleMapping(rule= Java8Parser.FieldDeclarationContext.class)
public class FieldDeclaration extends TypeDeclaration {

    @RelationMapping(ctxAccessorName= "fieldModifier", filter = "annotation", type=AnnotationUsageNode.class)
    private List<AnnotationUsageNode> annotations;

    @AttributeMapping(ctxAccessorName= "fieldModifier", filter = "!annotation")
    private List<Modifier> modifiers;

    @RelationMapping(ctxAccessorName= "unannType")
    private TypeRef type;

    @RelationMapping(ctxAccessorName = "variableDeclaratorList")
    private List<VariableDeclarator> variables;

    /*@AttributeMapping(ctxAccessorName = "Identifier")
    private String name;*/

    /*@RelationMapping()
    private List<TypeParameter> typeParameters;

    @RelationMapping()
    private ClassTypeRef superclass;

    @RelationMapping(ctxAccessorName = "superinterfaces")
    private List<InterfaceTypeRef> interfaces;

    @RelationMapping(ctxAccessorName = "classBody")
    private List<ClassElement> elements;*/

    public static final AstNodeType NODE_TYPE = AstNodeTypeDeriver.deriveFromNodeClass(FieldDeclaration.class);

    public FieldDeclaration(AstNode parent) {
        super(NODE_TYPE, parent);
    }

}
