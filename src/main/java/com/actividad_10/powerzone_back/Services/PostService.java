package com.actividad_10.powerzone_back.Services;

public class PostService implements IPostService {
    @Override
    public void createPost() {
        System.out.println("Post creado");
    }

    @Override
    public void deletePost() {
        System.out.println("Post eliminado");
    }


    @Override
    public void getPostByName() {
        System.out.println("Post obtenido por nombre");
    }
}
