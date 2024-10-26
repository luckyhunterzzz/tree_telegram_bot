package com.luckyhunterzzz.pandev_bot.command.commandImpl;

import com.luckyhunterzzz.pandev_bot.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddElementCommandTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private AddElementCommand addElementCommand;

    @Test
    void execute_addRootElement() {
        Update update = mockUpdate("/addElement RootElement");
        when(categoryService.addElement("RootElement")).thenReturn("Элемент \"RootElement\" добавлен в корневую категорию.");

        SendMessage response = (SendMessage) addElementCommand.execute(update);

        assertEquals("Элемент \"RootElement\" добавлен в корневую категорию.", response.getText());
    }

    @Test
    void execute_addChildElement() {
        Update update = mockUpdate("/addElement Parent Child");
        when(categoryService.addElement("Parent", "Child")).thenReturn("Элемент \"Child\" добавлен к родительскому элементу \"Parent\".");

        SendMessage response = (SendMessage) addElementCommand.execute(update);

        assertEquals("Элемент \"Child\" добавлен к родительскому элементу \"Parent\".", response.getText());
    }

    @Test
    void execute_invalidFormat() {
        Update update = mockUpdate("/addElement");

        SendMessage response = (SendMessage) addElementCommand.execute(update);

        assertEquals("Неправильный формат команды. Используйте: /addElement <название элемента> или /addElement <родитель> <дочерний>", response.getText());
    }

    private Update mockUpdate(String commandText) {
        Update update = new Update();
        Message message = mock(Message.class);
        when(message.getText()).thenReturn(commandText);
        when(message.getChatId()).thenReturn(12345L);
        update.setMessage(message);
        return update;
    }
}