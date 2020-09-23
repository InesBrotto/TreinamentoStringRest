package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.di.modelo.FormaPagamento;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.repository.FormaPagamentoRespository;

@Service
public class CadastroFormaPagamentoService {

	@Autowired
	private FormaPagamentoRespository formaPagamentoRespository;

	public FormaPagamento salvar(FormaPagamento formaPagamento) {
		return formaPagamentoRespository.save(formaPagamento);
	}


	public void excluir(Long formaPagamentoId) {
		try {
			formaPagamentoRespository.deleteById(formaPagamentoId);
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe cadastro de forma de pagamento com o código %d", formaPagamentoId));
		}
	}
	
}
