package com.example.photoprovider;

public class PhotoBean {
    private String imgPath;
    private boolean isChoosed;

    public PhotoBean(String imgPath, boolean isChoosed) {
        this.imgPath = imgPath;
        this.isChoosed = isChoosed;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }
}

