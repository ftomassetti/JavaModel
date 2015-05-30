package com.github.javamodel;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by ftomassetti on 29/05/15.
 */
public class NodeTreeTest {
    
    private String toTree(String code){
        Node root = new ParserCli().parse(code);
        StringBuffer stringBuffer = new StringBuffer();
        ParserCli.printTree(root, 0, stringBuffer);
        return stringBuffer.toString().trim();
    }
    
    @Test
    public void testFieldDeclarations(){
        assertEquals("<ROOT> CompilationUnit{}\n" +
                "  topTypes: ClassDeclaration{modifiers=[], name='A'}\n" +
                "    fields: FieldDeclaration{}\n" +
                "      type: PrimitiveTypeRef{INT}\n" +
                "      variableDeclarations: VariableDeclaration{name='a'}\n" +
                "      variableDeclarations: VariableDeclaration{name='b'}\n" +
                "      variableDeclarations: VariableDeclaration{name='c'}\n" +
                "    internalTypes: ClassDeclaration{modifiers=[], name='B'}\n" +
                "      internalTypes: ClassDeclaration{modifiers=[], name='C'}", toTree("class A { int a,b,c; class B { class C {} } }"));
        
    }

    @Test
    public void testInternalClasses(){
        assertEquals("<ROOT> CompilationUnit{}\n" +
                "  topTypes: ClassDeclaration{modifiers=[], name='A'}\n" +
                "    internalTypes: ClassDeclaration{modifiers=[], name='B'}\n" +
                "      internalTypes: ClassDeclaration{modifiers=[], name='C'}", toTree("class A { class B { class C {} }}"));

    }

    @Test
    public void testAddition(){
        assertEquals("<ROOT> CompilationUnit{}\n" +
                "  topTypes: ClassDeclaration{modifiers=[], name='A'}\n" +
                "    fields: FieldDeclaration{}\n" +
                "      type: PrimitiveTypeRef{INT}\n" +
                "      variableDeclarations: VariableDeclaration{name='a'}\n" +
                "        initializer: ExpressionVariableInitializater{}\n" +
                "          expression: BinaryExpression{operator=PLUS}\n" +
                "            leftOperand: IntegerLiteralExpression{text='1'}\n" +
                "            rightOperand: IntegerLiteralExpression{text='10'}", toTree("class A { int a = 1 + 10; }"));

    }

    @Test
    public void testMultiplication(){
        assertEquals("<ROOT> CompilationUnit{}\n" +
                "  topTypes: ClassDeclaration{modifiers=[], name='A'}\n" +
                "    fields: FieldDeclaration{}\n" +
                "      type: PrimitiveTypeRef{INT}\n" +
                "      variableDeclarations: VariableDeclaration{name='a'}\n" +
                "        initializer: ExpressionVariableInitializater{}\n" +
                "          expression: BinaryExpression{operator=MULT}\n" +
                "            leftOperand: IntegerLiteralExpression{text='1'}\n" +
                "            rightOperand: IntegerLiteralExpression{text='10'}", toTree("class A { int a = 1 * 10; }"));

    }

    @Test
    public void testNameReference(){
        assertEquals("<ROOT> CompilationUnit{}\n" +
                "  topTypes: ClassDeclaration{modifiers=[], name='A'}\n" +
                "    fields: FieldDeclaration{}\n" +
                "      type: PrimitiveTypeRef{INT}\n" +
                "      variableDeclarations: VariableDeclaration{name='a'}\n" +
                "        initializer: ExpressionVariableInitializater{}\n" +
                "          expression: NameReferenceExpression{name='b'}", toTree("class A { int a = b; }"));

    }

    @Test
    public void testSimplestClassMethod(){
        assertEquals("<ROOT> CompilationUnit{}\n" +
                "  topTypes: ClassDeclaration{modifiers=[], name='A'}\n" +
                "    methods: MethodDeclaration{name='foo', modifiers=[]}\n" +
                "      returnType: VoidTypeRef{}\n" +
                "      block: BlockStatement{}", toTree("class A { void foo(){} }"));

    }

    @Test
    public void testPrimitiveArray(){
        assertEquals("<ROOT> CompilationUnit{}\n" +
                "  topTypes: ClassDeclaration{modifiers=[], name='A'}\n" +
                "    fields: FieldDeclaration{}\n" +
                "      type: ArrayTypeRef{}\n" +
                "        baseType: ArrayTypeRef{}\n" +
                "          baseType: ArrayTypeRef{}\n" +
                "            baseType: PrimitiveTypeRef{INT}\n" +
                "      variableDeclarations: VariableDeclaration{name='i'}", toTree("class A { int[][][] i; }"));

    }

    @Test
    public void testClassExtension(){
        assertEquals("<ROOT> CompilationUnit{}\n" +
                "  topTypes: ClassDeclaration{modifiers=[PUBLIC], name='BinaryExpression'}\n" +
                "    superclass: DeclaredTypeRef{name='Expression'}", toTree("public class BinaryExpression extends Expression { }"));

    }
    @Test
    public void testClassImplementation(){
        assertEquals("<ROOT> CompilationUnit{}\n" +
                "  topTypes: ClassDeclaration{modifiers=[PUBLIC], name='BinaryExpression'}\n" +
                "    interfaces: DeclaredTypeRef{name='Iterable'}\n" +
                "      typeArguments: DeclaredTypeRef{name='Object'}", toTree("public class BinaryExpression implements Iterable<Object> { }"));

    }

}
