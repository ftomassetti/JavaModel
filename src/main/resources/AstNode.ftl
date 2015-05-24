// Generated class

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;
import java.util.ArrayList;
<#list imports as import>
import ${import};
</#list>

public class ${name?cap_first} extends Node {

<#list fields as field>

    <#if field.multiple>
    private List<${field.type?cap_first}> ${field.name} = new ArrayList<>();
    <#else>
    private ${field.type?cap_first} ${field.name};
    </#if>

</#list>

   public static ${name?cap_first} fromAntlrNode(Java8Parser.${antlrNodeClass?cap_first} antlrNode){

   }
}