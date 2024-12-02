import java.io.*;
import java.util.*;

// Abstract User class
abstract class User implements Serializable {
    private final String name;
    private final String username;
    private final String password;
    private List<Course> courses;

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.courses = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public abstract void enroll(Course course);

    public abstract void drop(Course course);

    public void viewCourses() {
        System.out.println("Courses enrolled in:");
        for (Course course : courses) {
            System.out.println("- " + course.getName());
        }
    }
}

// Student class
class Student extends User {
    public Student(String name, String username, String password) {
        super(name, username, password);
    }

    @Override
    public void enroll(Course course) {
        if (!getCourses().contains(course)) {
            getCourses().add(course);
            course.addStudent(this);
            System.out.println("Successfully enrolled in " + course.getName());
        } else {
            System.out.println("Already enrolled in " + course.getName());
        }
    }

    @Override
    public void drop(Course course) {
        if (getCourses().contains(course)) {
            getCourses().remove(course);
            course.removeStudent(this);
            System.out.println("Successfully dropped " + course.getName());
        } else {
            System.out.println("Not enrolled in " + course.getName());
        }
    }

    public void checkGrades() {
        System.out.println("Grades:");
        for (Course course : getCourses()) {
            System.out.println(course.getName() + ": " + course.getGrade(this));
        }
    }
}

// Teacher class
class Teacher extends User {
    public Teacher(String name, String username, String password) {
        super(name, username, password);
    }

    @Override
    public void enroll(Course course) {
        if (!getCourses().contains(course)) {
            getCourses().add(course);
            System.out.println("Successfully enrolled in " + course.getName());
        } else {
            System.out.println("Already enrolled in " + course.getName());
        }
    }

    @Override
    public void drop(Course course) {
        if (getCourses().contains(course)) {
            getCourses().remove(course);
            course.setTeacher(null);
            System.out.println("Successfully dropped " + course.getName());
        } else {
            System.out.println("Not enrolled in " + course.getName());
        }
    }

    public void addStudent(Student student, Course course) {
        if (getCourses().contains(course)) {
            student.enroll(course);
        } else {
            System.out.println("You are not enrolled in " + course.getName());
        }
    }

    public void removeStudent(Student student, Course course) {
        if (getCourses().contains(course)) {
            student.drop(course);
        } else {
            System.out.println("You are not enrolled in " + course.getName());
        }
    }

    public void assignGrade(Student student, Course course, int grade) {
        if (getCourses().contains(course)) {
            course.setGrade(student, grade);
        } else {
            System.out.println("You are not enrolled in " + course.getName());
        }
    }

    public void viewStudents(Course course) {
        if (getCourses().contains(course)) {
            System.out.println("Students enrolled in " + course.getName() + ":");
            for (Student student : course.getStudents()) {
                System.out.println("- " + student.getName());
            }
        } else {
            System.out.println("You are not enrolled in " + course.getName());
        }
    }
}

// Course class
class Course implements Serializable {
    private String name;
    private Teacher teacher;
    private List<Student> students;
    private Map<Student, Integer> grades;

    public Course(String name) {
        this.name = name;
        this.students = new ArrayList<>();
        this.grades = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(Student student) {
        students.remove(student);
        grades.remove(student);
    }

    public int getGrade(Student student) {
        return grades.getOrDefault(student, -1);
    }

    public void setGrade(Student student, int grade) {
        grades.put(student, grade);
    }
}

// Admin class
class Admin {
    public List<Student> students;
    public List<Teacher> teachers;
    public List<Course> courses;

    public Admin() {
        this.students = new ArrayList<>();
        this.teachers = new ArrayList<>();
        this.courses = new ArrayList<>();
    }

    public void createStudent(String name, String username, String password) {
        Student student = new Student(name, username, password);
        if (!isUsernameExists(username)) {
            students.add(student);
            System.out.println("Student account created successfully.");
        } else {
            System.out.println("Username already exists.");
        }
    }

    public void deleteStudent(Student student) {
        for (Course course : student.getCourses()) {
            course.removeStudent(student);
        }
        students.remove(student);
        System.out.println("Student account deleted successfully.");
    }

    public void createTeacher(String name, String username, String password) {
        Teacher teacher = new Teacher(name, username, password);
        if (!isUsernameExists(username)) {
            teachers.add(teacher);
            System.out.println("Teacher account created successfully.");
        } else {
            System.out.println("Username already exists.");
        }
    }

