package com.ch6.demo.menu.application;

import com.ch6.demo.menu.domain.Menu;
import com.ch6.demo.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Transactional(readOnly = true)
    public List<Menu> getMenus() {
        return menuRepository.findAllByActiveTrueOrderByIdAsc();
    }
}
