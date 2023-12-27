package platform.game.service.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import platform.game.service.action.MypageAction;
import platform.game.service.entity.Member;
import platform.game.service.entity.RiotInfo;
import platform.game.service.filter.JwtAuthFilter;
import platform.game.service.service.MemberInfoDetails;
import platform.game.service.service.RiotService;
import platform.game.service.service.jwt.JwtManager;

@RestController
@ComponentScan(basePackages = {"platform.game.action","platform.game.env.config","platform.game.jwt"})
@RequestMapping("/mypage")
public class MyPageController {
    
    private MypageAction mypageAction = new MypageAction();
    // 파일을 업로드할 디렉터리 경로
    String currentPath = Paths.get("").toAbsolutePath().toString();
    private final String mdDir = Paths.get(currentPath, "src/main/resources/static/", "md").toString();
    private final String mdImgDir = Paths.get(currentPath, "src/main/resources/static/", "md_img").toString();

    @Autowired
    JwtManager jwtManager;
    @Autowired
    private JwtAuthFilter authFilter;
    @Autowired
    private RiotService riotService;

    @GetMapping("/{userid}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ModelAndView mypage(@PathVariable("userid") String userid, Model model){
        String markdownValueFormLocal = null;

        Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMember();
        model.addAttribute("member", member);

        try {
            markdownValueFormLocal = mypageAction.getMarkdownValueFormLocal( mdDir, userid );
        } catch (Exception e) {
            System.out.println( "[에러] MyPageController : " + e.getMessage() );
        }

        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdownValueFormLocal);
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        model.addAttribute("contents", renderer.render(document));

        return new ModelAndView("mypage");
    }
 
    @GetMapping("/summonerByName")
    public RiotInfo callSummonerByName(String summonerName){
        // String summonerName = "Java를자바";
    
        summonerName = summonerName.replaceAll(" ","%20");
 
        RiotInfo apiResult = riotService.callRiotAPISummonerByName(summonerName);
 
        return apiResult;
    }

    @PostMapping("/tui-editor/image-upload")
    public String uploadEditorImage(@RequestParam final MultipartFile image) {
        System.out.println( "uploadEditorImage() 실행" );

        if (image.isEmpty()) {
            return "";
        }

        String orgFilename = image.getOriginalFilename();                                         // 원본 파일명
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");           // 32자리 랜덤 문자열
        String extension = orgFilename.substring(orgFilename.lastIndexOf(".") + 1);             // 확장자
        String saveFilename = uuid + "." + extension;                                             // 저장할 파일명
        String fileFullPath = Paths.get(mdImgDir, saveFilename).toString();                      // 전체 경로

        // 디렉터리 생성
        File dir = new File(mdImgDir);
        if (dir.exists() == false) {
            dir.mkdirs();
        }
        System.out.println( "orgFilename : " + orgFilename );
        System.out.println( "uuid : " + uuid );
        System.out.println( "extension : " + extension );
        System.out.println( "saveFilename : " + saveFilename );
        System.out.println( "fileFullPath : " + fileFullPath );
        try {
            // 파일 저장
            File uploadFile = new File(fileFullPath);
            image.transferTo(uploadFile);
            return saveFilename;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/tui-editor/image-print", produces = { MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
    public byte[] printEditorImage(@RequestParam final String filename) {
        String fileFullPath = Paths.get(mdImgDir, filename).toString();

        File uploadedFile = new File(fileFullPath);
        if (uploadedFile.exists() == false) {
            throw new RuntimeException();
        }

        try {
            byte[] imageBytes = Files.readAllBytes(uploadedFile.toPath());
            return imageBytes;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/qrcode/create")
    public Object createQr(@RequestParam String url) throws WriterException, IOException {
        int width = 200;
        int height = 200;
        BitMatrix matrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height);
 
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(out.toByteArray());
        }
    }

    @RequestMapping("/check-cookie")
    public String checkCookie(@CookieValue(name = "jwtTokenCookie", defaultValue = "defaultValue") String myCookieValue) {
        System.out.println( "checkCookie()");

        String valueID = jwtManager.extractToken("id", myCookieValue);
        String valuePW = jwtManager.extractToken("password", myCookieValue);
        String valueRo = jwtManager.extractToken("role", myCookieValue);

        System.out.println( "토큰에서 받아온 ID" + valueID );
        System.out.println( "토큰에서 받아온 PW" + valuePW );
        System.out.println( "토큰에서 받아온 Ro" + valueRo );

        return "Value of myCookie: " + myCookieValue;
    }
}
