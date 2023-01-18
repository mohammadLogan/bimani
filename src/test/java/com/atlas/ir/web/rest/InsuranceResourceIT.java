package com.atlas.ir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.atlas.ir.IntegrationTest;
import com.atlas.ir.domain.Insurance;
import com.atlas.ir.repository.InsuranceRepository;
import com.atlas.ir.service.criteria.InsuranceCriteria;
import com.atlas.ir.service.dto.InsuranceDTO;
import com.atlas.ir.service.mapper.InsuranceMapper;
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
 * Integration tests for the {@link InsuranceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InsuranceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/insurances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InsuranceRepository insuranceRepository;

    @Autowired
    private InsuranceMapper insuranceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsuranceMockMvc;

    private Insurance insurance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Insurance createEntity(EntityManager em) {
        Insurance insurance = new Insurance().name(DEFAULT_NAME);
        return insurance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Insurance createUpdatedEntity(EntityManager em) {
        Insurance insurance = new Insurance().name(UPDATED_NAME);
        return insurance;
    }

    @BeforeEach
    public void initTest() {
        insurance = createEntity(em);
    }

    @Test
    @Transactional
    void createInsurance() throws Exception {
        int databaseSizeBeforeCreate = insuranceRepository.findAll().size();
        // Create the Insurance
        InsuranceDTO insuranceDTO = insuranceMapper.toDto(insurance);
        restInsuranceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(insuranceDTO)))
            .andExpect(status().isCreated());

        // Validate the Insurance in the database
        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeCreate + 1);
        Insurance testInsurance = insuranceList.get(insuranceList.size() - 1);
        assertThat(testInsurance.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createInsuranceWithExistingId() throws Exception {
        // Create the Insurance with an existing ID
        insurance.setId(1L);
        InsuranceDTO insuranceDTO = insuranceMapper.toDto(insurance);

        int databaseSizeBeforeCreate = insuranceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsuranceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(insuranceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Insurance in the database
        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInsurances() throws Exception {
        // Initialize the database
        insuranceRepository.saveAndFlush(insurance);

        // Get all the insuranceList
        restInsuranceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insurance.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getInsurance() throws Exception {
        // Initialize the database
        insuranceRepository.saveAndFlush(insurance);

        // Get the insurance
        restInsuranceMockMvc
            .perform(get(ENTITY_API_URL_ID, insurance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insurance.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getInsurancesByIdFiltering() throws Exception {
        // Initialize the database
        insuranceRepository.saveAndFlush(insurance);

        Long id = insurance.getId();

        defaultInsuranceShouldBeFound("id.equals=" + id);
        defaultInsuranceShouldNotBeFound("id.notEquals=" + id);

        defaultInsuranceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInsuranceShouldNotBeFound("id.greaterThan=" + id);

        defaultInsuranceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInsuranceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInsurancesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insuranceRepository.saveAndFlush(insurance);

        // Get all the insuranceList where name equals to DEFAULT_NAME
        defaultInsuranceShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the insuranceList where name equals to UPDATED_NAME
        defaultInsuranceShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInsurancesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insuranceRepository.saveAndFlush(insurance);

        // Get all the insuranceList where name in DEFAULT_NAME or UPDATED_NAME
        defaultInsuranceShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the insuranceList where name equals to UPDATED_NAME
        defaultInsuranceShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInsurancesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insuranceRepository.saveAndFlush(insurance);

        // Get all the insuranceList where name is not null
        defaultInsuranceShouldBeFound("name.specified=true");

        // Get all the insuranceList where name is null
        defaultInsuranceShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllInsurancesByNameContainsSomething() throws Exception {
        // Initialize the database
        insuranceRepository.saveAndFlush(insurance);

        // Get all the insuranceList where name contains DEFAULT_NAME
        defaultInsuranceShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the insuranceList where name contains UPDATED_NAME
        defaultInsuranceShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInsurancesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insuranceRepository.saveAndFlush(insurance);

        // Get all the insuranceList where name does not contain DEFAULT_NAME
        defaultInsuranceShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the insuranceList where name does not contain UPDATED_NAME
        defaultInsuranceShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInsuranceShouldBeFound(String filter) throws Exception {
        restInsuranceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insurance.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restInsuranceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInsuranceShouldNotBeFound(String filter) throws Exception {
        restInsuranceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInsuranceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInsurance() throws Exception {
        // Get the insurance
        restInsuranceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInsurance() throws Exception {
        // Initialize the database
        insuranceRepository.saveAndFlush(insurance);

        int databaseSizeBeforeUpdate = insuranceRepository.findAll().size();

        // Update the insurance
        Insurance updatedInsurance = insuranceRepository.findById(insurance.getId()).get();
        // Disconnect from session so that the updates on updatedInsurance are not directly saved in db
        em.detach(updatedInsurance);
        updatedInsurance.name(UPDATED_NAME);
        InsuranceDTO insuranceDTO = insuranceMapper.toDto(updatedInsurance);

        restInsuranceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insuranceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insuranceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Insurance in the database
        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeUpdate);
        Insurance testInsurance = insuranceList.get(insuranceList.size() - 1);
        assertThat(testInsurance.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingInsurance() throws Exception {
        int databaseSizeBeforeUpdate = insuranceRepository.findAll().size();
        insurance.setId(count.incrementAndGet());

        // Create the Insurance
        InsuranceDTO insuranceDTO = insuranceMapper.toDto(insurance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insuranceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insuranceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Insurance in the database
        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsurance() throws Exception {
        int databaseSizeBeforeUpdate = insuranceRepository.findAll().size();
        insurance.setId(count.incrementAndGet());

        // Create the Insurance
        InsuranceDTO insuranceDTO = insuranceMapper.toDto(insurance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insuranceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Insurance in the database
        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsurance() throws Exception {
        int databaseSizeBeforeUpdate = insuranceRepository.findAll().size();
        insurance.setId(count.incrementAndGet());

        // Create the Insurance
        InsuranceDTO insuranceDTO = insuranceMapper.toDto(insurance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(insuranceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Insurance in the database
        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInsuranceWithPatch() throws Exception {
        // Initialize the database
        insuranceRepository.saveAndFlush(insurance);

        int databaseSizeBeforeUpdate = insuranceRepository.findAll().size();

        // Update the insurance using partial update
        Insurance partialUpdatedInsurance = new Insurance();
        partialUpdatedInsurance.setId(insurance.getId());

        restInsuranceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsurance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInsurance))
            )
            .andExpect(status().isOk());

        // Validate the Insurance in the database
        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeUpdate);
        Insurance testInsurance = insuranceList.get(insuranceList.size() - 1);
        assertThat(testInsurance.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateInsuranceWithPatch() throws Exception {
        // Initialize the database
        insuranceRepository.saveAndFlush(insurance);

        int databaseSizeBeforeUpdate = insuranceRepository.findAll().size();

        // Update the insurance using partial update
        Insurance partialUpdatedInsurance = new Insurance();
        partialUpdatedInsurance.setId(insurance.getId());

        partialUpdatedInsurance.name(UPDATED_NAME);

        restInsuranceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsurance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInsurance))
            )
            .andExpect(status().isOk());

        // Validate the Insurance in the database
        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeUpdate);
        Insurance testInsurance = insuranceList.get(insuranceList.size() - 1);
        assertThat(testInsurance.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingInsurance() throws Exception {
        int databaseSizeBeforeUpdate = insuranceRepository.findAll().size();
        insurance.setId(count.incrementAndGet());

        // Create the Insurance
        InsuranceDTO insuranceDTO = insuranceMapper.toDto(insurance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insuranceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(insuranceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Insurance in the database
        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsurance() throws Exception {
        int databaseSizeBeforeUpdate = insuranceRepository.findAll().size();
        insurance.setId(count.incrementAndGet());

        // Create the Insurance
        InsuranceDTO insuranceDTO = insuranceMapper.toDto(insurance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(insuranceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Insurance in the database
        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsurance() throws Exception {
        int databaseSizeBeforeUpdate = insuranceRepository.findAll().size();
        insurance.setId(count.incrementAndGet());

        // Create the Insurance
        InsuranceDTO insuranceDTO = insuranceMapper.toDto(insurance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(insuranceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Insurance in the database
        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInsurance() throws Exception {
        // Initialize the database
        insuranceRepository.saveAndFlush(insurance);

        int databaseSizeBeforeDelete = insuranceRepository.findAll().size();

        // Delete the insurance
        restInsuranceMockMvc
            .perform(delete(ENTITY_API_URL_ID, insurance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Insurance> insuranceList = insuranceRepository.findAll();
        assertThat(insuranceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
