package com.atlas.ir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.atlas.ir.IntegrationTest;
import com.atlas.ir.domain.Tickethistory;
import com.atlas.ir.repository.TickethistoryRepository;
import com.atlas.ir.service.criteria.TickethistoryCriteria;
import com.atlas.ir.service.dto.TickethistoryDTO;
import com.atlas.ir.service.mapper.TickethistoryMapper;
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
 * Integration tests for the {@link TickethistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TickethistoryResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/tickethistories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TickethistoryRepository tickethistoryRepository;

    @Autowired
    private TickethistoryMapper tickethistoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTickethistoryMockMvc;

    private Tickethistory tickethistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tickethistory createEntity(EntityManager em) {
        Tickethistory tickethistory = new Tickethistory().description(DEFAULT_DESCRIPTION).date(DEFAULT_DATE);
        return tickethistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tickethistory createUpdatedEntity(EntityManager em) {
        Tickethistory tickethistory = new Tickethistory().description(UPDATED_DESCRIPTION).date(UPDATED_DATE);
        return tickethistory;
    }

    @BeforeEach
    public void initTest() {
        tickethistory = createEntity(em);
    }

    @Test
    @Transactional
    void createTickethistory() throws Exception {
        int databaseSizeBeforeCreate = tickethistoryRepository.findAll().size();
        // Create the Tickethistory
        TickethistoryDTO tickethistoryDTO = tickethistoryMapper.toDto(tickethistory);
        restTickethistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tickethistoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Tickethistory in the database
        List<Tickethistory> tickethistoryList = tickethistoryRepository.findAll();
        assertThat(tickethistoryList).hasSize(databaseSizeBeforeCreate + 1);
        Tickethistory testTickethistory = tickethistoryList.get(tickethistoryList.size() - 1);
        assertThat(testTickethistory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTickethistory.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createTickethistoryWithExistingId() throws Exception {
        // Create the Tickethistory with an existing ID
        tickethistory.setId(1L);
        TickethistoryDTO tickethistoryDTO = tickethistoryMapper.toDto(tickethistory);

        int databaseSizeBeforeCreate = tickethistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTickethistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tickethistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tickethistory in the database
        List<Tickethistory> tickethistoryList = tickethistoryRepository.findAll();
        assertThat(tickethistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTickethistories() throws Exception {
        // Initialize the database
        tickethistoryRepository.saveAndFlush(tickethistory);

        // Get all the tickethistoryList
        restTickethistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tickethistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getTickethistory() throws Exception {
        // Initialize the database
        tickethistoryRepository.saveAndFlush(tickethistory);

        // Get the tickethistory
        restTickethistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, tickethistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tickethistory.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getTickethistoriesByIdFiltering() throws Exception {
        // Initialize the database
        tickethistoryRepository.saveAndFlush(tickethistory);

        Long id = tickethistory.getId();

        defaultTickethistoryShouldBeFound("id.equals=" + id);
        defaultTickethistoryShouldNotBeFound("id.notEquals=" + id);

        defaultTickethistoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTickethistoryShouldNotBeFound("id.greaterThan=" + id);

        defaultTickethistoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTickethistoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTickethistoriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        tickethistoryRepository.saveAndFlush(tickethistory);

        // Get all the tickethistoryList where description equals to DEFAULT_DESCRIPTION
        defaultTickethistoryShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the tickethistoryList where description equals to UPDATED_DESCRIPTION
        defaultTickethistoryShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTickethistoriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        tickethistoryRepository.saveAndFlush(tickethistory);

        // Get all the tickethistoryList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTickethistoryShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the tickethistoryList where description equals to UPDATED_DESCRIPTION
        defaultTickethistoryShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTickethistoriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        tickethistoryRepository.saveAndFlush(tickethistory);

        // Get all the tickethistoryList where description is not null
        defaultTickethistoryShouldBeFound("description.specified=true");

        // Get all the tickethistoryList where description is null
        defaultTickethistoryShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllTickethistoriesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        tickethistoryRepository.saveAndFlush(tickethistory);

        // Get all the tickethistoryList where description contains DEFAULT_DESCRIPTION
        defaultTickethistoryShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the tickethistoryList where description contains UPDATED_DESCRIPTION
        defaultTickethistoryShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTickethistoriesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        tickethistoryRepository.saveAndFlush(tickethistory);

        // Get all the tickethistoryList where description does not contain DEFAULT_DESCRIPTION
        defaultTickethistoryShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the tickethistoryList where description does not contain UPDATED_DESCRIPTION
        defaultTickethistoryShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTickethistoriesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        tickethistoryRepository.saveAndFlush(tickethistory);

        // Get all the tickethistoryList where date equals to DEFAULT_DATE
        defaultTickethistoryShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the tickethistoryList where date equals to UPDATED_DATE
        defaultTickethistoryShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTickethistoriesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        tickethistoryRepository.saveAndFlush(tickethistory);

        // Get all the tickethistoryList where date in DEFAULT_DATE or UPDATED_DATE
        defaultTickethistoryShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the tickethistoryList where date equals to UPDATED_DATE
        defaultTickethistoryShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTickethistoriesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        tickethistoryRepository.saveAndFlush(tickethistory);

        // Get all the tickethistoryList where date is not null
        defaultTickethistoryShouldBeFound("date.specified=true");

        // Get all the tickethistoryList where date is null
        defaultTickethistoryShouldNotBeFound("date.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTickethistoryShouldBeFound(String filter) throws Exception {
        restTickethistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tickethistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restTickethistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTickethistoryShouldNotBeFound(String filter) throws Exception {
        restTickethistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTickethistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTickethistory() throws Exception {
        // Get the tickethistory
        restTickethistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTickethistory() throws Exception {
        // Initialize the database
        tickethistoryRepository.saveAndFlush(tickethistory);

        int databaseSizeBeforeUpdate = tickethistoryRepository.findAll().size();

        // Update the tickethistory
        Tickethistory updatedTickethistory = tickethistoryRepository.findById(tickethistory.getId()).get();
        // Disconnect from session so that the updates on updatedTickethistory are not directly saved in db
        em.detach(updatedTickethistory);
        updatedTickethistory.description(UPDATED_DESCRIPTION).date(UPDATED_DATE);
        TickethistoryDTO tickethistoryDTO = tickethistoryMapper.toDto(updatedTickethistory);

        restTickethistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tickethistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tickethistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tickethistory in the database
        List<Tickethistory> tickethistoryList = tickethistoryRepository.findAll();
        assertThat(tickethistoryList).hasSize(databaseSizeBeforeUpdate);
        Tickethistory testTickethistory = tickethistoryList.get(tickethistoryList.size() - 1);
        assertThat(testTickethistory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTickethistory.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingTickethistory() throws Exception {
        int databaseSizeBeforeUpdate = tickethistoryRepository.findAll().size();
        tickethistory.setId(count.incrementAndGet());

        // Create the Tickethistory
        TickethistoryDTO tickethistoryDTO = tickethistoryMapper.toDto(tickethistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTickethistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tickethistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tickethistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tickethistory in the database
        List<Tickethistory> tickethistoryList = tickethistoryRepository.findAll();
        assertThat(tickethistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTickethistory() throws Exception {
        int databaseSizeBeforeUpdate = tickethistoryRepository.findAll().size();
        tickethistory.setId(count.incrementAndGet());

        // Create the Tickethistory
        TickethistoryDTO tickethistoryDTO = tickethistoryMapper.toDto(tickethistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTickethistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tickethistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tickethistory in the database
        List<Tickethistory> tickethistoryList = tickethistoryRepository.findAll();
        assertThat(tickethistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTickethistory() throws Exception {
        int databaseSizeBeforeUpdate = tickethistoryRepository.findAll().size();
        tickethistory.setId(count.incrementAndGet());

        // Create the Tickethistory
        TickethistoryDTO tickethistoryDTO = tickethistoryMapper.toDto(tickethistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTickethistoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tickethistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tickethistory in the database
        List<Tickethistory> tickethistoryList = tickethistoryRepository.findAll();
        assertThat(tickethistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTickethistoryWithPatch() throws Exception {
        // Initialize the database
        tickethistoryRepository.saveAndFlush(tickethistory);

        int databaseSizeBeforeUpdate = tickethistoryRepository.findAll().size();

        // Update the tickethistory using partial update
        Tickethistory partialUpdatedTickethistory = new Tickethistory();
        partialUpdatedTickethistory.setId(tickethistory.getId());

        partialUpdatedTickethistory.description(UPDATED_DESCRIPTION).date(UPDATED_DATE);

        restTickethistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTickethistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTickethistory))
            )
            .andExpect(status().isOk());

        // Validate the Tickethistory in the database
        List<Tickethistory> tickethistoryList = tickethistoryRepository.findAll();
        assertThat(tickethistoryList).hasSize(databaseSizeBeforeUpdate);
        Tickethistory testTickethistory = tickethistoryList.get(tickethistoryList.size() - 1);
        assertThat(testTickethistory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTickethistory.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateTickethistoryWithPatch() throws Exception {
        // Initialize the database
        tickethistoryRepository.saveAndFlush(tickethistory);

        int databaseSizeBeforeUpdate = tickethistoryRepository.findAll().size();

        // Update the tickethistory using partial update
        Tickethistory partialUpdatedTickethistory = new Tickethistory();
        partialUpdatedTickethistory.setId(tickethistory.getId());

        partialUpdatedTickethistory.description(UPDATED_DESCRIPTION).date(UPDATED_DATE);

        restTickethistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTickethistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTickethistory))
            )
            .andExpect(status().isOk());

        // Validate the Tickethistory in the database
        List<Tickethistory> tickethistoryList = tickethistoryRepository.findAll();
        assertThat(tickethistoryList).hasSize(databaseSizeBeforeUpdate);
        Tickethistory testTickethistory = tickethistoryList.get(tickethistoryList.size() - 1);
        assertThat(testTickethistory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTickethistory.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingTickethistory() throws Exception {
        int databaseSizeBeforeUpdate = tickethistoryRepository.findAll().size();
        tickethistory.setId(count.incrementAndGet());

        // Create the Tickethistory
        TickethistoryDTO tickethistoryDTO = tickethistoryMapper.toDto(tickethistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTickethistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tickethistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tickethistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tickethistory in the database
        List<Tickethistory> tickethistoryList = tickethistoryRepository.findAll();
        assertThat(tickethistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTickethistory() throws Exception {
        int databaseSizeBeforeUpdate = tickethistoryRepository.findAll().size();
        tickethistory.setId(count.incrementAndGet());

        // Create the Tickethistory
        TickethistoryDTO tickethistoryDTO = tickethistoryMapper.toDto(tickethistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTickethistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tickethistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tickethistory in the database
        List<Tickethistory> tickethistoryList = tickethistoryRepository.findAll();
        assertThat(tickethistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTickethistory() throws Exception {
        int databaseSizeBeforeUpdate = tickethistoryRepository.findAll().size();
        tickethistory.setId(count.incrementAndGet());

        // Create the Tickethistory
        TickethistoryDTO tickethistoryDTO = tickethistoryMapper.toDto(tickethistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTickethistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tickethistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tickethistory in the database
        List<Tickethistory> tickethistoryList = tickethistoryRepository.findAll();
        assertThat(tickethistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTickethistory() throws Exception {
        // Initialize the database
        tickethistoryRepository.saveAndFlush(tickethistory);

        int databaseSizeBeforeDelete = tickethistoryRepository.findAll().size();

        // Delete the tickethistory
        restTickethistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, tickethistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tickethistory> tickethistoryList = tickethistoryRepository.findAll();
        assertThat(tickethistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
