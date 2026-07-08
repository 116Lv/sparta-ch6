package com.ch6.demo.menu.application;

import com.ch6.demo.menu.domain.Menu;
import com.ch6.demo.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
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

    @Transactional(readOnly = true)
    public List<PopularMenuResult> getPopularMenus() {
        LocalDateTime orderedFrom = LocalDateTime.now().minusDays(7);
        return menuRepository.findPopularMenus(orderedFrom, PageRequest.of(0, 3));
    }
}
