package com.designfreed.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("NCR")
public class ComprobanteVtaNc extends ComprobanteVta {
}
