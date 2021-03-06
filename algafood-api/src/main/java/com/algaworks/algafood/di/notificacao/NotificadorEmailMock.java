package com.algaworks.algafood.di.notificacao;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.di.modelo.Cliente;

public class NotificadorEmailMock {

	@Profile("dev")
	@TipoDoNotificador(NivelUrgencia.NORMAL)
	@Component
	public class NotificadorEmail implements Notificador {

		@Override
		public void notificar(Cliente cliente, String mensagem) {
			System.out.printf("MOCK:Notificando %s através do e-mail %s: %s\n", cliente.getNome(), cliente.getEmail(),
					mensagem);
		}
	}
}
