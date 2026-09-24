package vn.iotstar.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductDTO {
    private Long id;
    @NotBlank private String name;
    private String description;
    @NotNull @DecimalMin("0.0") private BigDecimal price;
    private String imageUrl;
    private Long userId;
    private String username;
}