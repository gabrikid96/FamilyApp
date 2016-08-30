package grodrich.grc.familyapp.model;

/**
 * Created by gabri on 27/08/16.
 */
public class User {

    private String name;
    private int age;
    private String email;
    private String id;

    public User() {
    }

    public User(String name, int age, String email, String id) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.id = id;
    }

    public User(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
