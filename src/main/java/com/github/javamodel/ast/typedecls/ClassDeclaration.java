package com.github.javamodel.ast.typedecls;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.annotations.AttributeMapping;
import com.github.javamodel.annotations.RelationMapping;
import com.github.javamodel.annotations.RuleMapping;
import com.github.javamodel.ast.common.AnnotationUsageNode;
import com.github.javamodel.ast.common.Modifier;
import com.github.javamodel.ast.Node;
import com.github.javamodel.ast.reflection.NodeType;
import lombok.Data;

import java.util.List;

/**
 * Created by ftomassetti on 22/05/15.
 */
@Data
@RuleMapping(rule= Java8Parser.NormalClassDeclarationContext.class)
public class ClassDeclaration extends TypeDeclaration {

    @RelationMapping(ctxAccessorName= "classModifier", filter = "annotation")
    private List<AnnotationUsageNode> annotations;
    @AttributeMapping(ctxAccessorName= "classModifier", filter = "!annotation")
    private List<Modifier> modifiers;

    public static final NodeType NODE_TYPE = NodeType.deriveFromNodeClass(ClassDeclaration.class);

    public ClassDeclaration(Node parent) {
        super(NODE_TYPE, parent);
    }

    // classModifier* 'class' Identifier typeParameters? superclass? superinterfaces? classBody


}
