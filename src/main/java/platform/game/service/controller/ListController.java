package platform.game.service.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import platform.game.service.entity.Comment;
import platform.game.service.entity.Member;
import platform.game.service.entity.Post;
import platform.game.service.model.TO.BoardCpageTO;
import platform.game.service.repository.PostInfoRepository;
import platform.game.service.repository.CommentInfoRepository;
import platform.game.service.repository.MemberInfoRepository;
import platform.game.service.service.MemberInfoDetails;

@Controller
@ComponentScan(basePackages = { "platform.game.action", "platform.game.service.repository",
        "platform.game.service.model" })
@RequestMapping("/board")
public class ListController {

    @Autowired
    private PostInfoRepository postInfoRepository;

    @Autowired
    private MemberInfoRepository memberInfoRepository;

    // 한번에 조건 4개 다 검색하기
    @GetMapping("/list_MixList")
    public ModelAndView getBoardListMix(@RequestParam("board_cd") String boardCd,
            @RequestParam("selectedOption") String selectedOption,
            @RequestParam("searchValue") String searchValue,
            @RequestParam("selectedSize") int selectedSize,
            @RequestParam("selectTag") String selectTag,
            HttpServletRequest request) {
        // 모델에 필요한 데이터를 추가하고, 템플릿 이름을 반환
        System.out.println("getBoardListMix 호출");
        ArrayList<Post> lists = new ArrayList<>();

        System.out.println("boardCd : " + boardCd);
        System.out.println("selectOption : " + selectedOption);
        System.out.println("searchValue : " + searchValue);
        System.out.println("selectedSize : " + selectedSize);
        System.out.println("selectTag : " + selectTag);

        String loginCheck = "false";

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            System.out.println("멤버 있음 ");
            loginCheck = "true";
        } else {
            System.out.println("멤버 없음");
            loginCheck = "false";
        }

        // 검색 조건 체크하는 곳
        if ("title".equals(selectedOption)) { // 제목
            // 태그 체크
            if ("All".equals(selectTag)) {
                // 태그가 All인 경우 - 제목 검색
                lists = postInfoRepository.findByBoardCdAndPostTitleContainingOrderByPostIdDesc(boardCd, searchValue);
            } else {
                // 태그가 선택된 경우 - 제목 + 태그 검색
                lists = postInfoRepository.findByBoardCdAndPostTitleContainingAndPostTagsContainingOrderByPostIdDesc(
                        boardCd, searchValue, selectTag);
            }
        } else if ("content".equals(selectedOption)) { // 내용
            // 태그 체크
            if ("All".equals(selectTag)) {
                // 태그가 ALL인 경우 - 내용 검색
                lists = postInfoRepository.findByBoardCdAndPostContentContainingOrderByPostIdDesc(boardCd, searchValue);
            } else {
                // 태그가 선택된 경우 - 내용 + 태그 검색
                lists = postInfoRepository.findByBoardCdAndPostContentContainingAndPostTagsContainingOrderByPostIdDesc(
                        boardCd, searchValue, selectTag);
            }
        } else if ("writer".equals(selectedOption)) { // 글쓴이
            // 사용자의 memId를 기반으로 memNick 가져오기
            Member member = memberInfoRepository.findByMemNick(searchValue); // MemId를 사용하여 Member 엔티티 찾기
            int memId = (int) member.getMemId(); // 찾은 Member 엔티티에서 memNick 가져오기

            // 태그 체크
            if ("All".equals(selectTag)) {
                // 태그가 All인 경우 - 글쓴이 검색
                lists = postInfoRepository.findByBoardCdAndMember_MemIdOrderByPostIdDesc(boardCd,
                        memId);
            } else {
                // 태그가 선택된 경우 - 글쓴이 + 태그 검색
                lists = postInfoRepository.findByBoardCdAndMember_MemIdAndPostTagsContainingOrderByPostIdDesc(boardCd,
                        memId, selectTag);
            }
        }

        // System.out.println("lists check : " + lists);

        // cpage 작업
        BoardCpageTO cpageTO = new BoardCpageTO();
        int cpage = 1;
        int recordPerPage = selectedSize;
        int blockPerPage = cpageTO.getBlockPerPage();
        if (request.getParameter("cpage") != null && !request.getParameter("cpage").equals("")) {
            cpage = Integer.parseInt(request.getParameter("cpage"));
        }

        System.out.println(recordPerPage);

