var codeTxt=""
fun codeWriteInt(integer: Int) {
    codeTxt="push constant "+integer+"\n"
    VMfile.appendText(codeTxt)
}

fun codeWriteStr(str: String){
    codeTxt= "push constant "+ str.length+"\n"+
            "call String.new 1\n"
    var temp :Int
    for(i in str) {
        temp = i.toInt()
        codeTxt+="push constant "+temp+"\n"+
                "call String.appendChar 2\n"
    }
    VMfile.appendText(codeTxt)
}

fun codeWriteUnaryOp(op:String){
    if (op=="-")
        codeTxt="neg\n"
    else if(op=="~")
        codeTxt="not\n"
    VMfile.appendText(codeTxt)
}

fun codeWriteKeyWord(keyWord:String){
    codeTxt=""
    if (keyWord=="true")
        codeTxt="push constant 0\n" +
                "not\n"
    else if(keyWord=="false"||keyWord=="null")
        codeTxt="push constant 0\n"
    else if(keyWord=="this")
        codeTxt="push pointer 0\n"
    VMfile.appendText(codeTxt)
}

fun codeWriteArray(tuple: TupleSymbol?) {
    codeTxt="push "+tuple?.getSegment()+" "+tuple?.serial+"\n" +
            "add\n" +
            "pop pointer 1\n" +
            "push that 0\n"
    VMfile.appendText(codeTxt)
}

fun codeWriteID(tuple: TupleSymbol?){
    codeTxt="push "+tuple?.getSegment()+" "+tuple?.serial+"\n"
    VMfile.appendText(codeTxt)
}

fun codeWriteExpression(symbol:String) {
    when(symbol)
    {
       "+"->codeTxt="add\n"
        "="->codeTxt="eq\n"
        "-"->codeTxt="sub\n"
        "*"->codeTxt="call Math.multiply 2\n"
        "/"->codeTxt="call Math.divide 2\n"
        "&amp;"->codeTxt="and\n"
        "|"->codeTxt="or\n"
        "&lt;"->codeTxt="lt\n"
        "&gt;"->codeTxt="gt\n"
    }
    VMfile.appendText(codeTxt)
}

fun codeWriteIf(level:Int,numLabel: Int){
    when(level)
    {
        1->codeTxt="if-goto IF_TRUE"+numLabel+"\n"+
                "goto IF_FALSE"+numLabel+"\n"+
                "label IF_TRUE"+numLabel+"\n"
        2->codeTxt="goto IF_END"+numLabel+"\n" +
                "label IF_FALSE"+numLabel+"\n"
        3-> {
            codeTxt = "label IF_END" + numLabel + "\n"
        }
    }
    VMfile.appendText(codeTxt)

}

fun codeWriteWhile(level:Int, numLabel:Int){
    when(level)
    {
        1->codeTxt="label WHILE_EXP"+numLabel+"\n"
        2->codeTxt="not\n" +
                "if-goto WHILE_END"+numLabel+"\n"
        3-> {
            codeTxt = "goto WHILE_EXP" + numLabel + "\n" +
                    "label WHILE_END" + numLabel + "\n"
        }

    }

    VMfile.appendText(codeTxt)
}

fun codeWriteDo() {
    codeTxt="pop temp 0\n"
    VMfile.appendText(codeTxt)
}

fun codeWriteReturn(isEmpty:Int){
    codeTxt=""
    if (isEmpty==0)
        codeTxt="push constant 0\n"
    codeTxt+="return\n"
    VMfile.appendText(codeTxt)
}

fun codeWriteLetArr(tuple:TupleSymbol?, level :Int){
    when(level)
    {
        1->codeTxt="push "+tuple?.getSegment()+" "+tuple?.serial+"\n" +
                "add\n"
        2->codeTxt="pop temp 0\n" +
                "pop pointer 1\n" +
                "push temp 0\n" +
                "pop that 0\n"
    }
    VMfile.appendText(codeTxt)
}

fun codeWriteLet(tuple: TupleSymbol?){
    codeTxt="pop "+tuple?.getSegment()+" "+tuple?.serial+"\n"
    VMfile.appendText(codeTxt)
}

fun codeWriteFunctionGeneral(nameFunc:String, numLcl:Int) {
    codeTxt="function "+nameFunc+" "+numLcl.toString()+"\n"
    VMfile.appendText(codeTxt)
}

fun codeWriteConstructorDec(varCount: Int) {
    codeTxt="push constant "+varCount+"\n" +
            "call Memory.alloc 1\n" +
            "pop pointer 0\n"
    VMfile.appendText(codeTxt)
}

fun codeWriteMethodDec() {
    codeTxt="push argument 0\n" +
            "pop pointer 0\n"
    VMfile.appendText(codeTxt)
}

fun codeWriteFuncCallGeneral(nameFunc:String, numParam: Int) {
    codeTxt="call "+nameFunc+" "+numParam.toString()+"\n"
    VMfile.appendText(codeTxt)
}

fun codeWriteCurrentClassMethodCall() {
    codeTxt="push pointer 0\n"
    VMfile.appendText(codeTxt)
}

fun codeWriteOtherClassMethodCall(tuple: TupleSymbol?) {
    codeTxt="push "+tuple?.getSegment()+" "+tuple?.serial+"\n"
    VMfile.appendText(codeTxt)
}







