package grodrich.grc.familyapp.model;

/**
 * Created by gabri on 27/08/16.
 */
public class User {

    private String name;
    private int age;
    private String email;
    private String id;
    private String familyId;

    public User() {
    }

    public User(String name, int age, String email, String id) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.id = id;
        this.familyId = "";
    }

    public User(String name, int age, String email, String id,String familyId) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.id = id;
        this.familyId = familyId;
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

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public boolean hasFamily(){
        return !familyId.equals("");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return !(id != null ? !id.equals(user.id) : user.id != null);

    }

    @Override
    public int hashCode() {
        return 0;
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
