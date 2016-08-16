package com.hadroncfy;

/**
 * Created by cfy on 16-5-13.
 */
public class Tools {
    public static String unescapeString(String s){
        StringBuilder sb = new StringBuilder();
        int l = s.length();
        for(int i = 0;i < l;i++){
            if(s.charAt(i) == '\\'){
                if(++i < l){
                    char a;
                    switch(s.charAt(i)){
                        case 'n':a = '\n';break;
                        case 'r':a = '\r';break;
                        default:a = s.charAt(i);
                    }
                    sb.append(a);
                }
                else
                    sb.append('\\');
            }
            else
                sb.append(s.charAt(i));
        }
        return sb.toString();
    }
    public static String escapeString(String s){
        StringBuilder sb = new StringBuilder();
        int l = s.length();
        for(int i = 0;i < l;i++){
            switch(s.charAt(i)){
                case '\n':sb.append("\\n");break;
                case '\r':sb.append("\\r");break;
                default:sb.append(s.charAt(i));
            }
        }
        return sb.toString();
    }

}
