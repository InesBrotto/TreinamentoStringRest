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

import com.algaworks.algafood.di.modelo.PermissaoPagamento;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.repository.PermissaoPagamentoRepository;
import com.algaworks.algafood.domain.service.CadastroPermissaoPagamentoService;

@RestController
@RequestMapping("/permissaopagamentos")
public class PermissaoPagamentoController  {
	
	@Autowired
	private PermissaoPagamentoRepository permissaoPagamentoRepository;
	
	@Autowired
	private CadastroPermissaoPagamentoService cadastroPermissaoPagamentoService;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<PermissaoPagamento> listar(){
		return permissaoPagamentoRepository.findAll();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PermissaoPagamento adicionar(@RequestBody PermissaoPagamento permissaoPagamento) {
		return cadastroPermissaoPagamentoService.salvar(permissaoPagamento);
	}
	
	@PutMapping("/{permissaoPagamentoId}")
	public ResponseEntity<PermissaoPagamento> atualizar(@PathVariable Long permissaoPagamentoId, @RequestBody PermissaoPagamento permissaoPagamento) {
		Optional<PermissaoPagamento> permissaoPagamentoAtual = permissaoPagamentoRepository.findById(permissaoPagamentoId);
		if (permissaoPagamentoAtual.isPresent()) {
			BeanUtils.copyProperties(permissaoPagamento, permissaoPagamentoAtual.get(), "id");
			PermissaoPagamento permissaoPagamentoSalvo = cadastroPermissaoPagamentoService.salvar(permissaoPagamentoAtual.get()); 
			return ResponseEntity.ok(permissaoPagamentoSalvo);
		}
		// retorna sem corpo e com status 'nao encontrado' se nao encontrar o id
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{permissaoPagamentoId}")
	public ResponseEntity<PermissaoPagamento> remover(@PathVariable Long permissaoPagamentoId) {
		try {
			cadastroPermissaoPagamentoService.excluir(permissaoPagamentoId);
			// return ResponseEntity.status(HttpStatus.NO_CONTENT);
			// Ao inves de usar o codigo acima usa este que émelhor de visualizar
			return ResponseEntity.noContent().build();
		}catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	
}
