package net.karlshaffer.q.service;

import net.karlshaffer.q.model.Role;
import net.karlshaffer.q.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

public interface RoleService {
    Iterable<Role> findAll();
    Role findByName(String name);
}
