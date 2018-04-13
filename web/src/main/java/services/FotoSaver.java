package services;


import model.Item;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilya on 04.09.2016.
 * image service
 */
public final class FotoSaver {

    private FotoSaver(){}

    /**
     * Copies image file to filesystem using parameters to create directories
     * @param in            Source
     * @param category      Item's category. Serves as part of file path
     * @param name          Items' name
     * @param num           Number of image.
     * @throws IOException
     */
    public static void saveFotoToFileSystem(InputStream in, String category, String name, String num) throws IOException{
        Path p = Paths.get("/foto/"+category+"/"+name);
        Files.createDirectories(p);
        Path res = p.resolve("file"+num+".jpg");
        if(in.available()>0)
        Files.copy(in,res, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     *
     * @param item Item entity
     * @return List of Path objects
     * @throws IOException
     */
    public static List<String> getPathsOfFotos(Item item)throws IOException{
        Path p = Paths.get("/foto/"+item.getTheme()+"/"+item.getName().trim());
        List<String> list = new ArrayList<>();
        Files.createDirectories(p);
        try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream(p)){
            for(Path path : directoryStream){
                list.add(path.toString());
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     *  Tries to delete File
     * @param p String representation of Path to object
     * @throws IOException
     */
    public static void deleteFile(String p)throws IOException{

        Files.deleteIfExists(Paths.get(p));
    }

    public static void deleteByItem(String name,String theme)throws IOException{
        Path path = Paths.get("/foto/"+theme+"/"+name.trim());
        try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)){
            for (Path path1 : directoryStream){
                Files.deleteIfExists(path1);
            }
        }
        theme = theme.contains("/") ? theme.substring(0,theme.indexOf('/')) : theme;
        cleanDirectories(Paths.get("/foto/" + theme));
    }

    /**
     *  Move all files from path, represented by [old] to new location
     * @param name Part of new location path
     * @param theme Part of new location path
     * @param oldName Part of old location path
     * @param oldTheme  Part of old location path
     * @throws IOException
     */
    public static void renameFotoDirectory(String name,String theme,String oldName,String oldTheme)throws IOException{
        Path old = Paths.get("/foto/"+oldTheme+"/"+oldName.trim());
        Path hadash = Paths.get("/foto/"+theme+"/"+name.trim());
        Files.createDirectories(old);
        Files.createDirectories(hadash);
        try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream(old)){
            for (Path path : directoryStream){
                Path toSave =  hadash.resolve(path.getFileName());
                Files.move(path,toSave);
                Files.deleteIfExists(path);
            }
        }
        oldTheme = oldTheme.contains("/") ? oldTheme.substring(0,oldTheme.indexOf('/')) : oldTheme;
        cleanDirectories(Paths.get("/foto/" + oldTheme));
    }

    private static void cleanDirectories(Path path) throws IOException{
        Files.walkFileTree(path, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if(isDirEmpty(dir))Files.deleteIfExists(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static boolean isDirEmpty(final Path directory) throws IOException {
        try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
            return !dirStream.iterator().hasNext();
        }
    }

}
