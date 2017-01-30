package ru.vav1lon.verificationCenter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.vav1lon.verificationCenter.AbstractTest;
import ru.vav1lon.verificationCenter.model.PersonDTO;
import ru.vav1lon.verificationCenter.persistent.entity.Person;
import ru.vav1lon.verificationCenter.persistent.repository.PersonRepository;

import java.util.Random;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PersonControllerTest extends AbstractTest {

    @InjectMocks
    private PersonController personController;

    @Mock
    private PersonRepository repository;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void createPersonTest() throws Exception {

        PersonDTO model = PersonDTO.builder()
                .lastName("Denis")
                .firstName("Kuzmin")
                .patronymic("V")
                .email("vavilon.rus@gmail.com")
                .inn("112233445566")
                .build();

        Person entity = Person.builder()
                .lastName("Denis")
                .firstName("Kuzmin")
                .patronymic("V")
                .email("vavilon.rus@gmail.com")
                .inn("112233445566")
                .phone("7778885566")
                .build();

        Person modelReturn = Person.builder()
                .id(new Random().nextLong())
                .lastName("Denis")
                .firstName("Kuzmin")
                .patronymic("V")
                .email("vavilon.rus@gmail.com")
                .inn("112233445566")
                .phone("7778885566")
                .build();

        when(repository.save(entity)).thenReturn(modelReturn);

        String jsonContent = new ObjectMapper().writeValueAsString(model);
        mockMvc.perform(post("/v1/create").content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        verify(repository, times(1)).save(entity);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByIdTest() throws Exception {

        Long id = new Random().nextLong();

        Person entity = Person.builder()
                .id(id)
                .lastName("Denis")
                .firstName("Kuzmin")
                .patronymic("V")
                .email("vavilon.rus@gmail.com")
                .inn("112233445566")
                .phone("7778885566")
                .build();


        when(repository.findById(id)).thenReturn(entity);

        MvcResult mvcResult = mockMvc.perform(get("/v1/findById/")
                .param("id", id.toString())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        verify(repository, times(1)).findById(id);
        verifyNoMoreInteractions(repository);
    }

}