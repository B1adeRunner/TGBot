package ru.skynet.bot;

public class DataBaseObj {
/*
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    private void saveUserData(User user) {
        try {
            connection = DriverManager.getConnection(url, userName, password);
            statement = connection.createStatement();
            String selectChatId = String.join("", " SELECT userName \n", " FROM users \n", " WHERE userId = " + user.getId() + " \n");
            String insertValuesToUsers = String.join("", " INSERT INTO users ", " ( userId, firstName, lastName, userName ) ",
                    " VALUES ( " + user.getId() + ", '" + user.getFirstName() + "', '" + user.getLastName() + "', '" + user.getUserName()
                            + "' ", "        ) ");
            resultSet = statement.executeQuery(selectChatId);
            boolean isUserIdExists = resultSet.next();
            if (!isUserIdExists) {
                statement.executeUpdate(insertValuesToUsers);
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException se) {  }
            try {
                statement.close();
            } catch (SQLException se) {  }
            try {
                resultSet.close();
            } catch (SQLException se) {  }
        }
    }

    private void saveUserDataForBot(Integer userId, Long chatId) {
        try {
            connection = DriverManager.getConnection(url, userName, password);
            statement = connection.createStatement();
            String selectChatId = String.join("", " SELECT * ", " FROM usersDataForBot ", " WHERE userId = " + userId);
            String insertValuesToUsersDataForBot = String
                    .join("", " INSERT INTO usersDataForBot ", " ( userId, chatId ) ", " VALUES ( " + userId + ", " + chatId + " )");
            resultSet = statement.executeQuery(selectChatId);
            boolean isUserChatIdExists = resultSet.next();
            if (!isUserChatIdExists) {
                statement.executeUpdate(insertValuesToUsersDataForBot);
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException se) {  }
            try {
                statement.close();
            } catch (SQLException se) {  }
            try {
                resultSet.close();
            } catch (SQLException se) {  }
        }
    }*/
}
