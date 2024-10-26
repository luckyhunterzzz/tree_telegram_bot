package com.luckyhunterzzz.pandev_bot.repository;

import com.luckyhunterzzz.pandev_bot.model.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностями {@link Category}.
 *
 * Этот интерфейс предоставляет методы для выполнения операций с категориями,
 * включая поиск категории по имени. Он наследует стандартные методы из
 * {@link JpaRepository}, такие как сохранение, удаление и поиск категорий.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Находит категорию по имени.
     *
     * @param name имя категории
     * @return объект {@link Optional}, содержащий найденную категорию, если она существует
     */
    Optional<Category> findByName(String name);
}
