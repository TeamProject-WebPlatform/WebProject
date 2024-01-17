package platform.game.service.Crawling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import platform.game.service.entity.Crawling;
import platform.game.service.repository.CrawlingRepository;

import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Transactional
@RequestMapping("/Crawling")
@ComponentScan(basePackages = { "platform.game.service.repository", "platform.game.service.model",
        "platform.game.service.entity" })
public class CrawlingScraper {

    @Autowired
    CrawlingRepository crawlingRepository;

    @RequestMapping("/LoL_notice")
    public ModelAndView leagueOfLegendsNoticeScraper() {
        System.out.println("롤 공지사항 크롤링 호출");
        Crawling crawling = new Crawling();
        int i = 0;
        try {
            // 웹 페이지를 JSoup을 사용하여 가져옵니다.
            String url = "https://www.leagueoflegends.com/ko-kr/news/notices/";
            Document document = Jsoup.connect(url).get();

            // <li> 태그를 선택합니다.
            Elements liTags = document.select("li.style__Item-sc-106zuld-3.fWsPDo");

            // 각 <li> 태그의 정보를 출력합니다.
            for (Element liTag : liTags) {
                // 제목을 선택합니다.
                String title = liTag.select("h2.style__Title-sc-1h41bzo-8").text();

                // 날짜를 선택합니다.
                String date = liTag.select("time").attr("dateTime");

                // 이미지를 선택합니다.
                Element imgTag = liTag.select("img").first();
                String imageUrl = imgTag.attr("src");

                // 링크를 선택합니다.
                Element linkTag = liTag.select("a").first();
                String BoardLink = "https://www.leagueoflegends.com" + linkTag.attr("href");

                ///////////////////////////////////////////////////////////
                // 각 링크별 안에 본문 html형식으로 저장하기
                url = BoardLink;
                document = Jsoup.connect(url).get();
                Elements DivTags = document.select("div.style__Wrapper-sc-1wsvmz4-0.bJOeIp");

                crawling.setCrawlingId(crawling.getCrawlingId() + 1);
                crawling.setCrawlingTitle(title);
                crawling.setCrawlingDate(Date(date));
                crawling.setCrawlingImageUrl(imageUrl);
                crawling.setCrawlingBoardLing(BoardLink);
                crawling.setCrawlingContent(DivTags.html());
                crawling.setCrawlingBoardCd("20001");
                crawling.setCrawlingPostTag("LeagueofLegends");
                crawling.setInterfaceYn("N");

                // System.out.println("날짜 확인 : " + Date(date));
                // System.out.println("ceawling" + crawling);
                crawlingRepository.save(crawling);
                System.out.println("i :" + i++);

            }

        } catch (IOException e) {
            System.out.println("크롤링 오류: " + e.getMessage());
        }

        return null;
    }

