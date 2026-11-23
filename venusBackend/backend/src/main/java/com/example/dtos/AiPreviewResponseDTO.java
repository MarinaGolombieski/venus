package com.example.dtos;
public class AiPreviewResponseDTO {
    private String status;
    private String message;
    private String imageUrl;
    public AiPreviewResponseDTO(){}
    public AiPreviewResponseDTO(String status,String message,String imageUrl){this.status=status;this.message=message;this.imageUrl=imageUrl;}
    public String getStatus(){return status;}
    public String getMessage(){return message;}
    public String getImageUrl(){return imageUrl;}
}
