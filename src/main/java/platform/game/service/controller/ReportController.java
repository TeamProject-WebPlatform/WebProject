package platform.game.service.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import platform.game.service.entity.Report;
import platform.game.service.repository.ReportRepository;

@Controller
@RequestMapping("/report")
public class ReportController {
    
    @Value("${file.upload.path}")
    private String uploadPath;
    @Autowired
    private ReportRepository reportRepository;
    @RequestMapping("")
    public ModelAndView report(@RequestParam("subject") String subject,
                                @RequestParam("details") String details,
                                @RequestParam("file") MultipartFile file,
                                @RequestParam("btIdmodalInput") String btIdmodalInput){
        System.out.println("-----report-----");
        Path filePath = null;
        // 파일 업로드 처리
        if (!file.isEmpty()) {
            System.out.println("여기");
            try {
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                filePath = Paths.get(uploadPath + fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                // 파일 저장 중 예외 발생 시 처리
            } catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        Report report = Report.builder()
                            .reportId(0)
                            .report_kind(subject)
                            .memReportDesc(details)
                            .reportedAt(new Date())
                            .btId(Integer.parseInt(btIdmodalInput))
                            .memId(0)
                            .reportAttachImg(filePath!=null?filePath.toString():null).build();
                reportRepository.save(report);
        System.out.println("-----report-----");
        return new ModelAndView("index");
    }
}