package platform.game.action;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.core.io.ClassPathResource;

public class MypageAction implements BoardAction {

    private final static String LOCAL_MANUAL_PATH = "static/md/";

    @Override
    public void excute() {
        System.out.println( "MypageAction 호출" );
    }

    public String getMarkdownValueFormLocal(String manualPage) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        ClassPathResource classPathResource = new ClassPathResource(LOCAL_MANUAL_PATH + manualPage + ".md" );

        BufferedReader br = Files.newBufferedReader(Paths.get(classPathResource.getURI()));
        br.lines().forEach(line -> stringBuilder.append(line).append("\n"));

        return stringBuilder.toString();
    }

}