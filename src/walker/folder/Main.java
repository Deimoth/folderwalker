package walker.folder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        String path;
        if (args.length == 0) {
            System.out.println("Root folder path is not set");
            return;
        } else {
            path = args[0];
        }

        File f = new File(path);
        if (!f.exists()) {
            System.out.println("Root folder does not exist");
            return;
        } else if (!f.isDirectory()) {
            System.out.println("Root folder is not a directory");
            return;
        }

        File result = new File(path + File.separator + "result.txt");
        result.delete();
        List<File> fileList = new ArrayList<>();
        textFilesSearch(path, fileList);

        StringBuilder sb = new StringBuilder();
        if (!fileList.isEmpty()) {
            fileList.stream()
                    .sorted(Comparator.comparing(File::getName))
                    .collect(Collectors.toList())
                    .forEach(file -> sb.append(readFile(file)));
        }

        writeFile(result, sb.toString());
    }

    private static void textFilesSearch(String path, List<File> fileList) {
        File folder = new File(path);
        String[] dirList = folder.list();
        for (int i = 0; i < dirList.length; i++) {
            File file = new File(path + File.separator + dirList[i]);
            if (file.isFile()) {
                fileList.add(file);
            } else {
                textFilesSearch(path + File.separator + dirList[i], fileList);
            }
        }
    }

    private static String readFile(File file) {
        StringBuilder sb = new StringBuilder();
        try {
            FileReader fileReader = new FileReader(file);
            int i;
            while((i = fileReader.read()) != -1) {
                sb.append((char) i);
            }
            fileReader.close();
        } catch (IOException ioe) {
            handleIOException(ioe);
        }

        return sb.toString();
    }

    private static void writeFile(File result, String content) {
        try {
            result.createNewFile();
            FileWriter myWriter = new FileWriter(result);
            myWriter.write(content);
            myWriter.close();
            System.out.println("Result written to file: " + result.getAbsoluteFile());
        } catch (IOException ioe) {
            handleIOException(ioe);
        }
    }

    private static void handleIOException(IOException ioe) {
        System.out.println("An error occurred:");
        ioe.printStackTrace();
    }
}
