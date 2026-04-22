package Infnet.Assessment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Criei essa ak pra qubra de logica de neohgboicio

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class QuebraRegraNegocio extends RuntimeException {
    public QuebraRegraNegocio(String mensagem) {
        super(mensagem);
    }
}