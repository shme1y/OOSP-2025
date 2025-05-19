package models;

public class Between {
    public static StringBuilder originalInput;
    private static int start;
    private static int end;
    private static StringBuilder originalModule;
    private static int startModule;
    private static int endModule;
    private static boolean flag;
    public static void between (StringBuilder original, int one, int two){
        originalInput = original;
        start = one;
        end = two;
    }


    public static void betweenModule (StringBuilder original1, int one, int two){
        originalModule = original1;
        startModule = one;
        endModule = two;
    }
    public static void getFlag ( boolean b){
        flag = b;
    }


    public static StringBuilder between(){ return originalInput;}
    public static int start(){
        return start;
    }
    public static int end(){
        return end;
    }
    public static boolean flag(){
        return flag;
    }



    public static StringBuilder betweenModule(){
        return originalModule;
    }
    public static int getStartModule(){
        return startModule;
    }
    public static int getEndModule(){
        return endModule;
    }

}
