package com.rts.canal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CanalMessage {
    private String data;
    private String database;
    private Boolean isDdl;
    private String old;
    private String table;
    private String type;
}
