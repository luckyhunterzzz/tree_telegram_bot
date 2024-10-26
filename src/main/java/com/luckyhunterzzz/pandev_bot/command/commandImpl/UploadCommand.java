package com.luckyhunterzzz.pandev_bot.command.commandImpl;

import com.luckyhunterzzz.pandev_bot.command.Command;
import com.luckyhunterzzz.pandev_bot.service.CategoryService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Команда для загрузки данных из файла.
 *
 * Этот класс реализует интерфейс {@link Command} и предоставляет функциональность
 * для загрузки данных из Excel файла и добавления категорий в систему.
 */
@Slf4j
@Setter
@Getter
@Component
public class UploadCommand implements Command {

    private final CategoryService categoryService;
    private final String botToken;

    /**
     * Конструктор, который инициализирует UploadCommand.
     *
     * @param categoryService сервис для работы с категориями
     * @param botToken токен бота для доступа к Telegram API
     */
    public UploadCommand(CategoryService categoryService, @Value("${bot.token}")String botToken) {
        this.categoryService = categoryService;
        this.botToken = botToken;
    }

    /**
     * Выполняет команду загрузки данных из файла.
     *
     * Этот метод обрабатывает входящее обновление от Telegram, извлекает документ
     * из сообщения и загружает данные из Excel файла. Он выполняет следующие шаги:
     * <ol>
     *     <li>Извлекает идентификатор чата из входящего обновления.</li>
     *     <li>Получает документ из сообщения.</li>
     *     <li>Запрашивает InputStream файла по его идентификатору с помощью метода {@link #getFileInputStream(String)}.</li>
     *     <li>Если InputStream равен null, возвращает сообщение об ошибке, указывающее, что файл не удалось получить.</li>
     *     <li>Создает объект {@link XSSFWorkbook} для работы с Excel файлом.</li>
     *     <li>Извлекает первый лист из рабочей книги.</li>
     *     <li>Итерирует по строкам листа, начиная со второй строки (первая строка — заголовки).</li>
     *     <li>Для каждой строки извлекает значения ячеек, представляющие имя категории и идентификатор родительской категории.</li>
     *     <li>Если родительская категория равна "None", добавляет новую категорию с помощью {@link CategoryService#addElement(String)}.</li>
     *     <li>Если родительская категория указана, пытается преобразовать её в идентификатор и добавляет дочернюю категорию с помощью {@link CategoryService#addElement(Long, String)}.</li>
     *     <li>Если возникает ошибка при преобразовании идентификатора родительской категории, возвращает сообщение об ошибке.</li>
     *     <li>После успешной обработки всех строк возвращает сообщение об успешной загрузке данных.</li>
     * </ol>
     * Если файл не удается получить или если возникают ошибки при обработке данных,
     * метод возвращает соответствующее сообщение об ошибке.
     *
     * @param update входящее обновление от Telegram, содержащее информацию о сообщении,
     *               включая документ, который нужно загрузить
     * @return объект {@link SendMessage}, представляющий ответ о результате загрузки.
     *         Если загрузка прошла успешно, возвращается сообщение об успешной загрузке,
     *         в противном случае возвращается сообщение об ошибке.
     */
    @Override
    public BotApiMethod<?> execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        Document document = update.getMessage().getDocument();

        try (InputStream inputStream = getFileInputStream(document.getFileId())) {
            if (inputStream == null) {
                log.error("Не удалось получить файл с ID: [" + document.getFileId() + "]");
                return new SendMessage(chatId, "Не удалось получить файл.");
            }
            try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
                Sheet sheet = workbook.getSheetAt(0);
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    log.info("Обработка строки [" + i + "] файла.");
                    if (row == null) {
                        continue;
                    }

                    String name = getCellValueAsString(row.getCell(1));
                    String parent = getCellValueAsString(row.getCell(2));

                    if ("None".equalsIgnoreCase(parent)) {
                        categoryService.addElement(name);
                    } else {
                        try {
                            Long parentId = Double.valueOf(parent).longValue();
                            categoryService.addElement(parentId, name);
                        } catch (Exception e) {
                            log.error("В параметре parent_ID находится не валидное значение: " + e.getMessage());
                            return new SendMessage(chatId, "В параметре parent_ID находится не валидное значение.");
                        }
                    }
                }
            }
            log.info("Отправка сообщения об успешной загрузке данных");
            return new SendMessage(chatId, "Данные успешно загружены.");
        } catch (Exception e) {
            log.error("Ошибка при загрузке данных из файла: " + e.getMessage());
            return new SendMessage(chatId, "Произошла ошибка при загрузке данных.");
        }
    }

    /**
     * Получает InputStream файла по его ID.
     *
     * @param fileId идентификатор файла в Telegram
     * @return InputStream для чтения содержимого файла или null, если файл не найден
     */
    private InputStream getFileInputStream(String fileId) {
        try {
            String apiUrl = "https://api.telegram.org/bot" + botToken + "/getFile?file_id=" + fileId;
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            String filePath = jsonResponse.getJSONObject("result").getString("file_path");

            String fileUrl = "https://api.telegram.org/file/bot" + botToken + "/" + filePath;
            return new URL(fileUrl).openStream();

        } catch (Exception e) {
            log.error("Ошибка при получении InputStream файла: " + e.getMessage());
            return null;
        }
    }

    /**
     * Получает значение ячейки в виде строки.
     *
     * @param cell ячейка, значение которой нужно получить
     * @return строковое представление значения ячейки
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    /**
     * Получает имя команды.
     *
     * @return имя команды, используемое для её идентификации
     */
    @Override
    public String getCommandName() {
        return "/upload";
    }
}
