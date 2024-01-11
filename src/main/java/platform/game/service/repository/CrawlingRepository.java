package platform.game.service.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.Crawling;

@Repository
public interface CrawlingRepository extends JpaRepository<Crawling, Integer> {

}
