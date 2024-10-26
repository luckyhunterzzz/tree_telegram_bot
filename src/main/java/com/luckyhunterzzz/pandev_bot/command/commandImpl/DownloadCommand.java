package com.luckyhunterzzz.pandev_bot.command.commandImpl;

import com.luckyhunterzzz.pandev_bot.command.Command;
import com.luckyhunterzzz.pandev_bot.command.DocumentSender;
import com.luckyhunterzzz.pandev_bot.model.entities.Category;
import com.luckyhunterzzz.pandev_bot.service.CategoryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Команда для скачивания дерева категорий в формате Excel.
 *
 * Этот класс реализует интерфейс {@link Command} и предоставляет функциональность
 * для создания Excel файла с деревом категорий и отправки его пользователю.
 */
@Slf4j
@Setter
@Getter
@RequiredArgsConstructor
@Component
public class DownloadCommand implements Command {

    private final CategoryService categoryService;
    private DocumentSender documentSender;

    /**
     * Выполняет команду скачивания дерева категорий.
     *
     * Этот метод создает Excel файл, содержащий информацию о категориях,
     * и отправляет его пользователю. Он выполняет следующие шаги:
     * <ol>
     *     <li>Создает новый Excel файл с листом, названным "tree".</li>
     *     <li>Добавляет заголовки столбцов: "ID", "Name" и "Parent ID".</li>
     *     <li>Извлекает список всех категорий из {@link CategoryService}.</li>
     *     <li>Заполняет строки файла данными о категориях, включая их идентификаторы, имена и идентификаторы родительских категорий.</li>
     *     <li>Записывает содержимое файла в выходной поток.</li>
     *     <li>Создает {@link InputFile} для отправки через Telegram API.</li>
     *     <li>Отправляет файл пользователю с помощью {@link DocumentSender}.</li>
     * </ol>
     * Если возникает ошибка при создании или отправке файла, метод возвращает сообщение об ошибке.
     *
     * @param update входящее обновление от Telegram, содержащее информацию о сообщении,
     *               включая идентификатор чата, в который будет отправлен файл
     * @return объект {@link SendMessage}, представляющий ответ о результате операции.
     *         Если файл успешно отправлен, возвращается сообщение "Отправлен файл.",
     *         в противном случае возвращается сообщение об ошибке.
     */
    @Override
    public BotApiMethod<?> execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("tree");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Parent ID");

            List<Category> categories = categoryService.getAllCategories();

            int rowNumber = 1;
            for (Category category : categories) {
                headerRow = sheet.createRow(rowNumber++);
                headerRow.createCell(0).setCellValue(category.getId());
                headerRow.createCell(1).setCellValue(category.getName());
                headerRow.createCell(2)
                        .setCellValue(category.getParent() == null ? "None" : category.getParent().getId().toString());
            }

            workbook.write(out);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());

            InputFile inputFile = new InputFile(inputStream, "Categories.xlsx");

            documentSender.sendDocument(chatId, inputFile);

            log.info("Отправлен файл");
            return new SendMessage(chatId, "Отправлен файл.");

        } catch (Exception e) {
            log.error("Ошибка при создании или отправке Excel файла: " + e.getMessage());
            return new SendMessage(chatId, "Произошла ошибка при создании файла.");
        }
    }

    /**
     * Получает имя команды.
     *
     * @return имя команды, используемое для её идентификации
     */
    @Override
    public String getCommandName() {
        return "/download";
    }
}
