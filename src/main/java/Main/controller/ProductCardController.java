package Main.controller;

import Main.dto.*;
import Main.service.ProductCardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/product-cards")
@CrossOrigin(origins = "*")
public class ProductCardController {

    @Autowired
    private ProductCardService productCardService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductCardResponseDTO>>> getAllProductCards(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category) {
        List<ProductCardResponseDTO> productCards = productCardService.searchProductCards(search, category);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách Product Card thành công", productCards));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductCardResponseDTO>> getProductCardById(@PathVariable Long id) {
        ProductCardResponseDTO productCard = productCardService.getProductCardById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin Product Card thành công", productCard));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductCardResponseDTO>> createProductCard(
            @Valid @RequestBody ProductCardCreateDTO productCardDTO) {
        ProductCardResponseDTO productCard = productCardService.createProductCard(productCardDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo Product Card thành công", productCard));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductCardResponseDTO>> updateProductCard(
            @PathVariable Long id,
            @Valid @RequestBody ProductCardUpdateDTO productCardDTO) {
        ProductCardResponseDTO productCard = productCardService.updateProductCard(id, productCardDTO);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật Product Card thành công", productCard));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProductCard(@PathVariable Long id) {
        productCardService.deleteProductCard(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa Product Card thành công", null));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<String>>> getCategories() {
        List<String> categories = productCardService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách danh mục thành công", categories));
    }
}