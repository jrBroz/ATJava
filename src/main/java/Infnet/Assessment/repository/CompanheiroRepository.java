package Infnet.Assessment.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Infnet.Assessment.model.Companheiro;

@Repository
public interface CompanheiroRepository extends JpaRepository<Companheiro, Long> {
    Optional<Companheiro> findByAventureiroId(Long aventureiroId);
}