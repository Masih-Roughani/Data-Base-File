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
                case "Update" -> {
                    employeeConnection.updateUser();
                }
                case "Increase" -> {
                    employeeConnection.increaseCredit();
                }
                case "show" -> {
                    employeeConnection.showAllProgrammers();
                }
                case "Show" -> {
                    if (Objects.equals(EmployeeConnection.order[1], "full time programmers")) {
                        employeeConnection.showFullProgrammers();
                    } else if (Objects.equals(EmployeeConnection.order[1], "part time programmers")) {
                        employeeConnection.showHalfProgrammers();
                    } else if (EmployeeConnection.order.length == 3) {
                        employeeConnection.showSkill();
                    } else {
                        employeeConnection.showUser();
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
        } else if (Objects.equals(order[5], "FULL_TIME") || Objects.equals(order[5], "HALF_TIME")) {
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

    public void updateUser() throws SQLException {
        String sqlFormat = null;
        switch (order[1]) {
            case "name" -> {
                sqlFormat = String.format("update programmers set Name = '%s' where ID = %s", order[3], order[2]);
            }
            case "birth date" -> {
                sqlFormat = String.format("update programmers set BirthDate = '%s' where ID = %s", order[3] + "-" + order[4] + "-" + order[5], order[2]);
            }
            case "contract type" -> {
                sqlFormat = String.format("update programmers set ContractType = '%s' where ID = %s", order[3], order[2]);
            }
            case "income" -> {
                sqlFormat = String.format("update programmers set Income = %s where ID = %s", order[3], order[2]);
            }
        }
        execute(sqlFormat);
    }

    public void increaseCredit() throws SQLException {
        String sqlFormat;
        if (order.length == 4) {
            sqlFormat = String.format("update programmers set Income = Income+%s where Name = '%s' or Name = '%s'", order[3], order[1], order[2]);
        } else {
            sqlFormat = String.format("update programmers set Income = Income+%s where Name = '%s' and ContractType = '%s'", order[2], order[1], ContractType.FULL_TIME);
        }
        execute(sqlFormat);
    }

    public void showAllProgrammers() throws SQLException {
        String sqlFormat = String.format("select * from programmers");
        Statement statement = connection.prepareStatement(sqlFormat);
        ResultSet resultSet = statement.executeQuery(sqlFormat);
        while (resultSet.next()) {
            printInfo(resultSet);
        }
    }

    public void addSkillToUser() throws SQLException {
        String sqlFormat = String.format("insert into skills (employeeID,Skill) values (%s ,'%s')", order[2], order[3]);
        execute(sqlFormat);
    }

    public void showFullProgrammers() throws SQLException {
        String sqlFormat = String.format("select * from programmers where ContractType = '%s'", ContractType.FULL_TIME);
        ResultSet resultSet = executeQuery(sqlFormat);
        while (resultSet.next()) {
            printInfo(resultSet);
        }
    }

    public void showHalfProgrammers() throws SQLException {
        String sqlFormat = String.format("select * from programmers where ContractType = '%s'", ContractType.HALF_TIME);
        ResultSet resultSet = executeQuery(sqlFormat);
        while (resultSet.next()) {
            printInfo(resultSet);
        }
    }

    public void showSkill() throws SQLException {
        String sqlFormat = String.format("select skills.Skill from programmers inner join skills on programmers.ID = skills.employeeID where programmers.ID = %s", order[1]);
        ResultSet resultSet = executeQuery(sqlFormat);
        while (resultSet.next()) {
            System.out.println(resultSet.getString("Skill"));
        }
    }

    public void showUser() throws SQLException {
        String sqlFormat = String.format("select * from programmers where ID = %s", order[1]);
        ResultSet resultSet = executeQuery(sqlFormat);
        if (resultSet.next()) {
            System.out.println(resultSet.getString("Name") + " | " + (124 - resultSet.getDate("BirthDate").getYear()));
        }
    }

    public void printInfo(ResultSet resultSet) throws SQLException {
        System.out.print(resultSet.getString("ID") + "\t");
        System.out.print(resultSet.getString("Name") + "\t");
        System.out.print(resultSet.getString("BirthDate") + "\t");
        System.out.print(resultSet.getString("ContractType") + "\t");
        System.out.println(resultSet.getString("Income"));
    }

    public void execute(String sqlFormat) throws SQLException {
        Statement statement = connection.prepareStatement(sqlFormat);
        statement.execute(sqlFormat);
    }

    public ResultSet executeQuery(String sqlFormat) throws SQLException {
        Statement statement = connection.prepareStatement(sqlFormat);
        return statement.executeQuery(sqlFormat);
    }
}
//this type of coding is not safe because someone may attack and change the sqlFormat and drop the table.