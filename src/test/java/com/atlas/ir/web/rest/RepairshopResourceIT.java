package com.atlas.ir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.atlas.ir.IntegrationTest;
import com.atlas.ir.domain.Repairshop;
import com.atlas.ir.repository.RepairshopRepository;
import com.atlas.ir.service.criteria.RepairshopCriteria;
import com.atlas.ir.service.dto.RepairshopDTO;
import com.atlas.ir.service.mapper.RepairshopMapper;
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
 * Integration tests for the {@link RepairshopResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RepairshopResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/repairshops";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RepairshopRepository repairshopRepository;

    @Autowired
    private RepairshopMapper repairshopMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRepairshopMockMvc;

    private Repairshop repairshop;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Repairshop createEntity(EntityManager em) {
        Repairshop repairshop = new Repairshop().name(DEFAULT_NAME);
        return repairshop;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Repairshop createUpdatedEntity(EntityManager em) {
        Repairshop repairshop = new Repairshop().name(UPDATED_NAME);
        return repairshop;
    }

    @BeforeEach
    public void initTest() {
        repairshop = createEntity(em);
    }

    @Test
    @Transactional
    void createRepairshop() throws Exception {
        int databaseSizeBeforeCreate = repairshopRepository.findAll().size();
        // Create the Repairshop
        RepairshopDTO repairshopDTO = repairshopMapper.toDto(repairshop);
        restRepairshopMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(repairshopDTO)))
            .andExpect(status().isCreated());

        // Validate the Repairshop in the database
        List<Repairshop> repairshopList = repairshopRepository.findAll();
        assertThat(repairshopList).hasSize(databaseSizeBeforeCreate + 1);
        Repairshop testRepairshop = repairshopList.get(repairshopList.size() - 1);
        assertThat(testRepairshop.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createRepairshopWithExistingId() throws Exception {
        // Create the Repairshop with an existing ID
        repairshop.setId(1L);
        RepairshopDTO repairshopDTO = repairshopMapper.toDto(repairshop);

        int databaseSizeBeforeCreate = repairshopRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRepairshopMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(repairshopDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Repairshop in the database
        List<Repairshop> repairshopList = repairshopRepository.findAll();
        assertThat(repairshopList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRepairshops() throws Exception {
        // Initialize the database
        repairshopRepository.saveAndFlush(repairshop);

        // Get all the repairshopList
        restRepairshopMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(repairshop.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getRepairshop() throws Exception {
        // Initialize the database
        repairshopRepository.saveAndFlush(repairshop);

        // Get the repairshop
        restRepairshopMockMvc
            .perform(get(ENTITY_API_URL_ID, repairshop.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(repairshop.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getRepairshopsByIdFiltering() throws Exception {
        // Initialize the database
        repairshopRepository.saveAndFlush(repairshop);

        Long id = repairshop.getId();

        defaultRepairshopShouldBeFound("id.equals=" + id);
        defaultRepairshopShouldNotBeFound("id.notEquals=" + id);

        defaultRepairshopShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRepairshopShouldNotBeFound("id.greaterThan=" + id);

        defaultRepairshopShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRepairshopShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRepairshopsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        repairshopRepository.saveAndFlush(repairshop);

        // Get all the repairshopList where name equals to DEFAULT_NAME
        defaultRepairshopShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the repairshopList where name equals to UPDATED_NAME
        defaultRepairshopShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRepairshopsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        repairshopRepository.saveAndFlush(repairshop);

        // Get all the repairshopList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRepairshopShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the repairshopList where name equals to UPDATED_NAME
        defaultRepairshopShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRepairshopsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        repairshopRepository.saveAndFlush(repairshop);

        // Get all the repairshopList where name is not null
        defaultRepairshopShouldBeFound("name.specified=true");

        // Get all the repairshopList where name is null
        defaultRepairshopShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllRepairshopsByNameContainsSomething() throws Exception {
        // Initialize the database
        repairshopRepository.saveAndFlush(repairshop);

        // Get all the repairshopList where name contains DEFAULT_NAME
        defaultRepairshopShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the repairshopList where name contains UPDATED_NAME
        defaultRepairshopShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRepairshopsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        repairshopRepository.saveAndFlush(repairshop);

        // Get all the repairshopList where name does not contain DEFAULT_NAME
        defaultRepairshopShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the repairshopList where name does not contain UPDATED_NAME
        defaultRepairshopShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRepairshopShouldBeFound(String filter) throws Exception {
        restRepairshopMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(repairshop.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restRepairshopMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRepairshopShouldNotBeFound(String filter) throws Exception {
        restRepairshopMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRepairshopMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRepairshop() throws Exception {
        // Get the repairshop
        restRepairshopMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRepairshop() throws Exception {
        // Initialize the database
        repairshopRepository.saveAndFlush(repairshop);

        int databaseSizeBeforeUpdate = repairshopRepository.findAll().size();

        // Update the repairshop
        Repairshop updatedRepairshop = repairshopRepository.findById(repairshop.getId()).get();
        // Disconnect from session so that the updates on updatedRepairshop are not directly saved in db
        em.detach(updatedRepairshop);
        updatedRepairshop.name(UPDATED_NAME);
        RepairshopDTO repairshopDTO = repairshopMapper.toDto(updatedRepairshop);

        restRepairshopMockMvc
            .perform(
                put(ENTITY_API_URL_ID, repairshopDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(repairshopDTO))
            )
            .andExpect(status().isOk());

        // Validate the Repairshop in the database
        List<Repairshop> repairshopList = repairshopRepository.findAll();
        assertThat(repairshopList).hasSize(databaseSizeBeforeUpdate);
        Repairshop testRepairshop = repairshopList.get(repairshopList.size() - 1);
        assertThat(testRepairshop.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingRepairshop() throws Exception {
        int databaseSizeBeforeUpdate = repairshopRepository.findAll().size();
        repairshop.setId(count.incrementAndGet());

        // Create the Repairshop
        RepairshopDTO repairshopDTO = repairshopMapper.toDto(repairshop);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRepairshopMockMvc
            .perform(
                put(ENTITY_API_URL_ID, repairshopDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(repairshopDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repairshop in the database
        List<Repairshop> repairshopList = repairshopRepository.findAll();
        assertThat(repairshopList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRepairshop() throws Exception {
        int databaseSizeBeforeUpdate = repairshopRepository.findAll().size();
        repairshop.setId(count.incrementAndGet());

        // Create the Repairshop
        RepairshopDTO repairshopDTO = repairshopMapper.toDto(repairshop);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepairshopMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(repairshopDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repairshop in the database
        List<Repairshop> repairshopList = repairshopRepository.findAll();
        assertThat(repairshopList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRepairshop() throws Exception {
        int databaseSizeBeforeUpdate = repairshopRepository.findAll().size();
        repairshop.setId(count.incrementAndGet());

        // Create the Repairshop
        RepairshopDTO repairshopDTO = repairshopMapper.toDto(repairshop);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepairshopMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(repairshopDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Repairshop in the database
        List<Repairshop> repairshopList = repairshopRepository.findAll();
        assertThat(repairshopList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRepairshopWithPatch() throws Exception {
        // Initialize the database
        repairshopRepository.saveAndFlush(repairshop);

        int databaseSizeBeforeUpdate = repairshopRepository.findAll().size();

        // Update the repairshop using partial update
        Repairshop partialUpdatedRepairshop = new Repairshop();
        partialUpdatedRepairshop.setId(repairshop.getId());

        restRepairshopMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRepairshop.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRepairshop))
            )
            .andExpect(status().isOk());

        // Validate the Repairshop in the database
        List<Repairshop> repairshopList = repairshopRepository.findAll();
        assertThat(repairshopList).hasSize(databaseSizeBeforeUpdate);
        Repairshop testRepairshop = repairshopList.get(repairshopList.size() - 1);
        assertThat(testRepairshop.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateRepairshopWithPatch() throws Exception {
        // Initialize the database
        repairshopRepository.saveAndFlush(repairshop);

        int databaseSizeBeforeUpdate = repairshopRepository.findAll().size();

        // Update the repairshop using partial update
        Repairshop partialUpdatedRepairshop = new Repairshop();
        partialUpdatedRepairshop.setId(repairshop.getId());

        partialUpdatedRepairshop.name(UPDATED_NAME);

        restRepairshopMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRepairshop.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRepairshop))
            )
            .andExpect(status().isOk());

        // Validate the Repairshop in the database
        List<Repairshop> repairshopList = repairshopRepository.findAll();
        assertThat(repairshopList).hasSize(databaseSizeBeforeUpdate);
        Repairshop testRepairshop = repairshopList.get(repairshopList.size() - 1);
        assertThat(testRepairshop.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingRepairshop() throws Exception {
        int databaseSizeBeforeUpdate = repairshopRepository.findAll().size();
        repairshop.setId(count.incrementAndGet());

        // Create the Repairshop
        RepairshopDTO repairshopDTO = repairshopMapper.toDto(repairshop);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRepairshopMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, repairshopDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(repairshopDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repairshop in the database
        List<Repairshop> repairshopList = repairshopRepository.findAll();
        assertThat(repairshopList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRepairshop() throws Exception {
        int databaseSizeBeforeUpdate = repairshopRepository.findAll().size();
        repairshop.setId(count.incrementAndGet());

        // Create the Repairshop
        RepairshopDTO repairshopDTO = repairshopMapper.toDto(repairshop);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepairshopMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(repairshopDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repairshop in the database
        List<Repairshop> repairshopList = repairshopRepository.findAll();
        assertThat(repairshopList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRepairshop() throws Exception {
        int databaseSizeBeforeUpdate = repairshopRepository.findAll().size();
        repairshop.setId(count.incrementAndGet());

        // Create the Repairshop
        RepairshopDTO repairshopDTO = repairshopMapper.toDto(repairshop);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepairshopMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(repairshopDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Repairshop in the database
        List<Repairshop> repairshopList = repairshopRepository.findAll();
        assertThat(repairshopList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRepairshop() throws Exception {
        // Initialize the database
        repairshopRepository.saveAndFlush(repairshop);

        int databaseSizeBeforeDelete = repairshopRepository.findAll().size();

        // Delete the repairshop
        restRepairshopMockMvc
            .perform(delete(ENTITY_API_URL_ID, repairshop.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Repairshop> repairshopList = repairshopRepository.findAll();
        assertThat(repairshopList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
