package com.designfreed.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("NDB")
public class ComprobanteVtaNd extends ComprobanteVta{
}
