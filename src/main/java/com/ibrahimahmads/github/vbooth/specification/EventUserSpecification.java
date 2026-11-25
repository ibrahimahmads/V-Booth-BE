package com.ibrahimahmads.github.vbooth.specification;

import com.ibrahimahmads.github.vbooth.dto.request.EventUserSeach;
import com.ibrahimahmads.github.vbooth.entity.Event;
import com.ibrahimahmads.github.vbooth.entity.Paket;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventUserSpecification {
    public static Specification<Event> getSpecification(UUID accountId, EventUserSeach search){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("account").get("id"), accountId));
            if (search != null) {
                if (StringUtils.hasText(search.getKeyword())) {
                    String pattern = "%" + search.getKeyword().trim().toLowerCase() + "%";
                    Join<Event, Paket> paketJoin = root.join("paket", JoinType.INNER);
                    predicates.add(criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("eventName")), pattern),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("slug")), pattern),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("eventLocation")), pattern),
                            criteriaBuilder.like(criteriaBuilder.lower(paketJoin.get("name")), pattern)
                    ));
                }
                if (search.getStatus() != null) {
                    Predicate statusPredicate = criteriaBuilder.equal(
                            root.get("eventStatus"),
                            search.getStatus()
                    );
                    predicates.add(statusPredicate);
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
