package com.atlas.ir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.atlas.ir.IntegrationTest;
import com.atlas.ir.domain.Cau20status;
import com.atlas.ir.repository.Cau20statusRepository;
import com.atlas.ir.service.criteria.Cau20statusCriteria;
import com.atlas.ir.service.dto.Cau20statusDTO;
import com.atlas.ir.service.mapper.Cau20statusMapper;
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
 * Integration tests for the {@link Cau20statusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class Cau20statusResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cau-20-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private Cau20statusRepository cau20statusRepository;

    @Autowired
    private Cau20statusMapper cau20statusMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCau20statusMockMvc;

    private Cau20status cau20status;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cau20status createEntity(EntityManager em) {
        Cau20status cau20status = new Cau20status().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return cau20status;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cau20status createUpdatedEntity(EntityManager em) {
        Cau20status cau20status = new Cau20status().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return cau20status;
    }

    @BeforeEach
    public void initTest() {
        cau20status = createEntity(em);
    }

    @Test
    @Transactional
    void createCau20status() throws Exception {
        int databaseSizeBeforeCreate = cau20statusRepository.findAll().size();
        // Create the Cau20status
        Cau20statusDTO cau20statusDTO = cau20statusMapper.toDto(cau20status);
        restCau20statusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cau20statusDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Cau20status in the database
        List<Cau20status> cau20statusList = cau20statusRepository.findAll();
        assertThat(cau20statusList).hasSize(databaseSizeBeforeCreate + 1);
        Cau20status testCau20status = cau20statusList.get(cau20statusList.size() - 1);
        assertThat(testCau20status.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCau20status.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createCau20statusWithExistingId() throws Exception {
        // Create the Cau20status with an existing ID
        cau20status.setId(1L);
        Cau20statusDTO cau20statusDTO = cau20statusMapper.toDto(cau20status);

        int databaseSizeBeforeCreate = cau20statusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCau20statusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cau20statusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cau20status in the database
        List<Cau20status> cau20statusList = cau20statusRepository.findAll();
        assertThat(cau20statusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCau20statuses() throws Exception {
        // Initialize the database
        cau20statusRepository.saveAndFlush(cau20status);

        // Get all the cau20statusList
        restCau20statusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cau20status.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCau20status() throws Exception {
        // Initialize the database
        cau20statusRepository.saveAndFlush(cau20status);

        // Get the cau20status
        restCau20statusMockMvc
            .perform(get(ENTITY_API_URL_ID, cau20status.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cau20status.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getCau20statusesByIdFiltering() throws Exception {
        // Initialize the database
        cau20statusRepository.saveAndFlush(cau20status);

        Long id = cau20status.getId();

        defaultCau20statusShouldBeFound("id.equals=" + id);
        defaultCau20statusShouldNotBeFound("id.notEquals=" + id);

        defaultCau20statusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCau20statusShouldNotBeFound("id.greaterThan=" + id);

        defaultCau20statusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCau20statusShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCau20statusesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        cau20statusRepository.saveAndFlush(cau20status);

        // Get all the cau20statusList where name equals to DEFAULT_NAME
        defaultCau20statusShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the cau20statusList where name equals to UPDATED_NAME
        defaultCau20statusShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCau20statusesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        cau20statusRepository.saveAndFlush(cau20status);

        // Get all the cau20statusList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCau20statusShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the cau20statusList where name equals to UPDATED_NAME
        defaultCau20statusShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCau20statusesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        cau20statusRepository.saveAndFlush(cau20status);

        // Get all the cau20statusList where name is not null
        defaultCau20statusShouldBeFound("name.specified=true");

        // Get all the cau20statusList where name is null
        defaultCau20statusShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCau20statusesByNameContainsSomething() throws Exception {
        // Initialize the database
        cau20statusRepository.saveAndFlush(cau20status);

        // Get all the cau20statusList where name contains DEFAULT_NAME
        defaultCau20statusShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the cau20statusList where name contains UPDATED_NAME
        defaultCau20statusShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCau20statusesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        cau20statusRepository.saveAndFlush(cau20status);

        // Get all the cau20statusList where name does not contain DEFAULT_NAME
        defaultCau20statusShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the cau20statusList where name does not contain UPDATED_NAME
        defaultCau20statusShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCau20statusesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        cau20statusRepository.saveAndFlush(cau20status);

        // Get all the cau20statusList where description equals to DEFAULT_DESCRIPTION
        defaultCau20statusShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the cau20statusList where description equals to UPDATED_DESCRIPTION
        defaultCau20statusShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCau20statusesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        cau20statusRepository.saveAndFlush(cau20status);

        // Get all the cau20statusList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCau20statusShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the cau20statusList where description equals to UPDATED_DESCRIPTION
        defaultCau20statusShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCau20statusesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cau20statusRepository.saveAndFlush(cau20status);

        // Get all the cau20statusList where description is not null
        defaultCau20statusShouldBeFound("description.specified=true");

        // Get all the cau20statusList where description is null
        defaultCau20statusShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllCau20statusesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        cau20statusRepository.saveAndFlush(cau20status);

        // Get all the cau20statusList where description contains DEFAULT_DESCRIPTION
        defaultCau20statusShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the cau20statusList where description contains UPDATED_DESCRIPTION
        defaultCau20statusShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCau20statusesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        cau20statusRepository.saveAndFlush(cau20status);

        // Get all the cau20statusList where description does not contain DEFAULT_DESCRIPTION
        defaultCau20statusShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the cau20statusList where description does not contain UPDATED_DESCRIPTION
        defaultCau20statusShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCau20statusShouldBeFound(String filter) throws Exception {
        restCau20statusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cau20status.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restCau20statusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCau20statusShouldNotBeFound(String filter) throws Exception {
        restCau20statusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCau20statusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCau20status() throws Exception {
        // Get the cau20status
        restCau20statusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCau20status() throws Exception {
        // Initialize the database
        cau20statusRepository.saveAndFlush(cau20status);

        int databaseSizeBeforeUpdate = cau20statusRepository.findAll().size();

        // Update the cau20status
        Cau20status updatedCau20status = cau20statusRepository.findById(cau20status.getId()).get();
        // Disconnect from session so that the updates on updatedCau20status are not directly saved in db
        em.detach(updatedCau20status);
        updatedCau20status.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        Cau20statusDTO cau20statusDTO = cau20statusMapper.toDto(updatedCau20status);

        restCau20statusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cau20statusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cau20statusDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cau20status in the database
        List<Cau20status> cau20statusList = cau20statusRepository.findAll();
        assertThat(cau20statusList).hasSize(databaseSizeBeforeUpdate);
        Cau20status testCau20status = cau20statusList.get(cau20statusList.size() - 1);
        assertThat(testCau20status.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCau20status.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingCau20status() throws Exception {
        int databaseSizeBeforeUpdate = cau20statusRepository.findAll().size();
        cau20status.setId(count.incrementAndGet());

        // Create the Cau20status
        Cau20statusDTO cau20statusDTO = cau20statusMapper.toDto(cau20status);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCau20statusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cau20statusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cau20statusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cau20status in the database
        List<Cau20status> cau20statusList = cau20statusRepository.findAll();
        assertThat(cau20statusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCau20status() throws Exception {
        int databaseSizeBeforeUpdate = cau20statusRepository.findAll().size();
        cau20status.setId(count.incrementAndGet());

        // Create the Cau20status
        Cau20statusDTO cau20statusDTO = cau20statusMapper.toDto(cau20status);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCau20statusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cau20statusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cau20status in the database
        List<Cau20status> cau20statusList = cau20statusRepository.findAll();
        assertThat(cau20statusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCau20status() throws Exception {
        int databaseSizeBeforeUpdate = cau20statusRepository.findAll().size();
        cau20status.setId(count.incrementAndGet());

        // Create the Cau20status
        Cau20statusDTO cau20statusDTO = cau20statusMapper.toDto(cau20status);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCau20statusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cau20statusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cau20status in the database
        List<Cau20status> cau20statusList = cau20statusRepository.findAll();
        assertThat(cau20statusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCau20statusWithPatch() throws Exception {
        // Initialize the database
        cau20statusRepository.saveAndFlush(cau20status);

        int databaseSizeBeforeUpdate = cau20statusRepository.findAll().size();

        // Update the cau20status using partial update
        Cau20status partialUpdatedCau20status = new Cau20status();
        partialUpdatedCau20status.setId(cau20status.getId());

        partialUpdatedCau20status.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCau20statusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCau20status.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCau20status))
            )
            .andExpect(status().isOk());

        // Validate the Cau20status in the database
        List<Cau20status> cau20statusList = cau20statusRepository.findAll();
        assertThat(cau20statusList).hasSize(databaseSizeBeforeUpdate);
        Cau20status testCau20status = cau20statusList.get(cau20statusList.size() - 1);
        assertThat(testCau20status.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCau20status.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCau20statusWithPatch() throws Exception {
        // Initialize the database
        cau20statusRepository.saveAndFlush(cau20status);

        int databaseSizeBeforeUpdate = cau20statusRepository.findAll().size();

        // Update the cau20status using partial update
        Cau20status partialUpdatedCau20status = new Cau20status();
        partialUpdatedCau20status.setId(cau20status.getId());

        partialUpdatedCau20status.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCau20statusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCau20status.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCau20status))
            )
            .andExpect(status().isOk());

        // Validate the Cau20status in the database
        List<Cau20status> cau20statusList = cau20statusRepository.findAll();
        assertThat(cau20statusList).hasSize(databaseSizeBeforeUpdate);
        Cau20status testCau20status = cau20statusList.get(cau20statusList.size() - 1);
        assertThat(testCau20status.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCau20status.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCau20status() throws Exception {
        int databaseSizeBeforeUpdate = cau20statusRepository.findAll().size();
        cau20status.setId(count.incrementAndGet());

        // Create the Cau20status
        Cau20statusDTO cau20statusDTO = cau20statusMapper.toDto(cau20status);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCau20statusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cau20statusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cau20statusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cau20status in the database
        List<Cau20status> cau20statusList = cau20statusRepository.findAll();
        assertThat(cau20statusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCau20status() throws Exception {
        int databaseSizeBeforeUpdate = cau20statusRepository.findAll().size();
        cau20status.setId(count.incrementAndGet());

        // Create the Cau20status
        Cau20statusDTO cau20statusDTO = cau20statusMapper.toDto(cau20status);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCau20statusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cau20statusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cau20status in the database
        List<Cau20status> cau20statusList = cau20statusRepository.findAll();
        assertThat(cau20statusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCau20status() throws Exception {
        int databaseSizeBeforeUpdate = cau20statusRepository.findAll().size();
        cau20status.setId(count.incrementAndGet());

        // Create the Cau20status
        Cau20statusDTO cau20statusDTO = cau20statusMapper.toDto(cau20status);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCau20statusMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cau20statusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cau20status in the database
        List<Cau20status> cau20statusList = cau20statusRepository.findAll();
        assertThat(cau20statusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCau20status() throws Exception {
        // Initialize the database
        cau20statusRepository.saveAndFlush(cau20status);

        int databaseSizeBeforeDelete = cau20statusRepository.findAll().size();

        // Delete the cau20status
        restCau20statusMockMvc
            .perform(delete(ENTITY_API_URL_ID, cau20status.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cau20status> cau20statusList = cau20statusRepository.findAll();
        assertThat(cau20statusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
