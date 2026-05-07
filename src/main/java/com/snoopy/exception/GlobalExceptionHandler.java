package com.snoopy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	// ================= COMMON METHOD =================
	private boolean isAjax(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public Object handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest request) {

		if (isAjax(request)) {
			return new ResponseEntity<>(new ApiError(404, "Page Not Found", request.getRequestURI()),
					HttpStatus.NOT_FOUND);
		}

		return "redirect:/error_404.htm";
	}

	// ================= 400 =================
	@ExceptionHandler(IllegalArgumentException.class)
	public Object handleBadRequest(IllegalArgumentException ex, HttpServletRequest request) {
		if (isAjax(request)) {
			return new ResponseEntity<>(new ApiError(400, ex.getMessage(), request.getRequestURI()),
					HttpStatus.BAD_REQUEST);
		}
		return "redirect:/error_400.htm";
	}

	// ================= 401 =================
	@ExceptionHandler(SecurityException.class)
	public Object handle401(SecurityException ex, HttpServletRequest request) {
		if (isAjax(request)) {
			return new ResponseEntity<>(new ApiError(401, ex.getMessage(), request.getRequestURI()),
					HttpStatus.UNAUTHORIZED);
		}
		return "redirect:/error_401.htm";
	}

	// ================= 404 =================
	@ExceptionHandler(ResourceNotFoundException.class)
	public Object handle404(ResourceNotFoundException ex, HttpServletRequest request) {
		if (isAjax(request)) {
			return new ResponseEntity<>(new ApiError(404, ex.getMessage(), request.getRequestURI()),
					HttpStatus.NOT_FOUND);
		}
		return "redirect:/error_404.htm";
	}

	// ================= 500 =================
	// ✅ Only one Exception.class handler now
	@ExceptionHandler(Exception.class)
	public Object handle500(Exception ex, HttpServletRequest request) {
		ex.printStackTrace();
		if (isAjax(request)) {
			return new ResponseEntity<>(new ApiError(500, "Internal Server Error", request.getRequestURI()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return "redirect:/error_500.htm";
	}
}