package com.example.myeventsourcing.financial;

import com.example.myeventsourcing.common.SwaggerAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Created by Administrador on 16/03/2016.
 */

@SpringBootApplication
@Import({SwaggerAutoConfiguration.class})
public class FinancialConfiguration {
}
