package serejka.telegram.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import serejka.telegram.bot.models.Admin;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByUsername(String username);

}
