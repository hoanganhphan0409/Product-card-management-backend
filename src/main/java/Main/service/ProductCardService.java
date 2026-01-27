package Main.service;



import Main.dto.ProductCardCreateDTO;
import Main.dto.ProductCardResponseDTO;
import Main.dto.ProductCardUpdateDTO;
import Main.model.ProductCard;
import Main.exception.ResourceNotFoundException;
import Main.repository.ProductCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductCardService {

    @Autowired
    private ProductCardRepository productCardRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private ProductCardResponseDTO convertToDTO(ProductCard productCard) {
        ProductCardResponseDTO dto = new ProductCardResponseDTO();
        dto.setId(productCard.getId());
        dto.setName(productCard.getName());
        dto.setCategory(productCard.getCategory());
        dto.setPrice(productCard.getPrice());
        dto.setImage(productCard.getImage());
        dto.setDescription(productCard.getDescription());
        dto.setCreatedAt(productCard.getCreatedAt() != null ?
                productCard.getCreatedAt().format(DATE_FORMATTER) : null);
        dto.setUpdatedAt(productCard.getUpdatedAt() != null ?
                productCard.getUpdatedAt().format(DATE_FORMATTER) : null);
        return dto;
    }

    private ProductCard convertToEntity(ProductCardCreateDTO dto) {
        ProductCard productCard = new ProductCard();
        productCard.setName(dto.getName());
        productCard.setCategory(dto.getCategory());
        productCard.setPrice(dto.getPrice());
        productCard.setImage(dto.getImage());
        productCard.setDescription(dto.getDescription());
        return productCard;
    }

    private void updateEntityFromDTO(ProductCard productCard, ProductCardUpdateDTO dto) {
        productCard.setName(dto.getName());
        productCard.setCategory(dto.getCategory());
        productCard.setPrice(dto.getPrice());
        productCard.setImage(dto.getImage());
        productCard.setDescription(dto.getDescription());
    }

    public List<ProductCardResponseDTO> getAllProductCards() {
        return productCardRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductCardResponseDTO getProductCardById(Long id) {
        ProductCard productCard = productCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Product Card với ID: " + id));
        return convertToDTO(productCard);
    }

    public List<ProductCardResponseDTO> searchProductCards(String keyword, String category) {
        List<ProductCard> productCards;

        if (keyword != null && !keyword.trim().isEmpty() &&
                category != null && !category.trim().isEmpty() && !category.equals("all")) {
            productCards = productCardRepository.findByNameContainingIgnoreCaseAndCategory(keyword, category);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            productCards = productCardRepository.findByNameContainingIgnoreCase(keyword);
        } else if (category != null && !category.trim().isEmpty() && !category.equals("all")) {
            productCards = productCardRepository.findByCategory(category);
        } else {
            productCards = productCardRepository.findAll();
        }

        return productCards.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductCardResponseDTO createProductCard(ProductCardCreateDTO productCardDTO) {
        ProductCard productCard = convertToEntity(productCardDTO);
        ProductCard savedProductCard = productCardRepository.save(productCard);
        return convertToDTO(savedProductCard);
    }

    public ProductCardResponseDTO updateProductCard(Long id, ProductCardUpdateDTO productCardDTO) {
        ProductCard productCard = productCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Product Card với ID: " + id));

        updateEntityFromDTO(productCard, productCardDTO);
        ProductCard updatedProductCard = productCardRepository.save(productCard);
        return convertToDTO(updatedProductCard);
    }

    public void deleteProductCard(Long id) {
        if (!productCardRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy Product Card với ID: " + id);
        }
        productCardRepository.deleteById(id);
    }

    public List<String> getAllCategories() {
        return productCardRepository.findAll().stream()
                .map(ProductCard::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public long getTotalProductCards() {
        return productCardRepository.count();
    }

    public long getProductCardCountByCategory(String category) {
        return productCardRepository.findByCategory(category).size();
    }
}