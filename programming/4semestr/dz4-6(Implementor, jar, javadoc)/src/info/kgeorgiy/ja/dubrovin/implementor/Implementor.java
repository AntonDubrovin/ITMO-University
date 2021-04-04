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
 * Generates <code>.java</code> and <code>.jar</code> files, which implement given class ot interface.
 *
 * @author Anton Dubrovin
 */
public class Implementor implements JarImpler {
    /**
     * Constant equal to one space, used to generate class code.
     */
    private final String SPACE = " ";
    /**
     * Constant equal to one open figure bracket, used to generate class code to open body of methods, functions etc.
     */
    private final String LEFT_FIGURE_BRACKET = "{";
    /**
     * Constant equal to one close figure bracket, used to generate class code to close body of methods, functions etc.
     */
    private final String RIGHT_FIGURE_BRACKET = "}";
    /**
     * Constant equal to one line separator, used to generate class code to wrap a new line.
     */
    private final String NEW_LINE = System.lineSeparator();
    /**
     * Constant equal to one semicolon, used to generate class code for the end of each line.
     */
    private final String SEMICOLON = ";";
    /**
     * Constant equal to one comma, used to generate class code to separate parameters.
     */
    private final String COMMA = ",";
    /**
     * Constant equal to one open round bracket, used to generate class code to open parameters enumeration.
     */
    private final String LEFT_ROUND_BRACKET = "(";
    /**
     * Constant equal to one close round bracket, used to generate class code to close parameters enumeration.
     */
    private final String RIGHT_ROUND_BRACKET = ")";
    /**
     * Constant equal to <code>.class</code>, used to end files with the extension <code>.class</code>.
     */
    private final String CLASS_ENDING = ".class";
    /**
     * Constant equal to <code>.java</code>, used to end files with the extension <code>.java</code>.
     */
    private final String JAVA_ENDING = ".java";

    /**
     * Gets arguments from console, checks arguments, and then calls needed implementation.
     * <ul>
     *     <li>2 arguments - <code>className</code> and <code>rootPath</code>
     *     creates <code>.java</code> file by invoke {@link #implement}</li>
     *     <li>3 arguments - <code>-jar</code>, <code>className</code> and <code>jarPath</code>
     *     creates <code>.jar</code> file by invoke {@link #implementJar}</li>
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
     *
     * @param args arguments received by the code.
     * @throws ImplerException if
     *                         <ul>
     *                             <li>no arguments, or their number is incorrect.</li>
     *                             <li>one of the arguments is null.</li>
     *                             <li>3 arguments, and the first one is not "-jar".</li>
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
     * Invoke {@link Class#forName} for received class.
     *
     * @param className received class name.
     * @return {@code Class} for received class.
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
     * Creates <code>.java</code> file, which implement received <code>token</code>.
     * Places result file if <code>root</code> directory.
     * Result file name is <code>token</code> name + <code>Impl</code>.
     *
     * @param token type token to create implementation for.
     * @param root  root directory.
     * @throws ImplerException if implementation failed, because
     *                         <ul>
     *                             <li>One of variables become <code>null</code>.</li>
     *                             <li>Can't create path directory.</li>
     *                             <li>Received <code>class</code> equals to <code>array.</code>,
     *                             <code>enum</code>or <code>primitive</code> type.</li>
     *                             <li>Received <code>class</code> is final or private.</li>
     *                             <li>No constructors found in <code>token</code>.</li>
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
                simpleClassName(token) + JAVA_ENDING));
        createParentDirectories(path);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(createClass(token));
        } catch (IOException e) {
            throw new ImplerException("Something went wrong with output file.", e);
        }
    }

    /**
     * Creates <code>.jar</code> file, which implement received <code>token</code>.
     * Invokes {@link #implement}, {@link #buildJar}.
     * Creates temporary directory during the generation, then deletes it.
     *
     * @param token   type token to create implementation for.
     * @param jarFile target <var>.jar</var> file.
     * @throws ImplerException if implementation failed, because
     *                         <ul>
     *                             <li>One of variables become <code>null</code>.</li>
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
     * Cleans directory of created tmp files.
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
     * @return complete {@code Manifest}
     */
    private Manifest createManifest() {
        Manifest manifest = new Manifest();
        Attributes attributes = manifest.getMainAttributes();
        attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
        attributes.put(Attributes.Name.IMPLEMENTATION_VENDOR, "Anton Dubrovin");
        return manifest;
    }

    /**
     * Creates <code>.jar</code> file by <code>token</code>, creates manifest.
     *
     * @param token   received token to create implementation for.
     * @param tmpDir  directory, which contains all <code>.class</code> files.
     * @param jarFile {@code Path} for result <code>.jar</code> file.
     * @throws ImplerException if something will go wrong with jar output file.
     */
    private void buildJar(Class<?> token, Path tmpDir, Path jarFile) throws ImplerException {
        try (JarOutputStream writer = new JarOutputStream(Files.newOutputStream(jarFile), createManifest())) {
            writer.putNextEntry(new ZipEntry(getImplFilePath(token).toString()));
            Files.copy(getFile(tmpDir, token, CLASS_ENDING), writer);
        } catch (IOException e) {
            throw new ImplerException("Something went wrong with jar output file.", e);
        }
    }

