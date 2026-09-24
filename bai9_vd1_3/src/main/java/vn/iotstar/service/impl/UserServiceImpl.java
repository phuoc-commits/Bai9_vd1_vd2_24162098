package vn.iotstar.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.iotstar.entity.User;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.UserService;

@Service @RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository users;
    private final ProductRepository products;
    @Override public Page<User> search(String keyword, int page, int size) {
        String value = keyword == null ? "" : keyword.trim();
        return users.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(value, value, PageRequest.of(page, size));
    }
    @Override public User findById(Long id) { return users.findById(id).orElseThrow(); }
    @Override public long countUsers() { return users.count(); }
    @Override public long countProducts(Long id) { return products.countByUserId(id); }
}