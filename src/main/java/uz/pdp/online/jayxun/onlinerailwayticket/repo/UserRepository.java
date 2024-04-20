package uz.pdp.online.jayxun.onlinerailwayticket.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.jayxun.onlinerailwayticket.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
}