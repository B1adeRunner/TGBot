import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot
{
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
        if( message.getMessage() != null )
        {
            switch( message.getMessage().getText() )
            {
                case "/help": sendMsg( message.getMessage(), "Для справки набери /help\nДля приветственного сообщения набери /start" ); break;
                case "/start": sendMsg( message.getMessage(), "Таки здравствуйте ＼(⌒▽⌒)／\nНабери /help, чтобы узнать, что я умею." ); break;

                default: sendMsg( message.getMessage(), "¯|_(ツ)_/¯" );
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
        } catch ( TelegramApiException e )
        {
            e.printStackTrace();
        }
    }
}
