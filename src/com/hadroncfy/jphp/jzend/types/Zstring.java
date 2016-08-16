package com.hadroncfy.jphp.jzend.types;

/**
 * Created by cfy on 16-8-12.
 *
 */
public class Zstring extends Zval {
    protected String value;
    public Zstring(String s){
        value = s;
    }
    @Override
    public String getTypeName() {
        return "string";
    }

    @Override
    public String dump() {
        return new StringBuilder().append("string(").append(value.length()).append(") \"").append(value).append("\"").toString();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    String getValue(){
        return value;
    }

    @Override
    public Zbool boolCast() {
        if(value.equals("") || value.equals("0")){
            return Zbool.FALSE;
        }
        return Zbool.TRUE;
    }

    @Override
    public Zint intCast() {
        int ret = 0;
        try {
            ret = Integer.parseInt(value);
        }
        catch (NumberFormatException e){
            ret = 0;
        }
        return new Zint(ret);
    }

    @Override
    public Zfloat floatCast() {
        double ret = 0;
        try {
            ret = Double.parseDouble(value);
        }
        catch (NumberFormatException e){
            ret = 0;
        }
        return new Zfloat(ret);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Zstring){
            return value.equals(((Zstring) obj).value);
        }
        else if(obj instanceof Zint){
            int ret;
            try{
                ret = Integer.parseInt(value);
            }
            catch (NumberFormatException e){
                ret = 0;
            }
            return ret == ((Zint) obj).value;
        }
        else if(obj instanceof Zfloat){
            double ret;
            try{
                ret = Double.parseDouble(value);
            }
            catch (NumberFormatException e){
                ret = 0;
            }
            return ret == ((Zfloat) obj).value;
        }
        else if(obj instanceof Zbool){
            return value.equals("true") && ((Zbool) obj).value || value.equals("false") && !((Zbool) obj).value;
        }

        return super.equals(obj);
    }
}
