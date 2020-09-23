package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.di.modelo.Cidade;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private CadastroCidadeService cadastroCidadeService;

	@GetMapping
	public List<Cidade> listar() {
		return cidadeRepository.findAll();
	}

	@GetMapping("/{cidadeId}")
	public ResponseEntity<Cidade> buscar(@PathVariable Long cidadeId) {
		Optional<Cidade> cidade = cidadeRepository.findById(cidadeId);
		if (cidade.isPresent()) {
			return ResponseEntity.ok(cidade.get());
		}
		// retorna sem corpo se nao encontrar o id
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	// o ? significa que será retornado no corpo qualquer coisa
	public ResponseEntity<?> adicionar(@RequestBody Cidade cidade) {
		try {
			cidade = cadastroCidadeService.salvar(cidade);
			return ResponseEntity.status(HttpStatus.CREATED).body(cidade);
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/{cidadeId}")
	public ResponseEntity<?> atualizar(@PathVariable Long cidadeId, @RequestBody Cidade cidade) {
		// busca o id da cozinha digitada na URL (cidadaId) e pesquisa no banco de dados
		Optional<Cidade> cidadeAtual = cidadeRepository.findById(cidadeId);
		// se encontrar a cidade
		if (cidadeAtual.isPresent()) {
			// O metodo copyProperties copia os atributos de cidade passado como parametro
			// para o objeto cidade do banco de dados, a fim de que seja atualizado no banco
			// de dados.
			// O terceiro parametro especifica os campos que voce NÃO quer alterar
			BeanUtils.copyProperties(cidade, cidadeAtual.get(), "id");
			try {
				Cidade cidadeSalva = cadastroCidadeService.salvar(cidadeAtual.get());
				return ResponseEntity.status(HttpStatus.CREATED).body(cidadeSalva);
			} catch (EntidadeNaoEncontradaException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		// se nao encontrar o id do cidade
		// retorna sem corpo e com status 'nao encontrado'
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{cidadeId}")
	public ResponseEntity<Cidade> remover(@PathVariable Long cidadeId) {
			Optional<Cidade> cidade = cidadeRepository.findById(cidadeId);
			if (cidade.isPresent()) {
				cidadeRepository.delete(cidade.get());
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.notFound().build();
	}

	
}
