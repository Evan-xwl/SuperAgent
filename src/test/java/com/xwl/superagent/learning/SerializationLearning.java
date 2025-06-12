package com.xwl.superagent.learning;

import com.xwl.superagent.model.FilmEntity;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author ruoling
 * @date 2025/6/12 20:58:48
 * @description
 */
public class SerializationLearning {
    @Test
    public void JDKTest() {
        String fileName = "filmJDK.txt";
        String[] dirPathStr = new String[] {"temp"};
        String filmPathStr = createFilmObjFile(fileName, dirPathStr);
        FilmEntity filmEntity = new FilmEntity();//必须序列化
        filmEntity.setName("《唐顿庄园》");
        filmEntity.setDescription("《唐顿庄园》是1997年美国Director等导演的动画电影。");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filmPathStr))) {
            oos.writeObject(filmEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filmPathStr))) {
            FilmEntity person = (FilmEntity) ois.readObject();
            System.out.println("对象已反序列化: " + person.toString());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void JSONTest() {
        // 默认通过无参构造 + setter赋值
        // 默认无法序列化抽象父类（无法判断实际是哪个子类）-->可以使用JSON注解在父类上，在JSON字符串中添加类信息（修改源码）
    }

    private String createFilmObjFile(String fileName, String... more) {
        String projectDir = System.getProperty("user.dir"); // 获取当前项目根目录
        Path tempDirPath = Paths.get(projectDir, more);
        Path filePath = tempDirPath.resolve(fileName);

        try {
            // 如果目录不存在，则创建
            if (!Files.exists(tempDirPath)) {
                Files.createDirectories(tempDirPath);
                System.out.println("创建目录: " + tempDirPath);
            }

            // 创建文件（如果已存在不会重复创建）
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                System.out.println("文件创建成功: " + filePath);
            } else {
                System.out.println("文件已存在: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("操作过程中发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        return filePath.toString();
    }
}
