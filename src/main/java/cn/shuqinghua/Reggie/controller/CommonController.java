package cn.shuqinghua.Reggie.controller;

import cn.shuqinghua.Reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {


    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload") public R<String> upload(MultipartFile file) throws IOException {
        String suffix = file.getOriginalFilename().split("\\.")[1];
        //使用uuid重新生成文件名
        String prefix = UUID.randomUUID().toString();
        String fileName=basePath+prefix+"."+suffix;

        File dir=new File(basePath);
        if(dir.exists()){
            dir.mkdir();
        }
        log.info(file.toString());
        file.transferTo(new File(fileName));
        return R.success(prefix+"."+suffix);
   }


    @GetMapping("/download")
    public void download(String name,HttpServletResponse response){
         //输入流，读取文件内容
        try {
            FileInputStream fis = new FileInputStream(new File(basePath+name));

             response.setContentType("image/jpeg");
            ServletOutputStream outputStream = response.getOutputStream();
            byte bys[]=new byte[1024];
            int len=0;
            while((len=fis.read(bys))!=-1){
                outputStream.write(bys,0,len);
            }
            fis.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
