package com.ch6.demo.menu.repository;

import com.ch6.demo.menu.application.PopularMenuResult;
import com.ch6.demo.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findAllByActiveTrueOrderByIdAsc();

    @Query("""
            SELECT new com.ch6.demo.menu.application.PopularMenuResult(
                m.id,
                m.name,
                m.price,
                COUNT(o.id)
            )
            FROM Order o
            JOIN o.menu m
            WHERE o.orderedAt >= :orderedFrom
            GROUP BY m.id, m.name, m.price
            ORDER BY COUNT(o.id) DESC, m.id ASC
            """)
    List<PopularMenuResult> findPopularMenus(
            @Param("orderedFrom") LocalDateTime orderedFrom,
            Pageable pageable
    );
}
