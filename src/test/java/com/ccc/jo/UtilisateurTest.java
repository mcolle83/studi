package com.ccc.jo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import com.ccc.jo.model.Utilisateur;
import com.ccc.jo.controller.UtilisateurController;
import com.ccc.jo.service.UtilisateurServiceImpl;
import com.ccc.jo.repository.UtilisateurRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class UtilisateurTest {

	@Autowired
	private MockMvc mockMvc;
	@Mock
 	private UtilisateurController utilisateurController;
	@InjectMocks
 	private UtilisateurServiceImpl utilisateurService;
	@Mock
 	private UtilisateurRepository utilisateurRepository;
	@Mock
	private Utilisateur utilisateur;

	@BeforeEach
  	public void newUtilisateur() {
    utilisateur = new Utilisateur();
	utilisateur.setId(1L);
	utilisateur.setNom("Marchand");
	utilisateur.setPrenom("Leon");
	utilisateur.setEmail("leonmarchand@gmail.com");
	utilisateur.setMotdepasse(BCrypt.hashpw("QuatreOr2024", BCrypt.gensalt(12)));
  	}

	@AfterEach
  	public void cleanUtilisateur() {
	utilisateur = null;
  	}

	@Test
	void contextLoads() throws Exception {
		assertThat(utilisateurController).isNotNull();
	}
	
	@Test
  	public void testAccueil() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/"))
       .andExpect(MockMvcResultMatchers.status().isOk());
  	}

	@Test
  	public void testMentionsLegales() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/mentionslegales"))
       .andExpect(MockMvcResultMatchers.status().isOk());
  	}

	@Test
  	public void testPolitiqueConfid() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/politiqueconfid"))
       .andExpect(MockMvcResultMatchers.status().isOk());
  	}

	@Test
  	public void testCGV() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/cgv"))
       .andExpect(MockMvcResultMatchers.status().isOk());
  	}

	@Test
  	public void testContact() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/contact"))
       .andExpect(MockMvcResultMatchers.status().isOk());
  	}

	@Test
  	public void testInscriptionForm() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/inscription"))
       .andExpect(MockMvcResultMatchers.status().isOk());
  	}

	@Test
  	public void testConnexionForm() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/connexion"))
       .andExpect(MockMvcResultMatchers.status().isOk());
  	}

	@Test
  	public void testEditForm() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/compteclient"))
       .andExpect(MockMvcResultMatchers.status().isOk());
  	}

	@Test
	public void testGetAllUtilisateurs() throws Exception {
	List<Utilisateur> result = utilisateurService.getAllUtilisateurs();
	assertNotNull(result);
	}

	@Test
 	public void testGetUtilisateurById() throws Exception {
	Mockito.when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(utilisateur));
  	Utilisateur result = utilisateurService.getUtilisateurById(1L);
  	assertNotNull(result);
  	assertEquals(1L, result.getId());
  	assertEquals("Marchand", result.getNom());
	assertEquals("Leon", result.getPrenom());
  	assertEquals("leonmarchand@gmail.com", result.getEmail());
 	}

	@Test
  	public void testGetUtilisateurByEmail() throws Exception {
	Mockito.when(utilisateurRepository.findByEmail("leonmarchand@gmail.com")).thenReturn(utilisateur);
   	Utilisateur result = utilisateurService.getUtilisateurByEmail("leonmarchand@gmail.com");
   	assertNotNull(result);
   	assertEquals("Marchand", result.getNom());
	assertEquals("Leon", result.getPrenom());
  	assertEquals("leonmarchand@gmail.com", result.getEmail());
  	}

	@Test
  	public void testGetUtilisateurByTokenconfirm() throws Exception {
	utilisateur.setTokenconfirm("a1b2c3");
	Mockito.when(utilisateurRepository.findByTokenconfirm("a1b2c3")).thenReturn(utilisateur);
   	Utilisateur result = utilisateurService.getUtilisateurByTokenconfirm("a1b2c3");
   	assertNotNull(result);
   	assertEquals("Marchand", result.getNom());
	assertEquals("Leon", result.getPrenom());
  	assertEquals("leonmarchand@gmail.com", result.getEmail());
  	}

	@Test
  	public void testVerifMotdepasse() throws Exception {
	Mockito.when(utilisateurRepository.findByEmail("leonmarchand@gmail.com")).thenReturn(utilisateur);
   	Boolean result = utilisateurService.verifMotdepasse("QuatreOr2024", utilisateur.getMotdepasse());
   	assertTrue(result);
  	}

	@Test
  	public void testLoginUtilisateur() throws Exception {
	Utilisateur logUtilisateur = new Utilisateur();
	logUtilisateur.setEmail("leonmarchand@gmail.com");
	logUtilisateur.setMotdepasse("QuatreOr2024");
	Mockito.when(utilisateurRepository.findByEmail("leonmarchand@gmail.com")).thenReturn(utilisateur);
	Utilisateur result = utilisateurService.loginUtilisateur(logUtilisateur);
	assertNotNull(result);
	assertEquals("Marchand", result.getNom());
	assertEquals("Leon", result.getPrenom());
  	assertEquals("leonmarchand@gmail.com", result.getEmail());
  	}

	@Test
  	public void testUpdateUtilisateur() throws Exception {
	Mockito.when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(utilisateur));
	Utilisateur result = utilisateurService.updateUtilisateur(1L, "Manaudou", "Florent", "florentmanaudou@gmail.com");
	assertEquals("Manaudou", result.getNom());
	assertEquals("Florent", result.getPrenom());
  	assertEquals("florentmanaudou@gmail.com", result.getEmail());
  	}

	@Test
  	public void testUpdateMotdepasse() throws Exception {
	Mockito.when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(utilisateur));
	Utilisateur result = utilisateurService.updateMotdepasse(1L, "DeuxBronzes2024");
	Boolean result2 = utilisateurService.verifMotdepasse("DeuxBronzes2024", result.getMotdepasse());
	assertTrue(result2);
  	}
	
}