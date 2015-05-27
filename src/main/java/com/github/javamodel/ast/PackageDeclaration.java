package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ftomassetti on 27/05/15.
 */
public class PackageDeclaration extends Node {

    private final MultipleRelation<PackageDeclaration, AnnotationUsage> annotations = new MultipleRelation<>(this);
    private List<String> identifiers = new ArrayList<>();

    public static PackageDeclaration fromAntlrNode(Java8Parser.PackageDeclarationContext antlrNode) {
        PackageDeclaration instance = new PackageDeclaration();
        antlrNode.packageModifier().forEach((pm) -> instance.annotations.add(AnnotationUsage.fromAntlrNode(pm.annotation())));
        antlrNode.Identifier().forEach((id) -> instance.identifiers.add(id.getText()));
        return instance;
    }
}
