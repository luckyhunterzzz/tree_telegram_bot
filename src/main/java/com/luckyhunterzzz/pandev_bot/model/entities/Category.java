package com.luckyhunterzzz.pandev_bot.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * Сущность для представления категории.
 *
 * Этот класс представляет категорию в системе и содержит информацию о её имени,
 * родительской категории и дочерних категориях. Он используется для построения
 * иерархии категорий.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    /**
     * Уникальный идентификатор категории.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя категории.
     * Не может быть пустым и должно быть уникальным.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Родительская категория.
     * Связывает текущую категорию с её родительской категорией.
     */
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    /**
     * Дочерние категории.
     * Содержит набор категорий, которые являются дочерними для данной категории.
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Category> children;
}