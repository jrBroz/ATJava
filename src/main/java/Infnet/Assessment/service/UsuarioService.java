package Infnet.Assessment.service;
import Infnet.Assessment.model.Usuario;
import Infnet.Assessment.model.Organizacao;
import Infnet.Assessment.repository.UsuarioRepository;
import Infnet.Assessment.repository.OrganizacaoRepository;
import Infnet.Assessment.exceptions.QuebraRegraNegocio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepo;
    private OrganizacaoRepository orgRepo;

    public List<Usuario> listarTodos() {

        return usuarioRepo.findAll();
    }

    @Transactional
    public Usuario salvarUsuario(Usuario usuario, Long organizacaoId) {     
        Organizacao org = orgRepo.findById(organizacaoId)
            .orElseThrow(() -> new QuebraRegraNegocio("A organização informada não existe no banco."));

        usuario.setOrganizacao(org);

        return usuarioRepo.save(usuario);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepo.findById(id)
            .orElseThrow(() -> new QuebraRegraNegocio("Usuário não encontrado."));
    }
}