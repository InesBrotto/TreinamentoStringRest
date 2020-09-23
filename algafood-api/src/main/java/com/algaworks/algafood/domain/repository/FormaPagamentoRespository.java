package com.algaworks.algafood.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.di.modelo.FormaPagamento;

@Repository
public interface FormaPagamentoRespository extends JpaRepository<FormaPagamento, Long>{
	
	
}
