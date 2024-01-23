package platform.game.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import platform.game.service.entity.Report;


public interface ReportRepository extends JpaRepository<Report, Long> {

}
