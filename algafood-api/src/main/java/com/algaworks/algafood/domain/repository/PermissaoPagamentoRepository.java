package com.algaworks.algafood.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.di.modelo.PermissaoPagamento;

@Repository
public interface PermissaoPagamentoRepository extends JpaRepository<PermissaoPagamento, Long>{
	
}
