package com.hadroncfy.jphp;


import com.hadroncfy.jphp.jzend.Context;
import com.hadroncfy.jphp.jzend.compile.CompilationException;
import com.hadroncfy.jphp.jzend.compile.JZendCompiler;
import com.hadroncfy.jphp.jzend.compile.ParseException;
import com.hadroncfy.jphp.jzend.compile.Routine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class Main {

    public static void main(String[] args)  {
	// write your code here
        File f = new File(args[0]);
        System.out.println("lire la file,attendes,s'il vous plait...");

        try {
            JZendCompiler cp = new JZendCompiler();
            Routine result = cp.compile(new FileInputStream(args[0]), args[0]);

            if(cp.hasWarning()){
                cp.printWarnings(System.out);
            }
            System.out.println("program accepted.");
            result.dump(System.out);
            System.out.println("result:");

            Context env = new Context(System.out);
            result.call(env,null);
            if(env.isError()){
                System.out.print(env.getErrorMsg());
            }
        } catch (CompilationException e) {
            System.out.print("PHP Fatal Error:" + e.getMessage());
        } catch (FileNotFoundException | ParseException e) {
            e.printStackTrace();
        }
    }
}
