package Main.dto;

import lombok.Data;

@Data
public class ProductCardDTO {
    private Long id;
    private String name;
    private String category;
    private Double price;
    private Integer stock;
    private String image;
    private String description;
}