package com.atlas.ir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.atlas.ir.IntegrationTest;
import com.atlas.ir.domain.Addresstype;
import com.atlas.ir.repository.AddresstypeRepository;
import com.atlas.ir.service.criteria.AddresstypeCriteria;
import com.atlas.ir.service.dto.AddresstypeDTO;
import com.atlas.ir.service.mapper.AddresstypeMapper;
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
 * Integration tests for the {@link AddresstypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AddresstypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/addresstypes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AddresstypeRepository addresstypeRepository;

    @Autowired
    private AddresstypeMapper addresstypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAddresstypeMockMvc;

    private Addresstype addresstype;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Addresstype createEntity(EntityManager em) {
        Addresstype addresstype = new Addresstype().name(DEFAULT_NAME);
        return addresstype;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Addresstype createUpdatedEntity(EntityManager em) {
        Addresstype addresstype = new Addresstype().name(UPDATED_NAME);
        return addresstype;
    }

    @BeforeEach
    public void initTest() {
        addresstype = createEntity(em);
    }

    @Test
    @Transactional
    void createAddresstype() throws Exception {
        int databaseSizeBeforeCreate = addresstypeRepository.findAll().size();
        // Create the Addresstype
        AddresstypeDTO addresstypeDTO = addresstypeMapper.toDto(addresstype);
        restAddresstypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addresstypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Addresstype in the database
        List<Addresstype> addresstypeList = addresstypeRepository.findAll();
        assertThat(addresstypeList).hasSize(databaseSizeBeforeCreate + 1);
        Addresstype testAddresstype = addresstypeList.get(addresstypeList.size() - 1);
        assertThat(testAddresstype.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createAddresstypeWithExistingId() throws Exception {
        // Create the Addresstype with an existing ID
        addresstype.setId(1L);
        AddresstypeDTO addresstypeDTO = addresstypeMapper.toDto(addresstype);

        int databaseSizeBeforeCreate = addresstypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAddresstypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addresstypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Addresstype in the database
        List<Addresstype> addresstypeList = addresstypeRepository.findAll();
        assertThat(addresstypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAddresstypes() throws Exception {
        // Initialize the database
        addresstypeRepository.saveAndFlush(addresstype);

        // Get all the addresstypeList
        restAddresstypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(addresstype.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getAddresstype() throws Exception {
        // Initialize the database
        addresstypeRepository.saveAndFlush(addresstype);

        // Get the addresstype
        restAddresstypeMockMvc
            .perform(get(ENTITY_API_URL_ID, addresstype.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(addresstype.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getAddresstypesByIdFiltering() throws Exception {
        // Initialize the database
        addresstypeRepository.saveAndFlush(addresstype);

        Long id = addresstype.getId();

        defaultAddresstypeShouldBeFound("id.equals=" + id);
        defaultAddresstypeShouldNotBeFound("id.notEquals=" + id);

        defaultAddresstypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAddresstypeShouldNotBeFound("id.greaterThan=" + id);

        defaultAddresstypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAddresstypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAddresstypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        addresstypeRepository.saveAndFlush(addresstype);

        // Get all the addresstypeList where name equals to DEFAULT_NAME
        defaultAddresstypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the addresstypeList where name equals to UPDATED_NAME
        defaultAddresstypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAddresstypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        addresstypeRepository.saveAndFlush(addresstype);

        // Get all the addresstypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAddresstypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the addresstypeList where name equals to UPDATED_NAME
        defaultAddresstypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAddresstypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        addresstypeRepository.saveAndFlush(addresstype);

        // Get all the addresstypeList where name is not null
        defaultAddresstypeShouldBeFound("name.specified=true");

        // Get all the addresstypeList where name is null
        defaultAddresstypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllAddresstypesByNameContainsSomething() throws Exception {
        // Initialize the database
        addresstypeRepository.saveAndFlush(addresstype);

        // Get all the addresstypeList where name contains DEFAULT_NAME
        defaultAddresstypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the addresstypeList where name contains UPDATED_NAME
        defaultAddresstypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAddresstypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        addresstypeRepository.saveAndFlush(addresstype);

        // Get all the addresstypeList where name does not contain DEFAULT_NAME
        defaultAddresstypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the addresstypeList where name does not contain UPDATED_NAME
        defaultAddresstypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAddresstypeShouldBeFound(String filter) throws Exception {
        restAddresstypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(addresstype.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restAddresstypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAddresstypeShouldNotBeFound(String filter) throws Exception {
        restAddresstypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAddresstypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAddresstype() throws Exception {
        // Get the addresstype
        restAddresstypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAddresstype() throws Exception {
        // Initialize the database
        addresstypeRepository.saveAndFlush(addresstype);

        int databaseSizeBeforeUpdate = addresstypeRepository.findAll().size();

        // Update the addresstype
        Addresstype updatedAddresstype = addresstypeRepository.findById(addresstype.getId()).get();
        // Disconnect from session so that the updates on updatedAddresstype are not directly saved in db
        em.detach(updatedAddresstype);
        updatedAddresstype.name(UPDATED_NAME);
        AddresstypeDTO addresstypeDTO = addresstypeMapper.toDto(updatedAddresstype);

        restAddresstypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, addresstypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addresstypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Addresstype in the database
        List<Addresstype> addresstypeList = addresstypeRepository.findAll();
        assertThat(addresstypeList).hasSize(databaseSizeBeforeUpdate);
        Addresstype testAddresstype = addresstypeList.get(addresstypeList.size() - 1);
        assertThat(testAddresstype.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingAddresstype() throws Exception {
        int databaseSizeBeforeUpdate = addresstypeRepository.findAll().size();
        addresstype.setId(count.incrementAndGet());

        // Create the Addresstype
        AddresstypeDTO addresstypeDTO = addresstypeMapper.toDto(addresstype);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddresstypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, addresstypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addresstypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Addresstype in the database
        List<Addresstype> addresstypeList = addresstypeRepository.findAll();
        assertThat(addresstypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAddresstype() throws Exception {
        int databaseSizeBeforeUpdate = addresstypeRepository.findAll().size();
        addresstype.setId(count.incrementAndGet());

        // Create the Addresstype
        AddresstypeDTO addresstypeDTO = addresstypeMapper.toDto(addresstype);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddresstypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addresstypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Addresstype in the database
        List<Addresstype> addresstypeList = addresstypeRepository.findAll();
        assertThat(addresstypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAddresstype() throws Exception {
        int databaseSizeBeforeUpdate = addresstypeRepository.findAll().size();
        addresstype.setId(count.incrementAndGet());

        // Create the Addresstype
        AddresstypeDTO addresstypeDTO = addresstypeMapper.toDto(addresstype);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddresstypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addresstypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Addresstype in the database
        List<Addresstype> addresstypeList = addresstypeRepository.findAll();
        assertThat(addresstypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAddresstypeWithPatch() throws Exception {
        // Initialize the database
        addresstypeRepository.saveAndFlush(addresstype);

        int databaseSizeBeforeUpdate = addresstypeRepository.findAll().size();

        // Update the addresstype using partial update
        Addresstype partialUpdatedAddresstype = new Addresstype();
        partialUpdatedAddresstype.setId(addresstype.getId());

        partialUpdatedAddresstype.name(UPDATED_NAME);

        restAddresstypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAddresstype.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddresstype))
            )
            .andExpect(status().isOk());

        // Validate the Addresstype in the database
        List<Addresstype> addresstypeList = addresstypeRepository.findAll();
        assertThat(addresstypeList).hasSize(databaseSizeBeforeUpdate);
        Addresstype testAddresstype = addresstypeList.get(addresstypeList.size() - 1);
        assertThat(testAddresstype.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateAddresstypeWithPatch() throws Exception {
        // Initialize the database
        addresstypeRepository.saveAndFlush(addresstype);

        int databaseSizeBeforeUpdate = addresstypeRepository.findAll().size();

        // Update the addresstype using partial update
        Addresstype partialUpdatedAddresstype = new Addresstype();
        partialUpdatedAddresstype.setId(addresstype.getId());

        partialUpdatedAddresstype.name(UPDATED_NAME);

        restAddresstypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAddresstype.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddresstype))
            )
            .andExpect(status().isOk());

        // Validate the Addresstype in the database
        List<Addresstype> addresstypeList = addresstypeRepository.findAll();
        assertThat(addresstypeList).hasSize(databaseSizeBeforeUpdate);
        Addresstype testAddresstype = addresstypeList.get(addresstypeList.size() - 1);
        assertThat(testAddresstype.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingAddresstype() throws Exception {
        int databaseSizeBeforeUpdate = addresstypeRepository.findAll().size();
        addresstype.setId(count.incrementAndGet());

        // Create the Addresstype
        AddresstypeDTO addresstypeDTO = addresstypeMapper.toDto(addresstype);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddresstypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, addresstypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(addresstypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Addresstype in the database
        List<Addresstype> addresstypeList = addresstypeRepository.findAll();
        assertThat(addresstypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAddresstype() throws Exception {
        int databaseSizeBeforeUpdate = addresstypeRepository.findAll().size();
        addresstype.setId(count.incrementAndGet());

        // Create the Addresstype
        AddresstypeDTO addresstypeDTO = addresstypeMapper.toDto(addresstype);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddresstypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(addresstypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Addresstype in the database
        List<Addresstype> addresstypeList = addresstypeRepository.findAll();
        assertThat(addresstypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAddresstype() throws Exception {
        int databaseSizeBeforeUpdate = addresstypeRepository.findAll().size();
        addresstype.setId(count.incrementAndGet());

        // Create the Addresstype
        AddresstypeDTO addresstypeDTO = addresstypeMapper.toDto(addresstype);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddresstypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(addresstypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Addresstype in the database
        List<Addresstype> addresstypeList = addresstypeRepository.findAll();
        assertThat(addresstypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAddresstype() throws Exception {
        // Initialize the database
        addresstypeRepository.saveAndFlush(addresstype);

        int databaseSizeBeforeDelete = addresstypeRepository.findAll().size();

        // Delete the addresstype
        restAddresstypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, addresstype.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Addresstype> addresstypeList = addresstypeRepository.findAll();
        assertThat(addresstypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
