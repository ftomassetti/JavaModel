// Generated class

public class ${name?cap_first} extends AstNode {

<#list fields as field>

    <#if field.multiple>
    private List<${field.type?cap_first}> ${field.name} = new ArrayList<>();
    <#else>
    private ${field.type?cap_first} ${field.name};
    </#if>

</#list>
}