var numFunCall:Int=0
fun pushSavePointer()
{
    asmCommand+="D=M\n@SP\nA=M\nM=D\n"
    pushPost()
}

fun returnSegment(segmentName: String)
{
    asmCommand+="@LCL\nM=M-1\nA=M\nD=M\n@"+segmentName+"\nM=D\n"
}

fun handleCall(funName: String, numARG: Int)
{
    asmCommand+="//call\n"
    //push return-address
    asmCommand+="@"+funName+numFunCall.toString()+".ReturnAddress\nD=A\n@SP\nA=M\nM=D\n"
    pushPost()
    //push the save pointer-local,argumant that and this
    asmCommand+="@LCL\n"
    pushSavePointer()
    asmCommand+="@ARG\n"
    pushSavePointer()
    asmCommand+="@THIS\n"
    pushSavePointer()
    asmCommand+="@THAT\n"
    pushSavePointer()
    //ARG=sp-n-5
    asmCommand+="@SP\nD=M\n@"+numARG+"\nD=D-A\n@5\nD=D-A\n@ARG\nM=D\n"
    //LCL=SP
    asmCommand+="@SP\nD=M\n@LCL\nM=D\n"
    //go to function
    asmCommand+="@"+funName+"\n0;JMP\n"
    //label return address
    asmCommand+="("+funName+numFunCall.toString()+".ReturnAddress)\n"
    numFunCall++
    writeReset()
}

fun handleFunction(funName: String, numLCL: Int, fileName: String)
{
    asmCommand+="//function\n"
    //label for the function
    asmCommand+="("+funName+")\n"
    //initialized the local variable
    for (i in 0 until numLCL)
        handlePush(listOf("constant","0"),fileName)

    //asmCommand+="@"+numLCL+"\nD=A\n@SP\nM=D+M\n"
    writeReset()
}

fun handleReturn()
{
    asmCommand+="//return\n"
    // FRAME = LCL
    asmCommand+="@LCL\nD=M\n"
     // RET = * (FRAME-5)
    // RAM[5] = (LOCAL - 5) (temp=>5-12)
    asmCommand+="@5\nA=D-A\nD=M\n@13\nM=D\n"
    //// pop() * ARG->put the return value that is in the head of the stack in the ARG place
    asmCommand+="@SP\nM=M-1\nA=M\nD=M\n@ARG\nA=M\nM=D\n"
    //SP=ARG+1
    asmCommand+="@ARG\nD=M\n@SP\nM=D+1\n"
    //return the pointer for the segments of the caller function
    returnSegment("THAT")
    returnSegment("THIS")
    returnSegment("ARG")
    returnSegment("LCL")
    //goto RET
    asmCommand+="@13\nA=M\n0;JMP\n"
    writeReset()
}



