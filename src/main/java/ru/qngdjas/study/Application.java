package ru.qngdjas.study;

import ru.qngdjas.study.compiler.SimpleCompiler;

import java.io.*;
import java.nio.file.Paths;

public class Application {

    public void start() {
        String text = readFile("/input.txt");
        SimpleCompiler compiler = new SimpleCompiler(text);
        if (compiler.compile()) {
            System.out.println("Скомпилировано");
            writeFile("from_pascal.py", compiler.toPython());
        } else {
            System.out.println("Ошибка компиляции");
        }
    }

    private String readFile(String path) {
        StringBuilder result = new StringBuilder();
        try (InputStreamReader inputStreamReader = new InputStreamReader(getClass().getResource(path).openStream())) {
            String line;
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append("\n");
            }
        } catch (FileNotFoundException exception) {
            System.out.println("Файл не найден");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return result.toString();
    }

    private void writeFile(String path, String text) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(Paths.get("out", path).toFile())) {
            fileOutputStream.write(text.getBytes());
            System.out.println("Результат записан в директорию out проекта");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
