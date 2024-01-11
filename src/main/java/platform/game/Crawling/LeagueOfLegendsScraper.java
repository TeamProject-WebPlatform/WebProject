package platform.game.Crawling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class LeagueOfLegendsScraper {
    public static void main(String[] args) {
        try {
            // 웹 페이지를 JSoup을 사용하여 가져옵니다.
            String url = "https://www.leagueoflegends.com/ko-kr/news/notices/";
            Document document = Jsoup.connect(url).get();

            // <article> 태그를 선택합니다.
            // Elements articleTags =
            // document.select("article.style__WrapperInner-sc-1h41bzo-1.bnQsfh");
            Elements articleTags = document.select("li.style__Item-sc-106zuld-3.fWsPDo");

            // 각 <article> 태그의 정보를 출력합니다.
            for (Element articleTag : articleTags) {
                // 제목을 선택합니다.
                String title = articleTag.select("h2.style__Title-sc-1h41bzo-8").text();

                // 날짜를 선택합니다.
                String date = articleTag.select("time").attr("dateTime");

                // 이미지를 선택합니다.
                Element imgTag = articleTag.select("img").first();
                String imageUrl = imgTag.attr("src");
                String altText = imgTag.attr("alt");

                // 링크를 선택합니다.
                Element linkTag = articleTag.select("a").first();
                String articleLink = "https://www.leagueoflegends.com" + linkTag.attr("href");

                // 결과 출력
                System.out.println("Title: " + title);
                System.out.println("Date: " + date);
                System.out.println("Image URL: " + imageUrl);
                System.out.println("Alt Text: " + altText);
                System.out.println("Article Link: " + articleLink);
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println("크롤링 오류: " + e.getMessage());
        }
    }
}
