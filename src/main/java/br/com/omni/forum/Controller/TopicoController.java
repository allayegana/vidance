package br.com.omni.forum.Controller;


import br.com.omni.forum.dto.TopicoDto;
import br.com.omni.forum.repository.CursoRepository;
import br.com.omni.forum.repository.TopicoRepository;
import br.com.omni.forum.dto.detalhasDoTopicoDto;
import br.com.omni.forum.form.TopicoForm;
import br.com.omni.forum.form.atualizaoTopicoForm;
import br.com.omni.forum.modelo.Topico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/topícos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;


    @GetMapping
    public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso, @RequestParam int pagina, @RequestParam int qtd) {

        Pageable paginacao = PageRequest.of(pagina,qtd);

        if (nomeCurso == null) {
            Page<Topico> topicos = topicoRepository.findAll(paginacao);
            return TopicoDto.converte(topicos);
        } else {
            Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso,paginacao);
            return TopicoDto.converte(topicos);
        }

    }

    @PostMapping
    @Transactional
    public ResponseEntity<TopicoDto> Cadastrar(@RequestBody @Valid TopicoForm topicoForm, UriComponentsBuilder uriBuilder) {
        Topico topico = topicoForm.converte(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));

    }

    @GetMapping("/{id}")
    public ResponseEntity<detalhasDoTopicoDto> detalhas(@PathVariable Long id) {

        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()) {
            return ResponseEntity.ok(new detalhasDoTopicoDto(topico.get()));
        }
        return ResponseEntity.notFound().build();

    }


    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid atualizaoTopicoForm atualizaoTopicoForm) {
        Optional<Topico> optional = topicoRepository.findById(id);
        if (optional.isPresent()) {
            Topico topico = atualizaoTopicoForm.actulizar(id, topicoRepository);
            return ResponseEntity.ok(new TopicoDto(topico));
        }
        return ResponseEntity.notFound().build();
    }

//aa
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> remover(@PathVariable Long id) {
        Optional<Topico> optional = topicoRepository.findById(id);
        if (optional.isPresent()) {
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}