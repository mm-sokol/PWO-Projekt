import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Stream;
import Examples.*;;

public class MonteCarloIntegralEstimator {
    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: java MonteCarloIntegralEstimator <path_to_Java_file> <value_from> <value_to> <Number_of_points>");
            return;
        }

        // Path to the Java file (relative to the current working directory)
        String filePath = args[0];
        double from = Double.parseDouble(args[1]);
        double to = Double.parseDouble(args[2]);
        int N = Integer.parseInt(args[3]);

        // Use Path to handle file paths more reliably
        Path javaFilePath = Paths.get(filePath);
        Path classPath = javaFilePath.getParent(); // The directory where the .class file will be stored
        String className = javaFilePath.getFileName().toString().replace(".java", "");

        // Compile the Java file at runtime
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            System.out.println("Java compiler not available. Make sure you're running with a JDK, not just a JRE.");
            return;
        }

        // Convert Path to String for compiler
        int compileResult = compiler.run(null, null, null, javaFilePath.toString());
        if (compileResult != 0) {
            System.out.println("Error: Compilation failed.");
            return;
        }

        // Check if the class file exists after compilation
        Path classFilePath = classPath.resolve(className + ".class");
        System.out.println("Compiled class file path: " + classFilePath.toAbsolutePath());
        if (!classFilePath.toFile().exists()) {
            System.out.println("Error: Class file not found after compilation.");
            return;
        }

        try {
            // Print the URLClassLoader details
            URL classUrl = classPath.toUri().toURL();
            System.out.println("Class URL: " + classUrl);

            // Load the compiled class dynamically
            URLClassLoader classLoader = new URLClassLoader(new URL[]{classUrl});
            Class<?> loadedClass = classLoader.loadClass("Examples."+className);  // Dynamically load the compiled class

            // Print the class to confirm it was loaded
            System.out.println("Class loaded: " + loadedClass.getName());

            // Get method
            Method methodtemp = null;
            for (Method m : loadedClass.getMethods()) {
                if (m.getName().equals("calculate") && areAllParametersDouble(m)) {
                    methodtemp = m;
                    break;
                }
            }
            if (methodtemp == null) {
                System.out.println("No method calculate in class :(");
                return;
            }
            final Method method = methodtemp;
            method.setAccessible(true);
            Class<?>[] parameterTypes = method.getParameterTypes();
            int argLength = parameterTypes.length;

            // Integral calculation
            Random random = new Random();
            double integralValue = Math.pow((to - from), argLength) / N * Stream.generate(() -> getRandomItem(from, to, argLength, random)).parallel()
                    .limit(N)
                    .map(x -> {
                        try {
                            return (double) method.invoke(null, x);  // Invoke statically if method is static
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return 0.0;
                    })
                    .mapToDouble(Double::doubleValue)
                    .sum();
            // Invoke the function with sample values
            System.out.println("Integral value = " + integralValue);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Object[] getRandomItem(double a, double b, int size, Random random) {
        Object[] arguments = new Object[size];
        for (int i = 0; i < size; i++) {
            arguments[i] = a + (b - a) * random.nextDouble();
        }
        return arguments;
    }

    private static boolean areAllParametersDouble(Method method) {
        for (Class<?> paramType : method.getParameterTypes()) {
            if (paramType != double.class) {
                return false;
            }
        }
        return true;
    }
}
