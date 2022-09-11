import java.io.File

//write to file and reset the string
fun writeReset()
{
    File(asmFileName).appendText(asmCommand)
    asmCommand=""
}
//promoting the stack head
fun pushPost()
{
    asmCommand+="@SP\nM=M+1\n"
}

//update the stack head one step back
fun popPost()
{
    asmCommand+="@SP\nM=M-1\n"
}


fun handlePush(cmd:List<String>,nameFile:String)
{
    when(cmd[0])
    {
        "constant"->{
            asmCommand+="//push constant\n"
            asmCommand+="@"+cmd[1]+"\nD=A\n@SP\nA=M\nM=D\n"
            pushPost()
            writeReset()
        }
        "local"->{
            asmCommand+="//push local\n"
            asmCommand+="@"+cmd[1]+"\nD=A\n@LCL\nA=M+D\nD=M\n@SP\nA=M\nM=D\n"
            pushPost()
            writeReset()
        }
        "argument"->{
            asmCommand+="//push argument\n"
            asmCommand+="@"+cmd[1]+"\nD=A\n@ARG\nA=M+D\nD=M\n@SP\nA=M\nM=D\n"
            pushPost()
            writeReset()
        }
        "this"->{
            asmCommand+="//push this\n"
            asmCommand+="@"+cmd[1]+"\nD=A\n@THIS\nA=M+D\nD=M\n@SP\nA=M\nM=D\n"
            pushPost()
            writeReset()
        }
        "that"->{
            asmCommand+="//push that\n"
            asmCommand+="@"+cmd[1]+"\nD=A\n@THAT\nA=M+D\nD=M\n@SP\nA=M\nM=D\n"
            pushPost()
            writeReset()
        }
        "temp"->{
            asmCommand+="//push temp\n"
            var x=5+cmd[1].toInt()
            asmCommand+="@"+x.toString()+"\nD=M\n@SP\nA=M\nM=D\n"
            pushPost()
            writeReset()
        }
        "pointer"->{
            asmCommand+="//push pointer\n"
            if (cmd[1]=="0")
                asmCommand+="@THIS\n"
            else if(cmd[1]=="1")
                asmCommand+="@THAT\n"
            asmCommand+="D=M\n@SP\nA=M\nM=D\n"
            pushPost()
            writeReset()
        }
        "static"->{
            asmCommand+="//push static\n"
            asmCommand+="@"+nameFile+"."+cmd[1]+"\nD=M\n@SP\nA=M\nM=D\n"
            pushPost()
            writeReset()
        }

    }

}

fun handlePop(cmd:List<String>,nameFile:String)
{
    when(cmd[0])
    {
        "local"->{
            asmCommand+="//pop local\n"
            asmCommand+="@SP\nA=M-1\nD=M\n@LCL\nA=M\n"
            for (i in 0 until cmd[1].toInt())
                asmCommand+="A=A+1\n"
            asmCommand+="M=D\n"
            popPost()
            writeReset()
        }
        "argument"->{
            asmCommand+="//pop argument\n"
            asmCommand+="@SP\nA=M-1\nD=M\n@ARG\nA=M\n"
            for (i in 0 until cmd[1].toInt())
                asmCommand+="A=A+1\n"
            asmCommand+="M=D\n"
            popPost()
            writeReset()
        }

        "this"->{
            asmCommand+="//pop this\n"
            asmCommand+="@SP\nA=M-1\nD=M\n@THIS\nA=M\n"
            for (i in 0 until cmd[1].toInt())
                asmCommand+="A=A+1\n"
            asmCommand+="M=D\n"
            popPost()
            writeReset()
        }
        "that"->{
            asmCommand+="//pop that\n"
            asmCommand+="@SP\nA=M-1\nD=M\n@THAT\nA=M\n"
            for (i in 0 until cmd[1].toInt())
                asmCommand+="A=A+1\n"
            asmCommand+="M=D\n"
            popPost()
            writeReset()
        }
        "temp"->{
            asmCommand+="//pop temp\n"
            var x=5+cmd[1].toInt()
            asmCommand+="@SP\nA=M-1\nD=M\n@"+x.toString()+"\nM=D\n"
            popPost()
            writeReset()
        }
        "pointer"->{
            asmCommand+="//pop pointer\n"
            asmCommand+="@SP\nA=M-1\nD=M\n"
            if (cmd[1]=="0")
                asmCommand+="@THIS\n"
            else if(cmd[1]=="1")
                asmCommand+="@THAT\n"
            asmCommand+="M=D\n"
            popPost()
            writeReset()
        }
        "static"->{
            asmCommand+="//pop static\n"
            asmCommand+="@SP\nA=M-1\nD=M\n@"+nameFile+"."+cmd[1]+"\nM=D\n"
            popPost()
            writeReset()
        }
    }

}