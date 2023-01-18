package com.atlas.ir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.atlas.ir.IntegrationTest;
import com.atlas.ir.domain.Insurancecase;
import com.atlas.ir.repository.InsurancecaseRepository;
import com.atlas.ir.service.criteria.InsurancecaseCriteria;
import com.atlas.ir.service.dto.InsurancecaseDTO;
import com.atlas.ir.service.mapper.InsurancecaseMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link InsurancecaseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InsurancecaseResourceIT {

    private static final Long DEFAULT_CASENUMBER = 1L;
    private static final Long UPDATED_CASENUMBER = 2L;
    private static final Long SMALLER_CASENUMBER = 1L - 1L;

    private static final Instant DEFAULT_OCCURDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OCCURDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ISSUETRACKING = "AAAAAAAAAA";
    private static final String UPDATED_ISSUETRACKING = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/insurancecases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InsurancecaseRepository insurancecaseRepository;

    @Autowired
    private InsurancecaseMapper insurancecaseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsurancecaseMockMvc;

    private Insurancecase insurancecase;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Insurancecase createEntity(EntityManager em) {
        Insurancecase insurancecase = new Insurancecase()
            .casenumber(DEFAULT_CASENUMBER)
            .occurdate(DEFAULT_OCCURDATE)
            .issuetracking(DEFAULT_ISSUETRACKING);
        return insurancecase;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Insurancecase createUpdatedEntity(EntityManager em) {
        Insurancecase insurancecase = new Insurancecase()
            .casenumber(UPDATED_CASENUMBER)
            .occurdate(UPDATED_OCCURDATE)
            .issuetracking(UPDATED_ISSUETRACKING);
        return insurancecase;
    }

    @BeforeEach
    public void initTest() {
        insurancecase = createEntity(em);
    }

    @Test
    @Transactional
    void createInsurancecase() throws Exception {
        int databaseSizeBeforeCreate = insurancecaseRepository.findAll().size();
        // Create the Insurancecase
        InsurancecaseDTO insurancecaseDTO = insurancecaseMapper.toDto(insurancecase);
        restInsurancecaseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(insurancecaseDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Insurancecase in the database
        List<Insurancecase> insurancecaseList = insurancecaseRepository.findAll();
        assertThat(insurancecaseList).hasSize(databaseSizeBeforeCreate + 1);
        Insurancecase testInsurancecase = insurancecaseList.get(insurancecaseList.size() - 1);
        assertThat(testInsurancecase.getCasenumber()).isEqualTo(DEFAULT_CASENUMBER);
        assertThat(testInsurancecase.getOccurdate()).isEqualTo(DEFAULT_OCCURDATE);
        assertThat(testInsurancecase.getIssuetracking()).isEqualTo(DEFAULT_ISSUETRACKING);
    }

    @Test
    @Transactional
    void createInsurancecaseWithExistingId() throws Exception {
        // Create the Insurancecase with an existing ID
        insurancecase.setId(1L);
        InsurancecaseDTO insurancecaseDTO = insurancecaseMapper.toDto(insurancecase);

        int databaseSizeBeforeCreate = insurancecaseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsurancecaseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(insurancecaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Insurancecase in the database
        List<Insurancecase> insurancecaseList = insurancecaseRepository.findAll();
        assertThat(insurancecaseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInsurancecases() throws Exception {
        // Initialize the database
        insurancecaseRepository.saveAndFlush(insurancecase);

        // Get all the insurancecaseList
        restInsurancecaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insurancecase.getId().intValue())))
            .andExpect(jsonPath("$.[*].casenumber").value(hasItem(DEFAULT_CASENUMBER.intValue())))
            .andExpect(jsonPath("$.[*].occurdate").value(hasItem(DEFAULT_OCCURDATE.toString())))
            .andExpect(jsonPath("$.[*].issuetracking").value(hasItem(DEFAULT_ISSUETRACKING)));
    }

    @Test
    @Transactional
    void getInsurancecase() throws Exception {
        // Initialize the database
        insurancecaseRepository.saveAndFlush(insurancecase);

        // Get the insurancecase
        restInsurancecaseMockMvc
            .perform(get(ENTITY_API_URL_ID, insurancecase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insurancecase.getId().intValue()))
            .andExpect(jsonPath("$.casenumber").value(DEFAULT_CASENUMBER.intValue()))
            .andExpect(jsonPath("$.occurdate").value(DEFAULT_OCCURDATE.toString()))
            .andExpect(jsonPath("$.issuetracking").value(DEFAULT_ISSUETRACKING));
    }

    @Test
    @Transactional
    void getInsurancecasesByIdFiltering() throws Exception {
        // Initialize the database
        insurancecaseRepository.saveAndFlush(insurancecase);

        Long id = insurancecase.getId();

        defaultInsurancecaseShouldBeFound("id.equals=" + id);
        defaultInsurancecaseShouldNotBeFound("id.notEquals=" + id);

        defaultInsurancecaseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInsurancecaseShouldNotBeFound("id.greaterThan=" + id);

        defaultInsurancecaseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInsurancecaseShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInsurancecasesByCasenumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insurancecaseRepository.saveAndFlush(insurancecase);

        // Get all the insurancecaseList where casenumber equals to DEFAULT_CASENUMBER
        defaultInsurancecaseShouldBeFound("casenumber.equals=" + DEFAULT_CASENUMBER);

        // Get all the insurancecaseList where casenumber equals to UPDATED_CASENUMBER
        defaultInsurancecaseShouldNotBeFound("casenumber.equals=" + UPDATED_CASENUMBER);
    }

    @Test
    @Transactional
    void getAllInsurancecasesByCasenumberIsInShouldWork() throws Exception {
        // Initialize the database
        insurancecaseRepository.saveAndFlush(insurancecase);

        // Get all the insurancecaseList where casenumber in DEFAULT_CASENUMBER or UPDATED_CASENUMBER
        defaultInsurancecaseShouldBeFound("casenumber.in=" + DEFAULT_CASENUMBER + "," + UPDATED_CASENUMBER);

        // Get all the insurancecaseList where casenumber equals to UPDATED_CASENUMBER
        defaultInsurancecaseShouldNotBeFound("casenumber.in=" + UPDATED_CASENUMBER);
    }

    @Test
    @Transactional
    void getAllInsurancecasesByCasenumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insurancecaseRepository.saveAndFlush(insurancecase);

        // Get all the insurancecaseList where casenumber is not null
        defaultInsurancecaseShouldBeFound("casenumber.specified=true");

        // Get all the insurancecaseList where casenumber is null
        defaultInsurancecaseShouldNotBeFound("casenumber.specified=false");
    }

    @Test
    @Transactional
    void getAllInsurancecasesByCasenumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insurancecaseRepository.saveAndFlush(insurancecase);

        // Get all the insurancecaseList where casenumber is greater than or equal to DEFAULT_CASENUMBER
        defaultInsurancecaseShouldBeFound("casenumber.greaterThanOrEqual=" + DEFAULT_CASENUMBER);

        // Get all the insurancecaseList where casenumber is greater than or equal to UPDATED_CASENUMBER
        defaultInsurancecaseShouldNotBeFound("casenumber.greaterThanOrEqual=" + UPDATED_CASENUMBER);
    }

    @Test
    @Transactional
    void getAllInsurancecasesByCasenumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insurancecaseRepository.saveAndFlush(insurancecase);

        // Get all the insurancecaseList where casenumber is less than or equal to DEFAULT_CASENUMBER
        defaultInsurancecaseShouldBeFound("casenumber.lessThanOrEqual=" + DEFAULT_CASENUMBER);

        // Get all the insurancecaseList where casenumber is less than or equal to SMALLER_CASENUMBER
        defaultInsurancecaseShouldNotBeFound("casenumber.lessThanOrEqual=" + SMALLER_CASENUMBER);
    }

    @Test
    @Transactional
    void getAllInsurancecasesByCasenumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insurancecaseRepository.saveAndFlush(insurancecase);

        // Get all the insurancecaseList where casenumber is less than DEFAULT_CASENUMBER
        defaultInsurancecaseShouldNotBeFound("casenumber.lessThan=" + DEFAULT_CASENUMBER);

        // Get all the insurancecaseList where casenumber is less than UPDATED_CASENUMBER
        defaultInsurancecaseShouldBeFound("casenumber.lessThan=" + UPDATED_CASENUMBER);
    }

    @Test
    @Transactional
    void getAllInsurancecasesByCasenumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insurancecaseRepository.saveAndFlush(insurancecase);

        // Get all the insurancecaseList where casenumber is greater than DEFAULT_CASENUMBER
        defaultInsurancecaseShouldNotBeFound("casenumber.greaterThan=" + DEFAULT_CASENUMBER);

        // Get all the insurancecaseList where casenumber is greater than SMALLER_CASENUMBER
        defaultInsurancecaseShouldBeFound("casenumber.greaterThan=" + SMALLER_CASENUMBER);
    }

    @Test
    @Transactional
    void getAllInsurancecasesByOccurdateIsEqualToSomething() throws Exception {
        // Initialize the database
        insurancecaseRepository.saveAndFlush(insurancecase);

        // Get all the insurancecaseList where occurdate equals to DEFAULT_OCCURDATE
        defaultInsurancecaseShouldBeFound("occurdate.equals=" + DEFAULT_OCCURDATE);

        // Get all the insurancecaseList where occurdate equals to UPDATED_OCCURDATE
        defaultInsurancecaseShouldNotBeFound("occurdate.equals=" + UPDATED_OCCURDATE);
    }

    @Test
    @Transactional
    void getAllInsurancecasesByOccurdateIsInShouldWork() throws Exception {
        // Initialize the database
        insurancecaseRepository.saveAndFlush(insurancecase);

        // Get all the insurancecaseList where occurdate in DEFAULT_OCCURDATE or UPDATED_OCCURDATE
        defaultInsurancecaseShouldBeFound("occurdate.in=" + DEFAULT_OCCURDATE + "," + UPDATED_OCCURDATE);

        // Get all the insurancecaseList where occurdate equals to UPDATED_OCCURDATE
        defaultInsurancecaseShouldNotBeFound("occurdate.in=" + UPDATED_OCCURDATE);
    }

    @Test
    @Transactional
    void getAllInsurancecasesByOccurdateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insurancecaseRepository.saveAndFlush(insurancecase);

        // Get all the insurancecaseList where occurdate is not null
        defaultInsurancecaseShouldBeFound("occurdate.specified=true");

        // Get all the insurancecaseList where occurdate is null
        defaultInsurancecaseShouldNotBeFound("occurdate.specified=false");
    }

    @Test
    @Transactional
    void getAllInsurancecasesByIssuetrackingIsEqualToSomething() throws Exception {
        // Initialize the database
        insurancecaseRepository.saveAndFlush(insurancecase);

        // Get all the insurancecaseList where issuetracking equals to DEFAULT_ISSUETRACKING
        defaultInsurancecaseShouldBeFound("issuetracking.equals=" + DEFAULT_ISSUETRACKING);

        // Get all the insurancecaseList where issuetracking equals to UPDATED_ISSUETRACKING
        defaultInsurancecaseShouldNotBeFound("issuetracking.equals=" + UPDATED_ISSUETRACKING);
    }

    @Test
    @Transactional
    void getAllInsurancecasesByIssuetrackingIsInShouldWork() throws Exception {
        // Initialize the database
        insurancecaseRepository.saveAndFlush(insurancecase);

        // Get all the insurancecaseList where issuetracking in DEFAULT_ISSUETRACKING or UPDATED_ISSUETRACKING
        defaultInsurancecaseShouldBeFound("issuetracking.in=" + DEFAULT_ISSUETRACKING + "," + UPDATED_ISSUETRACKING);

        // Get all the insurancecaseList where issuetracking equals to UPDATED_ISSUETRACKING
        defaultInsurancecaseShouldNotBeFound("issuetracking.in=" + UPDATED_ISSUETRACKING);
    }

    @Test
    @Transactional
    void getAllInsurancecasesByIssuetrackingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insurancecaseRepository.saveAndFlush(insurancecase);

        // Get all the insurancecaseList where issuetracking is not null
        defaultInsurancecaseShouldBeFound("issuetracking.specified=true");

        // Get all the insurancecaseList where issuetracking is null
        defaultInsurancecaseShouldNotBeFound("issuetracking.specified=false");
    }

    @Test
    @Transactional
    void getAllInsurancecasesByIssuetrackingContainsSomething() throws Exception {
        // Initialize the database
        insurancecaseRepository.saveAndFlush(insurancecase);

        // Get all the insurancecaseList where issuetracking contains DEFAULT_ISSUETRACKING
        defaultInsurancecaseShouldBeFound("issuetracking.contains=" + DEFAULT_ISSUETRACKING);

        // Get all the insurancecaseList where issuetracking contains UPDATED_ISSUETRACKING
        defaultInsurancecaseShouldNotBeFound("issuetracking.contains=" + UPDATED_ISSUETRACKING);
    }

    @Test
    @Transactional
    void getAllInsurancecasesByIssuetrackingNotContainsSomething() throws Exception {
        // Initialize the database
        insurancecaseRepository.saveAndFlush(insurancecase);

        // Get all the insurancecaseList where issuetracking does not contain DEFAULT_ISSUETRACKING
        defaultInsurancecaseShouldNotBeFound("issuetracking.doesNotContain=" + DEFAULT_ISSUETRACKING);

        // Get all the insurancecaseList where issuetracking does not contain UPDATED_ISSUETRACKING
        defaultInsurancecaseShouldBeFound("issuetracking.doesNotContain=" + UPDATED_ISSUETRACKING);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInsurancecaseShouldBeFound(String filter) throws Exception {
        restInsurancecaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insurancecase.getId().intValue())))
            .andExpect(jsonPath("$.[*].casenumber").value(hasItem(DEFAULT_CASENUMBER.intValue())))
            .andExpect(jsonPath("$.[*].occurdate").value(hasItem(DEFAULT_OCCURDATE.toString())))
            .andExpect(jsonPath("$.[*].issuetracking").value(hasItem(DEFAULT_ISSUETRACKING)));

        // Check, that the count call also returns 1
        restInsurancecaseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInsurancecaseShouldNotBeFound(String filter) throws Exception {
        restInsurancecaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInsurancecaseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInsurancecase() throws Exception {
        // Get the insurancecase
        restInsurancecaseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInsurancecase() throws Exception {
        // Initialize the database
        insurancecaseRepository.saveAndFlush(insurancecase);

        int databaseSizeBeforeUpdate = insurancecaseRepository.findAll().size();

        // Update the insurancecase
        Insurancecase updatedInsurancecase = insurancecaseRepository.findById(insurancecase.getId()).get();
        // Disconnect from session so that the updates on updatedInsurancecase are not directly saved in db
        em.detach(updatedInsurancecase);
        updatedInsurancecase.casenumber(UPDATED_CASENUMBER).occurdate(UPDATED_OCCURDATE).issuetracking(UPDATED_ISSUETRACKING);
        InsurancecaseDTO insurancecaseDTO = insurancecaseMapper.toDto(updatedInsurancecase);

        restInsurancecaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insurancecaseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insurancecaseDTO))
            )
            .andExpect(status().isOk());

        // Validate the Insurancecase in the database
        List<Insurancecase> insurancecaseList = insurancecaseRepository.findAll();
        assertThat(insurancecaseList).hasSize(databaseSizeBeforeUpdate);
        Insurancecase testInsurancecase = insurancecaseList.get(insurancecaseList.size() - 1);
        assertThat(testInsurancecase.getCasenumber()).isEqualTo(UPDATED_CASENUMBER);
        assertThat(testInsurancecase.getOccurdate()).isEqualTo(UPDATED_OCCURDATE);
        assertThat(testInsurancecase.getIssuetracking()).isEqualTo(UPDATED_ISSUETRACKING);
    }

    @Test
    @Transactional
    void putNonExistingInsurancecase() throws Exception {
        int databaseSizeBeforeUpdate = insurancecaseRepository.findAll().size();
        insurancecase.setId(count.incrementAndGet());

        // Create the Insurancecase
        InsurancecaseDTO insurancecaseDTO = insurancecaseMapper.toDto(insurancecase);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsurancecaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insurancecaseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insurancecaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Insurancecase in the database
        List<Insurancecase> insurancecaseList = insurancecaseRepository.findAll();
        assertThat(insurancecaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsurancecase() throws Exception {
        int databaseSizeBeforeUpdate = insurancecaseRepository.findAll().size();
        insurancecase.setId(count.incrementAndGet());

        // Create the Insurancecase
        InsurancecaseDTO insurancecaseDTO = insurancecaseMapper.toDto(insurancecase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsurancecaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insurancecaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Insurancecase in the database
        List<Insurancecase> insurancecaseList = insurancecaseRepository.findAll();
        assertThat(insurancecaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsurancecase() throws Exception {
        int databaseSizeBeforeUpdate = insurancecaseRepository.findAll().size();
        insurancecase.setId(count.incrementAndGet());

        // Create the Insurancecase
        InsurancecaseDTO insurancecaseDTO = insurancecaseMapper.toDto(insurancecase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsurancecaseMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(insurancecaseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Insurancecase in the database
        List<Insurancecase> insurancecaseList = insurancecaseRepository.findAll();
        assertThat(insurancecaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInsurancecaseWithPatch() throws Exception {
        // Initialize the database
        insurancecaseRepository.saveAndFlush(insurancecase);

        int databaseSizeBeforeUpdate = insurancecaseRepository.findAll().size();

        // Update the insurancecase using partial update
        Insurancecase partialUpdatedInsurancecase = new Insurancecase();
        partialUpdatedInsurancecase.setId(insurancecase.getId());

        partialUpdatedInsurancecase.casenumber(UPDATED_CASENUMBER).occurdate(UPDATED_OCCURDATE).issuetracking(UPDATED_ISSUETRACKING);

        restInsurancecaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsurancecase.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInsurancecase))
            )
            .andExpect(status().isOk());

        // Validate the Insurancecase in the database
        List<Insurancecase> insurancecaseList = insurancecaseRepository.findAll();
        assertThat(insurancecaseList).hasSize(databaseSizeBeforeUpdate);
        Insurancecase testInsurancecase = insurancecaseList.get(insurancecaseList.size() - 1);
        assertThat(testInsurancecase.getCasenumber()).isEqualTo(UPDATED_CASENUMBER);
        assertThat(testInsurancecase.getOccurdate()).isEqualTo(UPDATED_OCCURDATE);
        assertThat(testInsurancecase.getIssuetracking()).isEqualTo(UPDATED_ISSUETRACKING);
    }

    @Test
    @Transactional
    void fullUpdateInsurancecaseWithPatch() throws Exception {
        // Initialize the database
        insurancecaseRepository.saveAndFlush(insurancecase);

        int databaseSizeBeforeUpdate = insurancecaseRepository.findAll().size();

        // Update the insurancecase using partial update
        Insurancecase partialUpdatedInsurancecase = new Insurancecase();
        partialUpdatedInsurancecase.setId(insurancecase.getId());

        partialUpdatedInsurancecase.casenumber(UPDATED_CASENUMBER).occurdate(UPDATED_OCCURDATE).issuetracking(UPDATED_ISSUETRACKING);

        restInsurancecaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsurancecase.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInsurancecase))
            )
            .andExpect(status().isOk());

        // Validate the Insurancecase in the database
        List<Insurancecase> insurancecaseList = insurancecaseRepository.findAll();
        assertThat(insurancecaseList).hasSize(databaseSizeBeforeUpdate);
        Insurancecase testInsurancecase = insurancecaseList.get(insurancecaseList.size() - 1);
        assertThat(testInsurancecase.getCasenumber()).isEqualTo(UPDATED_CASENUMBER);
        assertThat(testInsurancecase.getOccurdate()).isEqualTo(UPDATED_OCCURDATE);
        assertThat(testInsurancecase.getIssuetracking()).isEqualTo(UPDATED_ISSUETRACKING);
    }

    @Test
    @Transactional
    void patchNonExistingInsurancecase() throws Exception {
        int databaseSizeBeforeUpdate = insurancecaseRepository.findAll().size();
        insurancecase.setId(count.incrementAndGet());

        // Create the Insurancecase
        InsurancecaseDTO insurancecaseDTO = insurancecaseMapper.toDto(insurancecase);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsurancecaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insurancecaseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(insurancecaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Insurancecase in the database
        List<Insurancecase> insurancecaseList = insurancecaseRepository.findAll();
        assertThat(insurancecaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsurancecase() throws Exception {
        int databaseSizeBeforeUpdate = insurancecaseRepository.findAll().size();
        insurancecase.setId(count.incrementAndGet());

        // Create the Insurancecase
        InsurancecaseDTO insurancecaseDTO = insurancecaseMapper.toDto(insurancecase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsurancecaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(insurancecaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Insurancecase in the database
        List<Insurancecase> insurancecaseList = insurancecaseRepository.findAll();
        assertThat(insurancecaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsurancecase() throws Exception {
        int databaseSizeBeforeUpdate = insurancecaseRepository.findAll().size();
        insurancecase.setId(count.incrementAndGet());

        // Create the Insurancecase
        InsurancecaseDTO insurancecaseDTO = insurancecaseMapper.toDto(insurancecase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsurancecaseMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(insurancecaseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Insurancecase in the database
        List<Insurancecase> insurancecaseList = insurancecaseRepository.findAll();
        assertThat(insurancecaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInsurancecase() throws Exception {
        // Initialize the database
        insurancecaseRepository.saveAndFlush(insurancecase);

        int databaseSizeBeforeDelete = insurancecaseRepository.findAll().size();

        // Delete the insurancecase
        restInsurancecaseMockMvc
            .perform(delete(ENTITY_API_URL_ID, insurancecase.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Insurancecase> insurancecaseList = insurancecaseRepository.findAll();
        assertThat(insurancecaseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
