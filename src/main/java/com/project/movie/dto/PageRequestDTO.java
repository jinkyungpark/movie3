package com.project.movie.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {
	private int page;
	private int size;

	// 검색 때문에 추가
	private String type;
	private String keyword;

	public PageRequestDTO() {
		super();
		this.page = 1;
		this.size = 10;
		this.keyword = "";
		this.type = "";
	}

	public Pageable getPageable(Sort sort) {
		return PageRequest.of(page - 1, size, sort);
	}
}
