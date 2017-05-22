package com.designfreed.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("N/C")
public class ComprobanteCpaNc extends ComprobanteCpa {
}
