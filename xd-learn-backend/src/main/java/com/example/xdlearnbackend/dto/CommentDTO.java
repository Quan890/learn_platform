package com.example.xdlearnbackend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author 木又
 * @date 2026/5/23 15:57
 */
@Data
public class CommentDTO {

    @Min(1)
    @Max(5)
    private Integer rating;

    @NotBlank @Size(min = 10, max = 500)
    private String content;
}