    /**
     * Compiles <code>.java</code> files, which implement received <code>token</code>, place them in received directory.
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
        String filePath = getFile(directory, token, JAVA_ENDING).toString();
        String[] args = {"-cp", classPath, filePath};
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler.run(null, null, null, args) != 0) {
            throw new ImplerException("Can't to compile code.");
        }
    }

    /**
     * Returns file path containing the implementation of received <code>token</code>.
     * Converting <code>token</code> package to path and add file extension.
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
     * Returns file path with received token class name and <code>.class</code> extension.
     *
     * @param token received token to create implementation for.
     * @return result {@code String} path.
     */
    private Path getImplFilePath(Class<?> token) {
        return Paths.get(fullClassName(token).replace('.', '/'))
                .getParent().resolve(simpleClassName(token) + CLASS_ENDING);
    }

    /**
     * Checks if <code>token</code> and <code>path</code> exists.
     *
     * @param token received token to create implementation for.
     * @param path  jar ot root directory.
     * @throws ImplerException if <code>token</code> or <code>path</code> doesn't exist.
     */
    private void checkTokenAndPath(Class<?> token, Path path) throws ImplerException {
        if (token == null || path == null) {
            throw new ImplerException("Invalid arguments.");
        }
    }

    /**
     * Returns name of created file.
     *
     * @param token received token to create implementation for.
     * @return resulting {@code String} file name.
     */
    private String simpleClassName(Class<?> token) {
        return token.getSimpleName() + "Impl";
    }

    private String fullClassName(Class<?> token) {
        return token.getPackageName() + "." + simpleClassName(token);
    }

    /**
     * Invoke {@link Files#createDirectories} and create parent directories.
     *
     * @param path to which need to create parent directories.
     * @throws ImplerException if IOException occurs.
     */
    private void createParentDirectories(Path path) throws ImplerException {
        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            throw new ImplerException("Can't create path directory.", e);
        }
    }

    /**
     * Create result java code, which implement <code>token</code>.
     *
     * @param token received token to create implementation for.
     * @return {@code String}  representation of result java code.
     * @throws ImplerException if no constructors found in <code>token</code>.
     */
    private String createClass(Class<?> token) throws ImplerException {
        return createPackage(token) +
                createClassHeader(token) + SPACE + LEFT_FIGURE_BRACKET + NEW_LINE +
                createConstructors(token) +
                createMethods(token) +
                RIGHT_FIGURE_BRACKET;
    }

    /**
     * Creates package declaration for <code>.java</code> file, if package <code>token</code>is not empty,
     * otherwise returns empty string.
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
     * Creates class declaration for <code>.java</code> file.
     *
     * @param token received token to create implementation for.
     * @return result {@code String} with right class declaration.
     */
    private String createClassHeader(Class<?> token) {
        return "public" + SPACE + "class" + SPACE + simpleClassName(token) + SPACE +
                (token.isInterface() ? "implements" : "extends") + SPACE + token.getCanonicalName();
    }

    /**
     * Creates constructors for <code>.java</code> file, takes only non-private constructors.
     *
     * @param token received token to create implementation for.
     * @return result {@code String} with all constructors.
     * @throws ImplerException if no constructors found in <code>token</code>.
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
     * Create implementation of received {@code Constructor}.
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
     * Return list of declared parameters of {@code Executable}, mapped by {@link Function}.
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
     * Creates methods for <code>.java</code> file, takes only abstract methods.
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
     * Collect tokens of methods from array to {@code TreeSet}.
     *
     * @param methods array of methods.
     * @param storage {@code TreeSet} storage to store result.
     */
    private void collectMethods(Method[] methods, TreeSet<Method> storage) {
        Arrays.stream(methods).filter(m -> Modifier.isAbstract(m.getModifiers()))
                .collect(Collectors.toCollection(() -> storage));
    }

    /**
     * Create implementation of received {@code Method}.
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
     * Create code for default value by received <code>token</code>.
     * <ul>
     *     <li>for primitive default value is "0"</li>
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
     *
     * @param method     received token of method or constructor.
     * @param returnName {@code String} representation of {@code Executable} return value.
     * @param name       {@code String} representation of {@code Executable} name.
     * @param body       {@code String} representation of {@code Executable} body.
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
     * Returns list of declared exceptions to be thrown by {@link Executable}, if the executable declares exceptions,
     * and separates parameters with comma and space, wrapping in round brackets.
     * Otherwise returns empty string.
     *
     * @param method {@code Executable} object that represents all the parameters.
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