    @RequestMapping("/LoL_update")
    public ModelAndView leagueOfLegendsEventScraper() {
        System.out.println("롤 업데이트 크롤링 호출");
        Crawling crawling = new Crawling();
        int i = 6;
        try {
            // 웹 페이지를 JSoup을 사용하여 가져옵니다.
            String url = "https://www.leagueoflegends.com/ko-kr/news/game-updates/";
            Document document = Jsoup.connect(url).get();

            // <li> 태그를 선택합니다.
            Elements liTags = document.select("li.style__Item-sc-106zuld-3.fWsPDo");

            // 각 <li> 태그의 정보를 출력합니다.
            for (Element liTag : liTags) {
                // 제목을 선택합니다.
                String title = liTag.select("h2.style__Title-sc-1h41bzo-8").text();

                // 날짜를 선택합니다.
                String date = liTag.select("time").attr("dateTime");

                // 이미지를 선택합니다.
                Element imgTag = liTag.select("img").first();
                String imageUrl = imgTag.attr("src");

                // 링크를 선택합니다.
                Element linkTag = liTag.select("a").first();
                String links = linkTag.attr("href");
                // System.out.println("linkTag : " + boardlinks);
                String BoardLink;
                if (links.startsWith("https://")) {
                    BoardLink = links;
                } else {
                    BoardLink = "https://www.leagueoflegends.com" + links;
                }

                ///////////////////////////////////////////////////////////
                // 각 링크별 안에 본문 html형식으로 저장하기
                url = BoardLink;
                document = Jsoup.connect(url).get();
                Elements DivTags = document.select("div.style__Wrapper-sc-1wsvmz4-0.bJOeIp");
                if (DivTags.html() == "") {
                    DivTags = document.select("div.style__Content-sc-17x3yhp-1.hAcEIj");
                }

                crawling.setCrawlingId(i + 1);
                crawling.setCrawlingTitle(title);
                crawling.setCrawlingDate(Date(date));
                crawling.setCrawlingImageUrl(imageUrl);
                crawling.setCrawlingBoardLing(BoardLink);
                crawling.setCrawlingContent(DivTags.html());
                crawling.setCrawlingBoardCd("20002");
                crawling.setCrawlingPostTag("LeagueofLegends");
                crawling.setInterfaceYn("N");

                // System.out.println("ceawling" + crawling);
                crawlingRepository.save(crawling);
                System.out.println("i :" + i++);
            }

        } catch (IOException e) {
            System.out.println("크롤링 오류: " + e.getMessage());
        }

        return null;
    }

    @RequestMapping("/pubg_notice")
    public ModelAndView BattleGroundsNoticeScraper() {
        System.out.println("배그 공지사항 크롤링 호출");
        Crawling crawling = new Crawling();
        int i = 14;
        try {
            // 웹 페이지를 JSoup을 사용하여 가져옵니다.
            String url = "https://bbs.pubg.game.daum.net/gaia/do/pubg/notice/list?bbsId=PN001&objCate1=201";
            Document document = Jsoup.connect(url).get();

            // <li> 태그를 선택합니다.
            Elements trTags = document.select("tbody tr");

            int count = 0;
            // 각 <li> 태그의 정보를 출력합니다.
            for (Element trTag : trTags) {
                if (count >= 5) {
                    break;
                }
                // 제목을 선택합니다.
                String title = trTag.select(".link_subject").text();

                // 날짜를 선택합니다.
                String date = trTag.select(".cell_date").text();

                // 이미지를 선택합니다.
                Element imgTag = trTag.select("img").first();
                String imageUrl = imgTag.attr("src");

                // 링크를 선택합니다.
                Element linkTag = trTag.select("a").first();
                String boardLink = "https://bbs.pubg.game.daum.net/gaia/do/pubg/notice/" + linkTag.attr("href");

                ///////////////////////////////////////////////////////////
                // 각 링크별 안에 본문 html형식으로 저장하기
                document = Jsoup.connect(boardLink).get();
                Elements DivTags = document.select("div.content.board-view__area p");

                crawling.setCrawlingId(i + 1);
                crawling.setCrawlingTitle(title);
                crawling.setCrawlingDate(PubgDate(date));
                crawling.setCrawlingImageUrl(imageUrl);
                crawling.setCrawlingBoardLing(boardLink);
                crawling.setCrawlingContent(DivTags.html());
                crawling.setCrawlingBoardCd("20001");
                crawling.setCrawlingPostTag("BattleGround");
                crawling.setInterfaceYn("N");

                System.out.println(PubgDate(date));
                // System.out.println("ceawling" + crawling);
                crawlingRepository.save(crawling);
                System.out.println("i :" + i++);
                count++;
            }

        } catch (IOException e) {
            System.out.println("크롤링 오류: " + e.getMessage());
        }

        return null;
    }

