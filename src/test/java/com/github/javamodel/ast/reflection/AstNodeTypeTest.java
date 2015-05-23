package com.github.javamodel.ast.reflection;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.ast.AstNode;
import com.github.javamodel.ast.common.PrimitiveType;
import com.github.javamodel.ast.common.PrimitiveTypeRef;
import com.github.javamodel.parsing.NodeTree;
import com.github.javamodel.parsing.ParserCli;
import org.easymock.EasyMockSupport;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AstNodeTypeTest extends EasyMockSupport {


    @Test
    public void antlrToAstRelationValueForBoolean(){
        AstNode parent = null;
        Java8Parser.UnannPrimitiveTypeContext unannPrimitiveTypeContext = createMock(Java8Parser.UnannPrimitiveTypeContext.class);
        assertFalse(AstNodeType.hasCorrespondingTypeEnum(unannPrimitiveTypeContext.getClass()));
        Object res = AstNodeType.antlrToAstRelationValue(unannPrimitiveTypeContext, parent);
        PrimitiveTypeRef expected = new PrimitiveTypeRef(null);
        expected.setPrimitiveType(PrimitiveType.BOOL);
        assertEquals(expected, res);
    }
}