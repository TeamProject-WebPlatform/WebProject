package platform.game.service.action;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;

public class MypageAction implements BoardAction {

    private final static String LOCAL_MANUAL_PATH = "static/md/";

    @Override
    public void excute() {
        System.out.println( "MypageAction 호출" );
    }

    public void checkUserMarkdownFile(String PATH, String userid) {
        String filePath = PATH + "/" + userid + ".md";

        if (isFileExists(filePath)) {
            // System.out.println("파일이 존재합니다.");
        } else {
            // System.out.println("파일이 존재하지 않습니다.");

            Path sourcePath = Paths.get(PATH, "MarkdownText.md");
            Path targetPath = Paths.get(PATH + "/" + userid + ".md");

            try {
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                // System.out.println("파일 복사 및 이름 변경 성공");
            } catch (IOException e) {
                System.err.println("파일 복사 또는 이름 변경 중 오류 발생 : " + e.getMessage());
            }
        }
    }

    private static boolean isFileExists(String filePath) {
        Path path = Paths.get(filePath);
        return Files.exists(path);
    }

    public String getMarkdownValueFormLocal(String PATH, String userid) throws Exception {
        checkUserMarkdownFile(PATH, userid);

        // StringBuilder stringBuilder = new StringBuilder();
        // ClassPathResource classPathResource = new ClassPathResource(PATH + "\\" + userid + ".md" );
        // BufferedReader br = Files.newBufferedReader(Paths.get(classPathResource.getURI()));
        // br.lines().forEach(line -> stringBuilder.append(line).append("\n"));
        // return stringBuilder.toString();

        Path filePath = Paths.get(PATH, userid + ".md");
        if (Files.exists(filePath)) {
            return Files.lines(filePath).collect(Collectors.joining("\n"));
        } else {
            throw new FileNotFoundException("File not found: " + filePath);
        }
    }

}