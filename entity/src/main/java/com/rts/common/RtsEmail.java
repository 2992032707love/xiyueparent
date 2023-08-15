package com.rts.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RtsEmail {
    private String textl;
    private String to;
    private String subject;
}
