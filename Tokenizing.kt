import java.io.File
class Tokenizing {
    enum class LexicalElement{
        keyword, symbol, integerConstant, stringConstant, identifier
    }
    lateinit var XMLFile: File
    var textFile:String=""
    var tokens= mutableListOf<String>()
    var keywordRegex=("class |constructor |function |method |field |static " +
            "|var |int |char |boolean |void |true|false|null|this|let "+
            "|do |if|else|while|return|procedure ").toRegex()
    var symbolRegex="\\{|\\}|\\(|\\)|\\[|\\]|\\.|\\,|\\;|\\+|\\-|\\*|\\/|\\&|\\||\\>|\\<|\\=|\\~|\\$".toRegex()
    var intConstantRegex="[\\d]+".toRegex()
    var stringConstantRegex="\".*\"".toRegex()
    var identifierRegex="[a-zA-Z_][\\w]*".toRegex()
    var generalRegex=(keywordRegex.toString()+"|"+symbolRegex.toString()+"|"+identifierRegex.toString()+
            "|"+intConstantRegex.toString()+"|"+ stringConstantRegex.toString()).toRegex()

    //constructor
    constructor(jackFile: File)
    {
        createXMLFile(jackFile)
        textFile=jackFile.readText()
        removeCommands()
        val match=generalRegex.findAll(textFile)
        match.forEach {
            //for the string constant
            if(stringConstantRegex.containsMatchIn(it.value)) {
                var stringConstant=it.value.replace("\"","")
                tokens.add(stringConstant)
                writeToXML(stringConstant, LexicalElement.stringConstant)
            }
            //for the keywords
            else if(keywordRegex.containsMatchIn(it.value)) {
                tokens.add(it.value)
                writeToXML(it.value, LexicalElement.keyword)
            }
            //for the id
            else if(identifierRegex.containsMatchIn(it.value)) {
                tokens.add(it.value)
                writeToXML(it.value, LexicalElement.identifier)
            }
            //for the int constant
            else if(intConstantRegex.containsMatchIn(it.value)) {
                tokens.add(it.value)
                writeToXML(it.value, LexicalElement.integerConstant)
            }
            //for the symbols
            else if(symbolRegex.containsMatchIn(it.value)) {
                var symbol=it.value
                if (it.value==">")
                    symbol="&gt;"
                else if (it.value=="<")
                    symbol="&lt;"
                else if (it.value=="\"")
                    symbol="&quet;"
                else if (it.value=="&")
                    symbol="&amp;"

                tokens.add(symbol)
                writeToXML(symbol, LexicalElement.symbol)
            }

        }
        XMLFile.appendText("</tokens>\n")

    }

    fun createXMLFile(jackFile: File)
    {
        var pathDir=jackFile.path.removeSuffix(jackFile.name)
        var nameFile=jackFile.name.removeSuffix(".jack")
        xmlFileName = pathDir+"My" +nameFile + "T.xml"
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
        XMLFile.appendText("<tokens>\n")
    }
    fun writeToXML(value: String, type: LexicalElement )
    {
        var typeTxt=type.name
        var XMLTxt="<"+typeTxt+"> "+value+" </"+typeTxt+">\n"
        XMLFile.appendText(XMLTxt)
    }
    fun removeCommands()
    {
        var regexCommand="\\/\\*\\*((\\s?|.?)*)\\*\\/\\n*|\\/\\/.*\\n*".toRegex()
        val match=regexCommand.findAll(textFile)
        match.forEach {
            textFile=textFile.replace(it.value,"")
        }
      }
}
