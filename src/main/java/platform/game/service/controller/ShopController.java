package platform.game.service.controller;

// ShopController 클래스

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.core.context.SecurityContextHolder;
import platform.game.service.entity.Item;
import platform.game.service.entity.Member;
import platform.game.service.service.MemberInfoDetails;
import platform.game.service.repository.ItemInfoRepository;

import java.util.ArrayList;

@Controller
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ItemInfoRepository itemInfoRepository;

    // @GetMapping
    // public ModelAndView shop(@RequestParam(required = false) String itemName,
    //                         @RequestParam(required = false) String itemKindCd) {
    //     ModelAndView modelAndView = new ModelAndView("shop");

    //     long totalItemCount = itemInfoRepository.count();
    //     modelAndView.addObject("point", "로그인 해주세요.");

    //     if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
    //         Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMember();
    //         if (member != null) {
    //             modelAndView.addObject("nickname", member.getMemNick());
    //             modelAndView.addObject("point", member.getMemCurPoint());
    //         }
    //     } else {
    //         System.out.println("멤버 없음");
    //     }

    //     ArrayList<Item> items;
    //     if (itemName != null && !itemName.isEmpty()) {
    //         // 아이템 이름에 따라 필터링
    //         Item foundItem = itemInfoRepository.findByItemNm(itemName);
    //         items = new ArrayList<>();
    //         if (foundItem != null) {
    //             items.add(foundItem);
    //         }
    //     } else if (itemKindCd != null && !itemKindCd.isEmpty()) {
    //         // 아이템 종류에 따라 필터링
    //         items = itemInfoRepository.findByItemKindCd(itemKindCd);
    //         modelAndView.addObject("items", items);
    //     } else {
    //         // 모든 아이템 가져오기
    //         items = itemInfoRepository.findAll();
    //         modelAndView.addObject("items", items);
    //     }
        
    //     modelAndView.addObject("totalItemCount", totalItemCount);
    //     return modelAndView;
    // }


    @GetMapping
    public ModelAndView shop() {
        long totalItemCount = itemInfoRepository.count();
        ArrayList<Item> items = itemInfoRepository.findAll();

        ModelAndView modelAndView = new ModelAndView("shop");
        modelAndView.addObject("point", "로그인 해주세요.");
        // modelAndView.addObject("nickname", "");
        modelAndView.addObject("level", "0");

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMember();
            if (member != null) {
                modelAndView.addObject("nickname", member.getMemNick());
                modelAndView.addObject("level", member.getMemLvl());
                modelAndView.addObject("point", member.getMemCurPoint());
            }
        } else {
            System.out.println("멤버 없음");
        }

        modelAndView.addObject("totalItemCount", totalItemCount);
        modelAndView.addObject("items", items);
        return modelAndView;
    }

    @GetMapping("/searchItem")
    public ModelAndView searchItem(@RequestParam String itemName) {
        Item foundItem = itemInfoRepository.findByItemNm(itemName);
        
        ModelAndView modelAndView = new ModelAndView("searchResult");
        modelAndView.addObject("foundItem", foundItem);
        
        return modelAndView;
    }
    
    @GetMapping("/listItemsByKind")
    public ModelAndView listItemsByKind(@RequestParam String itemKindCd) {
        // 수정: 아이템 종류별로 찾기 메서드 변경
        ArrayList<Item> itemsByKind = itemInfoRepository.findByItemKindCd(itemKindCd);
        
        ModelAndView modelAndView = new ModelAndView("listItemsByKind");
        modelAndView.addObject("itemsByKind", itemsByKind);
        
        return modelAndView;
    }

    // @GetMapping("/search")
    // public ModelAndView shop(@RequestParam("categorySelect") String categorySelect,
    //         @RequestParam("ItemSearch") String ItemSearch) {
        
    //     System.out.println(categorySelect);
    //     System.out.println(ItemSearch);
        
    //     ArrayList<Item> lists = new ArrayList<>();

    //     // lists= ItemInfoRepository.findByItemNm("%" + ItemSearch + "%");
    //     if(categorySelect == "all"){
            

    //         System.out.println(lists);
    //     }else{

    //     }
        
    //     return null;
    // }

}