    @RequestMapping("/pubg_update")
    public ModelAndView BattleGroundsUpdateScraper() {
        System.out.println("배그 업데이트 크롤링 호출");
        Crawling crawling = new Crawling();
        int i = 20;
        try {
            // 웹 페이지를 JSoup을 사용하여 가져옵니다.
            String url = "https://bbs.pubg.game.daum.net/gaia/do/pubg/update/list?bbsId=PN002&objCate1=255";
            Document document = Jsoup.connect(url).get();

            // <li> 태그를 선택합니다.
            Elements trTags = document.select("tbody tr");

            int count = 0;
            // 각 <li> 태그의 정보를 출력합니다.
            for (Element trTag : trTags) {
                if (count >= 6) {
                    break;
                }
                // 제목을 선택합니다.
                String title = trTag.select(".link_subject").text();

                // 날짜를 선택합니다.
                String date = trTag.select(".cell_date").text();

                // 이미지를 선택합니다.
                Element imgTag = trTag.select("img").first();
                String imageUrl = imgTag.attr("src");

                // 링크를 선택합니다.
                Element linkTag = trTag.select("a").first();
                String boardLink = "https://bbs.pubg.game.daum.net/gaia/do/pubg/update/" + linkTag.attr("href");

                ///////////////////////////////////////////////////////////
                // 각 링크별 안에 본문 html형식으로 저장하기
                document = Jsoup.connect(boardLink).get();
                Elements DivTags = document.select("div.content.board-view__area p");

                crawling.setCrawlingId(i + 1);
                crawling.setCrawlingTitle(title);
                crawling.setCrawlingDate(PubgDate(date));
                crawling.setCrawlingImageUrl(imageUrl);
                crawling.setCrawlingBoardLing(boardLink);
                crawling.setCrawlingContent(DivTags.html());
                crawling.setCrawlingBoardCd("20002");
                crawling.setCrawlingPostTag("BattleGround");
                crawling.setInterfaceYn("N");

                // System.out.println("ceawling" + crawling);
                crawlingRepository.save(crawling);
                System.out.println("i :" + i++);
                count++;
            }

        } catch (IOException e) {
            System.out.println("크롤링 오류: " + e.getMessage());
        }

        return null;
    }

    public String Date(String date) {
        String inputDateString = date;

        // DateTimeFormatter 설정
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        // 문자열을 LocalDateTime으로 변환
        LocalDateTime localDateTime = LocalDateTime.parse(inputDateString, inputFormatter);

        // LocalDateTime을 다시 문자열로 변환
        String outputDateString = localDateTime.format(outputFormatter);

        return outputDateString;
    }

    public String PubgDate(String date) {
        String inputString = date;

        // 현재 날짜를 가져오기
        LocalDateTime today = LocalDateTime.now();

        // 패턴에 따라 입력된 문자열을 LocalDateTime으로 변환
        LocalDateTime resultDateTime;
        if (inputString.matches("\\d{2}.\\d{2}.\\d{2}")) {
            // 예: 23.11.21 형식의 문자열인 경우
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yy.MM.dd");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            LocalDate localDate = LocalDate.parse(inputString, inputFormatter);
            LocalDateTime localDateTime = localDate.atStartOfDay();
            String outputDateString = localDateTime.format(outputFormatter);
            System.out.println("입력된 문자열: " + inputString);
            System.out.println("변환된 날짜 및 시간 문자열: " + outputDateString);

            return outputDateString;
        } else {
            // 그 외의 경우
            LocalTime inputTime = LocalTime.parse(inputString, DateTimeFormatter.ofPattern("HH:mm"));
            resultDateTime = today.withHour(inputTime.getHour()).withMinute(inputTime.getMinute());
            // 다시 다른 형식의 문자열로 변환
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            String outputDateTimeString = resultDateTime.format(outputFormatter);
            System.out.println("입력된 문자열: " + inputString);
            System.out.println("변환된 날짜 및 시간 문자열: " + outputDateTimeString);

            return outputDateTimeString;
        }
    }

}