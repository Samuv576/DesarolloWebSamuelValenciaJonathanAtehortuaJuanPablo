package com.Patinaje.V1.domain.model;

import java.util.Objects;
import java.util.regex.Pattern;

import com.Patinaje.V1.shared.exception.DomainException;

public class Persona {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10}$");

    private final Long id;
    private final String nombre;
    private final String identificacion;
    private final int edad;
    private final String telefono;
    private final String correo;
    private final String direccion;

    public Persona(Long id, String nombre, String identificacion, int edad, String telefono, String correo, String direccion) {
        this.id = id;
        this.nombre = requireNonBlank(nombre, "El nombre es obligatorio");
        this.identificacion = requireNonBlank(identificacion, "La identificacion es obligatoria");
        if (edad < 0) {
            throw new DomainException("La edad no puede ser negativa");
        }
        this.edad = edad;
        if (!PHONE_PATTERN.matcher(telefono).matches()) {
            throw new DomainException("El telefono debe tener 10 digitos");
        }
        this.telefono = telefono;
        if (!EMAIL_PATTERN.matcher(correo).matches()) {
            throw new DomainException("El correo no es valido");
        }
        this.correo = correo;
        this.direccion = requireNonBlank(direccion, "La direccion es obligatoria");
    }

    private String requireNonBlank(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new DomainException(message);
        }
        return value.trim();
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public int getEdad() {
        return edad;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public String getDireccion() {
        return direccion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Persona)) return false;
        Persona persona = (Persona) o;
        return Objects.equals(identificacion, persona.identificacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificacion);
    }
}
