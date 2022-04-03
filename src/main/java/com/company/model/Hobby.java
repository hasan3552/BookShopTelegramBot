package com.company.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Hobby {

    Integer id;
    String nameUz;
    String nameRu;
    String nameEn;
    Boolean isDelete;

    public Hobby(Integer id, String nameUz, String nameRu, String nameEn) {
        this.id = id;
        this.nameUz = nameUz;
        this.nameRu = nameRu;
        this.nameEn = nameEn;
        isDelete = false;
    }
}