        cpageTO.setCpage(cpage);
        cpageTO.setTotalRecord(lists.size());
        cpageTO.setTotalPage(((cpageTO.getTotalRecord() - 1) / recordPerPage) + 1);
        cpageTO.setStartBlock(cpage - (cpage - 1) % blockPerPage);
        cpageTO.setEndBlock(cpage - (cpage - 1) % blockPerPage + blockPerPage - 1);
        if (cpageTO.getEndBlock() >= cpageTO.getTotalPage()) {
            cpageTO.setEndBlock(cpageTO.getTotalPage());
        }

        // SKIP - 각 페이지 별 게시글 자르기
        int skip = (cpage - 1) * selectedSize;
        int startIndex = Math.min(skip, lists.size());
        int endIndex = Math.min(startIndex + selectedSize, lists.size());
        lists = new ArrayList<>(lists.subList(startIndex, endIndex));

        cpageTO.setBoardLists(lists);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("board_list_MixList");
        modelAndView.addObject("lists", lists);
        modelAndView.addObject("loginCheck", loginCheck);
        modelAndView.addObject("boardCd", boardCd);
        modelAndView.addObject("cpage", cpageTO);
        modelAndView.addObject("recpage", recordPerPage);
        modelAndView.addObject("list_tag", selectTag);
        modelAndView.addObject("searchValue", searchValue);
        modelAndView.addObject("list_search", selectedOption);

