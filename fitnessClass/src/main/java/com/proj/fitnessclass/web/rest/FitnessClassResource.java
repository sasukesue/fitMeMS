package com.proj.fitnessclass.web.rest;

import com.proj.fitnessclass.domain.FitnessClass;
import com.proj.fitnessclass.repository.FitnessClassRepository;
import com.proj.fitnessclass.repository.search.FitnessClassSearchRepository;
import com.proj.fitnessclass.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.proj.fitnessclass.domain.FitnessClass}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FitnessClassResource {

    private final Logger log = LoggerFactory.getLogger(FitnessClassResource.class);

    private static final String ENTITY_NAME = "fitnessClassFitnessClass";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FitnessClassRepository fitnessClassRepository;

    private final FitnessClassSearchRepository fitnessClassSearchRepository;

    public FitnessClassResource(FitnessClassRepository fitnessClassRepository, FitnessClassSearchRepository fitnessClassSearchRepository) {
        this.fitnessClassRepository = fitnessClassRepository;
        this.fitnessClassSearchRepository = fitnessClassSearchRepository;
    }

    /**
     * {@code POST  /fitness-classes} : Create a new fitnessClass.
     *
     * @param fitnessClass the fitnessClass to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fitnessClass, or with status {@code 400 (Bad Request)} if the fitnessClass has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fitness-classes")
    public ResponseEntity<FitnessClass> createFitnessClass(@Valid @RequestBody FitnessClass fitnessClass) throws URISyntaxException {
        log.debug("REST request to save FitnessClass : {}", fitnessClass);
        if (fitnessClass.getId() != null) {
            throw new BadRequestAlertException("A new fitnessClass cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FitnessClass result = fitnessClassRepository.save(fitnessClass);
        fitnessClassSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/fitness-classes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fitness-classes} : Updates an existing fitnessClass.
     *
     * @param fitnessClass the fitnessClass to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fitnessClass,
     * or with status {@code 400 (Bad Request)} if the fitnessClass is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fitnessClass couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fitness-classes")
    public ResponseEntity<FitnessClass> updateFitnessClass(@Valid @RequestBody FitnessClass fitnessClass) throws URISyntaxException {
        log.debug("REST request to update FitnessClass : {}", fitnessClass);
        if (fitnessClass.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FitnessClass result = fitnessClassRepository.save(fitnessClass);
        fitnessClassSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fitnessClass.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /fitness-classes} : get all the fitnessClasses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fitnessClasses in body.
     */
    @GetMapping("/fitness-classes")
    public ResponseEntity<List<FitnessClass>> getAllFitnessClasses(Pageable pageable) {
        log.debug("REST request to get a page of FitnessClasses");
        Page<FitnessClass> page = fitnessClassRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fitness-classes/:id} : get the "id" fitnessClass.
     *
     * @param id the id of the fitnessClass to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fitnessClass, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fitness-classes/{id}")
    public ResponseEntity<FitnessClass> getFitnessClass(@PathVariable Long id) {
        log.debug("REST request to get FitnessClass : {}", id);
        Optional<FitnessClass> fitnessClass = fitnessClassRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fitnessClass);
    }

    /**
     * {@code DELETE  /fitness-classes/:id} : delete the "id" fitnessClass.
     *
     * @param id the id of the fitnessClass to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fitness-classes/{id}")
    public ResponseEntity<Void> deleteFitnessClass(@PathVariable Long id) {
        log.debug("REST request to delete FitnessClass : {}", id);
        fitnessClassRepository.deleteById(id);
        fitnessClassSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/fitness-classes?query=:query} : search for the fitnessClass corresponding
     * to the query.
     *
     * @param query the query of the fitnessClass search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/fitness-classes")
    public ResponseEntity<List<FitnessClass>> searchFitnessClasses(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FitnessClasses for query {}", query);
        Page<FitnessClass> page = fitnessClassSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
