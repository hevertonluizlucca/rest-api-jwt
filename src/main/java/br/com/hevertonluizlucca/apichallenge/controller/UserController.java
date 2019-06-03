package br.com.hevertonluizlucca.apichallenge.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.hevertonluizlucca.apichallenge.model.Token;
import br.com.hevertonluizlucca.apichallenge.model.UsuarioAutenticado;
import br.com.hevertonluizlucca.apichallenge.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
@Api(value = "API REST Challenge")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@ApiOperation(value = "Renovar token.")
	@PatchMapping()
	public ResponseEntity<String> renovar(@RequestHeader(value = "token") String token) {
		ResponseEntity<String> response = null;
		try {
			Token refreshedToken = userService.renovarToken(token);
			if(refreshedToken == null || StringUtils.isEmpty(refreshedToken.getToken())) {
				response = new ResponseEntity<String>("false", HttpStatus.OK);
			}else {
				response = new ResponseEntity<String>("true, new token is: "+refreshedToken.getToken(), HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error("Ocorreu um erro durante a autenticação.");
			response = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}

	@ApiOperation(value = "Autenticar usuário.")
	@PostMapping()
	public ResponseEntity<UsuarioAutenticado> autenticar(@RequestParam(value = "login") String login,
			@RequestParam(value = "senha") String senha) {
		ResponseEntity<UsuarioAutenticado> response = null;
		UsuarioAutenticado userAutenticate = null;
		

		try {
			userAutenticate = userService.autenticar(login, senha);

			if (userAutenticate.getAutenticado()) {
				response = new ResponseEntity<UsuarioAutenticado>(userAutenticate, HttpStatus.OK);
			} else {
				response = new ResponseEntity<UsuarioAutenticado>(userAutenticate, HttpStatus.UNAUTHORIZED);
			}

		}catch (Exception e) {
			log.error("Ocorreu um erro durante a autenticação.");
			response = new ResponseEntity<UsuarioAutenticado>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;

	}

}
