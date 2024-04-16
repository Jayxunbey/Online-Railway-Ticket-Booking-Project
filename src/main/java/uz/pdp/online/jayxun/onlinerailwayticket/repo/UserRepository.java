package uz.pdp.online.jayxun.onlinerailwayticket.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.jayxun.onlinerailwayticket.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
}