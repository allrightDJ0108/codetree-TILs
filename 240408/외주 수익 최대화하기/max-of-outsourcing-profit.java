import java.util.*;
import java.io.*;

public class Main {
    static int N;
    static int[][] works;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        works = new int[N][2];

        for (int i=0; i<N; i++){
            StringTokenizer str = new StringTokenizer(br.readLine());
            works[i][0] = Integer.parseInt(str.nextToken());
            works[i][1] = Integer.parseInt(str.nextToken());
        }

        int[] dp = new int[N+1];

        for (int i=0; i<N; i++){
            int time = works[i][0];
            int work = works[i][1];
            
            if (i + time <= N){
                dp[i + time] = Math.max(dp[i+time], work + dp[i]);
            }

            dp[i+1] = Math.max(dp[i+1], dp[i]);
            // for (int j=0; j<N+1; j++){
            //     System.out.print(dp[j] +" ");
            // }
        }

        System.out.println(dp[N]);
    }
}