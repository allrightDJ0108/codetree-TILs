import java.io.*;
import java.util.*;

public class Main {
    static int N, M, K, C;
    static int[][] map;
    static int[][] pesticide;
    static int[][] dir = {{0,1},{0,-1},{1,0},{-1,0}};
    static int[][] dirS = {{-1,-1}, {1,1}, {-1,1},{1,-1}};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer str = new StringTokenizer(br.readLine());

        N = Integer.parseInt(str.nextToken());
        M = Integer.parseInt(str.nextToken());
        K = Integer.parseInt(str.nextToken());
        C = Integer.parseInt(str.nextToken());

        map = new int[N][N];

        for ( int i=0; i<N; i++){
            str = new StringTokenizer(br.readLine());
            for (int j=0; j<N; j++){
                map[i][j] = Integer.parseInt(str.nextToken());
            }
        }

        int result = 0;
        while (M-- > 0){
            //1. grow
            growTrees();
            //2. ex
            expandTrees();
            //3. remove
            result += removeTrees();
        }

        System.out.println(result);

    }

    static boolean isWall(int nx, int ny){
        if (nx >= 0 && nx < N && ny >= 0 && ny < N ) return true;
        return false;
    }

    static void growTrees(){
        for (int i=0; i<N; i++){
            for (int j=0; j<N; j++){
                if (map[i][j] > 0){
                    int cnt = 0;
                    for (int d=0; d<4; d++){
                        int nx = i+dir[d][0];
                        int ny = j+dir[d][1];

                        if (isWall(nx, ny)){
                            if (map[nx][ny] > 0) cnt++;
                        }
                    }

                    map[i][j] += cnt;
                }
            }
        }
        //testFunc(map);
    }

    static void expandTrees(){
        Queue<int[]> q = new LinkedList<>();
        int[][] temp = new int[N][N];

        for (int i=0; i<N; i++){
            for (int j=0; j<N; j++){
                if (temp[i][j] == 0) temp[i][j] = map[i][j];
                if (map[i][j] > 0){
                    int cnt = 0;
                    for (int d=0; d<4; d++){
                        int nx = i + dir[d][0];
                        int ny = j + dir[d][1];

                        if (isWall(nx, ny)){
                            if (map[nx][ny] == 0) {
                                cnt++;
                                q.add(new int[]{nx, ny, map[i][j]});
                            }
                        }
                        
                    }

                    while (!q.isEmpty()){
                        int[] cur = q.poll();
                        int cx = cur[0];
                        int cy = cur[1];
                        int cc = cur[2];

                        temp[cx][cy] += (cc / cnt);

                    }

                }
            }
        }

        map = temp;
        //testFunc(map);
        
    }

    static int removeTrees(){

        if (pesticide == null) {
            pesticide = new int[N][N];
        }

        int[] area = checkTree();
        int x = area[0];
        int y = area[1];
        int removed = 0;

         // 선택된 위치에 제초제 뿌리기
        if (map[x][y] > 0) {
            removed += map[x][y];
            map[x][y] = 0; // 나무 박멸
            pesticide[x][y] = C; // 제초제 지속 기간 설정
        }

        // 대각선 방향으로 제초제 퍼트리기
        for (int d = 0; d < 4; d++) {
            int cx = x;
            int cy = y;

            for (int step = 1; step <= K; step++) {
                int nx = cx + dirS[d][0] * step;
                int ny = cy + dirS[d][1] * step;

                if (!isWall(nx, ny) || map[nx][ny] == -1) break; // 범위를 벗어나거나 벽을 만나면 중단

                if (map[nx][ny] > 0) {
                    removed += map[nx][ny]; // 나무 박멸
                    map[nx][ny] = 0;
                }

                pesticide[nx][ny] = C; // 제초제 지속 기간 설정
            }
        }

        // 제초제 지속 기간 감소
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (pesticide[i][j] > 0) {
                    pesticide[i][j]--;

                    // 제초제 기간이 끝나면 해당 위치의 제초제 효과 제거
                    if (pesticide[i][j] == 0) {
                        map[i][j] = 0; // 이전에 나무가 있었던 자리를 0으로 초기화
                    }
                }
            }
        }
        
        return removed;
    }

    static int[] checkTree(){
        int[][] temp = new int[N][N];
        int x = 0; int y = 0;
        int max = 0;

        for (int i=0; i<N; i++){
            for (int j=0; j<N; j++){
                if (map[i][j] > 0){
                    

                    int sum = map[i][j];

                    for (int d=0; d<4; d++){
                        int cx = i;
                        int cy = j; 

                        for (int k=1; k<=K; k++){
                            int nx = cx + dirS[d][0] * k;
                            int ny = cy + dirS[d][1] * k;

                            if (!isWall(nx, ny)) break;

                            if (map[nx][ny] == -1 || map[nx][ny] == 0) break;

                            sum += map[nx][ny];

                        }
                    }

                    if (sum > max){
                        max = sum;
                        x = i; y = j;
                    }
                }
            }
        }

        return new int[]{x, y};
    }


    static void testFunc(int[][] map){
        for (int i=0; i<N; i++){
            for (int j=0; j<N; j++){
                System.out.print(map[i][j]+" ");
            }
            System.out.println();
        }

        System.out.println("=======");
    }
}