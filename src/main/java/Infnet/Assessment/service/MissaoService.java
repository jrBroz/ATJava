package Infnet.Assessment.service;
import Infnet.Assessment.dto.MissaoDTO.MissaoDetalhadaDTO;
import Infnet.Assessment.dto.MissaoDTO.MissaoRequestDTO;
import Infnet.Assessment.dto.ParticipacaoDTO.ParticipacaoRequestDTO;
import Infnet.Assessment.enums.NivelPerigoMissao;
import Infnet.Assessment.enums.StatusMissao;
import Infnet.Assessment.exceptions.QuebraRegraNegocio;
import Infnet.Assessment.model.*;
import Infnet.Assessment.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MissaoService {

    private final MissaoRepository missaoRepo;
    private final OrganizacaoRepository orgRepo;
    private final AventureiroRepository aventureiroRepo;
    private final ParticipacaoEmMissaoRepository participacaoRepo;

    @Transactional // sem transactional n tem o conceito de atomicidade qnd no contexto de banco de dadois
    public Missao criar(MissaoRequestDTO dto) {
        Organizacao org = orgRepo.findById(dto.organizacaoId())
                .orElseThrow(() -> new QuebraRegraNegocio("Organização não encontrada para esta missão."));

        Missao nova = new Missao();
        nova.setTitulo(dto.titulo());
        nova.setNivelPerigoMissao(dto.nivelPerigo());
        nova.setOrganizacao(org);
        nova.setStatus(StatusMissao.PLANEJADA);
        nova.setCreatedAt(LocalDateTime.now());
        nova.setDataInicio(dto.dataInicio());
        nova.setDataFim(dto.dataTermino());

        return missaoRepo.save(nova);
    }

    public Missao buscarPorId(Long id) {
        return missaoRepo.findById(id)
                .orElseThrow(() -> new QuebraRegraNegocio("Missão não encontrada."));
    }

    public Page<Missao> listarComFiltros(
            StatusMissao status,
            NivelPerigoMissao nivel,
            LocalDateTime inicio,
            LocalDateTime fim,
            Pageable pageable) {
        
        return missaoRepo.findByStatusAndNivelPerigoMissaoAndCreatedAtBetween(
                status, nivel, inicio, fim, pageable);
    }

    @Transactional
    public Missao atualizar(Long id, MissaoRequestDTO dto) {
        Missao m = buscarPorId(id);
        
        if (dto.titulo() != null) m.setTitulo(dto.titulo());
        if (dto.nivelPerigo() != null) m.setNivelPerigoMissao(dto.nivelPerigo());
        
        return missaoRepo.save(m);
    }

    @Transactional
    public void excluir(Long id) {
        Missao m = buscarPorId(id);
        missaoRepo.delete(m);
    }

    @Transactional
    public Missao associarAventureiro(Long missaoId, ParticipacaoRequestDTO dto) {
        Missao missao = buscarPorId(missaoId);
        Aventureiro aventureiro = aventureiroRepo.findById(dto.aventureiroId())
                .orElseThrow(() -> new QuebraRegraNegocio("Aventureiro não encontrado"));

        // Lista de erros p validar, acho q vou botar wem forma de exception
        List<String> erros = new ArrayList<>();

        if (!missao.getOrganizacao().getId().equals(aventureiro.getOrganizacao().getId())) {
            erros.add("Aventureiro pertence a uma organização diferente da missão");
        }

        if (aventureiro.getAtivo() != null && !aventureiro.getAtivo()) {
            erros.add("Um aventureiro inativo não pode participar de missões");
        }

        if (missao.getStatus() == StatusMissao.CONCLUIDA || missao.getStatus() == StatusMissao.CANCELADA) {
            erros.add("Não é possível adicionar participantes a uma missão " + missao.getStatus());
        }

        if (!erros.isEmpty()) { throw new QuebraRegraNegocio(String.join(", ", erros));}

        ParticipacaoEmMissao participacao = new ParticipacaoEmMissao();
        participacao.setMissao(missao);
        participacao.setAventureiro(aventureiro);
        participacao.setPapel(dto.papel());
        participacao.setRecompensaOuro(dto.recompensaOuro());
        participacao.setDataRegistro(LocalDateTime.now());

        participacaoRepo.save(participacao);
        
        return missao; 
    }

    // @Transactional
    // public void removerParticipante(Long missaoId, Long aventureiroId) {
    //     // Busca a participação específica para deletar
    //     ParticipacaoEmMissao p = participacaoRepo.findAll().stream()
    //             .filter(part -> part.getMissao().getId().equals(missaoId) && 
    //                            part.getAventureiro().getId().equals(aventureiroId))
    //             .findFirst()
    //             .orElseThrow(() -> new BusinessException("Participação não encontrada"));
        
    //     participacaoRepo.delete(p);
    
    // tirei esse aq em cima pq em questao de performance tava pior
    // o de cima é mais lento pr causa do findALL() e .stream(), pq ele literalmente pega os dados do banco e traz pro java e dps filtra

        @Transactional
        public void removerParticipante(Long missaoId, Long aventureiroId) {
            // Busca direto no banco pela participação específica
            ParticipacaoEmMissao p = participacaoRepo.findByMissao_IdAndAventureiro_Id(missaoId, aventureiroId)
                    .orElseThrow(() -> new QuebraRegraNegocio("Participação não encontrada"));
            
            participacaoRepo.delete(p);
        }

 public MissaoDetalhadaDTO obterDetalhes(Long id) {
    // 1. Busca a missão no banco
    Missao missao = missaoRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Missão não encontrada"));

    return new MissaoDetalhadaDTO(
            missao.getId(),
            missao.getTitulo(),
            missao.getStatus().toString(), 
            missao.getNivelPerigoMissao().toString(),      
            missao.getCreatedAt()   
    );
}

    public List<Object[]> gerarRelatorioMetricas(LocalDateTime inicio, LocalDateTime fim) {
        return participacaoRepo.buscarMetricasMissoes(inicio, fim);
    }
}