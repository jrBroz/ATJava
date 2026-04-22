package Infnet.Assessment.service;
import Infnet.Assessment.dto.ParticipacaoDTO.ParticipacaoRequestDTO;
import Infnet.Assessment.exceptions.EntidadeNaoLocalizadaException;
import Infnet.Assessment.model.Aventureiro;
import Infnet.Assessment.model.Missao;
import Infnet.Assessment.model.ParticipacaoEmMissao;
import Infnet.Assessment.repository.AventureiroRepository;
import Infnet.Assessment.repository.MissaoRepository;
import Infnet.Assessment.repository.ParticipacaoEmMissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParticipacaoEmMissaoService {

    private final ParticipacaoEmMissaoRepository participacaoRepo;
    private final AventureiroRepository aventureiroRepo;
    private final MissaoRepository missaoRepo;

    @Transactional
    public ParticipacaoEmMissao registrar(ParticipacaoRequestDTO dto) {
   
        Aventureiro av = aventureiroRepo.findById(dto.aventureiroId())
                .orElseThrow(() -> new EntidadeNaoLocalizadaException("Aventureiro não encontrado")); //achar id do aventureiro na missao especificva

        Missao mi = missaoRepo.findById(dto.missaoId()) // pra achar o id da missao
                .orElseThrow(() -> new EntidadeNaoLocalizadaException("Missão não encontrada"));

        ParticipacaoEmMissao participacao = new ParticipacaoEmMissao();
        participacao.setAventureiro(av);
        participacao.setMissao(mi);

        participacao.setPapel(dto.papel());
        participacao.setRecompensaOuro(dto.recompensaOuro());
        participacao.setMvp(dto.mvp() != null ? dto.mvp() : false); // se diferente de null, mvp() vai usar, se nao false

        return participacaoRepo.save(participacao);
    }

    @Transactional
    public void deletar(Long id) {
        if (!participacaoRepo.existsById(id)) {
            throw new EntidadeNaoLocalizadaException("Participação não encontrada");
        }
        participacaoRepo.deleteById(id);
    }
}