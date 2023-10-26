package itmo.MainService.controller;

import io.swagger.v3.oas.annotations.Hidden;
import itmo.MainService.exception.FlatNotFoundException;
import itmo.MainService.exception.HouseExistsException;
import itmo.MainService.exception.HouseNotFoundException;
import itmo.MainService.exception.IncorrectParametersException;
import javax.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Hidden
public class GlobalExceptionHandler {

    @ExceptionHandler(FlatNotFoundException.class)
    public ResponseEntity<String> handleFlatNotFoundException(FlatNotFoundException exception){
        return new ResponseEntity<>("An error occurred: Flat with given id does not exists! " + exception.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HouseNotFoundException.class)
    public ResponseEntity<String> handleHouseNotFoundException(HouseNotFoundException e){
        return new ResponseEntity<>("An error occurred: House with given id does not exist! " + e.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException exception){
        return new ResponseEntity<>("An error occurred: parameters are invalid!!! " + exception.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HouseExistsException.class)
    public ResponseEntity<String> handleHouseExistsException(HouseExistsException e){
        return new ResponseEntity<>("An error occurred: House with this name already exists! " + e.getMessage(),
                HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(IncorrectParametersException.class)
    public ResponseEntity<String> handelIncorrectParametersException(IncorrectParametersException e){
        return new ResponseEntity<>("An error occurred: Parameters are incorrect! " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
