package com.ucsal.arqsoftware.servicies.exceptions;

public class TokenExpiredException extends RuntimeException {

	public TokenExpiredException(String msg) {
		super(msg);
	}
}
