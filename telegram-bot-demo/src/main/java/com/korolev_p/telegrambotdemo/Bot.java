package com.korolev_p.telegrambotdemo;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Bot extends TelegramLongPollingCommandBot {
    private final String BOT_NAME;
    private final String BOT_TOKEN;

    //Настройки по умолчанию
    @Getter
    private static final Settings defaultSettings = new Settings(1, 15, 1);

    //Класс для обработки сообщений, не являющихся командой
    private final NonCommand nonCommand;

    /** Настройки файла для разных пользователей. Ключ - уникальный id чата */
    @Getter
    private static Map<Long, Settings> userSettings;

    public Bot(String botName, String botToken) {
        super();
        this.BOT_NAME = botName;
        this.BOT_TOKEN = botToken;
        //создаём вспомогательный класс для работы с сообщениями, не являющимися командами
        this.nonCommand = new NonCommand();
        //регистрируем комманды
        register(new StartCommand("start", "Старт"));
        register(new PlusCommand("plus", "Сложение"));
        register(new MinusCommand("minus", "Вычитание"));
        register(new PlusMinusCommand("plusminus", "Сложение и вычитание"));
        register(new HelpCommand("help", "Помощь"));
        register(new SettingsCommand("settings", "Мои настройки"));
        userSettings = new HashMap<>();
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @SneakyThrows
    @Override
    public void processNonCommandUpdate(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId();
        var userName = getUserName(msg);

        var answer = nonCommand.nonCommandExecute(chatId, userName, msg.getText());
        setAnswer(chatId, userName, answer);
    }

    public static Settings getUserSettings(Long chatId) {
        var userSettings = Bot.getUserSettings();
        var settings = userSettings.get(chatId);
        return settings == null ? defaultSettings : settings;
    }

    private String getUserName(Message msg) {
        var user = msg.getFrom();
        var userName = user.getUserName();
        return userName != null ? userName : String.format("%s %s", user.getLastName(), user.getFirstName());
    }

    private void setAnswer(Long chatId, String userName, String text) throws TelegramApiException {
        var answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error execute answer={}, error message={}", answer, e.getMessage());
            throw e;
        }
    }
}
