package edu.otensoft.incident.api.application.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import edu.otensoft.incident.api.application.mapper.IncidentMapper;
import edu.otensoft.incident.api.application.service.IncidentService;
import edu.otensoft.incident.api.domain.dto.IncidentResponseDTO;
import edu.otensoft.incident.api.domain.entity.Incident;
import edu.otensoft.incident.api.infra.exception.NotFoundException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@WebMvcTest(IncidentController.class)
public class IncidentControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IncidentService service;

    @MockBean
    private IncidentMapper mapper;

    @Test
    void shouldListAllIncidents() throws Exception {
        List<Incident> incidents = List.of(new Incident());
        when(service.listAll()).thenReturn(incidents);
        when(mapper.toResponseDTOList(incidents)).thenReturn(List.of(new IncidentResponseDTO()));

        mockMvc.perform(get("/incidents"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldFindIncidentById() throws Exception {
        Incident incident = new Incident();
        incident.setId(1L);
        when(service.getById(1L)).thenReturn(incident);
        when(mapper.toResponseDTO(incident)).thenReturn(new IncidentResponseDTO());

        mockMvc.perform(get("/incidents/1"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldFindIncidentWhenIdDoesNotExist() throws Exception {
        Long idInvalid = 100L;
        when(service.getById(idInvalid)).thenThrow(new NotFoundException("Incident "+idInvalid+ " not found."));

        mockMvc.perform(get("/incidents/{id}", idInvalid))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateIncident() throws Exception {
        // IncidentRequestDTO request = new IncidentRequestDTO();
        Incident incident = new Incident();
        IncidentResponseDTO response = new IncidentResponseDTO();

        when(mapper.toEntity(any())).thenReturn(incident);
        when(service.save(any())).thenReturn(incident);
        when(mapper.toResponseDTO(incident)).thenReturn(response);

        mockMvc.perform(post("/incidents")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Teste Incident\", \"description\":\"Desc\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnBadRequestWhenCreateInvalidIncident() throws Exception {
        mockMvc.perform(post("/incidents")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")) 
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteIncident() throws Exception {
        doNothing().when(service).remove(1L);

        mockMvc.perform(delete("/incidents/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnPagedIncidents() throws Exception {
        Incident incident = new Incident();
        incident.setName("Incidente Teste");
        incident.setDescription("Descrição do incidente");

        IncidentResponseDTO dto = new IncidentResponseDTO();
        dto.setName(incident.getName());
        dto.setDescription(incident.getDescription());

        Page<Incident> pageIncidents = new PageImpl<>(List.of(incident));

        when(service.pageList(any(), any(), any(Pageable.class))).thenReturn(pageIncidents);
        when(mapper.toResponseDTO(incident)).thenReturn(dto);

        mockMvc.perform(get("/incidents/page")
            .contentType(MediaType.APPLICATION_JSON))            
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content.length()").value(1))
            .andExpect(jsonPath("$.content[0].name").value("Incidente Teste"));
    }
}
