package Models;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class BaseEntity {

    private long id;

    private LocalDate createdDate;

    private LocalDate deletedDate;

}
