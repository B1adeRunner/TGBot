import org.ho.yaml.Yaml;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;
//TODO: разбить файл на несколько, согласно слоеной архитектуре
public class Bot extends TelegramLongPollingBot {
    private static String url;
    private static String userName;
    private static String password;
    private static List<OpenBoobsContent> boobsContent;
    private static List<OpenBoobsContent> buttsContent;
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;
    private List allCommands;
    private List privateUserCommands;
    private Map botPhrases;

    private static final boolean isDataBaseEnabled = FALSE;
    private static final int MAX_BOOBS_PAGE_NUMBER = 648;
    private static final int MAX_BUTTS_PAGE_NUMBER = 332;

    public Bot() {
        try {
            Map connectionSettings = Yaml.loadType(new File("src/resources/connectionProfile.yml"), HashMap.class);
            //connectionSettings.forEach( ( k, v ) -> System.out.println( k.toString() + ": " + v ) );
            url = (String) connectionSettings.get("url");
            userName = (String) connectionSettings.get("user_name");
            password = (String) connectionSettings.get("user_password");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        OpenContentParser parser = new OpenContentParser();
        parser.doParseButts();
        parser.doParseBoobs();
        boobsContent = parser.getBoobsContent();
        buttsContent = parser.getButtsContent();
        TelegramBotsApi botAPI = new TelegramBotsApi();
        try {
            botAPI.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "AgentSmitBot";
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onUpdateReceived(Update message) {
        //TODO: апдейтить в отдельной джобе
        updateBotCommands();
        updateBotPhrases();

        if (message != null && message.hasMessage()) {
            Message msg = message.getMessage();
            parsingMessage(msg);

            if (msg.isCommand()) {
                if (!msg.getFrom().getBot() && isDataBaseEnabled) {
                    saveUserData(msg.getFrom());
                }
                commandProcess(msg);
            }
            if (msg.isChannelMessage()) {

            }
            if (msg.isGroupMessage() && isDataBaseEnabled) {
                if (!msg.getFrom().getBot())
                    saveUserData(msg.getFrom());
            }
            if (msg.isReply()) {

            }
            if (msg.isSuperGroupMessage()) {

            }
            if (msg.isUserMessage() && isDataBaseEnabled) {
                saveUserDataForBot(msg.getFrom().getId(), msg.getChatId());
                System.out.println("userMessage");
            }
        }
    }

    @Override
    public String getBotToken() {
        return "416583838:AAHzstBVDp5dUndmvqesXY0cbvPSY3Gu3R4";
    }

    @SuppressWarnings("unchecked")
    private void updateBotCommands() {
        try {
            List botCommands = Yaml.loadType(new File("src/resources/botCommands.yml"), ArrayList.class);
            allCommands = (List) (((Map) (botCommands.get(0))).get("allCommands"));
            privateUserCommands = (List) (((Map) (botCommands.get(0))).get("privateUserCommands"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void updateBotPhrases() {
        try {
            botPhrases = Yaml.loadType(new File("src/resources/botPhrases.yml"), HashMap.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void parsingMessage(Message message) {
        if (message.hasDocument()) {
            String sql = String.join("", "");
        } else if (message.hasEntities()) {

        } else if (message.hasInvoice()) {

        } else if (message.hasLocation()) {

        } else if (message.hasPhoto()) {

        } else if (message.hasSuccessfulPayment()) {

        } else if (message.hasText()) {

        }
    }

    private void commandProcess(Message message) {
        String receivedMessageText = message.getText() != null ? message.getText() : "";
        if (allCommands.contains(receivedMessageText)) {
            Boolean isNeedToAnswerUserPrivately = privateUserCommands.contains(receivedMessageText);
            sendOpenBoobsContent(message);
            //String botAnswer = botPhrases.get(receivedMessageText) != null ? (String) botPhrases.get(receivedMessageText) : "¯|_(ツ)_/¯";
            //sendMessage(message, botAnswer, isNeedToAnswerUserPrivately);
        } else if (receivedMessageText.startsWith("/")) {
            sendMessage(message, "я не знаю этой команды ⊙︿⊙ ", false);
            //TODO: прикруть логер сюда и выводить то, что пришло в лог
        }
    }

    private ContentType defineContent(Message msg) {
        return msg.getText().toUpperCase().contains(ContentType.BOOBS.name()) ? ContentType.BOOBS : ContentType.BUTTS;
    }

    private OpenBoobsContent fetchContent(ContentType contentType) {
        return contentType.equals(ContentType.BOOBS) ? getRandomBoobs() : getRandomButts();
    }

    private OpenBoobsContent getRandomBoobs() {
        return boobsContent.get((int) (Math.random() * MAX_BOOBS_PAGE_NUMBER));
    }

    private OpenBoobsContent getRandomButts() {
        return buttsContent.get((int) (Math.random() * MAX_BUTTS_PAGE_NUMBER));
    }

    private void sendOpenBoobsContent(Message msg) {
        try {
            ContentType contentType = defineContent(msg);
            OpenBoobsContent content = fetchContent(contentType);
            SendPhoto photo = new SendPhoto();
            photo.setChatId(msg.getChatId());
            photo.setPhoto(content.getUrl());
            photo.setCaption("rank: " + content.getRank());
            sendPhoto(photo);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(Message message, String text, Boolean isNeedToAnswerUserPrivately) {
        //String forWhomID = isNeedToAnswerUserPrivately ? message.getFrom().getId().toString() : message.getChatId().toString();
        //System.out.println( "forWhomID = " + forWhomID );
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        //sendMessage.setReplyToMessageId( message.getMessageId() );
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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
            } catch (SQLException se) { /*can't do anything */ }
            try {
                statement.close();
            } catch (SQLException se) { /*can't do anything */ }
            try {
                resultSet.close();
            } catch (SQLException se) { /*can't do anything */ }
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
            Boolean isUserChatIdExists = resultSet.next();
            if (!isUserChatIdExists) {
                statement.executeUpdate(insertValuesToUsersDataForBot);
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException se) { /*can't do anything */ }
            try {
                statement.close();
            } catch (SQLException se) { /*can't do anything */ }
            try {
                resultSet.close();
            } catch (SQLException se) { /*can't do anything */ }
        }
    }

    private void getBirthDaysToDay() {

    }

    private void getDollarExchangeRate() {

    }

    private void getBitCoinExchangeRate() {

    }
}
