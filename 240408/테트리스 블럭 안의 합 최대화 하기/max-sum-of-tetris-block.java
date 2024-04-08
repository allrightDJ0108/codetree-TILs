import java.io.*;
import java.util.*;

public class Main {
    static int N, M;
    static int[][] board;

    static int[][][] blocks = {
        {{0,0},{0,1},{0,2},{0,3}},
        {{0,0},{1,0},{2,0},{3,0}},

        {{0,0},{1,0},{0,1},{1,1}},

        {{0,0},{1,0},{1,1},{2,1}},
        {{1,0},{1,1},{0,1},{0,2}},
        {{1,0},{0,1},{1,1},{2,0}},
        {{0,0},{0,1},{1,1},{1,2}},

        {{0,0},{1,0},{1,1},{2,0}},
        {{1,0},{0,1},{1,1},{1,2}},
        {{0,0},{0,1},{0,2},{1,1}},
        {{0,1},{1,0},{1,1},{2,1}},

        {{0,0},{1,0},{2,0},{2,1}},
        {{0,1},{1,1},{2,1},{2,0}},
        {{0,0},{0,1},{1,0},{2,0}},
        {{1,0},{1,1},{1,2},{0,2}},
        {{0,0},{0,1},{0,2},{1,2}},
        {{0,0},{0,1},{1,1},{2,1}},
        {{0,0},{1,0},{1,1},{1,2}},
        {{0,0},{0,1},{0,2},{1,0}}
                            
    };
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer str = new StringTokenizer(br.readLine());

        N = Integer.parseInt(str.nextToken());
        M = Integer.parseInt(str.nextToken());

        board = new int[N][M];

        for (int i=0; i<N; i++){
            str = new StringTokenizer(br.readLine());
            for (int j=0; j<M; j++){
                board[i][j] = Integer.parseInt(str.nextToken());
            }
        }

        System.out.println(boarding());
    }

    static int calc(int x, int y){
        int max = 0;
        for (int i=0; i<blocks.length; i++){
            int sum = 0;
            for (int j=0; j<4; j++){
                int nx = x + blocks[i][j][0];
                int ny = y + blocks[i][j][1];

                if (nx >= 0 && nx < N && ny >= 0 && ny < M){
                    sum += board[nx][ny];
                } else continue;

            }
            max = Math.max(max, sum);
            //System.out.println("==="+max);
        }
        return max;
    }

    static int boarding(){
        int max = 0;
        for (int i=0; i<N; i++){
            for (int j=0; j<M; j++){
                max = Math.max(max, calc(i,j));
            }
        }

        return max;
    }
}