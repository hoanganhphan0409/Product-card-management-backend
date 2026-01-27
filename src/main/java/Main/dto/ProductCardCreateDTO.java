package Main.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductCardCreateDTO {
    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;

    @NotBlank(message = "Danh mục không được để trống")
    private String category;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
    private Double price;

    @NotBlank(message = "Hình ảnh không được để trống")
    private String image;

    @Size(max = 5000, message = "Mô tả không được vượt quá 5000 ký tự")
    private String description;
}