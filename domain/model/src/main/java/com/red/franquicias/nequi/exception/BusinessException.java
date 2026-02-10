package com.red.franquicias.nequi.exception;


import com.red.franquicias.nequi.enums.TechnicalMessage;
import lombok.Getter;

@Getter
public class BusinessException extends ProcessorException {

    public BusinessException(TechnicalMessage technicalMessage) {
        super(technicalMessage.getMessage(), technicalMessage);
    }


}
