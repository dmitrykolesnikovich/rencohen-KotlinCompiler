import java.io.File
var whileLabel=0
var ifLabel=0

class Parsing {
    var className:String
    lateinit var XMLFile: File
    var tokensList= mutableListOf<String>()
    var root=TreeNode("class")
    var tagTokens:MutableList<String>
    var classTable: SymbolTable
    var methodTable=SymbolTable("Method")
    var pointer=0


    constructor(myTokens: MutableList<String>, tokensXMLFile: File)
    {
        className=tokensXMLFile.name.removeSuffix("T.xml").removePrefix("My")
        classTable=SymbolTable(className)
        tokensList=myTokens
        tagTokens=tokensXMLFile.readLines().toMutableList()
        tagTokens.removeFirst()
        tagTokens.removeLast()
        myClass(root)
        createXMLFile(tokensXMLFile)
        writeToXML(root,"")

    }


    fun printToknes(){
        tagTokens.forEach{
            println(it)
        }

    }

    fun createXMLFile(tokenizingFile: File) {

        var pathDir=tokenizingFile.path.removeSuffix(tokenizingFile.name)
        var nameFile=tokenizingFile.name.removeSuffix("T.xml")
        xmlFileName = pathDir+nameFile + ".xml"
        XMLFile = File(xmlFileName)
        val isNewFileCreated: Boolean = XMLFile.createNewFile()

        if (isNewFileCreated) {
            println("$xmlFileName is created successfully.")
        }
        else {
            XMLFile.delete()
            println("$xmlFileName is deleted successfully.")
            XMLFile.createNewFile()
            println("$xmlFileName is created successfully.")
        }
    }

    //=============================program structor===================================
    fun myClass(currentNode: TreeNode<String>) {
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //class
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //id
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //{
        while(tokensList[pointer]=="static " || tokensList[pointer]=="field ") {
            currentNode.addChild(TreeNode("classVarDec"))
            classVarDec(currentNode.children.last())
        }
        while(tokensList[pointer]=="constructor " || tokensList[pointer]=="function "
            || tokensList[pointer]=="method "||tokensList[pointer]=="procedure " ){
            currentNode.addChild(TreeNode("subroutineDec"))
            subroutineDec(currentNode.children.last())
        }
        currentNode.addChild(TreeNode(tagTokens[pointer++])) //}

    }

    private fun subroutineDec(currentNode: TreeNode<String>) {
        methodTable.startSubroutin()        //delete the table
        val typeFun=tokensList[pointer]
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //constructor/functin/method/procedure
        if(tokensList[pointer]=="void ")
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //void
        else{
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //id/int/char/bool
        }
        val nameFunc=tokensList[pointer]
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //id
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //(
        currentNode.addChild(TreeNode("parameterList"))
        parameterList(currentNode.children.last())
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //)
        currentNode.addChild(TreeNode("subroutineBody"))
        subroutineBody(currentNode.children.last(),typeFun,nameFunc)


    }

    private fun subroutineBody(currentNode: TreeNode<String>, typeFun:String, nameFunc:String) {
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //{
        while(tokensList[pointer]=="var ") {
            currentNode.addChild(TreeNode("varDec"))
            varDec(currentNode.children.last())
        }
        val countLcl=methodTable.varCount(segment.VAR)
        codeWriteFunctionGeneral(className+"."+nameFunc,countLcl)
        when(typeFun)
        {
            "constructor "->{
                val count=classTable.varCount(segment.FIELD)
                codeWriteConstructorDec(count)
            }
            "method "->{
                methodTable.define("this",className,segment.ARGUMENT)
                codeWriteMethodDec()
            }
        }
        currentNode.addChild(TreeNode("statements"))
        statements(currentNode.children.last())
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //}
    }

    private fun varDec(currentNode: TreeNode<String>) {
        var kind=segment.VAR
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //var
        var type=tokensList[pointer]
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //id/int/char/
        var name=tokensList[pointer]
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //id
        methodTable.define(name,type,kind)
        while(tokensList[pointer]==",") {
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //,
            name=tokensList[pointer]
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //id
            methodTable.define(name,type,kind)
        }
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //;
    }

    private fun parameterList(currentNode: TreeNode<String>) {
        val kind=segment.ARGUMENT
        if (tokensList[pointer]==")")
            return
        var type=tokensList[pointer]
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //id/int/char/bool
        var name=tokensList[pointer]
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //id
        methodTable.define(name,type,kind)
        while(tokensList[pointer]==",") {
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //,
            type=tokensList[pointer]
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //id/int/char/bool
            name=tokensList[pointer]
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //id
            methodTable.define(name,type,kind)
        }
    }

    private fun classVarDec(currentNode: TreeNode<String>) {
        lateinit var kind: segment
        when(tokensList[pointer])
        {
            "static "->kind=segment.STATIC
            "field "->kind=segment.FIELD
        }
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //static/field
        var type=tokensList[pointer]
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //id/int/char/bool
        var name=tokensList[pointer]
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //id
        classTable.define(name,type,kind)       //add to table
        while(tokensList[pointer]==",") {
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //,
            name=tokensList[pointer]
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //id
            classTable.define(name,type,kind)
        }
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //;
    }

