package com.zqt;

/**
 * shixian jisuanceng caozuo
 */
public class Algorithm {
    public static String submitSPSFC(String split) {
        String[] args = new String[]{
                "--class","Main",
                "--master","spark://localhost:7077",
                "/home/zangtt/IdeaProjects//FinalParallelFusion/out/artifacts/FinalParallelFusion_jar/FinalParallelFusion.jar",
                "local[*]",
                "/home/zangtt/Data/zqt/PaviaU.txt",
                "/home/zangtt/Data/zqt/PaviaU_gt.txt",
                "/home/zangtt/Data/zqt/index_org.txt",
                "1",
                split,
                "hdfs://localhost:9000/home/zangtt/original-4.txt"
        };
        StringBuilder shell=new StringBuilder("spark-submit").append(" ");
        for(int i=0;i<args.length;i++) {
            shell.append(args[i]).append(" ");
        }
        String result = execShell(shell.toString());
        return result;
    }

    public static String execShell(String shell) {
        String result = "";
        try {
            Process process = Runtime.getRuntime().exec(shell);
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result == "" ? "success" : result;
    }

    public static String uploadData(String dataName) {
        String[] args = new String[]{
                "hadoop","dfs",
                "-D","dfs.block.size=42724400",
                "-D","io.bytes.per.checksum=100",
                "-D","dfs.namenode.fs-limits.min-block-size=324000",
                "-put",
                "/home/zangtt/Data/zqt/"+dataName,
                "/home/zangtt/double-4.txt"
        };
        StringBuilder shell=new StringBuilder();
        for(int i=0;i<args.length;i++) {
            shell.append(args[i]).append(" ");
        }
        String result = execShell(shell.toString());
        return result;
    }
}
