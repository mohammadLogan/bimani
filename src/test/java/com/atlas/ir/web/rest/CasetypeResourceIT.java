package com.atlas.ir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.atlas.ir.IntegrationTest;
import com.atlas.ir.domain.Casetype;
import com.atlas.ir.repository.CasetypeRepository;
import com.atlas.ir.service.criteria.CasetypeCriteria;
import com.atlas.ir.service.dto.CasetypeDTO;
import com.atlas.ir.service.mapper.CasetypeMapper;
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
 * Integration tests for the {@link CasetypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CasetypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PARENTTYPE = "AAAAAAAAAA";
    private static final String UPDATED_PARENTTYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/casetypes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CasetypeRepository casetypeRepository;

    @Autowired
    private CasetypeMapper casetypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCasetypeMockMvc;

    private Casetype casetype;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Casetype createEntity(EntityManager em) {
        Casetype casetype = new Casetype().name(DEFAULT_NAME).parenttype(DEFAULT_PARENTTYPE);
        return casetype;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Casetype createUpdatedEntity(EntityManager em) {
        Casetype casetype = new Casetype().name(UPDATED_NAME).parenttype(UPDATED_PARENTTYPE);
        return casetype;
    }

    @BeforeEach
    public void initTest() {
        casetype = createEntity(em);
    }

    @Test
    @Transactional
    void createCasetype() throws Exception {
        int databaseSizeBeforeCreate = casetypeRepository.findAll().size();
        // Create the Casetype
        CasetypeDTO casetypeDTO = casetypeMapper.toDto(casetype);
        restCasetypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(casetypeDTO)))
            .andExpect(status().isCreated());

        // Validate the Casetype in the database
        List<Casetype> casetypeList = casetypeRepository.findAll();
        assertThat(casetypeList).hasSize(databaseSizeBeforeCreate + 1);
        Casetype testCasetype = casetypeList.get(casetypeList.size() - 1);
        assertThat(testCasetype.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCasetype.getParenttype()).isEqualTo(DEFAULT_PARENTTYPE);
    }

    @Test
    @Transactional
    void createCasetypeWithExistingId() throws Exception {
        // Create the Casetype with an existing ID
        casetype.setId(1L);
        CasetypeDTO casetypeDTO = casetypeMapper.toDto(casetype);

        int databaseSizeBeforeCreate = casetypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCasetypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(casetypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Casetype in the database
        List<Casetype> casetypeList = casetypeRepository.findAll();
        assertThat(casetypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCasetypes() throws Exception {
        // Initialize the database
        casetypeRepository.saveAndFlush(casetype);

        // Get all the casetypeList
        restCasetypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(casetype.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].parenttype").value(hasItem(DEFAULT_PARENTTYPE)));
    }

    @Test
    @Transactional
    void getCasetype() throws Exception {
        // Initialize the database
        casetypeRepository.saveAndFlush(casetype);

        // Get the casetype
        restCasetypeMockMvc
            .perform(get(ENTITY_API_URL_ID, casetype.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(casetype.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.parenttype").value(DEFAULT_PARENTTYPE));
    }

    @Test
    @Transactional
    void getCasetypesByIdFiltering() throws Exception {
        // Initialize the database
        casetypeRepository.saveAndFlush(casetype);

        Long id = casetype.getId();

        defaultCasetypeShouldBeFound("id.equals=" + id);
        defaultCasetypeShouldNotBeFound("id.notEquals=" + id);

        defaultCasetypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCasetypeShouldNotBeFound("id.greaterThan=" + id);

        defaultCasetypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCasetypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCasetypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        casetypeRepository.saveAndFlush(casetype);

        // Get all the casetypeList where name equals to DEFAULT_NAME
        defaultCasetypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the casetypeList where name equals to UPDATED_NAME
        defaultCasetypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCasetypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        casetypeRepository.saveAndFlush(casetype);

        // Get all the casetypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCasetypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the casetypeList where name equals to UPDATED_NAME
        defaultCasetypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCasetypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        casetypeRepository.saveAndFlush(casetype);

        // Get all the casetypeList where name is not null
        defaultCasetypeShouldBeFound("name.specified=true");

        // Get all the casetypeList where name is null
        defaultCasetypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCasetypesByNameContainsSomething() throws Exception {
        // Initialize the database
        casetypeRepository.saveAndFlush(casetype);

        // Get all the casetypeList where name contains DEFAULT_NAME
        defaultCasetypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the casetypeList where name contains UPDATED_NAME
        defaultCasetypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCasetypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        casetypeRepository.saveAndFlush(casetype);

        // Get all the casetypeList where name does not contain DEFAULT_NAME
        defaultCasetypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the casetypeList where name does not contain UPDATED_NAME
        defaultCasetypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCasetypesByParenttypeIsEqualToSomething() throws Exception {
        // Initialize the database
        casetypeRepository.saveAndFlush(casetype);

        // Get all the casetypeList where parenttype equals to DEFAULT_PARENTTYPE
        defaultCasetypeShouldBeFound("parenttype.equals=" + DEFAULT_PARENTTYPE);

        // Get all the casetypeList where parenttype equals to UPDATED_PARENTTYPE
        defaultCasetypeShouldNotBeFound("parenttype.equals=" + UPDATED_PARENTTYPE);
    }

    @Test
    @Transactional
    void getAllCasetypesByParenttypeIsInShouldWork() throws Exception {
        // Initialize the database
        casetypeRepository.saveAndFlush(casetype);

        // Get all the casetypeList where parenttype in DEFAULT_PARENTTYPE or UPDATED_PARENTTYPE
        defaultCasetypeShouldBeFound("parenttype.in=" + DEFAULT_PARENTTYPE + "," + UPDATED_PARENTTYPE);

        // Get all the casetypeList where parenttype equals to UPDATED_PARENTTYPE
        defaultCasetypeShouldNotBeFound("parenttype.in=" + UPDATED_PARENTTYPE);
    }

    @Test
    @Transactional
    void getAllCasetypesByParenttypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        casetypeRepository.saveAndFlush(casetype);

        // Get all the casetypeList where parenttype is not null
        defaultCasetypeShouldBeFound("parenttype.specified=true");

        // Get all the casetypeList where parenttype is null
        defaultCasetypeShouldNotBeFound("parenttype.specified=false");
    }

    @Test
    @Transactional
    void getAllCasetypesByParenttypeContainsSomething() throws Exception {
        // Initialize the database
        casetypeRepository.saveAndFlush(casetype);

        // Get all the casetypeList where parenttype contains DEFAULT_PARENTTYPE
        defaultCasetypeShouldBeFound("parenttype.contains=" + DEFAULT_PARENTTYPE);

        // Get all the casetypeList where parenttype contains UPDATED_PARENTTYPE
        defaultCasetypeShouldNotBeFound("parenttype.contains=" + UPDATED_PARENTTYPE);
    }

    @Test
    @Transactional
    void getAllCasetypesByParenttypeNotContainsSomething() throws Exception {
        // Initialize the database
        casetypeRepository.saveAndFlush(casetype);

        // Get all the casetypeList where parenttype does not contain DEFAULT_PARENTTYPE
        defaultCasetypeShouldNotBeFound("parenttype.doesNotContain=" + DEFAULT_PARENTTYPE);

        // Get all the casetypeList where parenttype does not contain UPDATED_PARENTTYPE
        defaultCasetypeShouldBeFound("parenttype.doesNotContain=" + UPDATED_PARENTTYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCasetypeShouldBeFound(String filter) throws Exception {
        restCasetypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(casetype.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].parenttype").value(hasItem(DEFAULT_PARENTTYPE)));

        // Check, that the count call also returns 1
        restCasetypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCasetypeShouldNotBeFound(String filter) throws Exception {
        restCasetypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCasetypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCasetype() throws Exception {
        // Get the casetype
        restCasetypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCasetype() throws Exception {
        // Initialize the database
        casetypeRepository.saveAndFlush(casetype);

        int databaseSizeBeforeUpdate = casetypeRepository.findAll().size();

        // Update the casetype
        Casetype updatedCasetype = casetypeRepository.findById(casetype.getId()).get();
        // Disconnect from session so that the updates on updatedCasetype are not directly saved in db
        em.detach(updatedCasetype);
        updatedCasetype.name(UPDATED_NAME).parenttype(UPDATED_PARENTTYPE);
        CasetypeDTO casetypeDTO = casetypeMapper.toDto(updatedCasetype);

        restCasetypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, casetypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(casetypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Casetype in the database
        List<Casetype> casetypeList = casetypeRepository.findAll();
        assertThat(casetypeList).hasSize(databaseSizeBeforeUpdate);
        Casetype testCasetype = casetypeList.get(casetypeList.size() - 1);
        assertThat(testCasetype.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCasetype.getParenttype()).isEqualTo(UPDATED_PARENTTYPE);
    }

    @Test
    @Transactional
    void putNonExistingCasetype() throws Exception {
        int databaseSizeBeforeUpdate = casetypeRepository.findAll().size();
        casetype.setId(count.incrementAndGet());

        // Create the Casetype
        CasetypeDTO casetypeDTO = casetypeMapper.toDto(casetype);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCasetypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, casetypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(casetypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Casetype in the database
        List<Casetype> casetypeList = casetypeRepository.findAll();
        assertThat(casetypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCasetype() throws Exception {
        int databaseSizeBeforeUpdate = casetypeRepository.findAll().size();
        casetype.setId(count.incrementAndGet());

        // Create the Casetype
        CasetypeDTO casetypeDTO = casetypeMapper.toDto(casetype);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasetypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(casetypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Casetype in the database
        List<Casetype> casetypeList = casetypeRepository.findAll();
        assertThat(casetypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCasetype() throws Exception {
        int databaseSizeBeforeUpdate = casetypeRepository.findAll().size();
        casetype.setId(count.incrementAndGet());

        // Create the Casetype
        CasetypeDTO casetypeDTO = casetypeMapper.toDto(casetype);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasetypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(casetypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Casetype in the database
        List<Casetype> casetypeList = casetypeRepository.findAll();
        assertThat(casetypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCasetypeWithPatch() throws Exception {
        // Initialize the database
        casetypeRepository.saveAndFlush(casetype);

        int databaseSizeBeforeUpdate = casetypeRepository.findAll().size();

        // Update the casetype using partial update
        Casetype partialUpdatedCasetype = new Casetype();
        partialUpdatedCasetype.setId(casetype.getId());

        partialUpdatedCasetype.name(UPDATED_NAME);

        restCasetypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCasetype.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCasetype))
            )
            .andExpect(status().isOk());

        // Validate the Casetype in the database
        List<Casetype> casetypeList = casetypeRepository.findAll();
        assertThat(casetypeList).hasSize(databaseSizeBeforeUpdate);
        Casetype testCasetype = casetypeList.get(casetypeList.size() - 1);
        assertThat(testCasetype.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCasetype.getParenttype()).isEqualTo(DEFAULT_PARENTTYPE);
    }

    @Test
    @Transactional
    void fullUpdateCasetypeWithPatch() throws Exception {
        // Initialize the database
        casetypeRepository.saveAndFlush(casetype);

        int databaseSizeBeforeUpdate = casetypeRepository.findAll().size();

        // Update the casetype using partial update
        Casetype partialUpdatedCasetype = new Casetype();
        partialUpdatedCasetype.setId(casetype.getId());

        partialUpdatedCasetype.name(UPDATED_NAME).parenttype(UPDATED_PARENTTYPE);

        restCasetypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCasetype.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCasetype))
            )
            .andExpect(status().isOk());

        // Validate the Casetype in the database
        List<Casetype> casetypeList = casetypeRepository.findAll();
        assertThat(casetypeList).hasSize(databaseSizeBeforeUpdate);
        Casetype testCasetype = casetypeList.get(casetypeList.size() - 1);
        assertThat(testCasetype.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCasetype.getParenttype()).isEqualTo(UPDATED_PARENTTYPE);
    }

    @Test
    @Transactional
    void patchNonExistingCasetype() throws Exception {
        int databaseSizeBeforeUpdate = casetypeRepository.findAll().size();
        casetype.setId(count.incrementAndGet());

        // Create the Casetype
        CasetypeDTO casetypeDTO = casetypeMapper.toDto(casetype);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCasetypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, casetypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(casetypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Casetype in the database
        List<Casetype> casetypeList = casetypeRepository.findAll();
        assertThat(casetypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCasetype() throws Exception {
        int databaseSizeBeforeUpdate = casetypeRepository.findAll().size();
        casetype.setId(count.incrementAndGet());

        // Create the Casetype
        CasetypeDTO casetypeDTO = casetypeMapper.toDto(casetype);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasetypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(casetypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Casetype in the database
        List<Casetype> casetypeList = casetypeRepository.findAll();
        assertThat(casetypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCasetype() throws Exception {
        int databaseSizeBeforeUpdate = casetypeRepository.findAll().size();
        casetype.setId(count.incrementAndGet());

        // Create the Casetype
        CasetypeDTO casetypeDTO = casetypeMapper.toDto(casetype);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasetypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(casetypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Casetype in the database
        List<Casetype> casetypeList = casetypeRepository.findAll();
        assertThat(casetypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCasetype() throws Exception {
        // Initialize the database
        casetypeRepository.saveAndFlush(casetype);

        int databaseSizeBeforeDelete = casetypeRepository.findAll().size();

        // Delete the casetype
        restCasetypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, casetype.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Casetype> casetypeList = casetypeRepository.findAll();
        assertThat(casetypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
