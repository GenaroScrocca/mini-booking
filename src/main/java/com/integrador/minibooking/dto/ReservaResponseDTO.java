package com.integrador.minibooking.dto;

import com.integrador.minibooking.model.Producto;
import com.integrador.minibooking.model.Reserva;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ReservaResponseDTO {

    private Integer id;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Producto producto;
    private UsuarioResponseDTO usuario;

    public static ReservaResponseDTO fromEntity(Reserva reserva) {
        return new ReservaResponseDTO(
                reserva.getId(),
                reserva.getFechaInicio(),
                reserva.getFechaFin(),
                reserva.getProducto(),
                UsuarioResponseDTO.fromEntity(reserva.getUsuario())
        );
    }
}
