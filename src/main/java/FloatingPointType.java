// Generated class

import com.github.javamodel.Java8Parser;

public enum FloatingPointType {

    FLOAT("float")
    ,
    DOUBLE("double")
    ;

    private String literal;

    private FloatingPointType(String literal){
        this.literal = literal;
    }

    public static FloatingPointType fromAntlrNode(Java8Parser.FloatingPointTypeContext antlrNode){
        for (FloatingPointType value : values()){
            if (value.literal.equals(antlrNode.getText())){
                return value;
            }
        }
        throw new IllegalArgumentException();
    }
}