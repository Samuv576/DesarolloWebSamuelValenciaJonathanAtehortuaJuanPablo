package com.Patinaje.V1.application.port.in;

import java.util.List;

import com.Patinaje.V1.domain.model.Clase;
import com.Patinaje.V1.domain.model.Nivel;

public interface ConsultarClasesUseCase {
    List<Clase> listarTodas();
    List<Clase> listarPorNivel(Nivel nivel);
}
