class SymbolTable {
    var nameClass:String
    var table=mutableListOf<TupleSymbol>()
    var fieldCounter=0
    var staticCounter=0
    var argCounter=0
    var varCounter=0

    constructor(name:String)
    {
        nameClass=name
    }
    //reset the table
    fun startSubroutin()
    {
        table.clear()
        fieldCounter=0
        staticCounter=0
        argCounter=0
        varCounter=0

    }

    //add id to the table
    fun define(name:String, type:String, kind:segment) {
        var symbolLine: TupleSymbol
        when (kind) {
            segment.STATIC -> symbolLine = TupleSymbol(name, type, kind, staticCounter++)
            segment.FIELD -> symbolLine = TupleSymbol(name, type, kind, fieldCounter++)
            segment.ARGUMENT -> symbolLine = TupleSymbol(name, type, kind, argCounter++)
            segment.VAR -> symbolLine = TupleSymbol(name, type, kind, varCounter++)
        }
        table.add(symbolLine)
    }
    fun printTable()
    {
        table.forEach(){
            print("name: "+it.name)
            print(", type: "+it.type)
            print(", kind: "+it.kind)
            println(", serial: "+it.serial)
        }

    }

    fun getSymboleTuple(symbolName:String): TupleSymbol? {
        table.forEach(){
            if (it.name==symbolName)
                 return it
        }
        return null
    }

    fun varCount(kind:segment) :Int
    {
        var counter=0
        table.forEach(){
            if (it.kind==kind)
                counter++
        }
        return counter
    }

    fun isExist(symbolName: String):Boolean
    {
        table.asReversed().forEach(){
            if(it.name==symbolName)
                return true
        }
        return false
    }



}