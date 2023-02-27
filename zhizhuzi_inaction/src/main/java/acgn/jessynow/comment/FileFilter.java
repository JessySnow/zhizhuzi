package acgn.jessynow.comment;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class FileFilter {
    public static MessageDigest md5Signer;
    static {
        try {
            md5Signer = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static final String NEG_PATH_PREFIX = "/Users/jessy/cache/comment/neg/";
    private static final String POS_PATH_PREFIX = "/Users/jessy/cache/comment/pos/";

    public static void main(String[] args) {
        File dirPos = new File(POS_PATH_PREFIX);
        File dirNeg = new File(NEG_PATH_PREFIX);
//        randomDelete(823, dirNeg);
        randomDelete(1000, dirPos);
    }

    private static void filterSameContent(){
        Set<String> filterSet = new HashSet<>();
        File dirPos = new File(POS_PATH_PREFIX);
        File dirNeg = new File(NEG_PATH_PREFIX);


        Arrays.stream(dirPos.listFiles()).forEach(file -> {
            try {
                String md5;
                if (filterSet.contains((md5 = md5Sign(getContent(file))))){
                    file.delete();
                }else {
                    filterSet.add(md5);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        filterSet.clear();
        Arrays.stream(dirNeg.listFiles()).forEach(file -> {
            try {
                String md5;
                if (filterSet.contains((md5 = md5Sign(getContent(file))))){
                    file.delete();
                }else {
                    filterSet.add(md5);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static String md5Sign(String content) throws UnsupportedEncodingException {
        byte[] digest = md5Signer.digest(content.getBytes());
        return new String(digest);
    }

    private static String getContent(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String temp;
        while ((temp = reader.readLine()) != null) sb.append(temp);
        return sb.toString();
    }

    private static void filterWithSpecificRule(){
        File dirPos = new File(POS_PATH_PREFIX);
        File dirNeg = new File(NEG_PATH_PREFIX);


        Arrays.stream(Objects.requireNonNull(dirPos.listFiles())).forEach(file -> {
            try {
                String content = getContent(file);
                if (content.contains("&")){
                    file.delete();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Arrays.stream(Objects.requireNonNull(dirNeg.listFiles())).forEach(file -> {
            try {
                String content = getContent(file);
                if (content.contains("&")){
                    file.delete();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void filterWithLength(){
        File dirPos = new File(POS_PATH_PREFIX);
        File dirNeg = new File(NEG_PATH_PREFIX);


        Arrays.stream(Objects.requireNonNull(dirPos.listFiles())).forEach(file -> {
            try {
                String content = getContent(file);
                if (content.contains("*")){
                    System.out.println(file.getName());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Arrays.stream(Objects.requireNonNull(dirNeg.listFiles())).forEach(file -> {
            try {
                String content = getContent(file);
                if (content.contains("*")){
                    System.out.println(file.getName());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void randomDelete(int nums, File dir){
        File[] files = dir.listFiles();
        int index;
        while (nums > 0){
            if(files[index = (int)(Math.random() * files.length)].exists()){
                -- nums;
                files[index].delete();
            }
        }
    }
}
