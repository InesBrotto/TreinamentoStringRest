package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.di.modelo.Restaurante;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroRestauranteService cadastroRestaurante;

	@GetMapping
	public List<Restaurante> listar() {
		return restauranteRepository.findAll();
	}

	@GetMapping("/{restauranteId}")
	public ResponseEntity<Restaurante> buscar(@PathVariable Long restauranteId) {
		Optional<Restaurante> restaurante = restauranteRepository.findById(restauranteId);
		if (restaurante.isPresent()) {
			return ResponseEntity.ok(restaurante.get());
		}
		// retorna sem corpo se nao encontrar o id
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	// o ? significa que será retornado no corpo qualquer coisa
	public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante) {
		try {
			restaurante = cadastroRestaurante.salvar(restaurante);
			return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/{restauranteId}")
	public ResponseEntity<?> atualizar(@PathVariable Long restauranteId, @RequestBody Restaurante restaurante) {
		// busca o id da cozinha digitada na URL (restauranteId) e pesquisa no banco de
		// dados
		Optional<Restaurante> restauranteAtual = restauranteRepository.findById(restauranteId);
		// se encontrar o restaurante
		if (restauranteAtual.isPresent()) {
			// O metodo copyProperties copia os atributos de restaurante passado como
			// parametro
			// para o objeto restaurante do banco de dados, a fim de que seja atualizado no
			// banco de dados.
			// O terceiro parametro especifica os campos que voce NÃO quer alterar (neste caso nao quero alterar nem o id e nem a lista de formas de pagamento
			BeanUtils.copyProperties(restaurante, restauranteAtual.get(), "id", "formasPagamento", "produtos");
			try {
				Restaurante restauranteNovo = cadastroRestaurante.salvar(restauranteAtual.get());
				return ResponseEntity.status(HttpStatus.CREATED).body(restauranteNovo);
			} catch (EntidadeNaoEncontradaException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		// se nao encontrar o id do restaurante
		// retorna sem corpo e com status 'nao encontrado'
		return ResponseEntity.notFound().build();
	}

	@PatchMapping("/{restauranteId}")
	// Todos os campos do obejto são trazidos no corpo, so que os campos que nao
	// foram colocados no corpo, são atribuidos os valores null
	// Assim nao se sabe, quais os campos realmente foram passados pela requisição
	// Nao transforma o corpo em um objeto do tipo restaurante, mas para um map,
	// onde String é a chave e o objeto é o value
	// Com o uso do Map somente os campos que estao no corpo sao mapeados para o map
	public ResponseEntity<?> atualizarParcial(@PathVariable Long restauranteId,
			@RequestBody Map<String, Object> campos) {

		Optional<Restaurante> restauranteAtual = restauranteRepository.findById(restauranteId);
		if (restauranteAtual.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		// Na sequencia temos que fazer um merge do que esta no 'map' para o objeto
		// Restaurante
		// pois neste objeto temos somente o que foi enviado na requisição
		merge(campos, restauranteAtual.get());

		return atualizar(restauranteId, restauranteAtual.get());
	}

	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino) {
		ObjectMapper objectMapper = new ObjectMapper();
		// converte json em java
		// Na variavel restauranteOrigem voce tem os dados mapeados para o objeto Restaurante  
		Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);
		
		dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
			Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
			// torna a variavel private para acesso publico, isto é possível atraves do reflection
			field.setAccessible(true);
			
			// pega o valor que esta no campo field do objeto convertido RestauranteOrigem e atribui para o novoValor
			Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
			ReflectionUtils.setField(field, restauranteDestino, novoValor);
		});
	}

}
