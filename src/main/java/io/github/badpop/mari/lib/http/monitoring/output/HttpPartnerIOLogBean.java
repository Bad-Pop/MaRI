package io.github.badpop.mari.lib.http.monitoring.output;

import io.github.badpop.mari.lib.http.monitoring.HttpIOLogBean;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class HttpPartnerIOLogBean extends HttpIOLogBean {
    private String partner;
}
