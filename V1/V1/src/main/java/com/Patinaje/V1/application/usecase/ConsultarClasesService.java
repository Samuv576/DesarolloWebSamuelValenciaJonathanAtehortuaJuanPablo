package com.Patinaje.V1.application.usecase;

import java.util.List;

import com.Patinaje.V1.application.port.in.ConsultarClasesUseCase;
import com.Patinaje.V1.application.port.out.ClaseRepository;
import com.Patinaje.V1.domain.model.Clase;
import com.Patinaje.V1.domain.model.Nivel;

public class ConsultarClasesService implements ConsultarClasesUseCase {

    private final ClaseRepository claseRepository;

    public ConsultarClasesService(ClaseRepository claseRepository) {
        this.claseRepository = claseRepository;
    }

    @Override
    public List<Clase> listarTodas() {
        return claseRepository.findAll();
    }

    @Override
    public List<Clase> listarPorNivel(Nivel nivel) {
        return claseRepository.findByNivel(nivel);
    }
}
