package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.di.modelo.PermissaoPagamento;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.repository.PermissaoPagamentoRepository;

@Service
public class CadastroPermissaoPagamentoService {

	@Autowired
	private PermissaoPagamentoRepository permissaoPagamentoRepository;

	public PermissaoPagamento salvar(PermissaoPagamento permissaoPagamento) {
		return permissaoPagamentoRepository.save(permissaoPagamento);
	}


	public void excluir(Long permissaoPagamentoId) {
		try {
			permissaoPagamentoRepository.deleteById(permissaoPagamentoId);
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe cadastro de permissão de pagamento com o código %d", permissaoPagamentoId));
		}
	}
	
}
