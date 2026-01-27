package Main.dto;

import lombok.Data;

@Data
public class ProductCardResponseDTO {
    private Integer id;
    private String name;
    private String category;
    private Double price;
    private String image;
    private String description;
    private String createdAt;
    private String updatedAt;
}