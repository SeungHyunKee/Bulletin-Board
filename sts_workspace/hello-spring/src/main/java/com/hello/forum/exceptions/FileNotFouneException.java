package com.hello.forum.exceptions;

public class FileNotFouneException extends RuntimeException {

	private static final long serialVersionUID = -1565136828108528206L;
	
	public FileNotFouneException() {
		super("파일이 존재하지 않습니다.");
	}

}
