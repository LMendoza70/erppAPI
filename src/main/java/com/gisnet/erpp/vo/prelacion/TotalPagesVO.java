package com.gisnet.erpp.vo.prelacion;

public class TotalPagesVO {
  private Long totalElements;
  private Integer totalPages;
  
  	public TotalPagesVO() {}
    public TotalPagesVO(Long totalElements,Integer totalPages) {
	   this.totalElements = totalElements;
	   this.totalPages =  totalPages;
    }
    
	public Long getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}
	public Integer getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
}
