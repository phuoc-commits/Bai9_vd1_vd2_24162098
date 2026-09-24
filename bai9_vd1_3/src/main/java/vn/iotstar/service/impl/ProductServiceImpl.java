package vn.iotstar.service.impl;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import vn.iotstar.dto.ProductDTO;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.User;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.ProductService;

@Service @RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository products;
    private final UserRepository users;
    private final Cloudinary cloudinary;
    private ProductDTO toDto(Product p) {
        ProductDTO dto = new ProductDTO(); dto.setId(p.getId()); dto.setName(p.getName()); dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice()); dto.setImageUrl(p.getImageUrl()); dto.setUserId(p.getUser().getId()); dto.setUsername(p.getUser().getUsername()); return dto;
    }
    private String upload(MultipartFile image) {
        if (image == null || image.isEmpty()) return null;
        try { return String.valueOf(cloudinary.uploader().upload(image.getBytes(), Map.of("folder", "shop/products")).get("secure_url")); }
        catch (Exception e) { throw new IllegalStateException("Upload ảnh thất bại", e); }
    }
    @Override public Page<ProductDTO> search(String keyword, int page, int size) {
        String value = keyword == null ? "" : keyword.trim(); return products.findByNameContainingIgnoreCase(value, PageRequest.of(page, size)).map(this::toDto);
    }
    @Override public ProductDTO findById(Long id) { return toDto(products.findById(id).orElseThrow()); }
    @Override public ProductDTO save(ProductDTO dto, MultipartFile image) {
        User user = users.findById(dto.getUserId()).orElseThrow();
        Product p = Product.builder().name(dto.getName()).description(dto.getDescription()).price(dto.getPrice()).user(user).imageUrl(upload(image)).build();
        return toDto(products.save(p));
    }
    @Override public ProductDTO update(Long id, ProductDTO dto, MultipartFile image) {
        Product p = products.findById(id).orElseThrow(); p.setName(dto.getName()); p.setDescription(dto.getDescription()); p.setPrice(dto.getPrice());
        if (image != null && !image.isEmpty()) p.setImageUrl(upload(image)); return toDto(products.save(p));
    }
    @Override public void delete(Long id) { products.deleteById(id); }
    @Override public long countProducts() { return products.count(); }
    @Override public long countByUser(Long id) { return products.countByUserId(id); }
}