package com.fbw.OneBoot.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class PagDTO<T> {
    private List<T> data;
    private boolean showPre;
    private boolean showFirstPage;
    private boolean showNextPage;
    private boolean showEndPage;
    private Integer page;
    private List<Integer> pages=new ArrayList<>();
    private Integer totalPage;
    private Integer size;

    public void setPagInation(Integer totalCount, Integer page, Integer size) {
        this.page=page;
        this.size=size;

        if(totalCount%size==0){
            totalPage=totalCount/size;
        }
        else{
            totalPage=totalCount/size+1;
        }
  if(page<1){
      page=1;
  }
  if(page>totalPage){
      page=totalPage;
  }
  if(size<1){
      size=1;
  }
  if(size>totalCount){
      size=totalCount;
  }


      if(size==1) {
          for (int i = 1; i <= totalPage; i++) {
              pages.add(i);
          }
      }

      else{
        pages.add(page);
        for(int i=1; i<=totalPage;i++){
        if(page-i>0) {
            pages.add(0,page-i);
        }
            if (page + i <= totalPage) {
                pages.add(page + i);
        }
        }
      }

        if(page==1){
            showPre=false;

        }
        else {
       showPre=true;

        }
        if(page==totalPage){
            showNextPage=false;
        }
        else{
            showNextPage=true;
        }
        if(page>=1&&page<3){
            showFirstPage=false;
        }
        else{
            showFirstPage=true;
        }
        if(page==totalPage){
            showEndPage=false;
        }
        else{
            showEndPage=true;
        }
    }

}
