// Generated class

import com.github.javamodel.Java8Parser;

public enum ${name?cap_first} {

<#list values as value>
    ${value.name?upper_case}("${value.literal}")<#if value_has_next>,<#else>;</#if>
</#list>

   private String literal;

   private ${name?cap_first}(String literal){
       this.literal = literal;
   }

   public static ${name?cap_first} fromAntlrNode(Java8Parser.${antlrNodeClass?cap_first} antlrNode){
        for (${name?cap_first} value : values()){
            if (value.literal.equals(antlrNode.getText())){
                return value;
            }
        }
        throw new IllegalArgumentException();
    }
}