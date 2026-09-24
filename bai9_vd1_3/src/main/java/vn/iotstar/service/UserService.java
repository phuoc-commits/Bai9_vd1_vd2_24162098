package vn.iotstar.service;

import org.springframework.data.domain.Page;
import vn.iotstar.dto.ProductDTO;
import vn.iotstar.entity.User;

public interface UserService {
    Page<User> search(String keyword, int page, int size);
    User findById(Long id);
    long countUsers();
    long countProducts(Long id);
}