package com.gisnet.erpp.web.api.mesaayuda;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class ClienteEmitterDTO {
	private SseEmitter ssEmitter;
	private Long usuarioEscritorioId;
	public SseEmitter getSsEmitter() {
		return ssEmitter;
	}
	public void setSsEmitter(SseEmitter ssEmitter) {
		this.ssEmitter = ssEmitter;
	}
	public Long getUsuarioEscritorioId() {
		return usuarioEscritorioId;
	}
	public void setUsuarioEscritorioId(Long usuarioEscritorioId) {
		this.usuarioEscritorioId = usuarioEscritorioId;
	}
	

}
