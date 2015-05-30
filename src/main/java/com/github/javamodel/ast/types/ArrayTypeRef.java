package com.github.javamodel.ast.types;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;
import com.github.javamodel.ast.SingleRelation;
import com.github.javamodel.ast.types.TypeRef;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

/**
 * Created by ftomassetti on 27/05/15.
 */
public class ArrayTypeRef extends TypeRef {

    private SingleRelation<ArrayTypeRef, TypeRef> baseType = new SingleRelation<>(this);

    private static TypeRef fromAntlrNode(TypeRef baseType, long nDims) {
        ArrayTypeRef instance = new ArrayTypeRef();
        if (nDims == 1){
            instance.baseType.set(baseType);
        } else {
            instance.baseType.set(fromAntlrNode(baseType, nDims - 1));
        }
        return instance;
    }

    public static TypeRef fromAntlrNode(Java8Parser.UnannArrayTypeContext antlrNode) {
        if (antlrNode.unannPrimitiveType() != null){

            long nDims = antlrNode.dims().children.stream().filter((c)->c instanceof TerminalNodeImpl).map((c) -> (TerminalNodeImpl)c)
                    .filter((c)->c.getText().equals("[")).count();

            // TODO consider annotations
            // TODO consider multiple dimensions
            if (antlrNode.dims().annotation() != null && !antlrNode.dims().annotation().isEmpty()){
                throw new UnsupportedOperationException();
            }

            return fromAntlrNode(PrimitiveTypeRef.fromAntlrNode(antlrNode.unannPrimitiveType()), nDims);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public String toString() {
        return "ArrayTypeRef{}";
    }
}
