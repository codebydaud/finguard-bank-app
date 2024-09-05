package com.codebydaud.training.banking_app.util;
import com.codebydaud.training.banking_app.dto.AccountResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class PaginatedResponseUtil {

    private final List<AccountResponse> content;
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;

    public PaginatedResponseUtil(Page<AccountResponse> page) {
        this.content = page.getContent();
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }
}
