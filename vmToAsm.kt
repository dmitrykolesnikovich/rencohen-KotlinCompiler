import java.io.File

var asmFileName=""
fun counterVM(path: String?) :Int
{
    var counter=0
    File(path).walk().forEach {
        if (isVM(it.name)!="")
            counter++
    }
    return counter
}

fun addInit()
{
    asmCommand+="@256\nD=A\n@SP\nM=D\n"
    handleCall("Sys.init", 0)
}
fun ex2(path:String?) {
//    println("enter a path")
//    val path = readLine()
    //creat a new file
    var listPath = path?.split('\\')
    val length = listPath?.size
    asmFileName = path + "\\" + (length?.let { listPath?.get(it.minus(1)) }) + ".asm"
    var asmFile = File(asmFileName)
    val isNewFileCreated: Boolean = asmFile.createNewFile()

    if (isNewFileCreated) {
        println("$asmFileName is created successfully.")
    }
    else {
        asmFile.delete()
        println("$asmFileName is deleted successfully.")
        asmFile.createNewFile()
        println("$asmFileName is created successfully.")
    }
    var name:String
    //write to asssembly file
    if (counterVM(path)>1)
    {
        addInit()
    }
    File(path).walk().forEach {
        name = isVM(it.name)
        if (name != "") {
            it.readLines().forEach() { line ->
                var line1=line.replace('\t',' ')
                var linelist = line1.split(" ")
                //ignore comments
                if (linelist[0].startsWith("//"))
                    return@forEach//like 'continue'

                when(linelist[0])
                {
                    "push"->handlePush(linelist.subList(1,3),name)//send the 2 last parameters as list
                    "pop"->handlePop(linelist.subList(1,3),name)
                    "add"->handleAdd()
                    "sub"->handleSub()
                    "neg"->handleNeg()
                    "eq"->handleEq()
                    "gt"->handleGt()
                    "lt"->handleLt()
                    "and"->handleAnd()
                    "or"->handleOr()
                    "not"->handleNot()
                    "label"->handleLabel(linelist[1],name)
                    "goto"->handleGoto(linelist[1],name)
                    "if-goto"->handleIfGoto(linelist[1],name)
                    "call"->handleCall(linelist[1], linelist[2].toInt())
                    "function"->handleFunction(linelist[1], linelist[2].toInt(),name)
                    "return"->handleReturn()
                }
            }


        }
    }
}