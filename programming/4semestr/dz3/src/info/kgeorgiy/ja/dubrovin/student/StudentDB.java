package info.kgeorgiy.ja.dubrovin.student;

import info.kgeorgiy.java.advanced.student.*;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.function.*;

public class StudentDB implements GroupQuery {
    private final Function<Student, String> getFullName = student -> student.getFirstName() + " " + student.getLastName();
    private final Comparator<Student> idComparator = Comparator.comparing(Student::getId);
    private final Comparator<Student> nameComparator =
            Comparator.comparing(Student::getLastName, Comparator.reverseOrder())
                    .thenComparing(Student::getFirstName, Comparator.reverseOrder())
                    .thenComparing(Student::getId);

    @Override
    public List<Group> getGroupsByName(Collection<Student> students) {
        return null;
    }

    @Override
    public List<Group> getGroupsById(Collection<Student> students) {
        return null;
    }

    @Override
    public GroupName getLargestGroup(Collection<Student> students) {
        return null;
    }

    @Override
    public GroupName getLargestGroupFirstName(Collection<Student> students) {
        return null;
    }

    private List<String> getStudentList(List<Student> students, Function<Student, String> func) {
        return students.stream().map(func).collect(Collectors.toList());
    }

    @Override
    public List<String> getFirstNames(List<Student> students) {
        return getStudentList(students, Student::getFirstName);
    }

    @Override
    public List<String> getLastNames(List<Student> students) {
        return getStudentList(students, Student::getLastName);
    }

    @Override
    public List<GroupName> getGroups(List<Student> students) {
        return students.stream().map(Student::getGroup).collect(Collectors.toList());
    }

    @Override
    public List<String> getFullNames(List<Student> students) {
        return getStudentList(students, getFullName);
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> students) {
        return students.stream().map(Student::getFirstName).collect(Collectors.toSet());
    }

    @Override
    public String getMaxStudentFirstName(List<Student> students) {
        return students.stream().max(idComparator).map(Student::getFirstName)
                .orElse(null);
    }

    private List<Student> sortStudents(Collection<Student> students, Comparator<Student> comparator) {
        return students.stream().sorted(comparator).collect(Collectors.toList());
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return sortStudents(students, idComparator);
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return sortStudents(students, nameComparator);
    }

    private List<Student> findStudents(Collection<Student> students, Predicate<Student> predicate) {
        return students.stream().filter(predicate).sorted(nameComparator).collect(Collectors.toList());

    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return findStudents(students, student -> student.getFirstName().equals(name));
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return findStudents(students, student -> student.getLastName().equals(name));
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, GroupName group) {
        return findStudents(students, student -> student.getGroup().equals(group));
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, GroupName group) {
        return students.stream().filter(student -> student.getGroup().equals(group))
                .collect(Collectors.toMap(Student::getLastName, Student::getFirstName,
                        BinaryOperator.minBy(String::compareTo)));
    }
}
