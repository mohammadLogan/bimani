package com.atlas.ir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.atlas.ir.IntegrationTest;
import com.atlas.ir.domain.Ticketstatus;
import com.atlas.ir.repository.TicketstatusRepository;
import com.atlas.ir.service.criteria.TicketstatusCriteria;
import com.atlas.ir.service.dto.TicketstatusDTO;
import com.atlas.ir.service.mapper.TicketstatusMapper;
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
 * Integration tests for the {@link TicketstatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TicketstatusResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ticketstatuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TicketstatusRepository ticketstatusRepository;

    @Autowired
    private TicketstatusMapper ticketstatusMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTicketstatusMockMvc;

    private Ticketstatus ticketstatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticketstatus createEntity(EntityManager em) {
        Ticketstatus ticketstatus = new Ticketstatus().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return ticketstatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticketstatus createUpdatedEntity(EntityManager em) {
        Ticketstatus ticketstatus = new Ticketstatus().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return ticketstatus;
    }

    @BeforeEach
    public void initTest() {
        ticketstatus = createEntity(em);
    }

    @Test
    @Transactional
    void createTicketstatus() throws Exception {
        int databaseSizeBeforeCreate = ticketstatusRepository.findAll().size();
        // Create the Ticketstatus
        TicketstatusDTO ticketstatusDTO = ticketstatusMapper.toDto(ticketstatus);
        restTicketstatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticketstatusDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Ticketstatus in the database
        List<Ticketstatus> ticketstatusList = ticketstatusRepository.findAll();
        assertThat(ticketstatusList).hasSize(databaseSizeBeforeCreate + 1);
        Ticketstatus testTicketstatus = ticketstatusList.get(ticketstatusList.size() - 1);
        assertThat(testTicketstatus.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTicketstatus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createTicketstatusWithExistingId() throws Exception {
        // Create the Ticketstatus with an existing ID
        ticketstatus.setId(1L);
        TicketstatusDTO ticketstatusDTO = ticketstatusMapper.toDto(ticketstatus);

        int databaseSizeBeforeCreate = ticketstatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTicketstatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticketstatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticketstatus in the database
        List<Ticketstatus> ticketstatusList = ticketstatusRepository.findAll();
        assertThat(ticketstatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTicketstatuses() throws Exception {
        // Initialize the database
        ticketstatusRepository.saveAndFlush(ticketstatus);

        // Get all the ticketstatusList
        restTicketstatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticketstatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getTicketstatus() throws Exception {
        // Initialize the database
        ticketstatusRepository.saveAndFlush(ticketstatus);

        // Get the ticketstatus
        restTicketstatusMockMvc
            .perform(get(ENTITY_API_URL_ID, ticketstatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ticketstatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getTicketstatusesByIdFiltering() throws Exception {
        // Initialize the database
        ticketstatusRepository.saveAndFlush(ticketstatus);

        Long id = ticketstatus.getId();

        defaultTicketstatusShouldBeFound("id.equals=" + id);
        defaultTicketstatusShouldNotBeFound("id.notEquals=" + id);

        defaultTicketstatusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTicketstatusShouldNotBeFound("id.greaterThan=" + id);

        defaultTicketstatusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTicketstatusShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTicketstatusesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketstatusRepository.saveAndFlush(ticketstatus);

        // Get all the ticketstatusList where name equals to DEFAULT_NAME
        defaultTicketstatusShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the ticketstatusList where name equals to UPDATED_NAME
        defaultTicketstatusShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTicketstatusesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        ticketstatusRepository.saveAndFlush(ticketstatus);

        // Get all the ticketstatusList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTicketstatusShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the ticketstatusList where name equals to UPDATED_NAME
        defaultTicketstatusShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTicketstatusesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketstatusRepository.saveAndFlush(ticketstatus);

        // Get all the ticketstatusList where name is not null
        defaultTicketstatusShouldBeFound("name.specified=true");

        // Get all the ticketstatusList where name is null
        defaultTicketstatusShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketstatusesByNameContainsSomething() throws Exception {
        // Initialize the database
        ticketstatusRepository.saveAndFlush(ticketstatus);

        // Get all the ticketstatusList where name contains DEFAULT_NAME
        defaultTicketstatusShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the ticketstatusList where name contains UPDATED_NAME
        defaultTicketstatusShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTicketstatusesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        ticketstatusRepository.saveAndFlush(ticketstatus);

        // Get all the ticketstatusList where name does not contain DEFAULT_NAME
        defaultTicketstatusShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the ticketstatusList where name does not contain UPDATED_NAME
        defaultTicketstatusShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTicketstatusesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketstatusRepository.saveAndFlush(ticketstatus);

        // Get all the ticketstatusList where description equals to DEFAULT_DESCRIPTION
        defaultTicketstatusShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the ticketstatusList where description equals to UPDATED_DESCRIPTION
        defaultTicketstatusShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTicketstatusesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        ticketstatusRepository.saveAndFlush(ticketstatus);

        // Get all the ticketstatusList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTicketstatusShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the ticketstatusList where description equals to UPDATED_DESCRIPTION
        defaultTicketstatusShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTicketstatusesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketstatusRepository.saveAndFlush(ticketstatus);

        // Get all the ticketstatusList where description is not null
        defaultTicketstatusShouldBeFound("description.specified=true");

        // Get all the ticketstatusList where description is null
        defaultTicketstatusShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketstatusesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        ticketstatusRepository.saveAndFlush(ticketstatus);

        // Get all the ticketstatusList where description contains DEFAULT_DESCRIPTION
        defaultTicketstatusShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the ticketstatusList where description contains UPDATED_DESCRIPTION
        defaultTicketstatusShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTicketstatusesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        ticketstatusRepository.saveAndFlush(ticketstatus);

        // Get all the ticketstatusList where description does not contain DEFAULT_DESCRIPTION
        defaultTicketstatusShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the ticketstatusList where description does not contain UPDATED_DESCRIPTION
        defaultTicketstatusShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTicketstatusShouldBeFound(String filter) throws Exception {
        restTicketstatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticketstatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restTicketstatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTicketstatusShouldNotBeFound(String filter) throws Exception {
        restTicketstatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTicketstatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTicketstatus() throws Exception {
        // Get the ticketstatus
        restTicketstatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTicketstatus() throws Exception {
        // Initialize the database
        ticketstatusRepository.saveAndFlush(ticketstatus);

        int databaseSizeBeforeUpdate = ticketstatusRepository.findAll().size();

        // Update the ticketstatus
        Ticketstatus updatedTicketstatus = ticketstatusRepository.findById(ticketstatus.getId()).get();
        // Disconnect from session so that the updates on updatedTicketstatus are not directly saved in db
        em.detach(updatedTicketstatus);
        updatedTicketstatus.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        TicketstatusDTO ticketstatusDTO = ticketstatusMapper.toDto(updatedTicketstatus);

        restTicketstatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ticketstatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketstatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ticketstatus in the database
        List<Ticketstatus> ticketstatusList = ticketstatusRepository.findAll();
        assertThat(ticketstatusList).hasSize(databaseSizeBeforeUpdate);
        Ticketstatus testTicketstatus = ticketstatusList.get(ticketstatusList.size() - 1);
        assertThat(testTicketstatus.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTicketstatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingTicketstatus() throws Exception {
        int databaseSizeBeforeUpdate = ticketstatusRepository.findAll().size();
        ticketstatus.setId(count.incrementAndGet());

        // Create the Ticketstatus
        TicketstatusDTO ticketstatusDTO = ticketstatusMapper.toDto(ticketstatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTicketstatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ticketstatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketstatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticketstatus in the database
        List<Ticketstatus> ticketstatusList = ticketstatusRepository.findAll();
        assertThat(ticketstatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTicketstatus() throws Exception {
        int databaseSizeBeforeUpdate = ticketstatusRepository.findAll().size();
        ticketstatus.setId(count.incrementAndGet());

        // Create the Ticketstatus
        TicketstatusDTO ticketstatusDTO = ticketstatusMapper.toDto(ticketstatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketstatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketstatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticketstatus in the database
        List<Ticketstatus> ticketstatusList = ticketstatusRepository.findAll();
        assertThat(ticketstatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTicketstatus() throws Exception {
        int databaseSizeBeforeUpdate = ticketstatusRepository.findAll().size();
        ticketstatus.setId(count.incrementAndGet());

        // Create the Ticketstatus
        TicketstatusDTO ticketstatusDTO = ticketstatusMapper.toDto(ticketstatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketstatusMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticketstatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ticketstatus in the database
        List<Ticketstatus> ticketstatusList = ticketstatusRepository.findAll();
        assertThat(ticketstatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTicketstatusWithPatch() throws Exception {
        // Initialize the database
        ticketstatusRepository.saveAndFlush(ticketstatus);

        int databaseSizeBeforeUpdate = ticketstatusRepository.findAll().size();

        // Update the ticketstatus using partial update
        Ticketstatus partialUpdatedTicketstatus = new Ticketstatus();
        partialUpdatedTicketstatus.setId(ticketstatus.getId());

        partialUpdatedTicketstatus.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restTicketstatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTicketstatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTicketstatus))
            )
            .andExpect(status().isOk());

        // Validate the Ticketstatus in the database
        List<Ticketstatus> ticketstatusList = ticketstatusRepository.findAll();
        assertThat(ticketstatusList).hasSize(databaseSizeBeforeUpdate);
        Ticketstatus testTicketstatus = ticketstatusList.get(ticketstatusList.size() - 1);
        assertThat(testTicketstatus.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTicketstatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateTicketstatusWithPatch() throws Exception {
        // Initialize the database
        ticketstatusRepository.saveAndFlush(ticketstatus);

        int databaseSizeBeforeUpdate = ticketstatusRepository.findAll().size();

        // Update the ticketstatus using partial update
        Ticketstatus partialUpdatedTicketstatus = new Ticketstatus();
        partialUpdatedTicketstatus.setId(ticketstatus.getId());

        partialUpdatedTicketstatus.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restTicketstatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTicketstatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTicketstatus))
            )
            .andExpect(status().isOk());

        // Validate the Ticketstatus in the database
        List<Ticketstatus> ticketstatusList = ticketstatusRepository.findAll();
        assertThat(ticketstatusList).hasSize(databaseSizeBeforeUpdate);
        Ticketstatus testTicketstatus = ticketstatusList.get(ticketstatusList.size() - 1);
        assertThat(testTicketstatus.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTicketstatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingTicketstatus() throws Exception {
        int databaseSizeBeforeUpdate = ticketstatusRepository.findAll().size();
        ticketstatus.setId(count.incrementAndGet());

        // Create the Ticketstatus
        TicketstatusDTO ticketstatusDTO = ticketstatusMapper.toDto(ticketstatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTicketstatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ticketstatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ticketstatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticketstatus in the database
        List<Ticketstatus> ticketstatusList = ticketstatusRepository.findAll();
        assertThat(ticketstatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTicketstatus() throws Exception {
        int databaseSizeBeforeUpdate = ticketstatusRepository.findAll().size();
        ticketstatus.setId(count.incrementAndGet());

        // Create the Ticketstatus
        TicketstatusDTO ticketstatusDTO = ticketstatusMapper.toDto(ticketstatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketstatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ticketstatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticketstatus in the database
        List<Ticketstatus> ticketstatusList = ticketstatusRepository.findAll();
        assertThat(ticketstatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTicketstatus() throws Exception {
        int databaseSizeBeforeUpdate = ticketstatusRepository.findAll().size();
        ticketstatus.setId(count.incrementAndGet());

        // Create the Ticketstatus
        TicketstatusDTO ticketstatusDTO = ticketstatusMapper.toDto(ticketstatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketstatusMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ticketstatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ticketstatus in the database
        List<Ticketstatus> ticketstatusList = ticketstatusRepository.findAll();
        assertThat(ticketstatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTicketstatus() throws Exception {
        // Initialize the database
        ticketstatusRepository.saveAndFlush(ticketstatus);

        int databaseSizeBeforeDelete = ticketstatusRepository.findAll().size();

        // Delete the ticketstatus
        restTicketstatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, ticketstatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ticketstatus> ticketstatusList = ticketstatusRepository.findAll();
        assertThat(ticketstatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
