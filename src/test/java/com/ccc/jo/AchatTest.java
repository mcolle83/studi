package com.ccc.jo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

import com.ccc.jo.controller.AchatController;
import com.ccc.jo.model.Achat;
import com.ccc.jo.model.AchatEpreuve;
import com.ccc.jo.model.Utilisateur;
import com.ccc.jo.repository.AchatEpreuveRepository;
import com.ccc.jo.repository.AchatRepository;
import com.ccc.jo.service.AchatEpreuveServiceImpl;
import com.ccc.jo.service.AchatServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
public class AchatTest {

	@Autowired
	private MockMvc mockMvc;
	@Mock
 	private AchatController achatController;
	@InjectMocks
 	private AchatServiceImpl achatService;
	@Mock
 	private AchatRepository achatRepository;
	@InjectMocks
 	private AchatEpreuveServiceImpl achatepreuveService;
	@Mock
 	private AchatEpreuveRepository achatepreuveRepository;
	@Mock
	private Achat achat;
    @Mock
	private AchatEpreuve achatepreuve;
    @Mock
	private Utilisateur utilisateur;

    @BeforeEach
  	public void newAchat() {
    utilisateur = new Utilisateur();
	utilisateur.setId(1L);
	utilisateur.setNom("Marchand");
	utilisateur.setPrenom("Leon");
	utilisateur.setEmail("leonmarchand@gmail.com");
	utilisateur.setMotdepasse("QuatreOr2024");
    achat = new Achat();
	achat.setId(1L);
    achat.setUtilisateur(utilisateur);
    achat.setPrixtotal(BigDecimal.valueOf(10.00));
    achatepreuve = new AchatEpreuve();
    achatepreuve.setId(1L);
    achatepreuve.setAchat(achat);
    achatepreuve.setQuantite(2);
    achatepreuve.setPrixunitaire(BigDecimal.valueOf(5.00));
    achatepreuve.setPrixtotal();
  	}

    @AfterEach
  	public void cleanAchat() {
	utilisateur = null;
    achat = null;
    achatepreuve = null;
  	}

	/**
	* Vérifie que le Controller de Achat existe
	*/
    @Test
	void contextLoads() throws Exception {
		assertThat(achatController).isNotNull();
	}

	/**
	* Accède à la liste des achats effectués par l'utilisateur
	*/
    @Test
  	public void testShowAchatsutil() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/listeachatsutil"))
       .andExpect(MockMvcResultMatchers.status().isOk());
  	}

	/**
	* Accède à la liste de tous les achats effectués
	*/
    @Test
  	public void testShowAchatsadmin() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/listeachatsadmin"))
       .andExpect(MockMvcResultMatchers.status().isOk());
  	}

	/**
	* Récupère tous les achats
	*/
    @Test
	public void testGetAllAchats() throws Exception {
	List<Achat> result = achatService.getAllAchats();
	assertNotNull(result);
	}

	/**
	* Récupère l'achat par son identifiant
	*/
    @Test
 	public void testGetAchatById() throws Exception {
	Mockito.when(achatRepository.findById(1L)).thenReturn(Optional.of(achat));
  	Achat result = achatService.getAchatById(1L);
  	assertNotNull(result);
  	assertEquals(1L, result.getId());
  	assertEquals(utilisateur, result.getUtilisateur());
	assertEquals(BigDecimal.valueOf(10.00), result.getPrixtotal());
 	}

	/**
	* Récupère tous les achats effectués par l'utilisateur
	*/
    @Test
    public void testGetAllAchatsByUtilisateur() throws Exception {
    List<Achat> result = achatService.getAllAchatsByUtilisateur(utilisateur);
    assertNotNull(result);
    }

	/**
	* Récupère toutes les épreuves de tous les achats
	*/
    @Test
	public void testGetAllAchatEpreuves() throws Exception {
	List<AchatEpreuve> result = achatepreuveService.getAllAchatEpreuves();
	assertNotNull(result);
	}

	/**
	* Récupère l'épreuve de l'achat par son identifiant
	*/
    @Test
    public void testGetAchatEpreuveById() throws Exception {
    Mockito.when(achatepreuveRepository.findById(1L)).thenReturn(Optional.of(achatepreuve));
    AchatEpreuve result = achatepreuveService.getAchatEpreuveById(1L);
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals(achat, result.getAchat());
    assertEquals(2, result.getQuantite());
    assertEquals(BigDecimal.valueOf(5.00), result.getPrixunitaire());
    assertEquals(BigDecimal.valueOf(10.00), result.getPrixtotal());
    }

	/**
	* Compte le nombre d'achats pour chaque quantité
	*/
    @Test
	public void testGetAchatEpreuveCountByQuantite() throws Exception {
    Long result1 = achatepreuveService.getAchatEpreuveCountByQuantite(1);
	Long result2 = achatepreuveService.getAchatEpreuveCountByQuantite(2);
    Long result4 = achatepreuveService.getAchatEpreuveCountByQuantite(4);
	assertNotNull(result1);
    assertNotNull(result2);
    assertNotNull(result4);
	}

}
