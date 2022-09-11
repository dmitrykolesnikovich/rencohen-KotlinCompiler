import java.io.File

var asmFileName1:String=""

fun isVM(name: String ): String{
    val type=name.split(".")
    if (type.size<=1)
        return ""
    if (type[1]=="vm")
        return type[0]
    else
        return ""

}

fun HandleBuy(productName:String,amount: Int, price: Double){
    var result=amount*price
    var text="### BUY "+productName+" ###\n"+result+"\n"
    File(asmFileName1).appendText(text)
}

fun HandleCell(productName:String,amount: Int, price: Double){
    var result=amount*price
    var text="$$$ CELL "+productName+" $$$\n"+result+"\n"
    File(asmFileName1).appendText(text)

}

fun main() {
    println("enter a path")
    val path=readLine()
    //creat a new file
    var listPath= path?.split('\\')
    val length= listPath?.size
    asmFileName1 = path+"\\"+(length?.let { listPath?.get(it.minus(1)) }) +".asm"
    var file=File(asmFileName1)
    val isNewFileCreated :Boolean = file.createNewFile()

    if(isNewFileCreated){
        println("$asmFileName1 is created successfully.")
    } else{
        println("$asmFileName1 already exists.")
    }

    var sumBuys=0.0
    var sumSells=0.0
    //treatment files
    File(path).walk().forEach {
        val name=isVM(it.name)
        if (name!=""){
            file.appendText(name+"\n")
            it.readLines().forEach(){line->
                var linelist=line.split(" ")
                if (linelist.size==4) {
                    if (linelist[0] == "buy") {
                        HandleBuy(linelist[1], linelist[2].toInt(), linelist[3].toDouble())
                        sumBuys+=linelist[2].toInt()*linelist[3].toDouble()
                    } else if (linelist[0] == "cell") {
                        HandleCell(linelist[1], linelist[2].toInt(), linelist[3].toDouble())
                        sumSells+=linelist[2].toInt()*linelist[3].toDouble()
                    }
                }

            }

        }


    }
    file.appendText(sumBuys.toString()+"\n"+sumSells.toString())
    println(sumBuys)
    println(sumSells)


}
