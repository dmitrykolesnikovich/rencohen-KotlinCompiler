
var asmCommand:String=""
var numTrue:Int=0
var numFalse:Int=0

//take out the last two values in the stack for binary operations
fun binaryOperationPre()
{
    asmCommand+="@SP\nA=M-1\nD=M\nA=A-1\n"
}

//update the stack pointer to one down
fun binaryOperationPost()
{
    asmCommand+="@SP\nM=M-1\n"
}

//take out the last value in the stack for binary operations
fun unaryOperationPre()
{
    asmCommand+="@SP\nA=M-1\n"
}


fun handleAdd()
{
    asmCommand+="//add\n"
    binaryOperationPre()
    asmCommand+="M=D+M\n"
    binaryOperationPost()
    writeReset()
}

fun handleSub()
{
    asmCommand+="//sub\n"
    binaryOperationPre()
    asmCommand+="M=M-D\n"
    binaryOperationPost()
    writeReset()
}

fun handleNeg()
{
    asmCommand+="//neg\n"
    unaryOperationPre()
    asmCommand+="M=-M\n"
    writeReset()
}

fun handleEq()
{
    asmCommand+="//eq\n"
    binaryOperationPre()
    asmCommand+="D=D-M\n@IF_TRUE"+numTrue.toString()+"\nD;JEQ\n" +
            "D=0\n@SP\nA=M-1\nA=A-1\nM=D\n@IF_FALSE"+numFalse.toString()+"\n" +
            "0;JMP\n(IF_TRUE"+numTrue.toString()+")\nD=-1\n@SP\n" +
            "A=M-1\nA=A-1\nM=D\n(IF_FALSE"+numFalse.toString()+")\n"
    binaryOperationPost()
    writeReset()
    numFalse++
    numTrue++

}

fun handleGt()
{
    asmCommand+="//gt\n"
    binaryOperationPre()
    asmCommand+="D=M-D\n@IF_TRUE"+numTrue.toString()+"\nD;JGT\n" +
            "D=0\n@SP\nA=M-1\nA=A-1\nM=D\n@IF_FALSE"+numFalse.toString()+"\n" +
            "0;JMP\n(IF_TRUE"+numTrue.toString()+")\nD=-1\n@SP\n" +
            "A=M-1\nA=A-1\nM=D\n(IF_FALSE"+numFalse.toString()+")\n"
    binaryOperationPost()
    writeReset()
    numFalse++
    numTrue++

}

fun handleLt()
{
    asmCommand+="//lt\n"
    binaryOperationPre()
    asmCommand+="D=D-M\n@IF_TRUE"+numTrue.toString()+"\nD;JGT\n" +
            "D=0\n@SP\nA=M-1\nA=A-1\nM=D\n@IF_FALSE"+numFalse.toString()+"\n" +
            "0;JMP\n(IF_TRUE"+numTrue.toString()+")\nD=-1\n@SP\n" +
            "A=M-1\nA=A-1\nM=D\n(IF_FALSE"+numFalse.toString()+")\n"
    binaryOperationPost()
    writeReset()
    numFalse++
    numTrue++
}

fun handleAnd()
{
    asmCommand+="//and\n"
    binaryOperationPre()
    asmCommand+="M=D&M\n"
    binaryOperationPost()
    writeReset()
}

fun handleOr()
{
    asmCommand+="//or\n"
    binaryOperationPre()
    asmCommand+="M=D|M\n"
    binaryOperationPost()
    writeReset()
}

fun handleNot()
{
    asmCommand+="//not\n"
    unaryOperationPre()
    asmCommand+="M=!M\n"
    writeReset()
}