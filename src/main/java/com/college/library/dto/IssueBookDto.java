package com.college.library.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IssueBookDto {

    @NotNull(message = "Please select a member")
    private Long userId;

    @NotNull(message = "Please select a book")
    private Long bookId;
}
