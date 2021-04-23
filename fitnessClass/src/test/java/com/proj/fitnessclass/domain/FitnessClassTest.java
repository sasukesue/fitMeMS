package com.proj.fitnessclass.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.proj.fitnessclass.web.rest.TestUtil;

public class FitnessClassTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FitnessClass.class);
        FitnessClass fitnessClass1 = new FitnessClass();
        fitnessClass1.setId(1L);
        FitnessClass fitnessClass2 = new FitnessClass();
        fitnessClass2.setId(fitnessClass1.getId());
        assertThat(fitnessClass1).isEqualTo(fitnessClass2);
        fitnessClass2.setId(2L);
        assertThat(fitnessClass1).isNotEqualTo(fitnessClass2);
        fitnessClass1.setId(null);
        assertThat(fitnessClass1).isNotEqualTo(fitnessClass2);
    }
}
