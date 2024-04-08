import java.io.*;
import java.util.*;

public class Main {
    static int N, M, x, y, d;
    static int[][] dir = {{-1,0}, {0,1}, {1,0}, {0,-1}}; //상(0), 우(1), 하(2), 좌(3)
    static int[][] map;
    static int[][] visited;
    static Queue<int[]> q = new LinkedList<>();


    public static void main(String[] args) throws IOException {
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer str = new StringTokenizer(br.readLine());
        N = Integer.parseInt(str.nextToken());
        M = Integer.parseInt(str.nextToken());

        str = new StringTokenizer(br.readLine());
        x = Integer.parseInt(str.nextToken());
        y = Integer.parseInt(str.nextToken());
        d = Integer.parseInt(str.nextToken());

        map = new int[N][M];
        visited = new int[N][M];
        for (int i=0; i<N; i++){
            str = new StringTokenizer(br.readLine());
            for (int j=0; j<M; j++){
                map[i][j] = Integer.parseInt(str.nextToken());
            }
        }

        System.out.println(carFunc());

    }

    static int carFunc(){
        int result = 1;
        q.add(new int[]{x,y,d});
        visited[x][y] = 1;

        while (!q.isEmpty()){
            int[] cur = q.poll();
            int cx = cur[0];
            int cy = cur[1];
            int cd = cur[2];

            boolean flag = false;
            for (int i=0; i<4; i++){
                cd = (cd+3) % 4; // 0-> 3, 1-> 0, 2-> 1, 3-> 2
                int nx = cx + dir[cd][0];
                int ny = cy + dir[cd][1];

                if (nx >= 0 && nx < N && ny >= 0 && ny < M && map[nx][ny] == 0 && visited[nx][ny] == 0){
                    q.add(new int[]{nx, ny, cd});
                    visited[nx][ny] = 1;
                    result++;
                    flag = true;
                    break;
                } 

            }

            if (!flag){
                int nd = (cd+2) % 4; //0-> 2, 1-> 3, 2-> 0, 3-> 1
                int nx = cx + dir[nd][0];
                int ny = cy + dir[nd][1];

                if (nx >= 0 && nx < N && ny >= 0 && ny < M && map[nx][ny] == 0){
                    q.add(new int[]{nx, ny, cd});
                } else 
                    break;
            }

            
        }

        return result;
    }

}