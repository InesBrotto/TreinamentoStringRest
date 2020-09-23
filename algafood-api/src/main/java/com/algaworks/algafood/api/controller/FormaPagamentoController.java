package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.algaworks.algafood.di.modelo.FormaPagamento;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.repository.FormaPagamentoRespository;
import com.algaworks.algafood.domain.service.CadastroFormaPagamentoService;

@RestController
@RequestMapping("/formapagamentos")
public class FormaPagamentoController  {
	
	@Autowired
	private FormaPagamentoRespository formaPagamentoRespository;
	
	@Autowired
	private CadastroFormaPagamentoService cadastroFormaPagamentoService;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<FormaPagamento> listar(){
		return formaPagamentoRespository.findAll();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FormaPagamento adicionar(@RequestBody FormaPagamento formaPagamento) {
		return cadastroFormaPagamentoService.salvar(formaPagamento);
	}
	
	@PutMapping("/{formaPagamentoId}")
	public ResponseEntity<FormaPagamento> atualizar(@PathVariable Long formaPagamentoId, @RequestBody FormaPagamento formaPagamento) {
		Optional<FormaPagamento> formaPagamentoAtual = formaPagamentoRespository.findById(formaPagamentoId);
		if (formaPagamentoAtual.isPresent()) {
			BeanUtils.copyProperties(formaPagamento, formaPagamentoAtual.get(), "id");
			FormaPagamento formaPagamentoSalvo = cadastroFormaPagamentoService.salvar(formaPagamentoAtual.get()); 
			return ResponseEntity.ok(formaPagamentoSalvo);
		}
		// retorna sem corpo e com status 'nao encontrado' se nao encontrar o id
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{formaPagamentoId}")
	public ResponseEntity<FormaPagamento> remover(@PathVariable Long formaPagamentoId) {
		try {
			cadastroFormaPagamentoService.excluir(formaPagamentoId);
			// return ResponseEntity.status(HttpStatus.NO_CONTENT);
			// Ao inves de usar o codigo acima usa este que émelhor de visualizar
			return ResponseEntity.noContent().build();
		}catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	
}
