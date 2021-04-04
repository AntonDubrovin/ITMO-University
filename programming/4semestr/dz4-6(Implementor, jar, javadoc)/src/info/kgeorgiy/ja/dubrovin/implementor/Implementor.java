package info.kgeorgiy.ja.dubrovin.implementor;

import info.kgeorgiy.java.advanced.implementor.JarImpler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

/**
 * Implementation of {@link JarImpler}.
 * Generates <var>.java</var> and <var>.jar</var> files, which implement given class or interface.
 *
 * @author <font color=aqua>Anton Dubrovin</font>.
 */
public class Implementor implements JarImpler {
    /**
     * Constant equal to one space, used to generate class code.
     */
    private final String SPACE = " ";
    /**
     * Constant equal to one open figure bracket.
     * Used to generate class code to open body of methods, functions etc.
     */
    private final String LEFT_FIGURE_BRACKET = "{";
    /**
     * Constant equal to one close figure bracket.
     * Used to generate class code to close body of methods, functions etc.
     */
    private final String RIGHT_FIGURE_BRACKET = "}";
    /**
     * Constant equal to one line separator.
     * Used to generate class code to wrap a new line.
     */
    private final String NEW_LINE = System.lineSeparator();
    /**
     * Constant equal to one semicolon.
     * Used to generate class code for the end of each line.
     */
    private final String SEMICOLON = ";";
    /**
     * Constant equal to one comma.
     * Used to generate class code to separate parameters.
     */
    private final String COMMA = ",";
    /**
     * Constant equal to one open round bracket.
     * Used to generate class code to open parameters enumeration.
     */
    private final String LEFT_ROUND_BRACKET = "(";
    /**
     * Constant equal to one close round bracket.
     * Used to generate class code to close parameters enumeration.
     */
    private final String RIGHT_ROUND_BRACKET = ")";
    /**
     * Constant equal to <var>.class</var>.
     * Used to end files with the extension <var>.class</var>.
     */
    private final String CLASS_EXTENSION = ".class";
    /**
     * Constant equal to <var>.java</var>.
     * Used to end files with the extension <var>.java</var>.
     */
    private final String JAVA_EXTENSION = ".java";

