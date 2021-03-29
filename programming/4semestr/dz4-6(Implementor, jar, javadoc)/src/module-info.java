/**
 * Solution for <a href="https://www.kgeorgiy.info/courses/java-advanced/homeworks.html#homework-implementor">Implementor</a> homework
 * of <a href="https://www.kgeorgiy.info/courses/java-advanced/">Java Advanced</a> course.
 *
 * @author Anton Dubrovin
 */
module dz4 {
    requires info.kgeorgiy.java.advanced.implementor;
    requires java.compiler;

    opens info.kgeorgiy.ja.dubrovin.implementor;
    exports info.kgeorgiy.ja.dubrovin.implementor;
}