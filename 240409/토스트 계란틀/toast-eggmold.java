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
        // 판 위에 이동해야할 계란이 있는지 확인한다.
        // 계란을 이동한 경우 result를 증가시킨다.
        // 더이상 이동할 계란이 없으면 종료된다.
        while (checkFunc()){
            result++;
        }

        System.out.println(result);
    }

    // 판 위에 이동해야할 계란이 있는지 확인한다.
    static boolean checkFunc(){
        visited = new boolean[N][N];
        boolean moved = false;

        for (int i=0; i<N; i++){
            for (int j=0; j<N; j++){
                // 방문하지 않은 칸
                if (!visited[i][j]){
                    if (EggFunc(i,j)){
                        moved = true;
                    }
                }
            }
        }

        // 판 위에 이동해야할 계란이 없으면 false
        return moved;
    }

    // 판 위에 이동할 계란이 있는지 확인하고, 이동시킨다.
    static boolean EggFunc(int x, int y){
        Queue<Eggs> q = new LinkedList<>(); // 주변 칸을 탐색해서 그룹을 나눌 때 사용
        LinkedList<Eggs> list = new LinkedList<>(); // 계란 재분배 시 사용
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
