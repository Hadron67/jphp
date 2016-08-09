package com.hadroncfy;


import com.hadroncfy.jphp.jzend.compile.JZendCompiler;
import com.hadroncfy.jphp.jzend.compile.JZendParser;
import jdk.nashorn.internal.objects.NativeJSON;

import java.io.File;
import java.io.FileInputStream;


public class Main {

    public static void main(String[] args)  {
	// write your code here
        File f = new File("/home/cfy/IdeaProjects/jphp/src/com/hadroncfy/test.php");
        System.out.println("lire la file,attendes,s'il vous plait...");

        try {
            JZendParser c = new JZendParser(new FileInputStream(f));
            JZendCompiler cp = new JZendCompiler();
            c.setCompiler(cp);
            c.Parse();
            System.out.println("program accepted.");
            cp.printInstructionList(System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
