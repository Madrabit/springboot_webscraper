package ru.madrabit.webscraper.selenium.poi;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.madrabit.webscraper.selenium.domen.Answer;
import ru.madrabit.webscraper.selenium.domen.Question;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
public class CreateExcel {

    private final String filePath;


    public CreateExcel(String siteName, String id) {
        log.info(System.getProperty("user.dir"));
        filePath = System.getProperty("user.dir")
                + File.separator + "test" + File.separator
                + siteName + File.separator
                + id + ".xlsx";

    }

    public void createExcel(List<Question> questionList) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Test sheet");

        int rowNumber = 0;
        Cell cell;
        Row row;
        //
        XSSFCellStyle style = createStyleForTitle(workbook);

        row = sheet.createRow(rowNumber);

        // Id
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Код (первичный ключ)");
        cell.setCellStyle(style);
        // Title
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Заголовок");
        cell.setCellStyle(style);
        // Type
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Тип вопроса");
        cell.setCellStyle(style);
        // Question
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Вопрос");
        cell.setCellStyle(style);
        // Weight
        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("Вес вопроса (балл)");
        cell.setCellStyle(style);
        // Answer
        cell = row.createCell(5, CellType.STRING);
        cell.setCellValue("Тексты вариантов ответа");
        cell.setCellStyle(style);
        // Number of right answer
        cell = row.createCell(6, CellType.STRING);
        cell.setCellValue("Номера правильных ответов (начиная с 1)");
        cell.setCellStyle(style);
        // Default empty field
        cell = row.createCell(7, CellType.STRING);
        cell.setCellValue("Следование вариантов ответов (если пусто - последовательно, если Random - случайно)");
        cell.setCellStyle(style);
        // Default empty field
        cell = row.createCell(8, CellType.STRING);
        cell.setCellValue("Длительность (секунд)");
        cell.setCellStyle(style);
        // Default empty field
        cell = row.createCell(9, CellType.STRING);
        cell.setCellValue("Показывать правильный ответ (пусто - нет, иначе - да)");
        cell.setCellStyle(style);
        // Default empty field
        cell = row.createCell(10, CellType.STRING);
        cell.setCellValue("Инструкция к вопросу (либо пусто)");
        cell.setCellStyle(style);
        // Default empty field
        cell = row.createCell(11, CellType.STRING);
        cell.setCellValue("Инструкция к вопросу (либо пусто)");
        cell.setCellStyle(style);
        // Default "right" message
        cell = row.createCell(12, CellType.STRING);
        cell.setCellValue("Сообщение при верном ответе");
        cell.setCellStyle(style);
        // Default "wrong" message
        cell = row.createCell(13, CellType.STRING);
        cell.setCellValue("Сообщение при ошибочном ответе");
        cell.setCellStyle(style);
        // Default empty field
        cell = row.createCell(14, CellType.STRING);
        cell.setCellValue("Комментарий к вопросу (или пусто)");
        cell.setCellStyle(style);

        // Data
        for (Question q : questionList) {
            row = sheet.createRow(++rowNumber);

            // Id (A)
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(q.getId());
            // Title (B)
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(q.getText());
            // Type (C)
            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue("multiple_choice");
            // Question (D)
            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue(q.getText());
            // Weight (E)
            cell = row.createCell(4, CellType.NUMERIC);
            cell.setCellValue(1);
            // Answers (F)
            cell = row.createCell(5, CellType.STRING);
            StringBuilder answers = new StringBuilder();
            int answersLength = q.getAnswerSet().size();
            int i = 0;
            for (Answer answer : q.getAnswerSet()) {
                answers.append(answer.getId());
                answers.append(") ");
                if (++i == answersLength) {
                    answers.append(answer.getText());
                } else {
                    answers.append(answer.getText()).append("#");
                }
            }
            cell.setCellValue(answers.toString());
            // Right Answer (G)
            cell = row.createCell(6, CellType.STRING);
            int sizeOfAnswerSet = q.getAnswerNumber().size();
            int j = 0;
            StringBuilder answerNum = new StringBuilder();
            for (Integer serialNum : q.getAnswerNumber()) {
                if (++j == sizeOfAnswerSet) {
                    answerNum.append(serialNum);
                } else {
                    answerNum.append(serialNum).append(", ");
                }
            }
            cell.setCellValue(answerNum.toString());
            // Message "Right" (M)
            cell = row.createCell(12, CellType.STRING);
            cell.setCellValue("Правильно!");
            // Message "Wrong" (N)
            cell = row.createCell(13, CellType.STRING);
            cell.setCellValue("Не правильно");
        }
        createFile(workbook);
    }


    private static XSSFCellStyle createStyleForTitle(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    private void createFile(XSSFWorkbook workbook) {
        File file = new File(filePath);
        boolean mkdirs = file.getParentFile().mkdirs();
        if (mkdirs) {
            try (FileOutputStream outFile = new FileOutputStream(file)) {
                workbook.write(outFile);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            log.info("Created file: {}", file.getAbsolutePath());
        } else {
            log.error("Directory was not created. {}", false);
        }

    }
}
