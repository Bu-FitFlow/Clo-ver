package com.fitflow.clover.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(nullable = false, length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> children = new ArrayList<>();

    @Column(name = "depth_level", nullable = false)
    private Integer depthLevel;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    public void addChildCategory(Category child) {
        this.children.add(child);
        child.setParent(this);
    }

    private void setParent(Category parent) {
        this.parent = parent;
    }
}
