package com.atlas.ir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.atlas.ir.IntegrationTest;
import com.atlas.ir.domain.Caseaccidentu20;
import com.atlas.ir.repository.Caseaccidentu20Repository;
import com.atlas.ir.service.criteria.Caseaccidentu20Criteria;
import com.atlas.ir.service.dto.Caseaccidentu20DTO;
import com.atlas.ir.service.mapper.Caseaccidentu20Mapper;
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
 * Integration tests for the {@link Caseaccidentu20Resource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class Caseaccidentu20ResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/caseaccidentu-20-s";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private Caseaccidentu20Repository caseaccidentu20Repository;

    @Autowired
    private Caseaccidentu20Mapper caseaccidentu20Mapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCaseaccidentu20MockMvc;

    private Caseaccidentu20 caseaccidentu20;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caseaccidentu20 createEntity(EntityManager em) {
        Caseaccidentu20 caseaccidentu20 = new Caseaccidentu20().date(DEFAULT_DATE);
        return caseaccidentu20;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caseaccidentu20 createUpdatedEntity(EntityManager em) {
        Caseaccidentu20 caseaccidentu20 = new Caseaccidentu20().date(UPDATED_DATE);
        return caseaccidentu20;
    }

    @BeforeEach
    public void initTest() {
        caseaccidentu20 = createEntity(em);
    }

    @Test
    @Transactional
    void createCaseaccidentu20() throws Exception {
        int databaseSizeBeforeCreate = caseaccidentu20Repository.findAll().size();
        // Create the Caseaccidentu20
        Caseaccidentu20DTO caseaccidentu20DTO = caseaccidentu20Mapper.toDto(caseaccidentu20);
        restCaseaccidentu20MockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(caseaccidentu20DTO))
            )
            .andExpect(status().isCreated());

        // Validate the Caseaccidentu20 in the database
        List<Caseaccidentu20> caseaccidentu20List = caseaccidentu20Repository.findAll();
        assertThat(caseaccidentu20List).hasSize(databaseSizeBeforeCreate + 1);
        Caseaccidentu20 testCaseaccidentu20 = caseaccidentu20List.get(caseaccidentu20List.size() - 1);
        assertThat(testCaseaccidentu20.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createCaseaccidentu20WithExistingId() throws Exception {
        // Create the Caseaccidentu20 with an existing ID
        caseaccidentu20.setId(1L);
        Caseaccidentu20DTO caseaccidentu20DTO = caseaccidentu20Mapper.toDto(caseaccidentu20);

        int databaseSizeBeforeCreate = caseaccidentu20Repository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCaseaccidentu20MockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(caseaccidentu20DTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caseaccidentu20 in the database
        List<Caseaccidentu20> caseaccidentu20List = caseaccidentu20Repository.findAll();
        assertThat(caseaccidentu20List).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCaseaccidentu20s() throws Exception {
        // Initialize the database
        caseaccidentu20Repository.saveAndFlush(caseaccidentu20);

        // Get all the caseaccidentu20List
        restCaseaccidentu20MockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caseaccidentu20.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getCaseaccidentu20() throws Exception {
        // Initialize the database
        caseaccidentu20Repository.saveAndFlush(caseaccidentu20);

        // Get the caseaccidentu20
        restCaseaccidentu20MockMvc
            .perform(get(ENTITY_API_URL_ID, caseaccidentu20.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(caseaccidentu20.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getCaseaccidentu20sByIdFiltering() throws Exception {
        // Initialize the database
        caseaccidentu20Repository.saveAndFlush(caseaccidentu20);

        Long id = caseaccidentu20.getId();

        defaultCaseaccidentu20ShouldBeFound("id.equals=" + id);
        defaultCaseaccidentu20ShouldNotBeFound("id.notEquals=" + id);

        defaultCaseaccidentu20ShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCaseaccidentu20ShouldNotBeFound("id.greaterThan=" + id);

        defaultCaseaccidentu20ShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCaseaccidentu20ShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCaseaccidentu20sByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        caseaccidentu20Repository.saveAndFlush(caseaccidentu20);

        // Get all the caseaccidentu20List where date equals to DEFAULT_DATE
        defaultCaseaccidentu20ShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the caseaccidentu20List where date equals to UPDATED_DATE
        defaultCaseaccidentu20ShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCaseaccidentu20sByDateIsInShouldWork() throws Exception {
        // Initialize the database
        caseaccidentu20Repository.saveAndFlush(caseaccidentu20);

        // Get all the caseaccidentu20List where date in DEFAULT_DATE or UPDATED_DATE
        defaultCaseaccidentu20ShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the caseaccidentu20List where date equals to UPDATED_DATE
        defaultCaseaccidentu20ShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCaseaccidentu20sByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        caseaccidentu20Repository.saveAndFlush(caseaccidentu20);

        // Get all the caseaccidentu20List where date is not null
        defaultCaseaccidentu20ShouldBeFound("date.specified=true");

        // Get all the caseaccidentu20List where date is null
        defaultCaseaccidentu20ShouldNotBeFound("date.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCaseaccidentu20ShouldBeFound(String filter) throws Exception {
        restCaseaccidentu20MockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caseaccidentu20.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restCaseaccidentu20MockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCaseaccidentu20ShouldNotBeFound(String filter) throws Exception {
        restCaseaccidentu20MockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCaseaccidentu20MockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCaseaccidentu20() throws Exception {
        // Get the caseaccidentu20
        restCaseaccidentu20MockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCaseaccidentu20() throws Exception {
        // Initialize the database
        caseaccidentu20Repository.saveAndFlush(caseaccidentu20);

        int databaseSizeBeforeUpdate = caseaccidentu20Repository.findAll().size();

        // Update the caseaccidentu20
        Caseaccidentu20 updatedCaseaccidentu20 = caseaccidentu20Repository.findById(caseaccidentu20.getId()).get();
        // Disconnect from session so that the updates on updatedCaseaccidentu20 are not directly saved in db
        em.detach(updatedCaseaccidentu20);
        updatedCaseaccidentu20.date(UPDATED_DATE);
        Caseaccidentu20DTO caseaccidentu20DTO = caseaccidentu20Mapper.toDto(updatedCaseaccidentu20);

        restCaseaccidentu20MockMvc
            .perform(
                put(ENTITY_API_URL_ID, caseaccidentu20DTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caseaccidentu20DTO))
            )
            .andExpect(status().isOk());

        // Validate the Caseaccidentu20 in the database
        List<Caseaccidentu20> caseaccidentu20List = caseaccidentu20Repository.findAll();
        assertThat(caseaccidentu20List).hasSize(databaseSizeBeforeUpdate);
        Caseaccidentu20 testCaseaccidentu20 = caseaccidentu20List.get(caseaccidentu20List.size() - 1);
        assertThat(testCaseaccidentu20.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingCaseaccidentu20() throws Exception {
        int databaseSizeBeforeUpdate = caseaccidentu20Repository.findAll().size();
        caseaccidentu20.setId(count.incrementAndGet());

        // Create the Caseaccidentu20
        Caseaccidentu20DTO caseaccidentu20DTO = caseaccidentu20Mapper.toDto(caseaccidentu20);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaseaccidentu20MockMvc
            .perform(
                put(ENTITY_API_URL_ID, caseaccidentu20DTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caseaccidentu20DTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caseaccidentu20 in the database
        List<Caseaccidentu20> caseaccidentu20List = caseaccidentu20Repository.findAll();
        assertThat(caseaccidentu20List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCaseaccidentu20() throws Exception {
        int databaseSizeBeforeUpdate = caseaccidentu20Repository.findAll().size();
        caseaccidentu20.setId(count.incrementAndGet());

        // Create the Caseaccidentu20
        Caseaccidentu20DTO caseaccidentu20DTO = caseaccidentu20Mapper.toDto(caseaccidentu20);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaseaccidentu20MockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caseaccidentu20DTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caseaccidentu20 in the database
        List<Caseaccidentu20> caseaccidentu20List = caseaccidentu20Repository.findAll();
        assertThat(caseaccidentu20List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCaseaccidentu20() throws Exception {
        int databaseSizeBeforeUpdate = caseaccidentu20Repository.findAll().size();
        caseaccidentu20.setId(count.incrementAndGet());

        // Create the Caseaccidentu20
        Caseaccidentu20DTO caseaccidentu20DTO = caseaccidentu20Mapper.toDto(caseaccidentu20);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaseaccidentu20MockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(caseaccidentu20DTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Caseaccidentu20 in the database
        List<Caseaccidentu20> caseaccidentu20List = caseaccidentu20Repository.findAll();
        assertThat(caseaccidentu20List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCaseaccidentu20WithPatch() throws Exception {
        // Initialize the database
        caseaccidentu20Repository.saveAndFlush(caseaccidentu20);

        int databaseSizeBeforeUpdate = caseaccidentu20Repository.findAll().size();

        // Update the caseaccidentu20 using partial update
        Caseaccidentu20 partialUpdatedCaseaccidentu20 = new Caseaccidentu20();
        partialUpdatedCaseaccidentu20.setId(caseaccidentu20.getId());

        restCaseaccidentu20MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaseaccidentu20.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCaseaccidentu20))
            )
            .andExpect(status().isOk());

        // Validate the Caseaccidentu20 in the database
        List<Caseaccidentu20> caseaccidentu20List = caseaccidentu20Repository.findAll();
        assertThat(caseaccidentu20List).hasSize(databaseSizeBeforeUpdate);
        Caseaccidentu20 testCaseaccidentu20 = caseaccidentu20List.get(caseaccidentu20List.size() - 1);
        assertThat(testCaseaccidentu20.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateCaseaccidentu20WithPatch() throws Exception {
        // Initialize the database
        caseaccidentu20Repository.saveAndFlush(caseaccidentu20);

        int databaseSizeBeforeUpdate = caseaccidentu20Repository.findAll().size();

        // Update the caseaccidentu20 using partial update
        Caseaccidentu20 partialUpdatedCaseaccidentu20 = new Caseaccidentu20();
        partialUpdatedCaseaccidentu20.setId(caseaccidentu20.getId());

        partialUpdatedCaseaccidentu20.date(UPDATED_DATE);

        restCaseaccidentu20MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaseaccidentu20.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCaseaccidentu20))
            )
            .andExpect(status().isOk());

        // Validate the Caseaccidentu20 in the database
        List<Caseaccidentu20> caseaccidentu20List = caseaccidentu20Repository.findAll();
        assertThat(caseaccidentu20List).hasSize(databaseSizeBeforeUpdate);
        Caseaccidentu20 testCaseaccidentu20 = caseaccidentu20List.get(caseaccidentu20List.size() - 1);
        assertThat(testCaseaccidentu20.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingCaseaccidentu20() throws Exception {
        int databaseSizeBeforeUpdate = caseaccidentu20Repository.findAll().size();
        caseaccidentu20.setId(count.incrementAndGet());

        // Create the Caseaccidentu20
        Caseaccidentu20DTO caseaccidentu20DTO = caseaccidentu20Mapper.toDto(caseaccidentu20);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaseaccidentu20MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, caseaccidentu20DTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(caseaccidentu20DTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caseaccidentu20 in the database
        List<Caseaccidentu20> caseaccidentu20List = caseaccidentu20Repository.findAll();
        assertThat(caseaccidentu20List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCaseaccidentu20() throws Exception {
        int databaseSizeBeforeUpdate = caseaccidentu20Repository.findAll().size();
        caseaccidentu20.setId(count.incrementAndGet());

        // Create the Caseaccidentu20
        Caseaccidentu20DTO caseaccidentu20DTO = caseaccidentu20Mapper.toDto(caseaccidentu20);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaseaccidentu20MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(caseaccidentu20DTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caseaccidentu20 in the database
        List<Caseaccidentu20> caseaccidentu20List = caseaccidentu20Repository.findAll();
        assertThat(caseaccidentu20List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCaseaccidentu20() throws Exception {
        int databaseSizeBeforeUpdate = caseaccidentu20Repository.findAll().size();
        caseaccidentu20.setId(count.incrementAndGet());

        // Create the Caseaccidentu20
        Caseaccidentu20DTO caseaccidentu20DTO = caseaccidentu20Mapper.toDto(caseaccidentu20);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaseaccidentu20MockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(caseaccidentu20DTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Caseaccidentu20 in the database
        List<Caseaccidentu20> caseaccidentu20List = caseaccidentu20Repository.findAll();
        assertThat(caseaccidentu20List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCaseaccidentu20() throws Exception {
        // Initialize the database
        caseaccidentu20Repository.saveAndFlush(caseaccidentu20);

        int databaseSizeBeforeDelete = caseaccidentu20Repository.findAll().size();

        // Delete the caseaccidentu20
        restCaseaccidentu20MockMvc
            .perform(delete(ENTITY_API_URL_ID, caseaccidentu20.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Caseaccidentu20> caseaccidentu20List = caseaccidentu20Repository.findAll();
        assertThat(caseaccidentu20List).hasSize(databaseSizeBeforeDelete - 1);
    }
}
