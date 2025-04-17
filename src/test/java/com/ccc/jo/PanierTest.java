package com.ccc.jo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

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

import com.ccc.jo.controller.PanierController;
import com.ccc.jo.model.Panier;
import com.ccc.jo.model.PanierEpreuve;
import com.ccc.jo.repository.PanierEpreuveRepository;
import com.ccc.jo.repository.PanierRepository;
import com.ccc.jo.service.PanierEpreuveServiceImpl;
import com.ccc.jo.service.PanierServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
public class PanierTest {

	@Autowired
	private MockMvc mockMvc;
	@Mock
 	private PanierController panierController;
	@InjectMocks
 	private PanierServiceImpl panierService;
	@Mock
 	private PanierRepository panierRepository;
	@InjectMocks
 	private PanierEpreuveServiceImpl panierepreuveService;
	@Mock
 	private PanierEpreuveRepository panierepreuveRepository;
	@Mock
	private Panier panier;
    @Mock
	private PanierEpreuve panierepreuve;

    @BeforeEach
  	public void newPanier() {
    panier = new Panier();
	panier.setId(1L);
    panier.setIdsession("1a2b3c");
    panier.setPrixtotal(BigDecimal.valueOf(10.00));
    panierepreuve = new PanierEpreuve();
    panierepreuve.setId(1L);
    panierepreuve.setPanier(panier);
    panierepreuve.setQuantite(2);
    panierepreuve.setPrixunitaire(BigDecimal.valueOf(5.00));
    panierepreuve.setPrixtotal();
  	}

    @AfterEach
  	public void cleanPanier() {
    panierepreuve = null;
	panier = null;
  	}

    @Test
	void contextLoads() throws Exception {
		assertThat(panierController).isNotNull();
	}

	@Test
  	public void testOffres() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/offres"))
       .andExpect(MockMvcResultMatchers.status().isOk());
  	}

	@Test
  	public void testPanier() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/panier"))
       .andExpect(MockMvcResultMatchers.status().isOk());
  	}

	@Test
  	public void testPaiement() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/paiement"))
       .andExpect(MockMvcResultMatchers.status().isOk());
  	}

    @Test
 	public void testGetPanierByIdsession() throws Exception {
	Mockito.when(panierRepository.findByIdsession("1a2b3c")).thenReturn(panier);
  	Panier result = panierService.getPanierByIdsession("1a2b3c");
  	assertNotNull(result);
  	assertEquals(1L, result.getId());
  	assertEquals("1a2b3c", result.getIdsession());
    assertEquals(BigDecimal.valueOf(10.00), result.getPrixtotal());
 	}

}
