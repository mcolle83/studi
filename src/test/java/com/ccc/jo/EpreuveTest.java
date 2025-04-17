package com.ccc.jo;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.ccc.jo.controller.EpreuveController;
import com.ccc.jo.model.Epreuve;
import com.ccc.jo.repository.EpreuveRepository;
import com.ccc.jo.service.EpreuveServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
public class EpreuveTest {

	@Autowired
	private MockMvc mockMvc;
	@Mock
 	private EpreuveController epreuveController;
	@InjectMocks
 	private EpreuveServiceImpl epreuveService;
	@Mock
 	private EpreuveRepository epreuveRepository;
	@Mock
	private Epreuve epreuve;

	@BeforeEach
  	public void newEpreuve() {
    epreuve = new Epreuve("Finale masculin", "Football", "Parc des Princes", "Paris", LocalDateTime.parse("2024-08-09 18:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), Long.valueOf(47929), BigDecimal.valueOf(5.00), "", "");
	epreuve.setId(1L);
  	}

	@AfterEach
  	public void cleanEpreuve() {
	epreuve = null;
  	}

	@Test
	void contextLoads() throws Exception {
		assertThat(epreuveController).isNotNull();
	}

	@Test
  	public void testOffres() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/offres"))
       .andExpect(MockMvcResultMatchers.status().isOk());
  	}

	@Test
  	public void testCreateOffreForm() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/creationoffre"))
       .andExpect(MockMvcResultMatchers.status().isOk());
  	}

	@Test
  	public void testOffresForm() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/gestionoffres"))
       .andExpect(MockMvcResultMatchers.status().isOk());
  	}

    @Test
	public void testGetAllEpreuves() throws Exception {
	List<Epreuve> result = epreuveService.getAllEpreuves();
	assertNotNull(result);
	}
	
    @Test
 	public void testGetEpreuveById() throws Exception {
	Mockito.when(epreuveRepository.findById(1L)).thenReturn(Optional.of(epreuve));
  	Epreuve result = epreuveService.getEpreuveById(1L);
  	assertNotNull(result);
  	assertEquals(1L, result.getId());
  	assertEquals("Finale masculin", result.getNom());
	assertEquals("Football", result.getDiscipline());
  	assertEquals("Parc des Princes", result.getLieu());
    assertEquals("Paris", result.getVille());
    assertEquals(LocalDateTime.parse("2024-08-09 18:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), result.getDate());
    assertEquals(Long.valueOf(47929), result.getCapacite());
    assertEquals(BigDecimal.valueOf(5.00), result.getPrix());
 	}

}