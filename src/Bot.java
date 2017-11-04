import org.ho.yaml.Yaml;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Bot extends TelegramLongPollingBot
{
    private List botCommands;
    private Map botPhrases;

    public static void main( String[] args )
    {
        ApiContextInitializer.init(); // Инициализируем апи
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
        //возвращаем юзера
    }

    @Override
    public void onUpdateReceived( Update message )
    {
        botCommands = getBotCommands();
        botPhrases = getBotPhrases();
        System.out.println( botCommands.toString() );
        for( Iterator iter = botPhrases.keySet().iterator(); iter.hasNext(); )
        {
            System.out.println( botPhrases.get( iter.next() ) );
        }
        if( message.getMessage() != null && botCommands != null )
        {
            String recivedMessageText = message.getMessage().getText();
            System.out.println( recivedMessageText );
            if( botCommands.contains( recivedMessageText ) )
            {
                String botAnswer = botPhrases.get( recivedMessageText ) != null ?
                                        (String)botPhrases.get( recivedMessageText )
                                      : "¯|_(ツ)_/¯";
                sendMsg( message.getMessage(), botAnswer );
            }
        }
    }

    @Override
    public String getBotToken()
    {
        return "416583838:AAHzstBVDp5dUndmvqesXY0cbvPSY3Gu3R4";
        //Токен бота
    }

    private void sendMsg( Message message, String text )
    {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown( true );
        sendMessage.setChatId( message.getChatId().toString() );
        sendMessage.setReplyToMessageId( message.getMessageId() );
        sendMessage.setText( text );
        System.out.println( message.getFrom() );
        try
        {
            execute( sendMessage );
        }
        catch( TelegramApiException e )
        {
            e.printStackTrace();
        }
    }

    private List getBotCommands()
    {
        try
        {
            List entry = Yaml.loadType( new File( "configurations/botCommands.yml" ), ArrayList.class );
            return entry;
        }
        catch( FileNotFoundException e )
        {
            e.printStackTrace();
        }
        return null;
    }

    private Map getBotPhrases()
    {
        try
        {
            Map entry = Yaml.loadType( new File( "configurations/botPhrases.yml" ), HashMap.class );
            return entry;
        }
        catch( FileNotFoundException e )
        {
            e.printStackTrace();
        }
        return null;
    }
}
