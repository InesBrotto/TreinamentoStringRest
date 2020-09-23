package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.di.modelo.Cidade;
import com.algaworks.algafood.di.modelo.Estado;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.repository.EstadoRepository;

@Service
public class CadastroCidadeService {

	@Autowired
	CidadeRepository cidadeRepository;

	@Autowired
	EstadoRepository estadoRepository;

	public Cidade salvar(Cidade cidade) {
		Long estadoId = cidade.getEstado().getId();
		Estado estadoAtual = estadoRepository.findById(estadoId).orElseThrow(() -> new EntidadeNaoEncontradaException(
				String.format("Não existe cadastro de estado com código %d", estadoId)));

		// Esgta maneira abaixo foi substituida pelo jeito acima
		//if (estadoAtual.isEmpty()) {
		//	throw new EntidadeNaoEncontradaException(
		//			String.format("Não existe cadastro de estado com código %d", estadoId));
		//}

		cidade.setEstado(estadoAtual);
		return cidadeRepository.save(cidade);
	}

}
