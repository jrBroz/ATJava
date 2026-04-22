package Infnet.Assessment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntidadeNaoLocalizadaException extends RuntimeException {
    public EntidadeNaoLocalizadaException(String mensagem) {
        super(mensagem);
    }
}