package com.company.util;

import com.company.Main;
import com.company.db.Database;
import com.company.enums.Language;
import com.company.model.User;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Getter
@Setter
public class WorkWithFile extends Thread {

    private Message message;

    public WorkWithFile(Message message) {
        this.message = message;
    }

    @Override
    public void run() {

        File file = new File(DemoUtil.USERS_FILE_PATH);

        try (FileOutputStream outputStream = new FileOutputStream(file)) {

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Users");

            XSSFRow row = sheet.createRow(1);
            row.createCell(0).setCellValue("USERSNAME");
            row.createCell(1).setCellValue("ROLE");
            row.createCell(2).setCellValue("PHONE_NUMBER");

            List<User> users = Database.customers.stream()
                    .filter(user -> user.getUsername() != null && user.getRole() != null && user.getPhoneNumber() != null)
                    .toList();

            int i = 2;
            for (User user : users) {
                XSSFRow row1 = sheet.createRow(i++);
                row1.createCell(0).setCellValue("https://telegram.me/" + user.getUsername());
                row1.createCell(1).setCellValue(user.getRole().name());
                row1.createCell(2).setCellValue("+" + user.getPhoneNumber());
            }

            for (int j = 0; j < 3; j++) {
                sheet.autoSizeColumn(j);
            }

            workbook.write(outputStream);
            outputStream.close();

            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(String.valueOf(message.getChatId()));
            InputFile inputFile = new InputFile(file);
            sendDocument.setDocument(inputFile);

            Main.MY_TELEGRAM_BOT.sendMsg(sendDocument);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
