package me.jaeyeon.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private List<PostDto> content;  //포스트 리스트
    private int pageNo;             //페이지넘버
    private int pageSize;		 	//페이지당 포스트 수
    private long totalElements; 	//총 포스트 수
    private int totalPages;			//총 페이지 수
    private boolean last;			//마지막 페이지면 true
}
