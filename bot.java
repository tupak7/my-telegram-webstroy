import java.util.List;

import javax.management.RuntimeErrorException;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery.AnswerCallbackQueryBuilder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class osintBot extends TelegramLongPollingBot {
    private boolean screaming = false;

    private InlineKeyboardMarkup keyBoardM1;
    private InlineKeyboardMarkup keyBoardM2;

    @Override
    public void onUpdateReceived(Update update) {            
        var userId = update.getMessage().getChatId();
        var msgId = update.getMessage().getMessageId();
        var msg = update.getMessage();

        if (update.hasCallbackQuery()) {
            var query = update.getCallbackQuery();
            var queryId = query.getId();
            var data = query.getData();

            buttonTap(userId, queryId, data, msgId);
            return;
        }
        
        var next = InlineKeyboardButton.builder()
                    .text("Next").callbackData("next")
                    .build();
        
        var back = InlineKeyboardButton.builder()
                    .text("Back").callbackData("back")
                    .build();
        
        var url = InlineKeyboardButton.builder()
                .text("Check email api")
                .url("https://haveibeenpwned.com/")
                .build();
                
        keyBoardM1 = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(next)).build();

        keyBoardM2 = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(back))
                .keyboardRow(List.of(url))
                .build();
        
        if(screaming)
            scream(userId, update.getMessage());
        else 
            copyMessage(userId, msgId);
        
        var txt = msg.getText();
        if(msg.isCommand()) {
            if(txt.equals("/leakchecking")) 
                screaming = true;
            else if (txt.equals("/menu"))
                sendMenu(userId, "<b>Menu 1</b>", keyBoardM1);
            return;
        }
        
    }
    public void sendMenu(Long who, String txt, InlineKeyboardMarkup kb) {
        SendMessage sm = SendMessage.builder().chatId(who.toString())
                .parseMode("HTML").text(txt)
                .replyMarkup(kb).build();

        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    public void scream(Long userId, Message msg) {
        if(msg.hasText())
            sendText(userId, msg.getText().toUpperCase());
        else
            copyMessage(userId, msg.getMessageId());
    }

    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                            .chatId(who.toString()) //Who are we sending a message to
                            .text(what).build();    //Message content
        try {
                execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
                throw new RuntimeException(e);      //Any error will be printed here
        }
    }

    public void copyMessage(Long who, Integer msgId) {
        CopyMessage cm = CopyMessage.builder()
                    .fromChatId(who.toString()) // copy user input
                .chatId(who.toString()) //send it to him
                .messageId(msgId)
                .build();
                
        try {
            execute(cm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "osintbot";
    }

    @Override
    public String getBotToken() {
        return "8595197693:AAFrVbRPpas6kA5p7dTQHAgn6gay-1qK7SM";
    }

    private void buttonTap(Long userId, String queryId, String data, int msgId) {

            EditMessageText newTxt = EditMessageText.builder()
                    .chatId(userId.toString())
                    .messageId(msgId).text("").build();
            
            EditMessageReplyMarkup newKb = EditMessageReplyMarkup.builder()
                    .chatId(userId.toString()).messageId(msgId).build();

            if (data.equals("next")) {
                newTxt.setText("Menu 2");
                newKb.setReplyMarkup(keyBoardM2);
            } else if (data.equals("back")) {
                newTxt.setText("Menu 1");
                newKb.setReplyMarkup(keyBoardM1);
            }
            
            
            AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                    .callbackQueryId(queryId).build();
            
            try {
                execute(close);
                execute(newTxt);
                execute(newKb);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
    }
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        osintBot bot = new osintBot();
        botsApi.registerBot(bot);

    }
}
  
