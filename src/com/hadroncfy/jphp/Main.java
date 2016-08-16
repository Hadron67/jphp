package com.hadroncfy.jphp;


import com.hadroncfy.jphp.jzend.compile.JZendCompiler;
import com.hadroncfy.jphp.jzend.compile.Routine;

import java.io.File;
import java.io.FileInputStream;


public class Main {

    public static void main(String[] args)  {
	// write your code here
        File f = new File(args[0]);
        System.out.println("lire la file,attendes,s'il vous plait...");

        try {
            JZendCompiler cp = new JZendCompiler();
            Routine result = cp.compile(new FileInputStream(args[0]));

            if(cp.hasWarning()){
                cp.printWarnings(System.out);
            }

            System.out.println("program accepted.");
            result.dump(System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
