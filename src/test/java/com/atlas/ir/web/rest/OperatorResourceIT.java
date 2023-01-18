package com.atlas.ir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.atlas.ir.IntegrationTest;
import com.atlas.ir.domain.Operator;
import com.atlas.ir.domain.enumeration.Status;
import com.atlas.ir.repository.OperatorRepository;
import com.atlas.ir.service.criteria.OperatorCriteria;
import com.atlas.ir.service.dto.OperatorDTO;
import com.atlas.ir.service.mapper.OperatorMapper;
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
 * Integration tests for the {@link OperatorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OperatorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final Long DEFAULT_NATIONALID = 1L;
    private static final Long UPDATED_NATIONALID = 2L;
    private static final Long SMALLER_NATIONALID = 1L - 1L;

    private static final Instant DEFAULT_DATEOFBIRTH = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATEOFBIRTH = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Status DEFAULT_STATUS = Status.ONLINE;
    private static final Status UPDATED_STATUS = Status.OFFLINE;

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/operators";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private OperatorMapper operatorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOperatorMockMvc;

    private Operator operator;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operator createEntity(EntityManager em) {
        Operator operator = new Operator()
            .name(DEFAULT_NAME)
            .surname(DEFAULT_SURNAME)
            .nationalid(DEFAULT_NATIONALID)
            .dateofbirth(DEFAULT_DATEOFBIRTH)
            .status(DEFAULT_STATUS)
            .gender(DEFAULT_GENDER);
        return operator;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operator createUpdatedEntity(EntityManager em) {
        Operator operator = new Operator()
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .nationalid(UPDATED_NATIONALID)
            .dateofbirth(UPDATED_DATEOFBIRTH)
            .status(UPDATED_STATUS)
            .gender(UPDATED_GENDER);
        return operator;
    }

    @BeforeEach
    public void initTest() {
        operator = createEntity(em);
    }

    @Test
    @Transactional
    void createOperator() throws Exception {
        int databaseSizeBeforeCreate = operatorRepository.findAll().size();
        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);
        restOperatorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatorDTO)))
            .andExpect(status().isCreated());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeCreate + 1);
        Operator testOperator = operatorList.get(operatorList.size() - 1);
        assertThat(testOperator.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOperator.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testOperator.getNationalid()).isEqualTo(DEFAULT_NATIONALID);
        assertThat(testOperator.getDateofbirth()).isEqualTo(DEFAULT_DATEOFBIRTH);
        assertThat(testOperator.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOperator.getGender()).isEqualTo(DEFAULT_GENDER);
    }

    @Test
    @Transactional
    void createOperatorWithExistingId() throws Exception {
        // Create the Operator with an existing ID
        operator.setId(1L);
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        int databaseSizeBeforeCreate = operatorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperatorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOperators() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList
        restOperatorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operator.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].nationalid").value(hasItem(DEFAULT_NATIONALID.intValue())))
            .andExpect(jsonPath("$.[*].dateofbirth").value(hasItem(DEFAULT_DATEOFBIRTH.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)));
    }

    @Test
    @Transactional
    void getOperator() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get the operator
        restOperatorMockMvc
            .perform(get(ENTITY_API_URL_ID, operator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(operator.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME))
            .andExpect(jsonPath("$.nationalid").value(DEFAULT_NATIONALID.intValue()))
            .andExpect(jsonPath("$.dateofbirth").value(DEFAULT_DATEOFBIRTH.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER));
    }

    @Test
    @Transactional
    void getOperatorsByIdFiltering() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        Long id = operator.getId();

        defaultOperatorShouldBeFound("id.equals=" + id);
        defaultOperatorShouldNotBeFound("id.notEquals=" + id);

        defaultOperatorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOperatorShouldNotBeFound("id.greaterThan=" + id);

        defaultOperatorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOperatorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOperatorsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where name equals to DEFAULT_NAME
        defaultOperatorShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the operatorList where name equals to UPDATED_NAME
        defaultOperatorShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOperatorsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOperatorShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the operatorList where name equals to UPDATED_NAME
        defaultOperatorShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOperatorsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where name is not null
        defaultOperatorShouldBeFound("name.specified=true");

        // Get all the operatorList where name is null
        defaultOperatorShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllOperatorsByNameContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where name contains DEFAULT_NAME
        defaultOperatorShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the operatorList where name contains UPDATED_NAME
        defaultOperatorShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOperatorsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where name does not contain DEFAULT_NAME
        defaultOperatorShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the operatorList where name does not contain UPDATED_NAME
        defaultOperatorShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOperatorsBySurnameIsEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where surname equals to DEFAULT_SURNAME
        defaultOperatorShouldBeFound("surname.equals=" + DEFAULT_SURNAME);

        // Get all the operatorList where surname equals to UPDATED_SURNAME
        defaultOperatorShouldNotBeFound("surname.equals=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    void getAllOperatorsBySurnameIsInShouldWork() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where surname in DEFAULT_SURNAME or UPDATED_SURNAME
        defaultOperatorShouldBeFound("surname.in=" + DEFAULT_SURNAME + "," + UPDATED_SURNAME);

        // Get all the operatorList where surname equals to UPDATED_SURNAME
        defaultOperatorShouldNotBeFound("surname.in=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    void getAllOperatorsBySurnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where surname is not null
        defaultOperatorShouldBeFound("surname.specified=true");

        // Get all the operatorList where surname is null
        defaultOperatorShouldNotBeFound("surname.specified=false");
    }

    @Test
    @Transactional
    void getAllOperatorsBySurnameContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where surname contains DEFAULT_SURNAME
        defaultOperatorShouldBeFound("surname.contains=" + DEFAULT_SURNAME);

        // Get all the operatorList where surname contains UPDATED_SURNAME
        defaultOperatorShouldNotBeFound("surname.contains=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    void getAllOperatorsBySurnameNotContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where surname does not contain DEFAULT_SURNAME
        defaultOperatorShouldNotBeFound("surname.doesNotContain=" + DEFAULT_SURNAME);

        // Get all the operatorList where surname does not contain UPDATED_SURNAME
        defaultOperatorShouldBeFound("surname.doesNotContain=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    void getAllOperatorsByNationalidIsEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where nationalid equals to DEFAULT_NATIONALID
        defaultOperatorShouldBeFound("nationalid.equals=" + DEFAULT_NATIONALID);

        // Get all the operatorList where nationalid equals to UPDATED_NATIONALID
        defaultOperatorShouldNotBeFound("nationalid.equals=" + UPDATED_NATIONALID);
    }

    @Test
    @Transactional
    void getAllOperatorsByNationalidIsInShouldWork() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where nationalid in DEFAULT_NATIONALID or UPDATED_NATIONALID
        defaultOperatorShouldBeFound("nationalid.in=" + DEFAULT_NATIONALID + "," + UPDATED_NATIONALID);

        // Get all the operatorList where nationalid equals to UPDATED_NATIONALID
        defaultOperatorShouldNotBeFound("nationalid.in=" + UPDATED_NATIONALID);
    }

    @Test
    @Transactional
    void getAllOperatorsByNationalidIsNullOrNotNull() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where nationalid is not null
        defaultOperatorShouldBeFound("nationalid.specified=true");

        // Get all the operatorList where nationalid is null
        defaultOperatorShouldNotBeFound("nationalid.specified=false");
    }

    @Test
    @Transactional
    void getAllOperatorsByNationalidIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where nationalid is greater than or equal to DEFAULT_NATIONALID
        defaultOperatorShouldBeFound("nationalid.greaterThanOrEqual=" + DEFAULT_NATIONALID);

        // Get all the operatorList where nationalid is greater than or equal to UPDATED_NATIONALID
        defaultOperatorShouldNotBeFound("nationalid.greaterThanOrEqual=" + UPDATED_NATIONALID);
    }

    @Test
    @Transactional
    void getAllOperatorsByNationalidIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where nationalid is less than or equal to DEFAULT_NATIONALID
        defaultOperatorShouldBeFound("nationalid.lessThanOrEqual=" + DEFAULT_NATIONALID);

        // Get all the operatorList where nationalid is less than or equal to SMALLER_NATIONALID
        defaultOperatorShouldNotBeFound("nationalid.lessThanOrEqual=" + SMALLER_NATIONALID);
    }

    @Test
    @Transactional
    void getAllOperatorsByNationalidIsLessThanSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where nationalid is less than DEFAULT_NATIONALID
        defaultOperatorShouldNotBeFound("nationalid.lessThan=" + DEFAULT_NATIONALID);

        // Get all the operatorList where nationalid is less than UPDATED_NATIONALID
        defaultOperatorShouldBeFound("nationalid.lessThan=" + UPDATED_NATIONALID);
    }

    @Test
    @Transactional
    void getAllOperatorsByNationalidIsGreaterThanSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where nationalid is greater than DEFAULT_NATIONALID
        defaultOperatorShouldNotBeFound("nationalid.greaterThan=" + DEFAULT_NATIONALID);

        // Get all the operatorList where nationalid is greater than SMALLER_NATIONALID
        defaultOperatorShouldBeFound("nationalid.greaterThan=" + SMALLER_NATIONALID);
    }

    @Test
    @Transactional
    void getAllOperatorsByDateofbirthIsEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where dateofbirth equals to DEFAULT_DATEOFBIRTH
        defaultOperatorShouldBeFound("dateofbirth.equals=" + DEFAULT_DATEOFBIRTH);

        // Get all the operatorList where dateofbirth equals to UPDATED_DATEOFBIRTH
        defaultOperatorShouldNotBeFound("dateofbirth.equals=" + UPDATED_DATEOFBIRTH);
    }

    @Test
    @Transactional
    void getAllOperatorsByDateofbirthIsInShouldWork() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where dateofbirth in DEFAULT_DATEOFBIRTH or UPDATED_DATEOFBIRTH
        defaultOperatorShouldBeFound("dateofbirth.in=" + DEFAULT_DATEOFBIRTH + "," + UPDATED_DATEOFBIRTH);

        // Get all the operatorList where dateofbirth equals to UPDATED_DATEOFBIRTH
        defaultOperatorShouldNotBeFound("dateofbirth.in=" + UPDATED_DATEOFBIRTH);
    }

    @Test
    @Transactional
    void getAllOperatorsByDateofbirthIsNullOrNotNull() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where dateofbirth is not null
        defaultOperatorShouldBeFound("dateofbirth.specified=true");

        // Get all the operatorList where dateofbirth is null
        defaultOperatorShouldNotBeFound("dateofbirth.specified=false");
    }

    @Test
    @Transactional
    void getAllOperatorsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where status equals to DEFAULT_STATUS
        defaultOperatorShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the operatorList where status equals to UPDATED_STATUS
        defaultOperatorShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOperatorsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultOperatorShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the operatorList where status equals to UPDATED_STATUS
        defaultOperatorShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOperatorsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where status is not null
        defaultOperatorShouldBeFound("status.specified=true");

        // Get all the operatorList where status is null
        defaultOperatorShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllOperatorsByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where gender equals to DEFAULT_GENDER
        defaultOperatorShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the operatorList where gender equals to UPDATED_GENDER
        defaultOperatorShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllOperatorsByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultOperatorShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the operatorList where gender equals to UPDATED_GENDER
        defaultOperatorShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllOperatorsByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where gender is not null
        defaultOperatorShouldBeFound("gender.specified=true");

        // Get all the operatorList where gender is null
        defaultOperatorShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    void getAllOperatorsByGenderContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where gender contains DEFAULT_GENDER
        defaultOperatorShouldBeFound("gender.contains=" + DEFAULT_GENDER);

        // Get all the operatorList where gender contains UPDATED_GENDER
        defaultOperatorShouldNotBeFound("gender.contains=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllOperatorsByGenderNotContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where gender does not contain DEFAULT_GENDER
        defaultOperatorShouldNotBeFound("gender.doesNotContain=" + DEFAULT_GENDER);

        // Get all the operatorList where gender does not contain UPDATED_GENDER
        defaultOperatorShouldBeFound("gender.doesNotContain=" + UPDATED_GENDER);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOperatorShouldBeFound(String filter) throws Exception {
        restOperatorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operator.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].nationalid").value(hasItem(DEFAULT_NATIONALID.intValue())))
            .andExpect(jsonPath("$.[*].dateofbirth").value(hasItem(DEFAULT_DATEOFBIRTH.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)));

        // Check, that the count call also returns 1
        restOperatorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOperatorShouldNotBeFound(String filter) throws Exception {
        restOperatorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOperatorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOperator() throws Exception {
        // Get the operator
        restOperatorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOperator() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();

        // Update the operator
        Operator updatedOperator = operatorRepository.findById(operator.getId()).get();
        // Disconnect from session so that the updates on updatedOperator are not directly saved in db
        em.detach(updatedOperator);
        updatedOperator
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .nationalid(UPDATED_NATIONALID)
            .dateofbirth(UPDATED_DATEOFBIRTH)
            .status(UPDATED_STATUS)
            .gender(UPDATED_GENDER);
        OperatorDTO operatorDTO = operatorMapper.toDto(updatedOperator);

        restOperatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operatorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operatorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
        Operator testOperator = operatorList.get(operatorList.size() - 1);
        assertThat(testOperator.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOperator.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testOperator.getNationalid()).isEqualTo(UPDATED_NATIONALID);
        assertThat(testOperator.getDateofbirth()).isEqualTo(UPDATED_DATEOFBIRTH);
        assertThat(testOperator.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOperator.getGender()).isEqualTo(UPDATED_GENDER);
    }

    @Test
    @Transactional
    void putNonExistingOperator() throws Exception {
        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();
        operator.setId(count.incrementAndGet());

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operatorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOperator() throws Exception {
        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();
        operator.setId(count.incrementAndGet());

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOperator() throws Exception {
        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();
        operator.setId(count.incrementAndGet());

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOperatorWithPatch() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();

        // Update the operator using partial update
        Operator partialUpdatedOperator = new Operator();
        partialUpdatedOperator.setId(operator.getId());

        partialUpdatedOperator.nationalid(UPDATED_NATIONALID);

        restOperatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperator.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperator))
            )
            .andExpect(status().isOk());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
        Operator testOperator = operatorList.get(operatorList.size() - 1);
        assertThat(testOperator.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOperator.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testOperator.getNationalid()).isEqualTo(UPDATED_NATIONALID);
        assertThat(testOperator.getDateofbirth()).isEqualTo(DEFAULT_DATEOFBIRTH);
        assertThat(testOperator.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOperator.getGender()).isEqualTo(DEFAULT_GENDER);
    }

    @Test
    @Transactional
    void fullUpdateOperatorWithPatch() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();

        // Update the operator using partial update
        Operator partialUpdatedOperator = new Operator();
        partialUpdatedOperator.setId(operator.getId());

        partialUpdatedOperator
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .nationalid(UPDATED_NATIONALID)
            .dateofbirth(UPDATED_DATEOFBIRTH)
            .status(UPDATED_STATUS)
            .gender(UPDATED_GENDER);

        restOperatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperator.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperator))
            )
            .andExpect(status().isOk());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
        Operator testOperator = operatorList.get(operatorList.size() - 1);
        assertThat(testOperator.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOperator.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testOperator.getNationalid()).isEqualTo(UPDATED_NATIONALID);
        assertThat(testOperator.getDateofbirth()).isEqualTo(UPDATED_DATEOFBIRTH);
        assertThat(testOperator.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOperator.getGender()).isEqualTo(UPDATED_GENDER);
    }

    @Test
    @Transactional
    void patchNonExistingOperator() throws Exception {
        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();
        operator.setId(count.incrementAndGet());

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, operatorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOperator() throws Exception {
        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();
        operator.setId(count.incrementAndGet());

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOperator() throws Exception {
        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();
        operator.setId(count.incrementAndGet());

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(operatorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOperator() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        int databaseSizeBeforeDelete = operatorRepository.findAll().size();

        // Delete the operator
        restOperatorMockMvc
            .perform(delete(ENTITY_API_URL_ID, operator.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
