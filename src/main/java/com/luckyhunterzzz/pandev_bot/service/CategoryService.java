package com.luckyhunterzzz.pandev_bot.service;

import com.luckyhunterzzz.pandev_bot.model.entities.Category;
import com.luckyhunterzzz.pandev_bot.repository.CategoryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления категориями.
 *
 * Этот класс предоставляет методы для работы с категориями, включая добавление,
 * удаление и получение дерева категорий. Он использует {@link CategoryRepository}
 * для взаимодействия с базой данных и {@link CategoryTreeBuilder} для построения
 * дерева категорий.
 */
@Slf4j
@Setter
@Getter
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryTreeBuilder categoryTreeBuilder;

    /**
     * Получает строковое представление дерева категорий.
     *
     * @return строковое представление дерева категорий или сообщение о том, что дерево пусто
     */
    public String getCategoryTree() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            log.info("Дерево категорий пусто.");
            return "Дерево категорий пусто.";
        }
        log.info("Вызов дерева категорий");
        return categoryTreeBuilder.buildTree(categories);
    }

    /**
     * Добавляет новый элемент в корневую категорию.
     *
     * @param elementName имя элемента, который нужно добавить
     * @return сообщение о результате операции
     */
    public String addElement(String elementName) {
        Optional<Category> categoryOpt = categoryRepository.findByName(elementName);
        if (categoryOpt.isPresent()) {
            log.info("Элемент с таким именем уже существует.");
            return "Элемент с таким именем уже существует.";
        }
        Category category = new Category();
        category.setName(elementName);
        categoryRepository.save(category);
        log.info("Элемент \"" + elementName + "\" добавлен в корневую категорию.");
        return "Элемент \"" + elementName + "\" добавлен в корневую категорию.";
    }

    /**
     * Добавляет новый элемент как дочерний к родительскому элементу по имени.
     *
     * @param parentName имя родительского элемента
     * @param childName имя дочернего элемента
     * @return сообщение о результате операции
     */
    public String addElement(String parentName, String childName) {
        Optional<Category> parentOpt = categoryRepository.findByName(parentName);
        if (parentOpt.isEmpty()) {
            log.info("Родительский элемент \"" + parentName + "\" не найден.");
            return "Родительский элемент \"" + parentName + "\" не найден.";
        }

        Optional<Category> childOpt = categoryRepository.findByName(childName);
        if (childOpt.isPresent()) {
            log.info("Элемент с именем \"" + childName + "\" уже существует.");
            return "Элемент с именем \"" + childName + "\" уже существует.";
        }

        Category childCategory = new Category();
        childCategory.setName(childName);
        childCategory.setParent(parentOpt.get());
        categoryRepository.save(childCategory);
        log.info("Элемент \"" + childName + "\" добавлен к родительскому элементу \"" + parentName + "\".");
        return "Элемент \"" + childName + "\" добавлен к родительскому элементу \"" + parentName + "\".";
    }

    /**
     * Добавляет новый элемент как дочерний к родительскому элементу по ID.
     *
     * @param parentId идентификатор родительского элемента
     * @param childName имя дочернего элемента
     * @return сообщение о результате операции
     */
    public String addElement(Long parentId, String childName) {
        Optional<Category> parentOpt = categoryRepository.findById(parentId);
        if (parentOpt.isEmpty()) {
            log.info("Родительский элемент с ID \"" + parentId + "\" не найден.");
            return "Родительский элемент с ID \"" + parentId + "\" не найден.";
        }

        Optional<Category> childOpt = categoryRepository.findByName(childName);
        if (childOpt.isPresent()) {
            log.info("Элемент с именем \"" + childName + "\" уже существует.");
            return "Элемент с именем \"" + childName + "\" уже существует.";
        }

        Category childCategory = new Category();
        childCategory.setName(childName);
        childCategory.setParent(parentOpt.get());
        categoryRepository.save(childCategory);

        log.info("Элемент \"" + childName + "\" добавлен к родительскому элементу с ID \"" + parentId + "\".");
        return "Элемент \"" + childName + "\" добавлен к родительскому элементу с ID \"" + parentId + "\".";
    }

    /**
     * Удаляет элемент по имени.
     *
     * @param elementName имя элемента, который нужно удалить
     * @return сообщение о результате операции
     */
    public String removeElement(String elementName) {
        Optional<Category> categoryOpt = categoryRepository.findByName(elementName);
        if (categoryOpt.isEmpty()) {
            log.info("Элемент \"" + elementName + "\" не найден.");
            return "Элемент \"" + elementName + "\" не найден.";
        }
        categoryRepository.delete(categoryOpt.get());
        log.info("Элемент \\\"\" + elementName + \"\\\" и его дочерние элементы удалены.");
        return "Элемент \"" + elementName + "\" и его дочерние элементы удалены.";
    }

    /**
     * Получает список всех категорий.
     *
     * @return список всех категорий
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
