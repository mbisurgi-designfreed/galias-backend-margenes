package com.designfreed.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("FAC")
public class ComprobanteVtaFac extends ComprobanteVta {
}
