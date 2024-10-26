package com.luckyhunterzzz.pandev_bot.service;

import com.luckyhunterzzz.pandev_bot.model.entities.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class CategoryTreeBuilderTest {

    private final CategoryTreeBuilder categoryTreeBuilder = new CategoryTreeBuilder();

    @Test
    void buildTree_emptyTree() {
        List<Category> categories = Collections.emptyList();

        String result = categoryTreeBuilder.buildTree(categories);

        assertEquals("", result);
    }

    @Test
    void buildTree_withRootAndChild() {
        Category root = new Category();
        root.setId(1L);
        root.setName("Root");

        Category child = new Category();
        child.setId(2L);
        child.setName("Child");
        child.setParent(root);

        root.setChildren(new HashSet<>(List.of(child)));

        String result = categoryTreeBuilder.buildTree(List.of(root, child));

        assertTrue(result.contains("Root"));
        assertTrue(result.contains("  - Child"));
    }
}