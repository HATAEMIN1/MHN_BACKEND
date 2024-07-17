package com.project.mhnbackend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CMRespDTO<T> {

	private int code; //1(성공), -1(실패)
	private String message;
	private T data;
}
