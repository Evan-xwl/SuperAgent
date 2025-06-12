package com.xwl.superagent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ruoling
 * @date 2025/6/12 16:50:52
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilmEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
}
