package com.korolev_p.telegrambotdemo;

import java.util.Set;

public class NonCommand {


    public String nonCommandExecute(Long chatId, String userName, String text) {
        Settings settings;
        String answer;
        try {
            //создаём настройки из сообщения пользователя
            settings = createSettings(text);
            //добавляем настройки в мапу, чтобы потом их использовать для этого пользователя при генерации файла
            saveUserSettings(chatId, settings);
            answer = "Настройки обновлены. Вы всегда можете их посмотреть с помощью /settings";
//            todo: не понятно откуда ошибка
//        } catch (IllegalSettingsException e) {
//            answer = e.getMessage() +
//                    "\n\n Настройки не были изменены. Вы всегда можете их посмотреть с помощью /settings";
            //логируем событие, используя userName
        } catch (Exception e) {
            answer = "Простите, я не понимаю Вас. Возможно, Вам поможет /help";
            //логируем событие, используя userName
        }
    }
}
