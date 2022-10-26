package org.example;

import org.example.models.FileInfo;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.jar.JarFile;
import java.util.zip.CheckedInputStream;

public class DownloadThread extends Thread{
    private FileInfo file;
    DownloadManager manager;
    public DownloadThread(FileInfo file,DownloadManager manager){
        this.file = file;
        this.manager = manager;

    }
    @Override
    public void run() {
        this.file.setStatus("Downloading");
        this.manager.updateUI(this.file);
        try {
           // Files.copy(new URL(this.file.getUrl()).openStream(), Paths.get(this.file.getPath()));
            URL url = new URL(this.file.getUrl());
            URLConnection urlConnection = url.openConnection();
            int fileSize = urlConnection.getContentLength();
            int countByte = 0;
            double byteSum = 0.0;
            double per = 0.0;
            BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(this.file.getPath());
            byte data[] = new byte[1024];
            while(true){
                countByte = bufferedInputStream.read(data,0,1024);
                if(countByte == -1){
                    break;
                }
                fileOutputStream.write(data,0,countByte);
                byteSum += countByte;
                if(fileSize>0){
                    per = (byteSum / fileSize * 100);
                    this.file.setPer(per + "");
                    this.manager.updateUI(file);
                }
            }
            fileOutputStream.close();
            bufferedInputStream.close();
            this.setName(100 + "");
            this.file.setStatus("Complete");
        } catch (IOException e) {
            this.file.setStatus("Failed");
            System.out.println("Downloading Error!");
            e.printStackTrace();
        }
        this.manager.updateUI(this.file);

    }
}
