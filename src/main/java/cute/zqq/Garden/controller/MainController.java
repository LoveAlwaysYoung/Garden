package cute.zqq.Garden.controller;

import cute.zqq.Garden.pojo.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@CrossOrigin
public class MainController {

    @GetMapping("/home")
    public String gotoIndex(){
        return "/index";
    }

    @RequestMapping(value = "/uploadImg")
    @ResponseBody
    public Response imgUpdate(@RequestParam(value = "file") MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }
        // 获取文件名
        String oldFileName = file.getOriginalFilename();
        System.out.println("oldFileName:"+oldFileName);
        // 获取文件的后缀名
        String suffixName=".jpg";
        String[] splits=oldFileName.split(".");
        if (splits.length>1){
            suffixName="."+splits[splits.length-1];
        }
        // 文件上传后的路径
//        String filePath = "/Users/lip/Desktop/";
        String filePath = "/home/zqq/noteimg/";
        String YM=getYearMonth();
        String fileName=String.valueOf(System.currentTimeMillis());

        String finalName=filePath+YM+"/"+fileName+suffixName;
        System.out.println("文件名："+finalName);

        File localFile = new File(finalName);
        // 检测是否存在目录
        if (!localFile.getParentFile().exists()) {
            if(!localFile.getParentFile().mkdirs()) {
                Response response=new Response();
                String res="创建文件夹失败！";
                response.setCode(500);
                response.setMsg(res);
                return response;
            }
        }

        try {
            localFile.createNewFile();
            InputStream is = file.getInputStream();
            OutputStream os = new FileOutputStream(new File(finalName));
            int len=0;
            byte[] buffer = new byte[1024];
            while ((len=is.read(buffer))!=-1){
                os.write(buffer,0,len);
            }
            os.close();
            is.close();
        }catch (Exception e){
            Response response=new Response();
            response.setCode(500);
            String res="错误信息:"+e.toString();
            response.setMsg(res);
            return response;
        }
        Response response=new Response();
        response.setCode(200);
        String res=YM+"/"+fileName+suffixName;
        System.out.println(res);
        response.setMsg(res);
        return response;
    }


    // 获取年和月，用于分类照片
    public static String getYearMonth(){
        String ts=String.valueOf(System.currentTimeMillis());
        String formatStr="yyyyMM";
        SimpleDateFormat format=new SimpleDateFormat(formatStr);
        return format.format(new Date(Long.valueOf(ts)));
    }


}
