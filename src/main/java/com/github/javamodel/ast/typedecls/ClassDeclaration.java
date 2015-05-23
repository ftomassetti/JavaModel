package com.github.javamodel.ast.typedecls;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.annotations.AttributeMapping;
import com.github.javamodel.annotations.RelationMapping;
import com.github.javamodel.annotations.RuleMapping;
import com.github.javamodel.ast.common.AnnotationUsageNode;
import com.github.javamodel.ast.common.ClassTypeRef;
import com.github.javamodel.ast.common.InterfaceTypeRef;
import com.github.javamodel.ast.common.Modifier;
import com.github.javamodel.ast.AstNode;
import com.github.javamodel.ast.reflection.AstNodeType;
import lombok.Data;

import java.util.List;

/**
 * Created by ftomassetti on 22/05/15.
 */
@Data
@RuleMapping(rule= Java8Parser.NormalClassDeclarationContext.class)
public class ClassDeclaration extends TypeDeclaration {

    @RelationMapping(ctxAccessorName= "classModifier", filter = "annotation", type=AnnotationUsageNode.class)
    private List<AnnotationUsageNode> annotations;

    @AttributeMapping(ctxAccessorName= "classModifier", filter = "!annotation")
    private List<Modifier> modifiers;

    @AttributeMapping(ctxAccessorName = "Identifier")
    private String name;

    @RelationMapping()
    private List<TypeParameter> typeParameters;

    @RelationMapping()
    private ClassTypeRef superclass;

    @RelationMapping(ctxAccessorName = "superinterfaces")
    private List<InterfaceTypeRef> interfaces;

    @RelationMapping(ctxAccessorName = "classBody")
    private List<ClassElement> elements;

    public static final AstNodeType NODE_TYPE = AstNodeType.deriveFromNodeClass(ClassDeclaration.class);

    public ClassDeclaration(AstNode parent) {
        super(NODE_TYPE, parent);
    }

    // classModifier* 'class' Identifier typeParameters? superclass? superinterfaces? classBody


}
