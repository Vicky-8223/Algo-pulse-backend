package com.prepos.analytics.dto;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalendarDTO {

    private List<SubmissionDayDTO> submissions;
    private ConsistencyDTO consistency;
}