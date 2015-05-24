package com.github.javamodel.ast.reflection;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.ast.AstNode;
import com.github.javamodel.ast.common.PrimitiveType;
import com.github.javamodel.ast.common.PrimitiveTypeRef;
import org.easymock.EasyMockSupport;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

/**
 * Unit test for simple App.
 */
public class AstNodeTypeTest extends EasyMockSupport {

    @Test
    public void antlrToAstRelationValueForBoolean(){
        AstNode parent = null;
        Java8Parser.UnannPrimitiveTypeContext unannPrimitiveTypeContext = createMock(Java8Parser.UnannPrimitiveTypeContext.class);
        expect(unannPrimitiveTypeContext.numericType()).andReturn(null);
        expect(unannPrimitiveTypeContext.getText()).andReturn("boolean");
        replayAll();
        assertFalse(AstNodeType.hasCorrespondingTypeEnum(unannPrimitiveTypeContext.getClass()));
        Object res = AstNodeType.antlrToAstRelationValue(unannPrimitiveTypeContext, parent);
        PrimitiveTypeRef expected = new PrimitiveTypeRef(null);
        expected.setPrimitiveType(PrimitiveType.BOOLEAN);
        assertEquals(expected, res);
    }
}