    /**
     * Gets arguments from console, checks arguments, and then calls needed implementation.
     * <ul>
     *     <li>2 arguments - <strong>className</strong> and <strong>rootPath</strong> -
     *     creates <var>.java</var> file by invoke {@link #implement}</li>
     *     <li>3 arguments - <var>-jar</var>, <strong>className</strong> and <strong>jarPath</strong> -
     *     creates <var>.jar</var> file by invoke {@link #implementJar}</li>
     * </ul>
     *
     * @param args arguments received by the code.
     */
    public static void main(String[] args) {
        try {
            checkArguments(args);
            Implementor implementor = new Implementor();
            if (args.length == 2) {
                implementor.implement(createClass(args[0]), createPath(args[1]));
            } else {
                implementor.implementJar(createClass(args[1]), createPath(args[2]));
            }
        } catch (ImplerException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Checks the correctness of the received arguments.
     * Checks if there is a null argument, if the number of arguments is 2 or 3 and if 3, checks first argument.
     *
     * @param args arguments received by the code.
     * @throws ImplerException if
     *                         <ul>
     *                             <li>no arguments, or their number is incorrect.</li>
     *                             <li>one of the arguments is null.</li>
     *                             <li>3 arguments, and the first one is not <var>-jar"</var>.</li>
     *                         </ul>
     */
    private static void checkArguments(String[] args) throws ImplerException {
        if (args == null || args.length < 2 || args.length > 3
                || args.length == 3 && !args[0].equals("-jar")) {
            throw new ImplerException("Invalid program arguments.");
        }
        for (String arg : args) {
            if (arg == null) {
                throw new ImplerException("Invalid program arguments.");
            }
        }
    }

    /**
     * Returns an instance of this class with received <strong>className</strong>.
     * Invoke {@link Class#forName} for received class.
     *
     * @param className received class name.
     * @return An instance of {@code Class} for received class name.
     * @throws ImplerException if class can't be found.
     */
    private static Class<?> createClass(String className) throws ImplerException {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ImplerException("Invalid class name.", e);
        }
    }

    /**
     * Returns a <strong>Path</strong> by converting a string.
     * Invoke {@link Path#of} for received path and more sequence of strings.
     *
     * @param path received path name.
     * @return {@code Path} for received path.
     * @throws ImplerException if string path can't be converted to path.
     */
    private static Path createPath(String path) throws ImplerException {
        try {
            return Path.of(path);
        } catch (InvalidPathException e) {
            throw new ImplerException("Invalid path.", e);
        }
    }

    /**
     * Creates <var>.java</var> file, which implement received <code>token</code>.
     * Places result file in <strong>root</strong> directory.
     * Result file name is <strong>token</strong> name + <strong>Impl</strong>.
     *
     * @param token type token to create implementation for.
     * @param root  root directory.
     * @throws ImplerException if implementation failed, because
     *                         <ul>
     *                             <li>One of variables become <strong>null</strong>.</li>
     *                             <li>Can't create path directory.</li>
     *                             <li>Received <strong>class</strong> equals to <strong>array</strong>,
     *                             <strong>enum</strong> or <strong>primitive</strong> type.</li>
     *                             <li>Received <strong>class</strong> is final or private.</li>
     *                             <li>No constructors found in <strong>token</strong>.</li>
     *                             <li>Something will go wrong with output file.</li>
     *                         </ul>
     */
    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        checkTokenAndPath(token, root);

        if (token.isPrimitive() || token.isArray() || token == Enum.class ||
                Modifier.isFinal(token.getModifiers()) || Modifier.isPrivate(token.getModifiers())) {
            throw new ImplerException("Can't implement class.");
        }

        Path path = root.resolve(Paths.get(token.getPackageName().replace('.', File.separatorChar),
                simpleClassName(token) + JAVA_EXTENSION));
        createParentDirectories(path);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(createClass(token));
        } catch (IOException e) {
            throw new ImplerException("Something went wrong with output file.", e);
        }
    }

    /**
     * Creates <var>.jar</var> file, which implement received <strong>token</strong>.
     * Invokes {@link #implement}, {@link #buildJar}.
     * Creates temporary directory during the generation, then deletes it.
     *
     * @param token   type token to create implementation for.
     * @param jarFile target <var>.jar</var> file.
     * @throws ImplerException if implementation failed, because
     *                         <ul>
     *                             <li>One of variables become <strong>null</strong>.</li>
     *                              <li>Can't create path directory.</li>
     *                              <li>If tmp path is invalid</li>
     *                              <li>Something will go wrong with jar output file.</li>
     *                              <li>Something will go wrong with clean tmp directory</li>
     *                         </ul>
     */
    @Override
    public void implementJar(Class<?> token, Path jarFile) throws ImplerException {
        checkTokenAndPath(token, jarFile);
        createParentDirectories(jarFile);

        Path tmpDir = createPath(jarFile.toAbsolutePath().getParent().toString() + "ImplementorTmp");

        implement(token, tmpDir);
        compileClass(token, tmpDir);
        buildJar(token, tmpDir, jarFile);
        cleanDir(tmpDir);
    }

    /**
     * Cleans directory of created temporary files.
     *
     * @param directory to delete.
     * @throws ImplerException if something will went wrong while cleaning directory.
     */
    private void cleanDir(Path directory) throws ImplerException {
        try {
            Files.walkFileTree(directory, CLEANER);
        } catch (IOException e) {
            throw new ImplerException("Unable to clean tmp directory.", e);
        }
    }

    /**
     * Instance of {@link SimpleFileVisitor}, deletes files and directories.
     */
    private static final FileVisitor<Path> CLEANER = new SimpleFileVisitor<>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }
    };

    /**
     * Creates manifest with manifest version and author.
     *
     * @return complete {@code Manifest}.
     */
    private Manifest createManifest() {
        Manifest manifest = new Manifest();
        Attributes attributes = manifest.getMainAttributes();
        attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
        attributes.put(Attributes.Name.IMPLEMENTATION_VENDOR, "Anton Dubrovin");
        return manifest;
    }

    /**
     * Creates <var>.jar</var> file by <strong>token</strong>, and creates manifest.
     *
     * @param token   received token to create implementation for.
     * @param tmpDir  directory, which contains all <var>.class</var> files.
     * @param jarFile {@code Path} for result <var>.jar</var> file.
     * @throws ImplerException if something will go wrong with jar output file.
     */
    private void buildJar(Class<?> token, Path tmpDir, Path jarFile) throws ImplerException {
        try (JarOutputStream writer = new JarOutputStream(Files.newOutputStream(jarFile), createManifest())) {
            writer.putNextEntry(new ZipEntry(getImplFilePath(token)));
            Files.copy(getFile(tmpDir, token, CLASS_EXTENSION), writer);
        } catch (IOException e) {
            throw new ImplerException("Something went wrong with jar output file.", e);
        }
    }

    /**
     * Compiles <var>.java</var> files, which implement received <strong>token</strong>.
     *
     * @param token     received token to create implementation for.
     * @param directory where compiled classes will be located.
     * @throws ImplerException if
     *                         <ul>
     *                             <li>URL can't be converted to URI</li>
     *                             <li>Can't compile code</li>
     *                         </ul>
     */
    private void compileClass(Class<?> token, Path directory) throws ImplerException {
        String classPath;
        try {
            classPath = Path.of(token.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
        } catch (URISyntaxException e) {
            throw new ImplerException("Can't convert URL to URI.", e);
        }
        String filePath = getFile(directory, token, JAVA_EXTENSION).toString();
        String[] args = {"-cp", classPath, filePath};
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler.run(null, null, null, args) != 0) {
            throw new ImplerException("Can't to compile code.");
        }
    }

    /**
     * Returns file path containing the implementation of received <strong>token</strong>.
     * Takes full class name with {@link File#separator} instead of '.' and added the extension.
     *
     * @param directory parent path to resolve.
     * @param token     received token to create implementation for.
     * @param extension of file.
     * @return result {@code Path}
     */
    public Path getFile(Path directory, final Class<?> token, String extension) {
        return directory.resolve(fullClassName(token).replace(".", File.separator) + extension).toAbsolutePath();
    }

    /**
     * Returns file path with received token class name and <var>.class</var> extension.
     * Takes full class name with '/' instead of '.', resolves path, added class extension.
     * Replaces '\' to '/' for windows.
     *
     * @param token received token to create implementation for.
     * @return result {@code String} path.
     */
    private String getImplFilePath(Class<?> token) {
        return Paths.get(
                fullClassName(token).replace('.', '/'))
                .getParent().resolve(simpleClassName(token) + CLASS_EXTENSION)
                .toString().replace('\\', '/');
    }

    /**
     * Checks if <strong>token</strong> and <strong>path</strong> exists.
     * Checks if some argument is null.
     *
     * @param token received token to create implementation for.
     * @param path  to directory.
     * @throws ImplerException if <strong>token</strong> or <strong>path</strong> doesn't exist.
     */
    private void checkTokenAndPath(Class<?> token, Path path) throws ImplerException {
        if (token == null || path == null) {
            throw new ImplerException("Invalid arguments.");
        }
    }

    /**
     * Returns simple name of file, which implements <strong>token</strong>.
     * Consisting of simple token name + Impl.
     *
     * @param token received token to create implementation for.
     * @return resulting {@code String} file name.
     */
    private String simpleClassName(Class<?> token) {
        return token.getSimpleName() + "Impl";
    }

    /**
     * Returns full name of file, which implements <strong>token</strong>.
     * Consisting of package and simple name.
     *
     * @param token received token to create implementation for.
     * @return resulting {@code String} file name.
     */
    private String fullClassName(Class<?> token) {
        return token.getPackageName() + "." + simpleClassName(token);
    }

    /**
     * Creates parent directories.
     * Invoke {@link Files#createDirectories}.
     *
     * @param path to which need to create parent directories.
     * @throws ImplerException if {@link IOException} occurs.
     */
    private void createParentDirectories(Path path) throws ImplerException {
        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            throw new ImplerException("Can't create path directory.", e);
        }
    }

    /**
     * Creates result java class, which implement <strong>token</strong>.
     * Creates package, class header, constructors and methods by invoking
     * {@link #createPackage}, {@link #createClassHeader}, {@link #createConstructor},
     * and {@link #createMethod}.
     *
     * @param token received token to create implementation for.
     * @return {@code String}  representation of result java code.
     * @throws ImplerException if no constructors found in <strong>token</strong>.
     */
    private String createClass(Class<?> token) throws ImplerException {
        return createPackage(token) +
                createClassHeader(token) + SPACE + LEFT_FIGURE_BRACKET + NEW_LINE +
                createConstructors(token) +
                createMethods(token) +
                RIGHT_FIGURE_BRACKET;
    }

    /**
     * Creates package declaration for <var>.java</var> file.
     * Creates if package <strong>token</strong> is not empty, otherwise returns empty string.
     *
     * @param token received token to create implementation for.
     * @return result {@code String} with right package.
     */
    private String createPackage(Class<?> token) {
        final String packageName = token.getPackageName();
        if (!packageName.isEmpty()) {
            return "package" + SPACE + packageName + SEMICOLON + NEW_LINE;
        } else {
            return "";
        }
    }

    /**
     * Creates class declaration for <var>.java</var> file.
     * If <strong>token</strong> is interface, then implements its, otherwise extends.
     *
     * @param token received token to create implementation for.
     * @return result {@code String} with right class declaration.
     */
    private String createClassHeader(Class<?> token) {
        return "public" + SPACE + "class" + SPACE + simpleClassName(token) + SPACE +
                (token.isInterface() ? "implements" : "extends") + SPACE + token.getCanonicalName();
    }

    /**
     * Creates constructors for <var>.java</var> file.
     * Takes only non-private constructors. Invokes {@link #createConstructor}.
     *
     * @param token received token to create implementation for.
     * @return result {@code String} with all constructors.
     * @throws ImplerException if no constructors found in <strong>token</strong>.
     */
    private String createConstructors(Class<?> token) throws ImplerException {
        StringBuilder constructors = new StringBuilder();
        if (!token.isInterface()) {
            for (Constructor<?> constructor : token.getDeclaredConstructors()) {
                if (!Modifier.isPrivate(constructor.getModifiers())) {
                    constructors.append(createConstructor(constructor, simpleClassName(token))).append(NEW_LINE);
                }
            }

            if (constructors.toString().isEmpty()) {
                throw new ImplerException("Can't find constructors.");
            }
        }
        return constructors.toString();
    }

    /**
     * Create implementation of received <strong>constructor</strong>.
     * Invokes {@link #createBody} with constructor, empty return type, method name and body.
     *
     * @param constructor constructor token to generate implementation for.
     * @param name        constructor name.
     * @return result {@code String} complete constructor.
     */
    private String createConstructor(Constructor<?> constructor, String name) {
        String body = "super" + findParameters(constructor, Parameter::getName) + SEMICOLON;
        return createBody(constructor, "", name, body);
    }

    /**
     * Return list of declared parameters of {@link Executable}, mapped by {@link Function}.
     * Separates parameters with comma and space, wrapping in round brackets.
     *
     * @param method {@code Executable} object that represents all the parameters.
     * @param func   function to apply for all parameters.
     * @return result {@code String} of parameters.
     */
    private String findParameters(Executable method, Function<Parameter, String> func) {
        return Arrays.stream(method.getParameters()).map(func)
                .collect(Collectors.joining(COMMA + SPACE, LEFT_ROUND_BRACKET, RIGHT_ROUND_BRACKET));
    }

    /**
     * Creates methods for <var>.java</var> file, takes only abstract methods.
     * Collects the necessary methods in {@link TreeSet}, which can't contains equals methods.
     * Invokes {@link #createMethod}.
     *
     * @param token received token to create implementation for.
     * @return result {@code String} of methods.
     */
    private String createMethods(Class<?> token) {
        StringBuilder methods = new StringBuilder();
        TreeSet<Method> storage = new TreeSet<>(Comparator.comparing(
                method -> method.getName().hashCode() + Arrays.hashCode(method.getParameterTypes())));
        collectMethods(token.getMethods(), storage);
        while (token != null) {
            collectMethods(token.getDeclaredMethods(), storage);
            token = token.getSuperclass();
        }
        for (Method method : storage) {
            methods.append(createMethod(method)).append(NEW_LINE);
        }
        return methods.toString();
    }

    /**
     * Collect tokens of methods from array to {@link TreeSet}.
     * Takes only abstract methods.
     *
     * @param methods array of methods.
     * @param storage {@code TreeSet} storage to store result.
     */
    private void collectMethods(Method[] methods, TreeSet<Method> storage) {
        Arrays.stream(methods).filter(m -> Modifier.isAbstract(m.getModifiers()))
                .collect(Collectors.toCollection(() -> storage));
    }

    /**
     * Create implementation of received <strong>Method</strong>.
     * Invokes {@link #createBody} with method, correct return type, method name and body.
     *
     * @param method received token to generate implementation for.
     * @return result {@code String} complete method.
     */
    private String createMethod(Method method) {
        Class<?> returnType = method.getReturnType();
        String returnName = returnType.getCanonicalName();
        String body = "return" + SPACE + getDefaultValue(returnType) + SEMICOLON;
        return createBody(method, returnName, method.getName(), body);
    }

    /**
     * Create code for default value by received <strong>token</strong>.
     * <ul>
     *     <li>for primitive default value is 0</li>
     *     <li>for void default value is empty string</li>
     *     <li>for boolean default value is true</li>
     *     <li>otherwise return null</li>
     * </ul>
     *
     * @param token received token to create implementation for.
     * @return {@code String} of default value.
     */
    private String getDefaultValue(Class<?> token) {
        if (token.equals(void.class)) {
            return "";
        } else if (token.equals(boolean.class)) {
            return "true";
        } else if (token.isPrimitive()) {
            return "0";
        }
        return null;
    }

    /**
     * Create implementation of received method or constructor.
     * Creates <strong>StringBuilder</strong>, and appends modifiers(if needed), return name(if needed),
     * name, parameters, body of method/constructor.
     *
     * @param method     received token of method or constructor.
     * @param returnName {@code String} representation of {@link Executable} return value.
     * @param name       {@code String} representation of {@link Executable} name.
     * @param body       {@code String} representation of {@link Executable} body.
     * @return result {@code String} code of method or constructor.
     */
    private String createBody(Executable method, String returnName, String name, String body) {
        StringBuilder result = new StringBuilder();
        String modifiers = Modifier.toString(method.getModifiers() & ~Modifier.ABSTRACT & ~Modifier.TRANSIENT);
        if (!modifiers.isEmpty()) {
            result.append(modifiers).append(SPACE);
        }
        if (!returnName.isEmpty()) {
            result.append(returnName).append(SPACE);
        }
        result.append(name);
        result.append(findParameters(method, p -> p.getType().getCanonicalName() + SPACE + p.getName()))
                .append(SPACE);
        result.append(findExceptions(method)).append(LEFT_FIGURE_BRACKET).append(NEW_LINE);
        result.append(body);
        result.append(NEW_LINE).append(RIGHT_FIGURE_BRACKET).append(NEW_LINE);
        return result.toString();
    }

    /**
     * Returns list of declared exceptions to be thrown by {@link Executable}.
     * Returns exceptions if the executable declares exceptions,
     * and separates parameters with comma and space, wrapping in round brackets.
     * Otherwise returns empty string.
     *
     * @param method {@link Executable} object to find exceptions for.
     * @return {@code String} of all exceptions.
     */
    private String findExceptions(Executable method) {
        if (method.getExceptionTypes().length == 0) {
            return "";
        } else {
            return Arrays.stream(method.getExceptionTypes()).map(Class::getCanonicalName)
                    .collect(Collectors.joining(COMMA + SPACE, "throws" + SPACE, SPACE));
        }
    }
}
