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
import java.util.*;

@Controller
@Transactional
@RequestMapping("/Crawling")
@ComponentScan(basePackages = { "platform.game.service.repository", "platform.game.service.model",
        "platform.game.service.entity" })
public class LeagueOfLegendsScraper {

    @Autowired
    CrawlingRepository crawlingRepository;

    @RequestMapping("/crawl")
    public ModelAndView leagueOfLegendsScraper() {
        System.out.println("크롤링 호출");
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
                crawling.setCrawlingDate(date);
                crawling.setCrawlingImageUrl(imageUrl);
                crawling.setCrawlingBoardLing(BoardLink);
                crawling.setCrawlingContent(DivTags.html());

                System.out.println("ceawling" + crawling);
                crawlingRepository.save(crawling);
                System.out.println("i :" + i++);
            }

        } catch (IOException e) {
            System.out.println("크롤링 오류: " + e.getMessage());
        }

        return null;
    }
}