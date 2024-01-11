// package platform.game.service.entity;

// import java.io.Serializable;
// import java.sql.Date;

// import jakarta.persistence.Column;
// import jakarta.persistence.Embeddable;
// import jakarta.persistence.EmbeddedId;
// import jakarta.persistence.Entity;
// import jakarta.persistence.FetchType;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.Temporal;
// import jakarta.persistence.TemporalType;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Entity
// @Data
// @AllArgsConstructor
// @NoArgsConstructor
// @Builder
// public class PointHistory {
    
//     @Embeddable
//     public static class PointHistoryId implements Serializable {
//         @Column(name = "mem_id")
//         private Long memId;

//         @Column(name = "point_history_id")
//         private Long pointHistoryId;
//         // 생성자, equals, hashCode 등을 구현해도 좋습니다.
//     }

//     @EmbeddedId
//     private PointHistoryId id;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "mem_id", insertable = false, updatable = false)
//     private Member member;

//     @Column(length = 5)
//     private String pointKindCd;

//     private Integer pointCnt;

//     @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
//     @Temporal(TemporalType.TIMESTAMP)
//     private Date createdAt; 
    
// }