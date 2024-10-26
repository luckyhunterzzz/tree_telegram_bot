package com.luckyhunterzzz.pandev_bot.service;

import com.luckyhunterzzz.pandev_bot.model.entities.Category;
import com.luckyhunterzzz.pandev_bot.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryTreeBuilder categoryTreeBuilder;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void getCategoryTree_emptyTree() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        String result = categoryService.getCategoryTree();

        assertEquals("Дерево категорий пусто.", result);
    }

    @Test
    void addElement_existingCategory() {
        when(categoryRepository.findByName("Root")).thenReturn(Optional.of(new Category()));

        String result = categoryService.addElement("Root");

        assertEquals("Элемент с таким именем уже существует.", result);
    }

    @Test
    void addElement_newRootElement() {
        when(categoryRepository.findByName("Root")).thenReturn(Optional.empty());

        String result = categoryService.addElement("Root");

        assertEquals("Элемент \"Root\" добавлен в корневую категорию.", result);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }
}