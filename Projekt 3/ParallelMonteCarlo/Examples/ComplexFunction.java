package Examples;

public class ComplexFunction {
    public static double calculate(double x1,double x2,double x3, double x4){
        return ((x1+x2) *  Math.cos(x3)) / (Math.sqrt(Math.abs(x4*Math.sin(x1))));
    }
}
