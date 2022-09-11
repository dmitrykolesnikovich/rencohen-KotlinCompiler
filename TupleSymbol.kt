enum class segment{STATIC, FIELD, ARGUMENT, VAR}
class TupleSymbol {
    var name:String
    var type:String
    var kind:segment
    var serial: Int

    constructor(myName:String, myType:String, myKind: segment, mySerial:Int ) {
        name=myName
        type=myType
        kind=myKind
        serial=mySerial
    }

    fun getSegment():String{
        when(this.kind)
        {
            segment.VAR->return "local"
            segment.FIELD->return "this"
            segment.STATIC->return "static"
            segment.ARGUMENT->return "argument"
        }
    }
}