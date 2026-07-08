package com.ch6.demo.menu.repository;

import com.ch6.demo.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findAllByActiveTrueOrderByIdAsc();
}
