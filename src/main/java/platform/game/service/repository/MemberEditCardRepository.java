package platform.game.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.MemberCard;


@Repository
public interface MemberEditCardRepository extends JpaRepository<MemberCard, Long>{

    // 모든 카드 요소 불러오기
    MemberCard findAllByMemId(long mem_id);

    // 프로필사진만 불러오기
    @Query(value="select profile_image from member_card where mem_id=:mem_id", nativeQuery = true)
    String findByProfileImage(long mem_id);
}
