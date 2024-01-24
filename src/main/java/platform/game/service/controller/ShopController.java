package platform.game.service.controller;

// ShopController 클래스

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.security.core.context.SecurityContextHolder;

import platform.game.service.entity.CommonCode;
import platform.game.service.entity.Item;
import platform.game.service.entity.Member;
import platform.game.service.entity.MemberProfile;
import platform.game.service.service.MemberInfoDetails;
import platform.game.service.repository.CommonCodeRepository;
import platform.game.service.repository.ItemInfoRepository;
import platform.game.service.repository.MemberItemInfoRepository;
import platform.game.service.repository.MemberProfileRepository;

import java.util.ArrayList;
import java.util.List;

@Controller

public class ShopController {

    @Autowired
    private ItemInfoRepository itemInfoRepository;
    @Autowired
    private MemberItemInfoRepository memberItemInfoRepository;
    @Autowired
    private CommonCodeRepository commonCodeRepository;

    @Autowired
    private MemberProfileRepository memberProfileRepository;

    @RequestMapping("/shop")
    public ModelAndView shop() {
        long totalItemCount = itemInfoRepository.count();
        String navShop = "nav-shop";
        ArrayList<Item> items = itemInfoRepository.findAll();

        ModelAndView modelAndView = new ModelAndView("shop");
        modelAndView.addObject("point", "로그인 해주세요.");
        // modelAndView.addObject("nickname", "");
        modelAndView.addObject("level", "0");

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                MemberProfile memberProfile = memberProfileRepository.findProfileIntroByMemId(member.getMemId());
                List<Integer> memberHaveItems = memberItemInfoRepository.HaveItemCheck(member.getMemId());
                modelAndView.addObject("nickname", member.getMemNick());
                modelAndView.addObject("level", member.getMemLvl());
                modelAndView.addObject("currentPoint", member.getMemCurPoint());
                modelAndView.addObject("memId", member.getMemId());
                modelAndView.addObject("haveitem", memberHaveItems);
                modelAndView.addObject("memberProfile", memberProfile);
            }
        } else {
            System.out.println("멤버 없음");
        }

        modelAndView.addObject("navshop", navShop);
        modelAndView.addObject("totalItemCount", totalItemCount);
        modelAndView.addObject("items", items);
        // 사이드바에 방문자 수 보여주기
        CommonCode visitCount = commonCodeRepository.findByCdOrderByCd("99001");
        modelAndView.addObject("totalCount", visitCount.getRemark1());
        modelAndView.addObject("todayCount", visitCount.getRemark3());
        return modelAndView;
    }

    @GetMapping("/shop_search")
    public ModelAndView listItemsByKind(@RequestParam(name = "ItemSearch", required = false, defaultValue = "") String itemName,
                                        @RequestParam(name = "categorySelect", required = false, defaultValue = "all") String itemKindCd) {
        int totalItemCount = itemInfoRepository.countByItemKindCd(itemKindCd);

        System.out.println("listSearch 호출");
        String navShop = "nav-shop";
        ModelAndView modelAndView = new ModelAndView();

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                MemberProfile memberProfile = memberProfileRepository.findProfileIntroByMemId(member.getMemId());
                modelAndView.addObject("nickname", member.getMemNick());
                modelAndView.addObject("level", member.getMemLvl());
                modelAndView.addObject("currentPoint", member.getMemCurPoint());
                modelAndView.addObject("memId", member.getMemId());
                modelAndView.addObject("memberProfile",memberProfile);
            }
        } else {
            System.out.println("멤버 없음");
        }

        List<Item> items;

        if ("all".equals(itemKindCd) && itemName.isEmpty()) {
            items = itemInfoRepository.findAll();
        } else if (!itemName.isEmpty()) {
            items = itemInfoRepository.findByItemNmContainingIgnoreCase(itemName);
        } else {
            items = itemInfoRepository.findByItemKindCd(itemKindCd);
        }
        
        
        System.out.println("items : " + items);
        modelAndView.setViewName("shop");
        modelAndView.addObject("navshop", navShop);
        modelAndView.addObject("totalItemCount", totalItemCount);
        modelAndView.addObject("items", items);
        // 사이드바에 방문자 수 보여주기
        CommonCode visitCount = commonCodeRepository.findByCdOrderByCd("99001");
        modelAndView.addObject("totalCount", visitCount.getRemark1());
        modelAndView.addObject("todayCount", visitCount.getRemark3());

        return modelAndView;
    }

    @PostMapping("/itempurchase")
    public ResponseEntity<String> ItemPurchase(@RequestBody JsonNode item) {

        Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getMember();

        int flag = 0;

        int point = item.get("Point").asInt();
        String itemCd = item.get("Category").asText();
        int currentPoint = member.getMemCurPoint();

        if(currentPoint < point){
            flag=2;
        } else {
            if (memberItemInfoRepository.PurchaseItem(member.getMemId(), itemCd) == 1
                && memberItemInfoRepository.UpdatePoint(point, member.getMemId()) == 1) {
                flag = 1;
            }
        }

        return ResponseEntity.ok(String.valueOf(flag));
    }
}