    public void deleteTeacher(Teacher teacher) {
        for (Course course : teacher.getCourses()) {
            course.setTeacher(null);
        }
        teachers.remove(teacher);
        System.out.println("Teacher account deleted successfully.");
    }

    public void createCourse(String name) {
        Course course = new Course(name);
        courses.add(course);
        System.out.println("Course created successfully.");
    }

    public void deleteCourse(Course course) {
        for (Student student : course.getStudents()) {
            student.drop(course);
        }
        if (course.getTeacher() != null) {
            course.getTeacher().drop(course);
        }
        courses.remove(course);
        System.out.println("Course deleted successfully.");
    }

    public void viewAllStudents() {
        System.out.println("Students:");
        for (Student student : students) {
            System.out.println("- " + student.getName());
        }
    }

    public void viewAllTeachers() {
        System.out.println("Teachers:");
        for (Teacher teacher : teachers) {
            System.out.println("- " + teacher.getName());
        }
    }

    public void viewAllCourses() {
        System.out.println("Courses:");
        for (Course course : courses) {
            System.out.println("- " + course.getName());
        }
    }

    private boolean isUsernameExists(String username) {
        for (Student student : students) {
            if (student.getUsername().equals(username)) {
                return true;
            }
        }
        for (Teacher teacher : teachers) {
            if (teacher.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}

// Main class
public class SchoolManagementSystem {
    private static Admin admin;
    private static Scanner scanner;

    public static void main(String[] args) {
        admin = new Admin();
        scanner = new Scanner(System.in);

        // Initialize admin account
        admin.createStudent("Admin", "admin", "admin");

        // Create some sample data
        admin.createTeacher("John Doe", "jdoe", "password");
        admin.createCourse("Mathematics");
        admin.createCourse("Science");
        admin.viewAllStudents();
        admin.viewAllTeachers();
        admin.viewAllCourses();

        // Load data from file
        loadData();

        boolean exit = false;
        while (!exit) {
            displayLoginMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
                    break;
            }
        }

        // Save data to file
        saveData();
    }

    private static void loadData() {
        try {
            FileInputStream fileIn = new FileInputStream("data.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            admin = (Admin) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Data loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    private static void saveData() {
        try {
            FileOutputStream fileOut = new FileOutputStream("data.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(admin);
            out.close();
            fileOut.close();
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private static void displayLoginMenu() {
        System.out.println("=== School Management System ===");
        System.out.println("1. Login");
        System.out.println("2. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = (User) findUser(username, password);
        if (user == null) {
            System.out.println("Invalid username or password.");
            return;
        }

        if (user instanceof Student) {
            displayStudentMenu((Student) user);
        } else if (user instanceof Teacher) {
            displayTeacherMenu((Teacher) user);
        } else {
            displayAdminMenu();
        }
    }

    private static void displayAdminMenu() {
    }

    private static Object findUser(String username, String password) {
        for (Student student : admin.students) {
            if (student.getUsername().equals(username) && student.getPassword().equals(password)) {
                return student;
            }
        }
        for (Teacher teacher : admin.teachers) {
            if (teacher.getUsername().equals(username) && teacher.getPassword().equals(password)) {
                return teacher;
            }
        }
        if (username.equals("admin") && password.equals("admin")) {
            return admin;
        }
        return null;
    }

    private static void displayStudentMenu(Student student) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== Student Menu ===");
            System.out.println("1. Enroll in a course");
            System.out.println("2. Drop a course");
            System.out.println("3. Check grades");
            System.out.println("4. View courses");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    enrollCourse(student);
                    break;
                case 2:
                    dropCourse(student);
                    break;
                case 3:
                    student.checkGrades();
                    break;
                case 4:
                    student.viewCourses();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
                    break;
            }
        }
    }

    private static void enrollCourse(Student student) {
        System.out.println("Available courses:");
        for (Course course : admin.courses) {
            System.out.println("- " + course.getName());
        }
        System.out.print("Enter the course name to enroll: ");
        String courseName = scanner.nextLine();
        Course course = findCourse(courseName);
        if (course != null) {
            student.enroll(course);
        } else {
            System.out.println("Course not found.");
        }
    }

    private static void dropCourse(Student student) {
        System.out.println("Enrolled courses:");
        student.viewCourses();
        System.out.print("Enter the course name to drop: ");
        String courseName = scanner.nextLine();
        Course course = findCourse(courseName);
        if (course != null && student.getCourses().contains(course)) {
            student.drop(course);
        } else {
            System.out.println("You are not enrolled in this course.");
        }
    }

    private static Course findCourse(String courseName) {
        for (Course course : admin.courses) {
            if (course.getName().equalsIgnoreCase(courseName)) {
                return course;
            }
        }
        return null;
    }

    private static void displayTeacherMenu(Teacher teacher) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== Teacher Menu ===");
            System.out.println("1. Enroll in a course");
            System.out.println("2. Drop a course");
            System.out.println("3. Add a student to a course");
            System.out.println("4. Remove a student from a course");
            System.out.println("5. Assign grade");
            System.out.println("6. View students in a course");
            System.out.println("7. View courses");
            System.out.println("8. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    enrollCourse(teacher);
                    break;
                case 2:
                    dropCourse(teacher);
                    break;
                case 3:
                    addStudentToCourse(teacher);
                    break;
                case 4:
                    removeStudentFromCourse(teacher);
                    break;
                case 5:
                    assignGrade(teacher);
                    break;
                case 6:
                    viewStudentsInCourse(teacher);
                    break;
                case 7:
                    teacher.viewCourses();
                    break;
                case 8:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
                    break;
            }
        }
    }


    private static void removeStudentFromCourse(Teacher teacher) {
    }

    private static void enrollCourse(Teacher teacher) {
        System.out.println("Available courses:");
        for (Course course : admin.courses) {
            System.out.println("- " + course.getName());
        }
        System.out.print("Enter the course name to enroll: ");
        String courseName = scanner.nextLine();
        Course course = findCourse(courseName);
        if (course != null) {
            teacher.enroll(course);
        } else {
            System.out.println("Course not found.");
        }
    }

    private static void dropCourse(Teacher teacher) {
        System.out.println("Courses you are enrolled in:");
        teacher.viewCourses();
        System.out.print("Enter the course name: ");
        String courseName = scanner.nextLine();
        Course course = findCourse(courseName);
        if (course != null && teacher.getCourses().contains(course)) {
            System.out.println("Students enrolled in " + course.getName() + ":");
            for (Student student : course.getStudents()) {
                System.out.println("- " + student.getName());
            }
            System.out.print("Enter the student name to remove: ");
            String studentName = scanner.nextLine();
            Student student = findStudent(studentName);
            if (student != null && course.getStudents().contains(student)) {
                teacher.removeStudent(student, course);
            } else {
                System.out.println("Student not found in this course.");
            }
        } else {
            System.out.println("You are not enrolled in this course.");
        }
    }

    private static Student findStudent(String studentName) {
        return null;
    }

    private static void assignGrade(Teacher teacher) {
        System.out.println("Courses you are enrolled in:");
        teacher.viewCourses();
        System.out.print("Enter the course name: ");
        String courseName = scanner.nextLine();
        Course course = findCourse(courseName);
        if (course != null && teacher.getCourses().contains(course)) {
            System.out.println("Students enrolled in " + course.getName() + ":");
            for (Student student : course.getStudents()) {
                System.out.println("- " + student.getName());
            }
            System.out.print("Enter the student name: ");
            String studentName = scanner.nextLine();
            Student student = findStudent(studentName);
            if (student != null && course.getStudents().contains(student)) {
                System.out.print("Enter the grade: ");
                int grade = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
                teacher.assignGrade(student, course, grade);
            } else {
                System.out.println("Student not found in this course.");
            }
        } else {
            System.out.println("You are not enrolled in this course.");
        }
    }

    private static void viewStudentsInCourse(Teacher teacher) {
        System.out.println("Courses you are enrolled in:");
        teacher.viewCourses();
        System.out.print("Enter the course name: ");
        String courseName = scanner.nextLine();
        Course course = findCourse(courseName);
        if (course != null && teacher.getCourses().contains(course)) {
            teacher.viewStudents(course);
        } else {
            System.out.println("You are not enrolled in this course.");
        }
    }

    private static void addStudentToCourse(Teacher teacher) {
        System.out.println("Courses you are enrolled in:");
        teacher.viewCourses();
        System.out.print("Enter the course name: ");
        String courseName = scanner.nextLine();
        Course course = findCourse(courseName);
        if (course != null && teacher.getCourses().contains(course)) {
            System.out.println("Available students:");
            for (Student student : admin.students) {
                if (!course.getStudents().contains(student)) {
                    System.out.println("- " + student.getName());
                }
            }
            System.out.print("Enter the student name to add: ");
            String studentName = scanner.nextLine();
            Student student = findStudent(studentName);
            if (student != null) {
                teacher.addStudent(student, course);
            } else {
                System.out.println("Student not found.");
            }
        } else {
            System.out.println("You are not enrolled in this course.");
        }
    }
}
