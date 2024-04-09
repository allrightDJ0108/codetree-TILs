import java.io.*;
import java.util.*;

public class Main {
    static int N, L, R;
    static int[][] map;
    static boolean[][] visited;
    static int[][] dir = {{0,1},{0,-1},{1,0},{-1,0}};

    static class Eggs{
        int x, y;

        Eggs(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer str = new StringTokenizer(br.readLine());
        N = Integer.parseInt(str.nextToken());
        L = Integer.parseInt(str.nextToken());
        R = Integer.parseInt(str.nextToken());

        map = new int[N][N];
        for (int i=0; i<N; i++){
            str = new StringTokenizer(br.readLine());
            for (int j=0; j<N; j++){
                map[i][j] = Integer.parseInt(str.nextToken());
            }
        }
        
        int result = 0;
        while (checkFunc()){
            result++;
        }

        System.out.println(result);
    }

    static boolean checkFunc(){
        visited = new boolean[N][N];
        boolean moved = false;

        for (int i=0; i<N; i++){
            for (int j=0; j<N; j++){
                if (!visited[i][j]){
                    if (EggFunc(i,j)){
                        moved = true;
                    }
                }
            }
        }

        return moved;
    }

    static boolean EggFunc(int x, int y){
        Queue<Eggs> q = new LinkedList<>();
        LinkedList<Eggs> list = new LinkedList<>();
        q.add(new Eggs(x, y));
        list.add(new Eggs(x,y));
        visited[x][y] = true;

        int sum = map[x][y];
        int cnt = 1;


        while (!q.isEmpty()){
            Eggs egg = q.poll();
            
            for (int d=0; d<4; d++){
                int nx = egg.x + dir[d][0];
                int ny = egg.y + dir[d][1];

                if (nx >= 0 && nx < N && ny >= 0 && ny < N && !visited[nx][ny]){
                    int diff = Math.abs(map[egg.x][egg.y] - map[nx][ny]);
                    if (diff >= L && diff <= R){
                        q.add(new Eggs(nx, ny));
                        list.add(new Eggs(nx,ny));
                        visited[nx][ny] = true;
                        sum += map[nx][ny];
                        cnt++;
                    }
                }
            }

        }

        if (cnt > 1){
            int avg = sum / cnt;
            for (Eggs e : list){
                map[e.x][e.y] = avg;
            }
            return true;
        }

        return false;
    }
}