package com.proj.fitnessclass.repository.search;

import com.proj.fitnessclass.domain.FitnessClass;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link FitnessClass} entity.
 */
public interface FitnessClassSearchRepository extends ElasticsearchRepository<FitnessClass, Long> {
}
