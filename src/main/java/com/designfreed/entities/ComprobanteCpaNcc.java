package com.designfreed.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("NCC")
public class ComprobanteCpaNcc extends ComprobanteCpa {
}
