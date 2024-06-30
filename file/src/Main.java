import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

    }
}

@Getter
@Setter
class Person {
    String name;
    String lastName;
    int ID;
    LocalDate birthDate;

    public Person(String name, String lastName, int ID, LocalDate birthDate) {
        this.name = name;
        this.lastName = lastName;
        this.ID = ID;
        this.birthDate = birthDate;
    }

    public String showPersonInfo() {
        return "Name : " + name + "\r\n" + "Last name : " + lastName + "\r\n"
                + "ID : " + ID + "\r\n" + "Birthday date : " + birthDate;
    }
}

class Admin {
    @Getter
    static Admin admin = new Admin("rebel guy");
    String password;

    private Admin(String password) {
        this.password = password;
    }
}