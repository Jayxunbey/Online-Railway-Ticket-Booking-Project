package uz.pdp.online.jayxun.onlinerailwayticket.scheduls;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uz.pdp.online.jayxun.onlinerailwayticket.repo.ConfirmSentCodeRepository;

import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Transactional
public class AutoDeleteExpiredCodeOnDatabase {

    private final ConfirmSentCodeRepository confirmSentCodeRepository;

    @Scheduled(fixedDelay = 5000)
    public void run() {
        confirmSentCodeRepository.deleteByExpireBefore(Date.from(Instant.now()));
    }
}
