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

public class ShopController {

    @Autowired
    private ItemInfoRepository itemInfoRepository;

    @RequestMapping("/shop")
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
                modelAndView.addObject("memId",member.getMemId());
            }
        } else {
            System.out.println("멤버 없음");
        }

        modelAndView.addObject("totalItemCount", totalItemCount);
        modelAndView.addObject("items", items);
        return modelAndView;
    }

    
    @GetMapping("/shop_search")
    public ModelAndView listItemsByKind(@RequestParam("ItemSearch") String itemName,
            @RequestParam("categorySelect") String itemKindCd) {
        System.out.println("listSearch 호출");
        ModelAndView modelAndView = new ModelAndView();

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMember();
            if (member != null) {
                modelAndView.addObject("nickname", member.getMemNick());
                modelAndView.addObject("level", member.getMemLvl());
                modelAndView.addObject("point", member.getMemCurPoint());
                modelAndView.addObject("memId",member.getMemId());
            }
        } else {
            System.out.println("멤버 없음");
        }
        System.out.println(itemName);
        System.out.println(itemKindCd);

        ArrayList<Item> items = new ArrayList<>();
        if(itemKindCd=="all"){
            items = itemInfoRepository.findByItemNmContaining(itemName);
        }else{
            items = itemInfoRepository.findByItemNmContainingAndItemKindCdContaining(itemName,itemKindCd);
        }

        System.out.println("items : " +items);
        modelAndView.setViewName("shop");
        modelAndView.addObject("items", items);

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