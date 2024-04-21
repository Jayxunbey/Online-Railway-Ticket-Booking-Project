package uz.pdp.online.jayxun.onlinerailwayticket.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.online.jayxun.onlinerailwayticket.entity.ConfirmSentCode;

import java.util.Optional;


public interface ConfirmSentCodeRepository extends JpaRepository<ConfirmSentCode, String> {

    Optional<ConfirmSentCode> findByToken(String token);
}