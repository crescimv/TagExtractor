import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Main {
    public static void main(String[] args) {
        Set<String> stopWords = stopWordsLoader();

        JFrame frame = new JFrame();

        JFileChooser chooser = new JFileChooser();

        int selectedFile = chooser.showOpenDialog(frame);

        if (selectedFile == JFileChooser.APPROVE_OPTION) {
            File stopWordsFile = chooser.getSelectedFile();
            stopWordsFromFile(stopWordsFile, stopWords);
        }

        selectedFile = chooser.showOpenDialog(frame);

        if (selectedFile == JFileChooser.APPROVE_OPTION) {
            File literatureFile = chooser.getSelectedFile();
            Map<String, Integer> keywordCounter = count(literatureFile, stopWords);

            System.out.println("Keywords: ");
            for (Map.Entry<String, Integer> entry : keywordCounter.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }
    private static Set<String> stopWordsLoader() {
        return new HashSet<>();
    }

    private static void stopWordsFromFile(File stopWordsFile, Set<String> stopWords) {

        try (BufferedReader reader = new BufferedReader(new FileReader(stopWordsFile))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");

                for (String word : words) {
                    stopWords.add(word.toLowerCase().replaceAll("[^a-z]", ""));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Integer> count(File literatureFile, Set<String> stopWords) {
        Map<String, Integer> keyWordCounter = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(literatureFile))) {
            String line;
            Pattern pattern = Pattern.compile("[a-zA-Z]+"); // Regex to match only words (letters only)

            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line.toLowerCase());

                while (matcher.find()) {
                    String word = matcher.group();

                    if (!stopWords.contains(word)) {
                        keyWordCounter.put(word, keyWordCounter.getOrDefault(word, 0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keyWordCounter;
    }
}