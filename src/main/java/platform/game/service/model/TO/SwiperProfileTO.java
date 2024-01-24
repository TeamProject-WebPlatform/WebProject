package platform.game.service.model.TO;

import lombok.Data;

@Data
public class SwiperProfileTO {
    private long mem_id;
    private String mem_nick;
    private int mem_lvl;
    private String profile_intro;
    private String profile_image;
    private String profile_header;
    private String profile_card;
    private String profile_rep_badge;
    private String profile_badge_list;

}
