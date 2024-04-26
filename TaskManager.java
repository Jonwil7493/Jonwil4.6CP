import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TaskManager {
    private ArrayList<Task> tasks;

    public TaskManager() {
        tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void updateTask(int index, Task updatedTask) {
        if (index >= 0 && index < tasks.size()) {
            tasks.set(index, updatedTask);
        } else {
            System.out.println("Invalid task index.");
        }
    }

    public void deleteTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
        } else {
            System.out.println("Invalid task index.");
        }
    }

    public void displayTasks() {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("Task " + (i + 1) + ": " + tasks.get(i).getTitle());
        }
    }

    public void displayTaskDetails(int index) {
        if (index >= 0 && index < tasks.size()) {
            System.out.println("Task Details:");
            System.out.println(tasks.get(index));
        } else {
            System.out.println("Invalid task index.");
        }
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void saveTasksToDatabase() {
        try {
            DatabaseManager.connect();
            DatabaseManager.createTables();

            // Insert tasks into the database
            for (Task task : tasks) {
                String insertTaskSQL = "INSERT INTO tasks (title, description, due_date, priority, status) " +
                                       "VALUES (?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = DatabaseManager.getConnection().prepareStatement(insertTaskSQL);
                preparedStatement.setString(1, task.getTitle());
                preparedStatement.setString(2, task.getDescription());
                preparedStatement.setDate(3, new java.sql.Date(task.getDueDate().getTime()));
                preparedStatement.setString(4, task.getPriority().toString());
                preparedStatement.setString(5, task.getStatus().toString());
                preparedStatement.executeUpdate();
            }

            DatabaseManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadTasksFromDatabase() {
        try {
            DatabaseManager.connect();

            String selectTasksSQL = "SELECT * FROM tasks";
            PreparedStatement preparedStatement = DatabaseManager.getConnection().prepareStatement(selectTasksSQL);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                java.util.Date dueDate = resultSet.getDate("due_date");
                Priority priority = Priority.valueOf(resultSet.getString("priority"));
                Status status = Status.valueOf(resultSet.getString("status"));

                Task task = new Task(title, description, dueDate, priority, status);
                tasks.add(task);
            }

            DatabaseManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
