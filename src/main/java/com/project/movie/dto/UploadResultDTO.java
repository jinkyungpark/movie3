package com.project.movie.dto;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 *  업로드 된 결과를 담을 dto
 */

@Data
@AllArgsConstructor
public class UploadResultDTO implements Serializable{
    
    private String fileName;
    private String uuid;
    private String folderPath;

    public String getThumbnailURL(){
        String thumbFullPath = "";
        try {
            thumbFullPath = URLEncoder.encode(folderPath+"/s_"+uuid+"_"+fileName,"UTF-8");
            System.out.println("fullPath "+thumbFullPath);
        } catch (UnsupportedEncodingException e) {            
            e.printStackTrace();
        }
        return thumbFullPath;
    }


    public String getImageURL(){
        String fullPath = "";
        try {
            fullPath = URLEncoder.encode(folderPath+"/"+uuid+"_"+fileName,"UTF-8");
            System.out.println("fullPath "+fullPath);
        } catch (UnsupportedEncodingException e) {            
            e.printStackTrace();
        }
        return fullPath;
    }
}
