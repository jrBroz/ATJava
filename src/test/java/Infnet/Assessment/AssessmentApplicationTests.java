package Infnet.Assessment;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import Infnet.Assessment.exceptions.EntidadeNaoLocalizadaException;
import Infnet.Assessment.service.AventureiroService;

@SpringBootTest
class AssessmentApplicationTests {

    @Autowired // teste n tava passando sem o autowiredg
    private AventureiroService aventureiroService;

    @Test
    public void testeIDInexistente() {
        // ID que sabemos que não existe
        Long idInexistente = 999L;


        assertThrows(
            EntidadeNaoLocalizadaException.class, 
            () -> aventureiroService.getById(idInexistente)
        );
    }

}