    private fun statements(currentNode: TreeNode<String>) {
        while(tokensList[pointer]=="let " ||tokensList[pointer]=="if"
            ||tokensList[pointer]=="while" ||
            tokensList[pointer]=="do " ||tokensList[pointer]=="do"
            ||tokensList[pointer]=="return") {
            if (tokensList[pointer] == "let ") {
                currentNode.addChild(TreeNode("letStatement"))
                letStatement(currentNode.children.last())
            } else if (tokensList[pointer] == "if") {
                currentNode.addChild(TreeNode("ifStatement"))
                ifStatement(currentNode.children.last())
            } else if (tokensList[pointer] == "while") {
                currentNode.addChild(TreeNode("whileStatement"))
                whileStatement(currentNode.children.last())
            }else if (tokensList[pointer] == "do"|| tokensList[pointer] == "do ") {
                if (tokensList[pointer+1]=="{") {  //do while
                    currentNode.addChild(TreeNode("doWhileStatement"))
                    doWhileStatement(currentNode.children.last())
                }
                else{ //regular do
                    currentNode.addChild(TreeNode("doStatement"))
                    doStatement(currentNode.children.last())
                }
            } else if (tokensList[pointer] == "return") {
                currentNode.addChild(TreeNode("returnStatement"))
                returnStatement(currentNode.children.last())
            }
        }

    }

    private fun doWhileStatement(currentNode: TreeNode<String>) {
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //do
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //{
        currentNode.addChild(TreeNode("statements"))
        statements(currentNode.children.last())
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //}
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //while
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //(
        currentNode.addChild(TreeNode("expression"))
        expression(currentNode.children.last())
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //)
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //;
    }

    private fun returnStatement(currentNode: TreeNode<String>) {
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //return
        if(tokensList[pointer] == ";")
        {
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //;
            codeWriteReturn(0)
            return
        }

        currentNode.addChild(TreeNode("expression"))
        expression(currentNode.children.last())
        codeWriteReturn(1)
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //;
    }

    private fun doStatement(currentNode: TreeNode<String>) {
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //do
        subroutineCall(currentNode)
        codeWriteDo()
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //;

    }

    private fun whileStatement(currentNode: TreeNode<String>) {
        var numLabel=whileLabel++
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //while
        codeWriteWhile(1,numLabel)
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //(
        currentNode.addChild(TreeNode("expression"))
        expression(currentNode.children.last())
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //)
        codeWriteWhile(2,numLabel)
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //{
        currentNode.addChild(TreeNode("statements"))
        statements(currentNode.children.last())
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //}
        codeWriteWhile(3,numLabel)

    }

    private fun ifStatement(currentNode: TreeNode<String>) {
        var numLabel=ifLabel++
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //if
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //(
        currentNode.addChild(TreeNode("expression"))
        expression(currentNode.children.last())
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //)
        codeWriteIf(1, numLabel)
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //{
        currentNode.addChild(TreeNode("statements"))
        statements(currentNode.children.last())
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //}
        codeWriteIf(2,numLabel)
        if(tokensList[pointer] == "else"){
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //else
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //{
            currentNode.addChild(TreeNode("statements"))
            statements(currentNode.children.last())
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //}
        }
        codeWriteIf(3,numLabel)
    }

    private fun letStatement(currentNode: TreeNode<String>) {
        currentNode.addChild(TreeNode(tagTokens[pointer++]))      //let
        var tupleSymbol=methodTable.getSymboleTuple(tokensList[pointer])
        if (tupleSymbol==null)
            tupleSymbol=classTable.getSymboleTuple(tokensList[pointer])
        currentNode.addChild(TreeNode(tagTokens[pointer++]))        //id
        if (tokensList[pointer] == "[")
        {
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //[
            currentNode.addChild(TreeNode("expression"))
            expression(currentNode.children.last())
            codeWriteLetArr(tupleSymbol,1)
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //]
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //=
            currentNode.addChild(TreeNode("expression"))
            expression(currentNode.children.last())
            codeWriteLetArr(tupleSymbol,2)
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //;
        }
        else
        {
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //=
            currentNode.addChild(TreeNode("expression"))
            expression(currentNode.children.last())
            codeWriteLet(tupleSymbol)
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //;
        }

    }

    //========================expression============================
    private fun expression(currentNode: TreeNode<String>) {
        currentNode.addChild(TreeNode("term"))
        term(currentNode.children.last())
        while(tokensList[pointer] in operatorsList) {
            val op=tokensList[pointer]
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //+|=|-|*|/|&|||<|>|$
            currentNode.addChild(TreeNode("term"))
            term(currentNode.children.last())
            codeWriteExpression(op)
        }

    }

