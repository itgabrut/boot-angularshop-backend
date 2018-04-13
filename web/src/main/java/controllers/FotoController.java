package controllers;

import model.Item;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import services.FotoSaver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping(value = "/fotos")
public class FotoController {


    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin
    public byte[] getFotoFromSYS(@RequestParam( name = "path")String path)throws IOException{
        return Files.readAllBytes(Paths.get(path));
    }

    @RequestMapping(value = "/list",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin( value = "*")
    public List<String> getPaths(@RequestBody Item item)throws IOException{
        return FotoSaver.getPathsOfFotos(item);
    }
}
