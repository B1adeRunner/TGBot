import org.ho.yaml.Yaml;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.*;

public class Bot extends TelegramLongPollingBot
{
    private static final String url = "jdbc:mysql://localhost:3306/TGBotData";
    private static final String userName = "OnePointZero";
    private static final String password = "!A9z5Q!8jK23!";

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;
    private PreparedStatement preparedStatement;

    private List allCommands;
    private List privateUserCommands;
    private Map botPhrases;

    public static void main( String[] args )
    {
        ApiContextInitializer.init();
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }
        catch( InstantiationException e )
        {
            e.printStackTrace();
        }
        catch( IllegalAccessException e )
        {
            e.printStackTrace();
        }
        catch( ClassNotFoundException e )
        {
            e.printStackTrace();
        }
        TelegramBotsApi botapi = new TelegramBotsApi();
        try
        {
            botapi.registerBot( new Bot() );
        } catch ( TelegramApiException e )
        {
            e.printStackTrace();
        }
    }
    @Override
    public String getBotUsername()
    {
        return "AgentSmitBot";
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onUpdateReceived( Update message )
    {
        updateBotCommans();
        botPhrases = getBotPhrases();
        /*System.out.println( allBotCommands.toString() );
        if( botPhrases != null )
            botPhrases.forEach( (k,v) -> System.out.println( v ) );
        */

        if( message != null && message.hasMessage() )
        {
            Message msg = message.getMessage();
            parsingMessage( msg );

            if( msg.isCommand() )
            {
                if( !msg.getFrom().getBot() )
                    saveUserData( msg.getFrom() );
                commandProcess( msg );
            }
            if( msg.isChannelMessage() )
            {

            }
            if( msg.isGroupMessage() )
            {
                if( !msg.getFrom().getBot() )
                    saveUserData( msg.getFrom() );
            }
            if( msg.isReply() )
            {

            }
            if( msg.isSuperGroupMessage() )
            {

            }
            if( msg.isUserMessage() )
            {
                saveUserDataForBot( msg.getFrom().getId(), msg.getChatId() );
                System.out.println( "userMessage" );
            }
        }
    }

    @Override
    public String getBotToken()
    {
        return "416583838:AAHzstBVDp5dUndmvqesXY0cbvPSY3Gu3R4";
    }

    @SuppressWarnings("unchecked")
    private void updateBotCommans()
    {
        try
        {
            List botCommands = Yaml.loadType( new File( "configurations/botCommands.yml" ), ArrayList.class );
            botCommands.forEach( System.out::println );
            allCommands = ( List )( ( ( Map )( botCommands.get( 0 ) ) ).get( "allCommands" ) );
            privateUserCommands = ( List )( ( ( Map )( botCommands.get( 0 ) ) ).get( "privateUserCommands" ) );
        }
        catch( FileNotFoundException e )
        {
            e.printStackTrace();
        }
    }

    private Map getBotPhrases()
    {
        try
        {
            return Yaml.loadType( new File( "configurations/botPhrases.yml" ), HashMap.class );
        }
        catch( FileNotFoundException e )
        {
            e.printStackTrace();
        }
        return null;
    }

    private void parsingMessage( Message message )
    {
        if( message.hasDocument() )
        {
            String sql = String.join
                            (
                                "",
                                ""
                            );
        }
        else if( message.hasEntities() )
        {

        }
        else if( message.hasInvoice() )
        {

        }
        else if( message.hasLocation() )
        {

        }
        else if( message.hasPhoto() )
        {

        }
        else if( message.hasSuccessfulPayment() )
        {

        }
        else if( message.hasText() )
        {

        }
    }

    private void commandProcess( Message message )
    {
        String recivedMessageText = message.getText() != null ? message.getText() : "";
        //System.out.println( recivedMessageText );
        if( allCommands.contains( recivedMessageText ) )
        {
            Boolean isNeedToAnswerUserPrivately = privateUserCommands.contains( recivedMessageText );
            String botAnswer = botPhrases.get( recivedMessageText ) != null ?
                    (String)botPhrases.get( recivedMessageText )
                    : "¯|_(ツ)_/¯";
            sendMessage( message, botAnswer, isNeedToAnswerUserPrivately );
        }
        else if( recivedMessageText.startsWith( "/" ) )
        {
            sendMessage( message, "я не знаю этой команды ⊙︿⊙ ", false );
        }
    }

    private void sendMessage( Message message, String text , Boolean isNeedToAnswerUserPrivately )
    {
        //String forWhomID = isNeedToAnswerUserPrivately ? message.getFrom().getId().toString() : message.getChatId().toString();
        //System.out.println( "forWhomID = " + forWhomID );
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown( true );
        sendMessage.setChatId( message.getChatId().toString() );
        //sendMessage.setReplyToMessageId( message.getMessageId() );
        sendMessage.setText( text );
        /*System.out.println( message.getFrom() );
        System.out.println( message.getContact() );
        System.out.println( message.getAudio() );
        */
        try
        {
            execute( sendMessage );
        }
        catch( TelegramApiException e )
        {
            e.printStackTrace();
        }
    }

    private void saveUserData( User user )
    {
        try
        {
            connection = DriverManager.getConnection( url, userName, password );
            statement = connection.createStatement();
            String selectChatId = String.join
                    (
                        "",
                        " SELECT userName \n",
                        " FROM users \n",
                        " WHERE userId = " + user.getId() + " \n"
                    );
            String insertValuesToUsers = String.join
                    (
                        "",
                        " INSERT INTO users ",
                        " ( userId, firstName, lastName, userName ) ",
                        " VALUES ( " + user.getId() + ", '" +
                                      user.getFirstName() + "', '" +
                                      user.getLastName() + "', '" +
                                      user.getUserName() + "' ",
                        "        ) "
                    );
            resultSet = statement.executeQuery( selectChatId );
            Boolean isUserIdExists = resultSet.next();
            if( !isUserIdExists )
            {
                statement.executeUpdate( insertValuesToUsers );
            }

        }
        catch(SQLException sqlEx)
        {
            sqlEx.printStackTrace();
        }
        finally
        {
            try { connection.close(); } catch(SQLException se) { /*can't do anything */ }
            try { statement.close(); } catch(SQLException se) { /*can't do anything */ }
            try { resultSet.close(); } catch(SQLException se) { /*can't do anything */ }
        }
    }

    private void saveUserDataForBot( Integer userId, Long chatId )
    {
        try
        {
            connection = DriverManager.getConnection( url, userName, password );
            statement = connection.createStatement();
            String selectChatId = String.join
            (
                "",
                " SELECT * ",
                " FROM usersDataForBot ",
                " WHERE userId = " + userId
            );
            System.out.println( selectChatId + "    gfdgd" );
            String insertValuesToUsersDataForBot = String.join
            (
                "",
                " INSERT INTO usersDataForBot ",
                " ( userId, chatId ) ",
                " VALUES ( " + userId + ", " + chatId + " )"
            );
            resultSet = statement.executeQuery( selectChatId );
            Boolean isUserChatIdExists = resultSet.next();
            if( !isUserChatIdExists )
            {
                statement.executeUpdate( insertValuesToUsersDataForBot );
            }

        }
        catch(SQLException sqlEx)
        {
            sqlEx.printStackTrace();
        }
        finally
        {
            try { connection.close(); } catch(SQLException se) { /*can't do anything */ }
            try { statement.close(); } catch(SQLException se) { /*can't do anything */ }
            try { resultSet.close(); } catch(SQLException se) { /*can't do anything */ }
        }
    }

    private void getBirthDaysToDay()
    {

    }

    private void getDollarExchangeRate()
    {

    }

    private void getBitCoinExchangeRate()
    {

    }
}
