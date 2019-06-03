package br.com.hevertonluizlucca.apichallenge.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.hevertonluizlucca.apichallenge.model.Pais;
import br.com.hevertonluizlucca.apichallenge.repository.PaisRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/pais")
@Api(value="API REST Challenge")
public class PaisController {
	
	private static final Logger log = LoggerFactory.getLogger(PaisController.class);
	
	@Autowired
	PaisRepository paisRepository;
	
	
	@ApiOperation(value="Lista os países cadastrados", authorizations = { @Authorization(value="apiKey") })
	@GetMapping()
	public ResponseEntity<List<Pais>> getAllPaises(){
		
		ResponseEntity<List<Pais>> response = null;
		try {
			List<Pais> values = paisRepository.findAll();
			
			if(values != null && !values.isEmpty()){
				response = new ResponseEntity<List<Pais>>(values, HttpStatus.OK);
			}else {
				response = new ResponseEntity<List<Pais>>(values, HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			log.error("Ocorreu um erro durante a autenticação.");
			response = new ResponseEntity<List<Pais>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}

	@ApiOperation(value = "Retorna os países cujo nome contém o texto informado (case insensitive)", authorizations = { @Authorization(value="apiKey") })
	@GetMapping("/{filter}")
	public ResponseEntity<List<Pais>> getAllPaisesByFilter(@RequestParam(value="filter") String filter){
		ResponseEntity<List<Pais>> response = null;
		try {
			List<Pais> values = paisRepository.findByFilter(filter);
			
			if(values != null && !values.isEmpty()){
				response = new ResponseEntity<List<Pais>>(values, HttpStatus.OK);
			}else {
				response = new ResponseEntity<List<Pais>>(values, HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			log.error("Ocorreu um erro durante a autenticação.");
			response = new ResponseEntity<List<Pais>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}
	
	@ApiOperation(value="Remove o pais de id informado", authorizations = { @Authorization(value="apiKey") })
	@DeleteMapping()
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Boolean> remove(@RequestParam @Valid Long id) {
		
		ResponseEntity<Boolean> response = null;
		try {
			
			if(paisRepository.existsById(id)) {
				try {
					paisRepository.deleteById(id);
					response = new ResponseEntity<Boolean>(true, HttpStatus.OK);
				} catch (Exception e) {
					response = new ResponseEntity<Boolean>(false, HttpStatus.OK);
				}
			}else {
				response = new ResponseEntity<Boolean>(false, HttpStatus.OK);
			}
			
		} catch (Exception e) {
			log.error("Ocorreu um erro ao tentar excluir o país.");
			response = new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}
	
	@ApiOperation(value="Inclui/altera um país cadastrado.", authorizations = { @Authorization(value="apiKey") })
	@PostMapping()
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Pais> save(@RequestBody @Valid Pais pais) {
		
		ResponseEntity<Pais> response = null;

		try {
			Pais paisReturned = paisRepository.save(pais);

			if (paisReturned != null) {
				response = new ResponseEntity<Pais>(paisReturned, HttpStatus.OK);
			} else {
				response = new ResponseEntity<Pais>(paisReturned, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			log.error("Ocorreu um erro durante a autenticação.");
			response = new ResponseEntity<Pais>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}
	
}
