package com.luckyhunterzzz.pandev_bot.service;

import com.luckyhunterzzz.pandev_bot.model.entities.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс для построения дерева категорий.
 *
 * Этот класс предоставляет методы для создания строкового представления дерева категорий
 * на основе списка объектов {@link Category}. Он используется для визуализации иерархии категорий.
 */
@Component
public class CategoryTreeBuilder {

    /**
     * Строит дерево категорий из списка категорий.
     *
     * @param categories список категорий, из которых будет построено дерево
     * @return строковое представление дерева категорий
     */
    public String buildTree(List<Category> categories) {
        return buildCategoryTree(null, "", categories);
    }

    /**
     * Рекурсивно строит строковое представление дерева категорий.
     *
     * @param parentId идентификатор родительской категории
     * @param prefix префикс для форматирования строки
     * @param categories список категорий
     * @return строковое представление поддерева категорий
     */
    private String buildCategoryTree(Long parentId, String prefix, List<Category> categories) {
        return categories.stream()
                .filter(category -> (parentId == null && category.getParent() == null) ||
                        (category.getParent() != null && category.getParent().getId().equals(parentId)))
                .map(category -> prefix + "- " + category.getName() + "\n" +
                        buildCategoryTree(category.getId(), prefix + "  ", categories))
                .collect(Collectors.joining());
    }
}