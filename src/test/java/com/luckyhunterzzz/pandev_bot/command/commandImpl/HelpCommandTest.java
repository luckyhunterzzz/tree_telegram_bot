package com.luckyhunterzzz.pandev_bot.command.commandImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HelpCommandTest {

    @InjectMocks
    private HelpCommand helpCommand;

    @Test
    void execute_helpMessage() {
        Update update = mockUpdate();
        SendMessage response = (SendMessage) helpCommand.execute(update);

        assertTrue(response.getText().contains("/viewTree"));
        assertTrue(response.getText().contains("/addElement"));
    }

    private Update mockUpdate() {
        Update update = new Update();
        Message message = mock(Message.class);
        when(message.getChatId()).thenReturn(12345L);
        update.setMessage(message);
        return update;
    }
}