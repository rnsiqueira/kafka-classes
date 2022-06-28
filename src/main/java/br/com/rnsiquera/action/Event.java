package br.com.rnsiquera.action;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Event {

    private final Long id;
    private final String name;
    private final String message;

}