        return modelAndView;
    }

    // @GetMapping("/list_search") // 검색만 하는경우
    // public ModelAndView getBoardListFragment(@RequestParam("board_cd") String
    // boardCd,
    // @RequestParam("selectOption") String selectOption,
    // @RequestParam("searchValue") String searchValue) {
    // // 모델에 필요한 데이터를 추가하고, 템플릿 이름을 반환
    // System.out.println("getSearchBoardListFragment 호출");

    // String loginCheck = "false";

    // if
    // (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser"))
    // {
    // System.out.println("멤버 있음 ");
    // loginCheck = "true";
    // } else {
    // System.out.println("멤버 없음");
    // loginCheck = "false";
    // }

    // ArrayList<Post> lists = new ArrayList<>();

    // if ("title".equals(selectOption)) {
    // lists =
    // postInfoRepository.findByBoardCdAndPostTitleLikeOrderByPostIdDesc(boardCd,
    // "%" + searchValue + "%");
    // } else if ("content".equals(selectOption)) {
    // lists =
    // postInfoRepository.findByBoardCdAndPostContentLikeOrderByPostIdDesc(boardCd,
    // "%" + searchValue + "%");
    // } else if ("writer".equals(selectOption)) {
    // // 사용자의 memId를 기반으로 memNick 가져오기
    // Member member = memberInfoRepository.findByMemNick(searchValue); // MemId를
    // 사용하여 Member 엔티티 찾기
    // int memId = (int) member.getMemId(); // 찾은 Member 엔티티에서 memNick 가져오기

    // lists =
    // postInfoRepository.findByBoardCdAndMember_MemIdOrderByPostIdDesc(boardCd,
    // memId);
    // }

    // // for (Post postComment : lists) {
    // // System.out.println("PostId: " + postComment.getPostId());
    // // System.out.println("post_comment : " +
    // // commentInfoRepository.countByPost_PostId(postComment.getPostId()));
    // // }

    // // cpage 작업
    // BoardCpageTO cpageTO = new BoardCpageTO();
    // int cpage = 1;
    // int recordPerPage = cpageTO.getRecordPerPage();
    // int blockPerPage = cpageTO.getBlockPerPage();

    // cpageTO.setCpage(cpage);
    // cpageTO.setTotalRecord(lists.size());
    // cpageTO.setTotalPage(((cpageTO.getTotalRecord() - 1) / recordPerPage) + 1);
    // cpageTO.setStartBlock(cpage - (cpage - 1) % blockPerPage);
    // cpageTO.setEndBlock(cpage - (cpage - 1) % blockPerPage + blockPerPage - 1);
    // if (cpageTO.getEndBlock() >= cpageTO.getTotalPage()) {
    // cpageTO.setEndBlock(cpageTO.getTotalPage());
    // }

    // // SKIP - 각 페이지 별 게시글 자르기
    // int skip = (cpage - 1) * cpageTO.getRecordPerPage();
    // int startIndex = Math.min(skip, lists.size());
    // int endIndex = Math.min(startIndex + cpageTO.getRecordPerPage(),
    // lists.size());
    // lists = new ArrayList<>(lists.subList(startIndex, endIndex));

    // cpageTO.setBoardLists(lists);

    // ModelAndView modelAndView = new ModelAndView();
    // modelAndView.setViewName("board_list");
    // modelAndView.addObject("lists", lists);
    // modelAndView.addObject("loginCheck", loginCheck);
    // modelAndView.addObject("boardCd", boardCd);
    // modelAndView.addObject("cpage", cpageTO);

    // return modelAndView;
    // }

    // @GetMapping("/list_search_size") // 검색+사이즈가 있는경우
    // public ModelAndView getBoardListFragment(@RequestParam("board_cd") String
    // boardCd,
    // @RequestParam("selectOption") String selectOption,
    // @RequestParam("searchValue") String searchValue,
    // @RequestParam("selectedSize") int selectedSize) {
    // // 모델에 필요한 데이터를 추가하고, 템플릿 이름을 반환
    // System.out.println("getSearchBoardListFragment 호출");

    // String loginCheck = "false";

    // if
    // (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser"))
    // {
    // System.out.println("멤버 있음 ");
    // loginCheck = "true";
    // } else {
    // System.out.println("멤버 없음");
    // loginCheck = "false";
    // }

    // ArrayList<Post> lists = new ArrayList<>();

    // if ("title".equals(selectOption)) {
    // lists =
    // postInfoRepository.findByBoardCdAndPostTitleLikeOrderByPostIdDesc(boardCd,
    // "%" + searchValue + "%");
    // } else if ("content".equals(selectOption)) {
    // lists =
    // postInfoRepository.findByBoardCdAndPostContentLikeOrderByPostIdDesc(boardCd,
    // "%" + searchValue + "%");
    // } else if ("writer".equals(selectOption)) {
    // // 사용자의 memId를 기반으로 memNick 가져오기
    // Member member = memberInfoRepository.findByMemNick(searchValue); // MemId를
    // 사용하여 Member 엔티티 찾기
    // int memId = (int) member.getMemId(); // 찾은 Member 엔티티에서 memNick 가져오기

    // lists =
    // postInfoRepository.findByBoardCdAndMember_MemIdOrderByPostIdDesc(boardCd,
    // memId);
    // }

    // // cpage 작업
    // BoardCpageTO cpageTO = new BoardCpageTO();
    // int cpage = 1;
    // int recordPerPage = selectedSize;
    // int blockPerPage = cpageTO.getBlockPerPage();

    // System.out.println(recordPerPage);

    // cpageTO.setCpage(cpage);
    // cpageTO.setTotalRecord(lists.size());
    // cpageTO.setTotalPage(((cpageTO.getTotalRecord() - 1) / recordPerPage) + 1);
    // cpageTO.setStartBlock(cpage - (cpage - 1) % blockPerPage);
    // cpageTO.setEndBlock(cpage - (cpage - 1) % blockPerPage + blockPerPage - 1);
    // if (cpageTO.getEndBlock() >= cpageTO.getTotalPage()) {
    // cpageTO.setEndBlock(cpageTO.getTotalPage());
    // }

    // // SKIP - 각 페이지 별 게시글 자르기
    // int skip = (cpage - 1) * selectedSize;
    // int startIndex = Math.min(skip, lists.size());
    // int endIndex = Math.min(startIndex + selectedSize, lists.size());
    // lists = new ArrayList<>(lists.subList(startIndex, endIndex));

    // cpageTO.setBoardLists(lists);

    // ModelAndView modelAndView = new ModelAndView();
    // modelAndView.setViewName("board_list");
    // modelAndView.addObject("lists", lists);
    // modelAndView.addObject("loginCheck", loginCheck);
    // modelAndView.addObject("boardCd", boardCd);
    // modelAndView.addObject("cpage", cpageTO);
    // modelAndView.addObject("recpage", recordPerPage);

    // return modelAndView;
    // }

    // @GetMapping("/list_size") // 사이즈만 있는경우
    // public ModelAndView getBoardListFragment(@RequestParam("board_cd") String
    // boardCd,
    // @RequestParam("selectedSize") int selectedSize) {
    // // 모델에 필요한 데이터를 추가하고, 템플릿 이름을 반환
    // System.out.println("getSearchBoardListFragment 호출");

    // String loginCheck = "false";

    // if
    // (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser"))
    // {
    // System.out.println("멤버 있음 ");
    // loginCheck = "true";
    // } else {
    // System.out.println("멤버 없음");
    // loginCheck = "false";
    // }

    // ArrayList<Post> lists = new ArrayList<>();
    // lists = postInfoRepository.findByBoardCdOrderByPostIdDesc(boardCd);

    // // cpage 작업
    // BoardCpageTO cpageTO = new BoardCpageTO();
    // int cpage = 1;
    // int recordPerPage = selectedSize;
    // int blockPerPage = cpageTO.getBlockPerPage();

    // System.out.println(recordPerPage);

    // cpageTO.setCpage(cpage);
    // cpageTO.setTotalRecord(lists.size());
    // cpageTO.setTotalPage(((cpageTO.getTotalRecord() - 1) / recordPerPage) + 1);
    // cpageTO.setStartBlock(cpage - (cpage - 1) % blockPerPage);
    // cpageTO.setEndBlock(cpage - (cpage - 1) % blockPerPage + blockPerPage - 1);
    // if (cpageTO.getEndBlock() >= cpageTO.getTotalPage()) {
    // cpageTO.setEndBlock(cpageTO.getTotalPage());
    // }

    // // SKIP - 각 페이지 별 게시글 자르기
    // int skip = (cpage - 1) * selectedSize;
    // int startIndex = Math.min(skip, lists.size());
    // int endIndex = Math.min(startIndex + selectedSize, lists.size());
    // lists = new ArrayList<>(lists.subList(startIndex, endIndex));

    // cpageTO.setBoardLists(lists);

    // ModelAndView modelAndView = new ModelAndView();
    // modelAndView.setViewName("board_list");
    // modelAndView.addObject("lists", lists);
    // modelAndView.addObject("loginCheck", loginCheck);
    // modelAndView.addObject("boardCd", boardCd);
    // modelAndView.addObject("cpage", cpageTO);
    // modelAndView.addObject("recpage", recordPerPage);

    // return modelAndView;
    // }

    // @GetMapping("/list_tag") // 태그만 검색
    // public ModelAndView getBoardListFragment(@RequestParam("board_cd") String
    // boardCd,
    // @RequestParam("selectTag") String selectTag) {
    // // 모델에 필요한 데이터를 추가하고, 템플릿 이름을 반환
    // System.out.println("getTagBoardListFragment 호출");

    // String loginCheck = "false";

    // if
    // (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser"))
    // {
    // System.out.println("멤버 있음 ");
    // loginCheck = "true";
    // } else {
    // System.out.println("멤버 없음");
    // loginCheck = "false";
    // }

    // ArrayList<Post> lists = new ArrayList<>();
    // if ("All".equals(selectTag)) {
    // lists = postInfoRepository.findByBoardCdOrderByPostIdDesc(boardCd);
    // } else {
    // lists =
    // postInfoRepository.findByBoardCdAndPostTagsLikeOrderByPostIdDesc(boardCd,
    // selectTag);
    // }

    // // cpage 작업
    // BoardCpageTO cpageTO = new BoardCpageTO();
    // int cpage = 1;
    // int recordPerPage = cpageTO.getRecordPerPage();
    // int blockPerPage = cpageTO.getBlockPerPage();

    // cpageTO.setCpage(cpage);
    // cpageTO.setTotalRecord(lists.size());
    // cpageTO.setTotalPage(((cpageTO.getTotalRecord() - 1) / recordPerPage) + 1);
    // cpageTO.setStartBlock(cpage - (cpage - 1) % blockPerPage);
    // cpageTO.setEndBlock(cpage - (cpage - 1) % blockPerPage + blockPerPage - 1);
    // if (cpageTO.getEndBlock() >= cpageTO.getTotalPage()) {
    // cpageTO.setEndBlock(cpageTO.getTotalPage());
    // }

    // // SKIP - 각 페이지 별 게시글 자르기
    // int skip = (cpage - 1) * cpageTO.getRecordPerPage();
    // int startIndex = Math.min(skip, lists.size());
    // int endIndex = Math.min(startIndex + cpageTO.getRecordPerPage(),
    // lists.size());
    // lists = new ArrayList<>(lists.subList(startIndex, endIndex));

    // cpageTO.setBoardLists(lists);

    // ModelAndView modelAndView = new ModelAndView();
    // modelAndView.setViewName("board_list");
    // modelAndView.addObject("lists", lists);
    // modelAndView.addObject("loginCheck", loginCheck);
    // modelAndView.addObject("boardCd", boardCd);
    // modelAndView.addObject("cpage", cpageTO);
    // modelAndView.addObject("list_tag", selectTag);

    // return modelAndView;
    // }

}