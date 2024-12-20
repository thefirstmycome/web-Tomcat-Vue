package org.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PersonDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/userdata?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL,  USER, PASSWORD);
    }

    public boolean register(String account, String username, String password) {
        String jsonInput = "{ \"PaperPublish\": [], \"PaperCollect\": [], \"PaperDesign\": [ { \"SurveyID\": 1, \"SurveyName\": \"调查问卷案例数据\", \"SurveyCreateTime\": \"2023/04/01 12:00:00\", \"SurveyPublishTime\": \"\", \"SurveyStopCTime\": \"\", \"SurveyData\": [ { \"Type\": \"sigle\", \"Question\": \"单项选择案例\", \"QuestionID\": 1, \"Required\": true, \"Options\": [ { \"Option\": \"选项1\", \"Value\": 1 }, { \"Option\": \"选项2\", \"Value\": 2 }, { \"Option\": \"选项3\", \"Value\": 3 }, { \"Option\": \"选项4\", \"Value\": 4 } ], \"Answer\": 0, \"CollectAnswers\": [] }, { \"Type\": \"multiple\", \"Question\": \"多项选择案例\", \"QuestionID\": 2, \"Required\": true, \"Options\": [ { \"Option\": \"多选项1\", \"Value\": 1 }, { \"Option\": \"选项2\", \"Value\": 2 }, { \"Option\": \"选项3\", \"Value\": 3 }, { \"Option\": \"选项4\", \"Value\": 4 } ], \"Answers\": [], \"CollectAnswers\": [] }, { \"Type\": \"text\", \"Question\": \"文本案例\", \"QuestionID\": 3, \"Required\": false, \"Answers\": \"\", \"CollectAnswers\": [] } ] } ] }";
        ObjectMapper objectMapper = new ObjectMapper();
        String sql = "INSERT INTO datatest1 (Account, UserName, Password, Remark, Profile) VALUES (?, ?, ?, '正常用户', ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))  {
            Object profileData = objectMapper.readValue(jsonInput,  Object.class);
            String profileJson = objectMapper.writeValueAsString(profileData);
            statement.setString(1,  account);
            statement.setString(2,  username);
            statement.setString(3,  password);
            statement.setString(4,  profileJson);
            int count = statement.executeUpdate();
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean login(String account, String password) {
        String sql = "SELECT * FROM datatest1 WHERE account = ? AND password = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))  {
            statement.setString(1,  account);
            statement.setString(2,  password);
            try (ResultSet resultSet = statement.executeQuery())  {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(String UserName, String Password, String Avatar, String Account) {
        String sql = "UPDATE datatest1 SET UserName = ?, Password = ?, Avatar = ? WHERE Account = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))  {
            statement.setString(1,  UserName);
            statement.setString(2,  Password);
            statement.setString(3,  Avatar);
            statement.setString(4,  Account);
            try (ResultSet resultSet = statement.executeQuery())  {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean changeStutes(String Account) {
        String sql = "UPDATE datatest1 SET NewUser = ? WHERE Account = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))  {
            statement.setBoolean(1,  false);
            statement.setString(2,  Account);

            int count = statement.executeUpdate();
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String GetServerMySQL(String Account) {
        Map<String, Object> res = new HashMap<>();
        String sql = "SELECT * FROM datatest1 WHERE Account = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))  {
            statement.setString(1,  Account);
            try (ResultSet resultSet = statement.executeQuery())  {
                if (resultSet.next())  {
                    res.put("UserName",  resultSet.getString("UserName"));
                    res.put("NewUser",  resultSet.getBoolean("NewUser"));
                    res.put("Avatar",  resultSet.getString("Avatar"));
                    res.put("Account",  resultSet.getString("Account"));
                    res.put("DefaultAvatar",  resultSet.getString("DefaultAvatar"));
                    res.put("Profile",  resultSet.getString("Profile"));
                } else {
                    res.put("message",  "No user found with the given Account");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            res.put("error",  "Database error: " + e.getMessage());
        }

        // 将Map转换为JSON字符串
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(res);
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Failed to convert to JSON: " + e.getMessage()  + "\"}";
        }
    }

    public boolean UpdateServerMySQL(String Account, String Profile) {
        String sql = "UPDATE datatest1 SET Profile = ? WHERE Account = ?";
        ObjectMapper objectMapper = new ObjectMapper();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))  {
            Object profileData = objectMapper.readValue(Profile,  Object.class);
            String profileJson = objectMapper.writeValueAsString(profileData);
            statement.setString(1,  profileJson);
            statement.setString(2,  Account);

            int count = statement.executeUpdate();
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}