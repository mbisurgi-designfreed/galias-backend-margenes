package com.designfreed.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("FAC")
public class ComprobanteCpaFac extends ComprobanteCpa {
}