    private fun term(currentNode: TreeNode<String>) {
        if(tagTokens[pointer].startsWith("<stringConstant>")){
                codeWriteStr(tokensList[pointer])
                currentNode.addChild(TreeNode(tagTokens[pointer++]))        //string

            }
        else if(tagTokens[pointer].startsWith("<integerConstant>")){
            codeWriteInt(Integer.parseInt(tokensList[pointer]))
            currentNode.addChild(TreeNode(tagTokens[pointer++]))        //int
        }



        else if(tokensList[pointer] in keyWordList) {
            codeWriteKeyWord(tokensList[pointer])
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //true/false/null/this
        }

        else if(tokensList[pointer] in unaryOpList){
            val op=tokensList[pointer]
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //-/~
            currentNode.addChild(TreeNode("term"))
            term(currentNode.children.last())
            codeWriteUnaryOp(op)
        }

        else if(tokensList[pointer+1] == "[")
        {
            var arrayName=tokensList[pointer]
            var tupleSymbol=methodTable.getSymboleTuple(arrayName)
            if (tupleSymbol==null)
                tupleSymbol=classTable.getSymboleTuple(tokensList[pointer])
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //id
            currentNode.addChild(TreeNode(tagTokens[pointer++]))         //[
            currentNode.addChild(TreeNode("expression"))
            expression(currentNode.children.last())
            codeWriteArray(tupleSymbol)
            currentNode.addChild(TreeNode(tagTokens[pointer++]))         //]

        }
        else if(tokensList[pointer] == "(")
        {
            currentNode.addChild(TreeNode(tagTokens[pointer++]))         //(
            currentNode.addChild(TreeNode("expression"))
            expression(currentNode.children.last())
            currentNode.addChild(TreeNode(tagTokens[pointer++]))         //)
        }
        else if(tokensList[pointer+1] == "("||tokensList[pointer+1] == ".")
        {
            subroutineCall(currentNode)
        }
        else{
            var id=tokensList[pointer]
            var tupleSymbol=methodTable.getSymboleTuple(id)
            if (tupleSymbol==null)
                tupleSymbol=classTable.getSymboleTuple(tokensList[pointer])
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //id
            codeWriteID(tupleSymbol)
        }


    }

    private fun subroutineCall(currentNode: TreeNode<String>) {
        var numArg=0
        var nameFunc=""
        var tupleSymbol:TupleSymbol?
        if(tokensList[pointer+1]=="(") {
            //current class method
            codeWriteCurrentClassMethodCall()
            nameFunc=className+"."+tokensList[pointer]
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //id
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //(
            currentNode.addChild(TreeNode("expressionList"))
            numArg=expressionList(currentNode.children.last())+1
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //)
        }
        else if((tokensList[pointer+1]==".")){
            //other class method
            if(methodTable.isExist(tokensList[pointer])){
                numArg=1
                tupleSymbol=methodTable.getSymboleTuple(tokensList[pointer])
                if (tupleSymbol==null)
                    tupleSymbol=classTable.getSymboleTuple(tokensList[pointer])
                codeWriteOtherClassMethodCall(tupleSymbol)
                nameFunc=tupleSymbol?.type+"."+tokensList[pointer+2]
            }
            else if(classTable.isExist(tokensList[pointer])) {
                numArg=1
                tupleSymbol=classTable.getSymboleTuple(tokensList[pointer])
                if (tupleSymbol==null)
                    tupleSymbol=classTable.getSymboleTuple(tokensList[pointer])
                codeWriteOtherClassMethodCall(tupleSymbol)
                nameFunc=tupleSymbol?.type+"."+tokensList[pointer+2]

            }
            else if(tokensList[pointer]==className)
                //current class function
                nameFunc=className+"."+tokensList[pointer+2]
            else
                //other class function
                nameFunc=tokensList[pointer]+"."+tokensList[pointer+2]

            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //id
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //.
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //id
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //(
            currentNode.addChild(TreeNode("expressionList"))
            numArg=numArg+expressionList(currentNode.children.last())
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //)

        }
        codeWriteFuncCallGeneral(nameFunc,numArg)
    }

    private fun expressionList(currentNode: TreeNode<String>) :Int{
        var counter=0
        if(tokensList[pointer]==")")
            return counter
        currentNode.addChild(TreeNode("expression"))
        expression(currentNode.children.last())
        counter++
        while(tokensList[pointer]==",") {
            currentNode.addChild(TreeNode(tagTokens[pointer++]))      //,
            currentNode.addChild(TreeNode("expression"))
            expression(currentNode.children.last())
            counter++
        }
        return counter
    }


    //Goes through the whole tree and writes to the file
    private fun writeToXML(currentNode: TreeNode<String>, spaces:String)
    {
        if(currentNode.value.startsWith("<")) {
            XMLFile.appendText(spaces + currentNode.value+"\n")
        }
        else {
            var parsingLine = spaces + "<" + currentNode.value + ">\n"
            XMLFile.appendText(parsingLine)
            currentNode.children.forEach() {
                writeToXML(it, spaces + "  ")
            }
            parsingLine = spaces + "</" + currentNode.value + ">\n"
            XMLFile.appendText(parsingLine)
        }


    }
}