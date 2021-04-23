package com.proj.fitnessclass.web.rest;

import com.proj.fitnessclass.FitnessClassApp;
import com.proj.fitnessclass.domain.FitnessClass;
import com.proj.fitnessclass.repository.FitnessClassRepository;
import com.proj.fitnessclass.repository.search.FitnessClassSearchRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.proj.fitnessclass.domain.enumeration.Level;
import com.proj.fitnessclass.domain.enumeration.Type;
/**
 * Integration tests for the {@link FitnessClassResource} REST controller.
 */
@SpringBootTest(classes = FitnessClassApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class FitnessClassResourceIT {

    private static final String DEFAULT_CLASS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final Level DEFAULT_LEVEL = Level.BEGINNER;
    private static final Level UPDATED_LEVEL = Level.INTERMEDIATE;

    private static final String DEFAULT_INSTRUCTOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_INSTRUCTOR_NAME = "BBBBBBBBBB";

    private static final Type DEFAULT_TYPE = Type.ALL;
    private static final Type UPDATED_TYPE = Type.CYCLING;

    @Autowired
    private FitnessClassRepository fitnessClassRepository;

    /**
     * This repository is mocked in the com.proj.fitnessclass.repository.search test package.
     *
     * @see com.proj.fitnessclass.repository.search.FitnessClassSearchRepositoryMockConfiguration
     */
    @Autowired
    private FitnessClassSearchRepository mockFitnessClassSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFitnessClassMockMvc;

    private FitnessClass fitnessClass;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FitnessClass createEntity(EntityManager em) {
        FitnessClass fitnessClass = new FitnessClass()
            .className(DEFAULT_CLASS_NAME)
            .duration(DEFAULT_DURATION)
            .level(DEFAULT_LEVEL)
            .instructorName(DEFAULT_INSTRUCTOR_NAME)
            .type(DEFAULT_TYPE);
        return fitnessClass;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FitnessClass createUpdatedEntity(EntityManager em) {
        FitnessClass fitnessClass = new FitnessClass()
            .className(UPDATED_CLASS_NAME)
            .duration(UPDATED_DURATION)
            .level(UPDATED_LEVEL)
            .instructorName(UPDATED_INSTRUCTOR_NAME)
            .type(UPDATED_TYPE);
        return fitnessClass;
    }

    @BeforeEach
    public void initTest() {
        fitnessClass = createEntity(em);
    }

    @Test
    @Transactional
    public void createFitnessClass() throws Exception {
        int databaseSizeBeforeCreate = fitnessClassRepository.findAll().size();

        // Create the FitnessClass
        restFitnessClassMockMvc.perform(post("/api/fitness-classes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fitnessClass)))
            .andExpect(status().isCreated());

        // Validate the FitnessClass in the database
        List<FitnessClass> fitnessClassList = fitnessClassRepository.findAll();
        assertThat(fitnessClassList).hasSize(databaseSizeBeforeCreate + 1);
        FitnessClass testFitnessClass = fitnessClassList.get(fitnessClassList.size() - 1);
        assertThat(testFitnessClass.getClassName()).isEqualTo(DEFAULT_CLASS_NAME);
        assertThat(testFitnessClass.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testFitnessClass.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testFitnessClass.getInstructorName()).isEqualTo(DEFAULT_INSTRUCTOR_NAME);
        assertThat(testFitnessClass.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the FitnessClass in Elasticsearch
        verify(mockFitnessClassSearchRepository, times(1)).save(testFitnessClass);
    }

    @Test
    @Transactional
    public void createFitnessClassWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fitnessClassRepository.findAll().size();

        // Create the FitnessClass with an existing ID
        fitnessClass.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFitnessClassMockMvc.perform(post("/api/fitness-classes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fitnessClass)))
            .andExpect(status().isBadRequest());

        // Validate the FitnessClass in the database
        List<FitnessClass> fitnessClassList = fitnessClassRepository.findAll();
        assertThat(fitnessClassList).hasSize(databaseSizeBeforeCreate);

        // Validate the FitnessClass in Elasticsearch
        verify(mockFitnessClassSearchRepository, times(0)).save(fitnessClass);
    }


    @Test
    @Transactional
    public void checkClassNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = fitnessClassRepository.findAll().size();
        // set the field null
        fitnessClass.setClassName(null);

        // Create the FitnessClass, which fails.

        restFitnessClassMockMvc.perform(post("/api/fitness-classes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fitnessClass)))
            .andExpect(status().isBadRequest());

        List<FitnessClass> fitnessClassList = fitnessClassRepository.findAll();
        assertThat(fitnessClassList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = fitnessClassRepository.findAll().size();
        // set the field null
        fitnessClass.setDuration(null);

        // Create the FitnessClass, which fails.

        restFitnessClassMockMvc.perform(post("/api/fitness-classes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fitnessClass)))
            .andExpect(status().isBadRequest());

        List<FitnessClass> fitnessClassList = fitnessClassRepository.findAll();
        assertThat(fitnessClassList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = fitnessClassRepository.findAll().size();
        // set the field null
        fitnessClass.setLevel(null);

        // Create the FitnessClass, which fails.

        restFitnessClassMockMvc.perform(post("/api/fitness-classes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fitnessClass)))
            .andExpect(status().isBadRequest());

        List<FitnessClass> fitnessClassList = fitnessClassRepository.findAll();
        assertThat(fitnessClassList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInstructorNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = fitnessClassRepository.findAll().size();
        // set the field null
        fitnessClass.setInstructorName(null);

        // Create the FitnessClass, which fails.

        restFitnessClassMockMvc.perform(post("/api/fitness-classes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fitnessClass)))
            .andExpect(status().isBadRequest());

        List<FitnessClass> fitnessClassList = fitnessClassRepository.findAll();
        assertThat(fitnessClassList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fitnessClassRepository.findAll().size();
        // set the field null
        fitnessClass.setType(null);

        // Create the FitnessClass, which fails.

        restFitnessClassMockMvc.perform(post("/api/fitness-classes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fitnessClass)))
            .andExpect(status().isBadRequest());

        List<FitnessClass> fitnessClassList = fitnessClassRepository.findAll();
        assertThat(fitnessClassList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFitnessClasses() throws Exception {
        // Initialize the database
        fitnessClassRepository.saveAndFlush(fitnessClass);

        // Get all the fitnessClassList
        restFitnessClassMockMvc.perform(get("/api/fitness-classes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fitnessClass.getId().intValue())))
            .andExpect(jsonPath("$.[*].className").value(hasItem(DEFAULT_CLASS_NAME)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].instructorName").value(hasItem(DEFAULT_INSTRUCTOR_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getFitnessClass() throws Exception {
        // Initialize the database
        fitnessClassRepository.saveAndFlush(fitnessClass);

        // Get the fitnessClass
        restFitnessClassMockMvc.perform(get("/api/fitness-classes/{id}", fitnessClass.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fitnessClass.getId().intValue()))
            .andExpect(jsonPath("$.className").value(DEFAULT_CLASS_NAME))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()))
            .andExpect(jsonPath("$.instructorName").value(DEFAULT_INSTRUCTOR_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFitnessClass() throws Exception {
        // Get the fitnessClass
        restFitnessClassMockMvc.perform(get("/api/fitness-classes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFitnessClass() throws Exception {
        // Initialize the database
        fitnessClassRepository.saveAndFlush(fitnessClass);

        int databaseSizeBeforeUpdate = fitnessClassRepository.findAll().size();

        // Update the fitnessClass
        FitnessClass updatedFitnessClass = fitnessClassRepository.findById(fitnessClass.getId()).get();
        // Disconnect from session so that the updates on updatedFitnessClass are not directly saved in db
        em.detach(updatedFitnessClass);
        updatedFitnessClass
            .className(UPDATED_CLASS_NAME)
            .duration(UPDATED_DURATION)
            .level(UPDATED_LEVEL)
            .instructorName(UPDATED_INSTRUCTOR_NAME)
            .type(UPDATED_TYPE);

        restFitnessClassMockMvc.perform(put("/api/fitness-classes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFitnessClass)))
            .andExpect(status().isOk());

        // Validate the FitnessClass in the database
        List<FitnessClass> fitnessClassList = fitnessClassRepository.findAll();
        assertThat(fitnessClassList).hasSize(databaseSizeBeforeUpdate);
        FitnessClass testFitnessClass = fitnessClassList.get(fitnessClassList.size() - 1);
        assertThat(testFitnessClass.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
        assertThat(testFitnessClass.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testFitnessClass.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testFitnessClass.getInstructorName()).isEqualTo(UPDATED_INSTRUCTOR_NAME);
        assertThat(testFitnessClass.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the FitnessClass in Elasticsearch
        verify(mockFitnessClassSearchRepository, times(1)).save(testFitnessClass);
    }

    @Test
    @Transactional
    public void updateNonExistingFitnessClass() throws Exception {
        int databaseSizeBeforeUpdate = fitnessClassRepository.findAll().size();

        // Create the FitnessClass

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFitnessClassMockMvc.perform(put("/api/fitness-classes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fitnessClass)))
            .andExpect(status().isBadRequest());

        // Validate the FitnessClass in the database
        List<FitnessClass> fitnessClassList = fitnessClassRepository.findAll();
        assertThat(fitnessClassList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FitnessClass in Elasticsearch
        verify(mockFitnessClassSearchRepository, times(0)).save(fitnessClass);
    }

    @Test
    @Transactional
    public void deleteFitnessClass() throws Exception {
        // Initialize the database
        fitnessClassRepository.saveAndFlush(fitnessClass);

        int databaseSizeBeforeDelete = fitnessClassRepository.findAll().size();

        // Delete the fitnessClass
        restFitnessClassMockMvc.perform(delete("/api/fitness-classes/{id}", fitnessClass.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FitnessClass> fitnessClassList = fitnessClassRepository.findAll();
        assertThat(fitnessClassList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FitnessClass in Elasticsearch
        verify(mockFitnessClassSearchRepository, times(1)).deleteById(fitnessClass.getId());
    }

    @Test
    @Transactional
    public void searchFitnessClass() throws Exception {
        // Initialize the database
        fitnessClassRepository.saveAndFlush(fitnessClass);
        when(mockFitnessClassSearchRepository.search(queryStringQuery("id:" + fitnessClass.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fitnessClass), PageRequest.of(0, 1), 1));
        // Search the fitnessClass
        restFitnessClassMockMvc.perform(get("/api/_search/fitness-classes?query=id:" + fitnessClass.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fitnessClass.getId().intValue())))
            .andExpect(jsonPath("$.[*].className").value(hasItem(DEFAULT_CLASS_NAME)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].instructorName").value(hasItem(DEFAULT_INSTRUCTOR_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
}
