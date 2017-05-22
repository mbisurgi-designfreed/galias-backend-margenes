package com.designfreed.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("N/C")
public class ComprobanteVtaNcc extends ComprobanteVta {
}
