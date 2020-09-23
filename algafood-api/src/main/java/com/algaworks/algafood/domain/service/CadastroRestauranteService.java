package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.di.modelo.Cozinha;
import com.algaworks.algafood.di.modelo.Restaurante;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {

	@Autowired
	RestauranteRepository restauranteRepository;

	@Autowired
	CozinhaRepository cozinhaRepository;

	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		// Retorna a cozinha que esta no Optional mas se nao tiver nada lanca a exceção que estou passando via lambda
		Cozinha cozinha = cozinhaRepository.findById(cozinhaId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format("Não existe cadastro de cozinha com código %d", cozinhaId)));
		
		// Pode fazer assim tambem
//		if (cozinha.isEmpty()) {
			//throw new EntidadeNaoEncontradaException(
			//		String.format("Não existe cadastro de cozinha com código %d", cozinhaId));
//		}
		restaurante.setCozinha(cozinha);
		return restauranteRepository.save(restaurante);
	}
}
