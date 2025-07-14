package com.project.movie.dto;


import lombok.Builder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MovieImageDTO {
    private Long inum;
    private String uuid;
    private String imgName;
    private String path;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public String getThumbnailURL(){
        String thumbFullPath = "";
        try {
            thumbFullPath = URLEncoder.encode(path+"/s_"+uuid+"_"+imgName,"UTF-8");
            System.out.println("fullPath "+thumbFullPath);
        } catch (UnsupportedEncodingException e) {            
            e.printStackTrace();
        }
        return thumbFullPath;
    }


    public String getImageURL(){
        String fullPath = "";
        try {
            fullPath = URLEncoder.encode(path+"/"+uuid+"_"+imgName,"UTF-8");
            System.out.println("fullPath "+fullPath);
        } catch (UnsupportedEncodingException e) {            
            e.printStackTrace();
        }
        return fullPath;
    }
}
