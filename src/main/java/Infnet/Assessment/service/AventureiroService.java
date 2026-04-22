package Infnet.Assessment.service;
import Infnet.Assessment.dto.AventureiroDTO.*;
import Infnet.Assessment.dto.RankingDTO.RankingDTO;
import Infnet.Assessment.dto.CompanheiroDTO.CompanheiroCriacaoDTO;
import Infnet.Assessment.exceptions.EntidadeNaoLocalizadaException;
import Infnet.Assessment.model.*;
import Infnet.Assessment.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AventureiroService {

    private final AventureiroRepository aventureiroRepo;
    private final CompanheiroRepository companheiroRepo;
    private final OrganizacaoRepository orgRepo;
    private final UsuarioRepository usuarioRepo;
    private final ParticipacaoEmMissaoRepository participacaoRepo;

    public Page<AventureiroResumidoDTO> getAll(AventureiroResumidoDTO filtro, int page, int size) {
        List<AventureiroResumidoDTO> todosFiltrados = aventureiroRepo.findAll().stream()
                .filter(av -> filtro.classe() == null || av.getClasse().equals(filtro.classe()))
                .filter(av -> filtro.ativo() == null || av.getAtivo().equals(filtro.ativo()))
                .filter(av -> filtro.nivel() == null || av.getNivel().equals(filtro.nivel()))
                .map(this::mapearParaResumo)
                .toList();

        return aplicarPaginacao(todosFiltrados, page, size);
    }

    public Page<AventureiroResumidoDTO> filtrarPorNome(String nomeBusca, int page, int size) {
        List<AventureiroResumidoDTO> resultados = aventureiroRepo.findByNomeContainingIgnoreCase(nomeBusca, null).stream()
                .map(this::mapearParaResumo)
                .toList();

        return aplicarPaginacao(resultados, page, size);
    }

    @Transactional
    public AventureiroResponseDTO create(CriacaoAventureiroDTO dadosNovoAventureiro) {
        Organizacao orgVinculada = orgRepo.findById(dadosNovoAventureiro.organizacaoId())
                .orElseThrow(() -> new EntidadeNaoLocalizadaException("Organização inexistente."));

        Usuario responsavel = usuarioRepo.findById(dadosNovoAventureiro.usuarioId())
                .orElseThrow(() -> new EntidadeNaoLocalizadaException("Usuário inexistente."));

        Aventureiro entidade = new Aventureiro();
        entidade.setNome(dadosNovoAventureiro.nome().trim());
        entidade.setClasse(dadosNovoAventureiro.classe());
        entidade.setNivel(dadosNovoAventureiro.nivel());
        entidade.setAtivo(true);
        entidade.setOrganizacao(orgVinculada);
        entidade.setUsuarioResponsavel(responsavel);

        aventureiroRepo.save(entidade);
        
        return compilarRespostaCompleta(entidade);
    }

    public AventureiroResponseDTO getById(Long aventureiroId) {
        Aventureiro av = buscarAventureiroOuFalhar(aventureiroId);
        return compilarRespostaCompleta(av);
    }

    @Transactional
    public AventureiroResponseDTO atribuirCompanheiro(Long idAventureiro, CompanheiroCriacaoDTO dadosCompanheiro) {
        Aventureiro av = buscarAventureiroOuFalhar(idAventureiro);

        companheiroRepo.findByAventureiroId(idAventureiro).ifPresent(companheiroRepo::delete);
        companheiroRepo.flush();

        Companheiro novoPet = new Companheiro();
        novoPet.setAventureiroId(idAventureiro);
        novoPet.setNome(dadosCompanheiro.nome().trim());
        novoPet.setEspecie(dadosCompanheiro.especie());
        novoPet.setIndice_lealdade(dadosCompanheiro.lealdade());
        companheiroRepo.save(novoPet);

        return compilarRespostaCompleta(av);
    }

    @Transactional
    public AventureiroResponseDTO update(Long id, CriacaoAventureiroDTO dadosAtualizacao) {
        Aventureiro av = buscarAventureiroOuFalhar(id);

        av.setNome(dadosAtualizacao.nome());
        av.setClasse(dadosAtualizacao.classe());
        av.setNivel(dadosAtualizacao.nivel());
        av.setUpdatedAt(OffsetDateTime.now());

        aventureiroRepo.save(av);
        return compilarRespostaCompleta(av);
    }

    @Transactional
    public AventureiroResponseDTO inativar(Long id) {
        return alterarStatus(id, false);
    }

    @Transactional
    public AventureiroResponseDTO recrutar(Long id) {
        return alterarStatus(id, true);
    }

    @Transactional
    public AventureiroResponseDTO removerCompanheiro(Long idAventureiro) {
        Aventureiro av = buscarAventureiroOuFalhar(idAventureiro);
        
        companheiroRepo.findByAventureiroId(idAventureiro).ifPresent(companheiroRepo::delete);
        companheiroRepo.flush();

        return compilarRespostaCompleta(av);
    }

    @Transactional(readOnly = true)
    public Page<RankingDTO> gerarRanking(Pageable pageable) {
        return aventureiroRepo.buscarRankingParaDTO(null, null, pageable);
    }

    private Aventureiro buscarAventureiroOuFalhar(Long id) {
        return aventureiroRepo.findById(id)
                .orElseThrow(() -> new EntidadeNaoLocalizadaException("Aventureiro não encontrado."));
    }

    private AventureiroResponseDTO alterarStatus(Long id, boolean statusAtivo) {
        Aventureiro av = buscarAventureiroOuFalhar(id);
        av.setAtivo(statusAtivo);
        av.setUpdatedAt(OffsetDateTime.now());
        aventureiroRepo.save(av);
        return compilarRespostaCompleta(av);
    }

    private Page<AventureiroResumidoDTO> aplicarPaginacao(List<AventureiroResumidoDTO> listaCompleta, int page, int size) {
        long offset = (long) page * size;
        List<AventureiroResumidoDTO> pagina = listaCompleta.stream()
                .skip(offset)
                .limit(size)
                .toList();
        return new PageImpl<>(pagina, PageRequest.of(page, size), listaCompleta.size());
    }

    private AventureiroResumidoDTO mapearParaResumo(Aventureiro av) {
        return new AventureiroResumidoDTO(
                av.getClasse(), av.getNivel(), av.getAtivo()
        );
    }

    private AventureiroResponseDTO compilarRespostaCompleta(Aventureiro av) {
        Long idAv = av.getId();
        
        // 1. Conta participacao em missoes
        Long totalMissoes = participacaoRepo.countByAventureiro_Id(idAv);
        
        // 2. Busca companheiro
        Companheiro comp = companheiroRepo.findByAventureiroId(idAv).orElse(null);

        // 3. Extrai a missão mais recente de forma direta
        Optional<ParticipacaoEmMissao> participacaoOpcional = participacaoRepo.findFirstByAventureiro_IdOrderByMissao_CreatedAtDesc(idAv);
        Missao ultima = null;
        if (participacaoOpcional.isPresent()) {
            ultima = participacaoOpcional.get().getMissao();
        }

        OffsetDateTime createdAt = av.getUpdatedAt() != null ? av.getUpdatedAt() : av.getCreatedAt();

        return new AventureiroResponseDTO(
                av.getId(), av.getNome(), av.getClasse(), av.getNivel(),
                av.getAtivo(), createdAt, totalMissoes, ultima, comp
        );
    }
}