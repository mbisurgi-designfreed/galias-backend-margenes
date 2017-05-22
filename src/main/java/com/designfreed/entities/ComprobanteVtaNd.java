package com.designfreed.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("NDB")
public class ComprobanteVtaNd extends ComprobanteVta{
}
