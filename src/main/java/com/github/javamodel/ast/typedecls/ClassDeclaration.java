package com.github.javamodel.ast.typedecls;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.RelationMapping;
import com.github.javamodel.RuleMapping;
import com.github.javamodel.ast.AnnotationUsageNode;
import com.github.javamodel.ast.Modifier;
import com.github.javamodel.ast.Node;
import com.github.javamodel.ast.NodeType;
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
    @RelationMapping(ctxAccessorName= "classModifier", filter = "!annotation")
    private List<Modifier> modifiers;

    public static final NodeType NODE_TYPE = NodeType.deriveFromNodeClass(ClassDeclaration.class);

    public ClassDeclaration(Node parent) {
        super(NODE_TYPE, parent);
    }

    // classModifier* 'class' Identifier typeParameters? superclass? superinterfaces? classBody


}
