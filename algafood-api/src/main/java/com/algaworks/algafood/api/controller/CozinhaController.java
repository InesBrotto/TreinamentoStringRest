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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.di.modelo.Cozinha;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

	@Autowired
	private CozinhaRepository cozinhaRepository;

	@Autowired
	private CadastroCozinhaService cadastroCozinha;

	@GetMapping
	public List<Cozinha> listar() {
		return cozinhaRepository.findAll();
	}

	@GetMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId) {
		// neste objeto optional, nunca vai retornar nulo. Pode ter uma cozinha ou nao
		// entao nao precisa testar por cozinha != null
		Optional<Cozinha> cozinha = cozinhaRepository.findById(cozinhaId);
		if (cozinha.isPresent()) {
			// cozinha é um objeto do tipo Optional. Para pegar o objeto cozinha voce usar o get()
			return ResponseEntity.ok(cozinha.get());
		}
		// retorna sem corpo se nao encontrar o id
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	// @RequestBody é responsável por pegar o corpo da resposta e vincular com o
	// objeto cozinha para fazer o bind
	// @ResponseStatus(HttpStatus.CREATED) muda o retorno do status para CRIADO ao
	// inves de OK que é o padrão
	@ResponseStatus(HttpStatus.CREATED)
	public Cozinha adicionar(@RequestBody Cozinha cozinha) {
		return cadastroCozinha.salvar(cozinha);
	}

	@PutMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> atualizar(@PathVariable Long cozinhaId, @RequestBody Cozinha cozinha) {
		// busca o id da cozinha digitada na URL e pesquisa no banco de dados
		Optional<Cozinha> cozinhaAtual = cozinhaRepository.findById(cozinhaId);
		if (cozinhaAtual.isPresent()) {
			// O metodo copyProperties copia os atributos da cozinha passada como parametro
			// para o objeto cozinha lida do banco de dados. O terceiro parametro especifica
			// os campos que voce NÃO quer alterar
			BeanUtils.copyProperties(cozinha, cozinhaAtual.get(), "id");
			Cozinha cozinhaSalva = cadastroCozinha.salvar(cozinhaAtual.get());
			return ResponseEntity.ok(cozinhaSalva);
		}
		// retorna sem corpo e com status 'nao encontrado' se nao encontrar o id
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> remover(@PathVariable Long cozinhaId) {
		try {
			cadastroCozinha.excluir(cozinhaId);
			// return ResponseEntity.status(HttpStatus.NO_CONTENT);
			// Ao inves de usar o codigo acima usa este que émelhor de visualizar
			return ResponseEntity.noContent().build();
		}catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}
	
}
