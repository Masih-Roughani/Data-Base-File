import java.sql.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        EmployeeConnection employeeConnection = new EmployeeConnection();
        employeeConnection.executeSQL();
        Scanner scanner = new Scanner(System.in);
        String inp = "start";
        while (!Objects.equals(inp, "end")) {
            inp = scanner.nextLine();
            EmployeeConnection.order = inp.split("-");
            switch (EmployeeConnection.order[0]) {
                case "add" -> {
                    if (Objects.equals(EmployeeConnection.order[1], "skill")) {
                        employeeConnection.addSkillToUser();
                    } else {
                        try {
                            employeeConnection.addUser();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
        }
        System.out.print("Done");
    }
}

enum ContractType {
    FULL_TIME, HALF_TIME
}

class EmployeeConnection {
    public static String[] order;
    private Connection connection;

    void executeSQL() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String URL = "jdbc:mysql://localhost/TestDb";
        String userName = "root";
        String password = "1274432545";
        connection = DriverManager.getConnection(URL, userName, password);
    }

    public void addUser() throws Exception {
        String sqlFormat;
        if (order.length == 7 && Integer.parseInt(order[6]) < 0) {
            throw new Exception("The income must be more that 0");
        } else if (2024 - Integer.parseInt(order[2]) < 18) {
            throw new Exception("You are too young");
        } else if (!checkFormat(order[2], order[3], order[4])) {
            throw new Exception("Date format is wrong");
        } else if (order.length == 7) {
            sqlFormat = String.format("insert into programmers (Name,BirthDate,ContractType,Income) values ('%s','%s','%s',%s)", order[1], order[2] + "-" + order[3] + "-" + order[4], order[5], order[6]);
        } else if (Objects.equals(order[5], "FULL_TIME") || Objects.equals(order[5],"HALF_TIME")) {
            sqlFormat = String.format("insert into programmers (Name,BirthDate,ContractType) values ('%s','%s','%s')", order[1], order[2] + "-" + order[3] + "-" + order[4], order[5]);
        } else {
            sqlFormat = String.format("insert into programmers (Name,BirthDate,Income) values ('%s','%s',%s)", order[1], order[2] + "-" + order[3] + "-" + order[4], order[5]);
        }
        execute(sqlFormat);
    }

    public boolean checkFormat(String year, String month, String day) {
        if (Integer.parseInt(year) > 2024 || Integer.parseInt(year) < 1984) {
            return false;
        } else if (Integer.parseInt(month) > 12 || Integer.parseInt(month) < 1) {
            return false;
        }
        return Integer.parseInt(day) <= 31 && Integer.parseInt(day) >= 1;
    }
    public void addSkillToUser() throws SQLException {
        String sqlFormat = String.format("insert into skills (employeeID,Skill) values (%s ,'%s')", order[2], order[3]);
        execute(sqlFormat);
    }

    public void execute(String sqlFormat) throws SQLException {
        Statement statement = connection.prepareStatement(sqlFormat);
        statement.execute(sqlFormat);
//      this type of coding is not safe because someone may attack and change the sqlFormat and drop the table.
    }
}