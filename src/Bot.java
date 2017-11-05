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

    @SuppressWarnings("unchecked")
    @Override
    public void onUpdateReceived( Update message )
    {
        botCommands = getBotCommands();
        botPhrases = getBotPhrases();
        //System.out.println( botCommands.toString() );
        if( botPhrases != null )
            botPhrases.forEach( (k,v) -> System.out.println( v ) );
        if( message.getMessage() != null && botCommands != null && botPhrases != null )
        {
            String recivedMessageText = message.getMessage().getText();
            //System.out.println( recivedMessageText );
            if( botCommands.contains( recivedMessageText ) )
            {
                String botAnswer = botPhrases.get( recivedMessageText ) != null ?
                                        (String)botPhrases.get( recivedMessageText )
                                      : "¯|_(ツ)_/¯";
                sendMessage( message.getMessage(), botAnswer );
            }
            else if( recivedMessageText.startsWith( "/" ) )
            {
                sendMessage( message.getMessage(), "я не знаю этой команды ⊙︿⊙ " );
            }
        }
    }

    @Override
    public String getBotToken()
    {
        return "416583838:AAHzstBVDp5dUndmvqesXY0cbvPSY3Gu3R4";
        //Токен бота
    }

    private void sendMessage( Message message, String text )
    {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown( true );
        sendMessage.setChatId( message.getChatId().toString() );
        sendMessage.setReplyToMessageId( message.getMessageId() );
        sendMessage.setText( text );
        //System.out.println( message.getFrom() );
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
            return Yaml.loadType( new File( "configurations/botCommands.yml" ), ArrayList.class );
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
            return Yaml.loadType( new File( "configurations/botPhrases.yml" ), HashMap.class );
        }
        catch( FileNotFoundException e )
        {
            e.printStackTrace();
        }
        return null;
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
