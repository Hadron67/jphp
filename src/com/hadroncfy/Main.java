package com.hadroncfy;


import com.hadroncfy.jphp.jzend.compile.JZendCompiler;
import com.hadroncfy.jphp.jzend.compile.JZendParser;

import java.io.*;
import java.util.Stack;

public class Main {

    public static void main(String[] args)  {
	// write your code here
        File f = new File("/home/cfy/IdeaProjects/jphp/src/com/hadroncfy/test.php");
        System.out.println("lire la file,attendez,s'il vous plait...");
        String s;

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
