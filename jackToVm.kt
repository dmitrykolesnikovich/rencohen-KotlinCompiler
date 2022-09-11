import java.io.File

val operatorsList:List<String> = listOf("+","-","*","/","&amp;", "|","&lt;","&gt;","=","$")
val keyWordList:List<String> = listOf("true","false","null","this")
val unaryOpList:List<String> = listOf("-","~")
var xmlFileName=""
lateinit var VMfile:File
val classTableLst= mutableListOf<SymbolTable>()


fun isJack(name: String ): String{
    val type=name.split(".")
    if (type.size<=1)
        return ""
    if (type[1]=="jack")
        return type[0]
    else
        return ""
}

fun createVM(nameFile:String, path: String?){
    VMfile=File(path+"\\"+nameFile+".vm")
    val isNewFileCreated: Boolean = VMfile.createNewFile()

    if (isNewFileCreated) {
        println("$nameFile is created successfully.")
    }
    else {
        VMfile.delete()
        println("$xmlFileName is deleted successfully.")
        VMfile.createNewFile()
        println("$xmlFileName is created successfully.")
    }
}

fun getClassTable(nameClass:String):SymbolTable?{
    classTableLst.forEach() {
        if (it.nameClass==nameClass)
            return it
    }
    return null
}

fun ex4() {
    println("enter a path")
    val path = readLine()
    //create a new file
    val listPath = path?.split('\\')
    val length = listPath?.size

    var name: String
    File(path).walk().forEach {
        name = isJack(it.name)
        if (name != "") {
            createVM(name, path)
            //cast the jack file to xml file
            val tokenizing = Tokenizing(it)
            val parsing=Parsing(tokenizing.tokens, tokenizing.XMLFile)
            classTableLst.add(parsing.classTable)
        }
    }

    ex2(path)
    getClassTable("SnakeGame")?.printTable()


}