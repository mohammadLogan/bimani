package com.atlas.ir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.atlas.ir.IntegrationTest;
import com.atlas.ir.domain.Phone;
import com.atlas.ir.repository.PhoneRepository;
import com.atlas.ir.service.criteria.PhoneCriteria;
import com.atlas.ir.service.dto.PhoneDTO;
import com.atlas.ir.service.mapper.PhoneMapper;
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
 * Integration tests for the {@link PhoneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PhoneResourceIT {

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PHONETYPE = "AAAAAAAAAA";
    private static final String UPDATED_PHONETYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/phones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private PhoneMapper phoneMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPhoneMockMvc;

    private Phone phone;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Phone createEntity(EntityManager em) {
        Phone phone = new Phone().number(DEFAULT_NUMBER).phonetype(DEFAULT_PHONETYPE);
        return phone;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Phone createUpdatedEntity(EntityManager em) {
        Phone phone = new Phone().number(UPDATED_NUMBER).phonetype(UPDATED_PHONETYPE);
        return phone;
    }

    @BeforeEach
    public void initTest() {
        phone = createEntity(em);
    }

    @Test
    @Transactional
    void createPhone() throws Exception {
        int databaseSizeBeforeCreate = phoneRepository.findAll().size();
        // Create the Phone
        PhoneDTO phoneDTO = phoneMapper.toDto(phone);
        restPhoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phoneDTO)))
            .andExpect(status().isCreated());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeCreate + 1);
        Phone testPhone = phoneList.get(phoneList.size() - 1);
        assertThat(testPhone.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testPhone.getPhonetype()).isEqualTo(DEFAULT_PHONETYPE);
    }

    @Test
    @Transactional
    void createPhoneWithExistingId() throws Exception {
        // Create the Phone with an existing ID
        phone.setId(1L);
        PhoneDTO phoneDTO = phoneMapper.toDto(phone);

        int databaseSizeBeforeCreate = phoneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phoneDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPhones() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList
        restPhoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(phone.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].phonetype").value(hasItem(DEFAULT_PHONETYPE)));
    }

    @Test
    @Transactional
    void getPhone() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get the phone
        restPhoneMockMvc
            .perform(get(ENTITY_API_URL_ID, phone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(phone.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.phonetype").value(DEFAULT_PHONETYPE));
    }

    @Test
    @Transactional
    void getPhonesByIdFiltering() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        Long id = phone.getId();

        defaultPhoneShouldBeFound("id.equals=" + id);
        defaultPhoneShouldNotBeFound("id.notEquals=" + id);

        defaultPhoneShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPhoneShouldNotBeFound("id.greaterThan=" + id);

        defaultPhoneShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPhoneShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPhonesByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where number equals to DEFAULT_NUMBER
        defaultPhoneShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the phoneList where number equals to UPDATED_NUMBER
        defaultPhoneShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllPhonesByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultPhoneShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the phoneList where number equals to UPDATED_NUMBER
        defaultPhoneShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllPhonesByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where number is not null
        defaultPhoneShouldBeFound("number.specified=true");

        // Get all the phoneList where number is null
        defaultPhoneShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    void getAllPhonesByNumberContainsSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where number contains DEFAULT_NUMBER
        defaultPhoneShouldBeFound("number.contains=" + DEFAULT_NUMBER);

        // Get all the phoneList where number contains UPDATED_NUMBER
        defaultPhoneShouldNotBeFound("number.contains=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllPhonesByNumberNotContainsSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where number does not contain DEFAULT_NUMBER
        defaultPhoneShouldNotBeFound("number.doesNotContain=" + DEFAULT_NUMBER);

        // Get all the phoneList where number does not contain UPDATED_NUMBER
        defaultPhoneShouldBeFound("number.doesNotContain=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllPhonesByPhonetypeIsEqualToSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where phonetype equals to DEFAULT_PHONETYPE
        defaultPhoneShouldBeFound("phonetype.equals=" + DEFAULT_PHONETYPE);

        // Get all the phoneList where phonetype equals to UPDATED_PHONETYPE
        defaultPhoneShouldNotBeFound("phonetype.equals=" + UPDATED_PHONETYPE);
    }

    @Test
    @Transactional
    void getAllPhonesByPhonetypeIsInShouldWork() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where phonetype in DEFAULT_PHONETYPE or UPDATED_PHONETYPE
        defaultPhoneShouldBeFound("phonetype.in=" + DEFAULT_PHONETYPE + "," + UPDATED_PHONETYPE);

        // Get all the phoneList where phonetype equals to UPDATED_PHONETYPE
        defaultPhoneShouldNotBeFound("phonetype.in=" + UPDATED_PHONETYPE);
    }

    @Test
    @Transactional
    void getAllPhonesByPhonetypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where phonetype is not null
        defaultPhoneShouldBeFound("phonetype.specified=true");

        // Get all the phoneList where phonetype is null
        defaultPhoneShouldNotBeFound("phonetype.specified=false");
    }

    @Test
    @Transactional
    void getAllPhonesByPhonetypeContainsSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where phonetype contains DEFAULT_PHONETYPE
        defaultPhoneShouldBeFound("phonetype.contains=" + DEFAULT_PHONETYPE);

        // Get all the phoneList where phonetype contains UPDATED_PHONETYPE
        defaultPhoneShouldNotBeFound("phonetype.contains=" + UPDATED_PHONETYPE);
    }

    @Test
    @Transactional
    void getAllPhonesByPhonetypeNotContainsSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where phonetype does not contain DEFAULT_PHONETYPE
        defaultPhoneShouldNotBeFound("phonetype.doesNotContain=" + DEFAULT_PHONETYPE);

        // Get all the phoneList where phonetype does not contain UPDATED_PHONETYPE
        defaultPhoneShouldBeFound("phonetype.doesNotContain=" + UPDATED_PHONETYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPhoneShouldBeFound(String filter) throws Exception {
        restPhoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(phone.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].phonetype").value(hasItem(DEFAULT_PHONETYPE)));

        // Check, that the count call also returns 1
        restPhoneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPhoneShouldNotBeFound(String filter) throws Exception {
        restPhoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPhoneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPhone() throws Exception {
        // Get the phone
        restPhoneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPhone() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        int databaseSizeBeforeUpdate = phoneRepository.findAll().size();

        // Update the phone
        Phone updatedPhone = phoneRepository.findById(phone.getId()).get();
        // Disconnect from session so that the updates on updatedPhone are not directly saved in db
        em.detach(updatedPhone);
        updatedPhone.number(UPDATED_NUMBER).phonetype(UPDATED_PHONETYPE);
        PhoneDTO phoneDTO = phoneMapper.toDto(updatedPhone);

        restPhoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, phoneDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phoneDTO))
            )
            .andExpect(status().isOk());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate);
        Phone testPhone = phoneList.get(phoneList.size() - 1);
        assertThat(testPhone.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPhone.getPhonetype()).isEqualTo(UPDATED_PHONETYPE);
    }

    @Test
    @Transactional
    void putNonExistingPhone() throws Exception {
        int databaseSizeBeforeUpdate = phoneRepository.findAll().size();
        phone.setId(count.incrementAndGet());

        // Create the Phone
        PhoneDTO phoneDTO = phoneMapper.toDto(phone);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, phoneDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPhone() throws Exception {
        int databaseSizeBeforeUpdate = phoneRepository.findAll().size();
        phone.setId(count.incrementAndGet());

        // Create the Phone
        PhoneDTO phoneDTO = phoneMapper.toDto(phone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPhone() throws Exception {
        int databaseSizeBeforeUpdate = phoneRepository.findAll().size();
        phone.setId(count.incrementAndGet());

        // Create the Phone
        PhoneDTO phoneDTO = phoneMapper.toDto(phone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phoneDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePhoneWithPatch() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        int databaseSizeBeforeUpdate = phoneRepository.findAll().size();

        // Update the phone using partial update
        Phone partialUpdatedPhone = new Phone();
        partialUpdatedPhone.setId(phone.getId());

        restPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhone))
            )
            .andExpect(status().isOk());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate);
        Phone testPhone = phoneList.get(phoneList.size() - 1);
        assertThat(testPhone.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testPhone.getPhonetype()).isEqualTo(DEFAULT_PHONETYPE);
    }

    @Test
    @Transactional
    void fullUpdatePhoneWithPatch() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        int databaseSizeBeforeUpdate = phoneRepository.findAll().size();

        // Update the phone using partial update
        Phone partialUpdatedPhone = new Phone();
        partialUpdatedPhone.setId(phone.getId());

        partialUpdatedPhone.number(UPDATED_NUMBER).phonetype(UPDATED_PHONETYPE);

        restPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhone))
            )
            .andExpect(status().isOk());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate);
        Phone testPhone = phoneList.get(phoneList.size() - 1);
        assertThat(testPhone.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPhone.getPhonetype()).isEqualTo(UPDATED_PHONETYPE);
    }

    @Test
    @Transactional
    void patchNonExistingPhone() throws Exception {
        int databaseSizeBeforeUpdate = phoneRepository.findAll().size();
        phone.setId(count.incrementAndGet());

        // Create the Phone
        PhoneDTO phoneDTO = phoneMapper.toDto(phone);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, phoneDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(phoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPhone() throws Exception {
        int databaseSizeBeforeUpdate = phoneRepository.findAll().size();
        phone.setId(count.incrementAndGet());

        // Create the Phone
        PhoneDTO phoneDTO = phoneMapper.toDto(phone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(phoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPhone() throws Exception {
        int databaseSizeBeforeUpdate = phoneRepository.findAll().size();
        phone.setId(count.incrementAndGet());

        // Create the Phone
        PhoneDTO phoneDTO = phoneMapper.toDto(phone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(phoneDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePhone() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        int databaseSizeBeforeDelete = phoneRepository.findAll().size();

        // Delete the phone
        restPhoneMockMvc
            .perform(delete(ENTITY_API_URL_ID, phone.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
