package it.buniva.strage.utility.csvfile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCSV {

    private Long no;

    private String email;

    private String name;

    private String surname;
}
