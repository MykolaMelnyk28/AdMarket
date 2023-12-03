package com.api.admarket.api.v1.entity.ad;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_name")
    private Category parent;

    @Column(columnDefinition = "BOOLEAN NOT NULL DEFAULT TRUE")
    private boolean isLeaf = true;

    @OneToMany(mappedBy = "parent")
    private List<Category> children;
}