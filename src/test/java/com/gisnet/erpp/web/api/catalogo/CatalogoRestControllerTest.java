package com.gisnet.erpp.web.api.catalogo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.ErppApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ErppApplication.class)
@Transactional
public class CatalogoRestControllerTest {
	private MockMvc mockMvc;
	
	@Autowired
    private CatalogoRestController catalogoRestController;
	
	@Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(catalogoRestController).build();
    }

	@Test
	public void listAreasTest() throws Exception {
		mockMvc.perform(get("/api/catalogo/area")).andExpect(status().isOk());
	}
	

}
