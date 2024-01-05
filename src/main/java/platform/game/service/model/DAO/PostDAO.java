package platform.game.service.model.DAO;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.Post;
import platform.game.service.repository.PostInfoRepository;


@Repository
public class PostDAO {
    
    @Autowired
    private PostInfoRepository postInfoRepository;
	
	public ArrayList<Post> List(){
        System.out.println("List 호출");
        ArrayList<Post> lists = postInfoRepository.findAll();
        return lists;
    }

	public void Write() {}

	public int WriteOk(Post post){
		System.out.println("WriteOk 호출");
		try {
            // Spring Data JPA를 이용한 저장
            postInfoRepository.save(post);

            return 0; // 성공 시 0 반환

        } catch (Exception e) {
            System.out.println("WriteOk(Post post) 오류 : " + e.getMessage());

            return 1; // 실패 시 1 반환
        }
	}

    public Post View(Post post){
        System.out.println("postDAO_View호출");

        post = postInfoRepository.findByPostId(post.getPostId());
        System.out.println("post : " + post);

        return post;
    }

    public int ModifyOk(Post post){
		System.out.println("ModifyOk 호출");
		try {
            // Spring Data JPA를 이용한 저장
            postInfoRepository.save(post);

            return 0; // 성공 시 0 반환

        } catch (Exception e) {
            System.out.println("ModifyOk(Post post) 오류 : " + e.getMessage());

            return 1; // 실패 시 1 반환
        }
	}
}
