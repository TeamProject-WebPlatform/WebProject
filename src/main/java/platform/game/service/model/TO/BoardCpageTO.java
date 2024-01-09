package platform.game.service.model.TO;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import platform.game.service.entity.Post;

@Getter
@Setter
@ToString
public class BoardCpageTO {
    private int cpage;
	private int recordPerPage;
	private int blockPerPage;
	private int totalPage;
	private int totalRecord;
	private int startBlock;
	private int endBlock;
	
	private ArrayList<Post> boardLists;

	public BoardCpageTO() {
		this.cpage = 1;
		// 페이지 당 게시글 수. 10추천
		this.recordPerPage = 10;
		this.blockPerPage = 5;
		this.totalPage = 1;
		this.totalRecord = 0;
	}
}
