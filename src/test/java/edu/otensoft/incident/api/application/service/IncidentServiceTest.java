package edu.otensoft.incident.api.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.otensoft.incident.api.application.mapper.IncidentMapper;
import edu.otensoft.incident.api.domain.entity.Incident;
import edu.otensoft.incident.api.infra.exception.NotFoundException;
import edu.otensoft.incident.api.infra.repository.IncidentRepository;

@ExtendWith(MockitoExtension.class)
public class IncidentServiceTest {
    
    @InjectMocks
    private IncidentService service;

    @Mock
    private IncidentRepository repository;
    
    @Mock
    private IncidentMapper mapper;

    @Test
    void shouldReturnAllIncidents() {
        Incident incident = new Incident();
        incident.setName("Incident Test");
        when(repository.findAll()).thenReturn(List.of(incident));

        List<Incident> incidents = service.listAll();
        assertEquals(1, incidents.size());
        assertEquals("Incident Test", incidents.get(0).getName());
    }

    @Test
    void shouldSaveIncident() {
        Incident incident = new Incident();
        incident.setName("Novo Incidente");

        when(repository.save(any(Incident.class))).thenReturn(incident);

        Incident saved = service.save(incident);

        assertNotNull(saved);
        assertEquals("Novo Incidente", saved.getName());
    }

    @Test
    void shouldFindIncidentById() {
        Incident incident = new Incident();
        incident.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(incident));

        Incident found = service.getById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
    }

    @Test
    void shouldThrowNotFoundWhenIdDoesNotExist() {        
        Long idInvalid = 1L;
        when(repository.findById(idInvalid)).thenThrow(new NotFoundException("Incident "+idInvalid+ " not found."));

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.getById(idInvalid));
        assertEquals("Incident 1 not found.", ex.getMessage());
    }

    @Test
    void shouldReturnOpenIncidentsPaged() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Incident> page = new PageImpl<>(List.of(new Incident()));

        when(repository.findByNameContainingIgnoreCaseAndClosedAtIsNull(anyString(), any(Pageable.class)))
            .thenReturn(page);

        Page<Incident> result = service.pageList("teste", true, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void shouldRemoveIncident() {
        Incident incident = new Incident();
        incident.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(incident));

        service.remove(1L);

        verify(repository).delete(incident);
    }
}
