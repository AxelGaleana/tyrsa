package com.tyrsa.api_erp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampoActualizado {
    private String campo;
    private Object de;
    private Object a;    
}
