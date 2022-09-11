fun handleLabel(labelName: String, nameFile: String)
{
    asmCommand+="//label\n"
    asmCommand+="("+nameFile+"."+labelName+")\n"
    writeReset()
}

fun handleGoto(labelName: String, nameFile: String)
{
    asmCommand+="//goto\n"
    asmCommand+="@"+nameFile+"."+labelName+"\n0;JMP\n"
    writeReset()
}

fun handleIfGoto(labelName: String, nameFile: String)
{
    asmCommand+="//if-goto\n"
    asmCommand+="@SP\nM=M-1\nA=M\nD=M\n@"+nameFile+"."+labelName+"\nD;JNE\n"
    writeReset()
}