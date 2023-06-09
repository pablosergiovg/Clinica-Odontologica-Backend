package com.example.pabloVieraPF.controller;
import com.example.pabloVieraPF.dto.TurnoDTO;
import com.example.pabloVieraPF.exception.BadRequestException;
import com.example.pabloVieraPF.exception.ResourceNotFoundException;
import com.example.pabloVieraPF.service.OdontologoService;
import com.example.pabloVieraPF.service.PacienteService;
import com.example.pabloVieraPF.service.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/turno")
public class TurnoController {

    private TurnoService turnoService;
    private PacienteService pacienteService;
    private OdontologoService odontologoService;

    @Autowired
    public TurnoController(TurnoService turnoService, PacienteService pacienteService, OdontologoService odontologoService) {
        this.turnoService = turnoService;
        this.pacienteService = pacienteService;
        this.odontologoService = odontologoService;
    }


    @PostMapping
    public ResponseEntity<TurnoDTO> registrarTurno(@RequestBody TurnoDTO turnoDTO) throws BadRequestException {
        ResponseEntity<TurnoDTO> respuesta;

        if (pacienteService.buscarPaciente(turnoDTO.getPacienteId()).isPresent()&&
                odontologoService.buscarOdontologo(turnoDTO.getOdontologoId()).isPresent()
        ){
            respuesta=ResponseEntity.ok(turnoService.registrarTurno(turnoDTO));
        }
        else{
            throw new BadRequestException("No se puede registrar el turno");
        }
        return respuesta;


    }


    @GetMapping("/{id}")
    public ResponseEntity<TurnoDTO> buscarTurno(@PathVariable Long id){
        Optional<TurnoDTO> turnoBuscado = turnoService.buscarTurno(id);
        if (turnoBuscado.isPresent()){
            return ResponseEntity.ok(turnoBuscado.get());
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping
    public ResponseEntity<List<TurnoDTO>> listarTurnos(){
        return ResponseEntity.ok(turnoService.buscarTodosTurno());
    }


    @PutMapping()
    public ResponseEntity<String> actualizarTurno(@RequestBody TurnoDTO turnoDTO){

        ResponseEntity<TurnoDTO> respuesta;

        if(turnoService.buscarTurno(turnoDTO.getId()).isPresent()){
            if (pacienteService.buscarPaciente(turnoDTO.getPacienteId()).isPresent()&&
                    odontologoService.buscarOdontologo(turnoDTO.getOdontologoId()).isPresent()
            ){
                turnoService.actualizarTurno(turnoDTO);
                return ResponseEntity.ok("Se actualizó el turno con id: " + turnoDTO.getId());
            }
            else{
                return ResponseEntity.badRequest().body("Error al actualizar, verificar si el odontologo y/o el paciente existen en la base de datos.");
            }
        }else{
            return ResponseEntity.badRequest().body("No se puede actualizar el turno, no aparece registrado en la base de datos.");
        }

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarTurno(@PathVariable Long id) throws ResourceNotFoundException{
        turnoService.eliminarTurno(id);
        return ResponseEntity.ok("Se eliminó el turno con id: " + id);
    }

}
