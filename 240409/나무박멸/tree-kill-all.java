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

    static boolean isInBounds(int nx, int ny){
        if (nx >= 0 && nx < N && ny >= 0 && ny < N ) return true;
        return false;
    }

    static void copy(int[][] a, int[][] b) {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                a[i][j] = b[i][j];
    }

    static void growTrees() {
    int[][] temp = new int[N][N];
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            temp[i][j] = map[i][j];  // 기존 나무의 수를 복사
            if (map[i][j] > 0) {
                int cnt = 0;
                for (int d = 0; d < 4; d++) {
                    int nx = i + dir[d][0];
                    int ny = j + dir[d][1];
                    if (isInBounds(nx, ny) && map[nx][ny] > 0) cnt++;
                }
                temp[i][j] += cnt;  // 임시 배열에 성장한 나무의 수 반영
            }
        }
    }
    map = temp;  // map 업데이트
}

static void expandTrees() {
    int[][] temp = new int[N][N];
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            temp[i][j] = map[i][j];  // 기존 나무의 수를 복사
        }
    }

    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            if (map[i][j] > 0) {
                List<int[]> emptyCells = new ArrayList<>();
                for (int d = 0; d < 4; d++) {
                    int nx = i + dir[d][0];
                    int ny = j + dir[d][1];
                    if (isInBounds(nx, ny) && map[nx][ny] == 0) {
                        emptyCells.add(new int[]{nx, ny});
                    }
                }

                int treesToSpread = map[i][j] / emptyCells.size();
                for (int[] cell : emptyCells) {
                    temp[cell[0]][cell[1]] += treesToSpread;  // 나누어진 나무 수를 빈 칸에 추가
                }
            }
        }
    }
    map = temp;  // map 업데이트
}

static int removeTrees() {
    int[][] temp = new int[N][N];
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            temp[i][j] = map[i][j];  // 기존 나무의 수를 복사
        }
    }

    int[] area = checkTree();
    int x = area[0];
    int y = area[1];
    int removed = 0;

    if (x == -1 && y == -1) return 0; // 박멸할 나무가 없는 경우

    // 선택된 위치에 제초제 뿌리기
    if (temp[x][y] > 0) {
        removed += temp[x][y];
        temp[x][y] = -2;  // 제초제 뿌렸다는 표시
        if (pesticide == null) pesticide = new int[N][N];
        pesticide[x][y] = C;  // 제초제 지속 기간 설정
    }

    // 대각선 방향으로 제초제 확장
    for (int d = 0; d < 4; d++) {  // 대각선 4방
        for (int step = 1; step <= K; step++) {  // 확장 범위 k
            int nx = x + dirS[d][0] * step;
            int ny = y + dirS[d][1] * step;
            if (isInBounds(nx, ny) && temp[nx][ny] >= 0) {
                removed += temp[nx][ny];
                temp[nx][ny] = -2;  // 제초제 뿌렸다는 표시
                pesticide[nx][ny] = C;  // 제초제 지속 기간 설정
            } else {
                break;  // 확장 중단
            }
        }
    }
    map = temp;  // map 업데이트

    return removed;  // 박멸된 나무의 수 반환
}

    static int[] checkTree(){
        int[][] temp = new int[N][N];
        int x = 0; int y = 0;
        int max = -1; // 최댓값 초기화

        for (int i=0; i<N; i++){
            for (int j=0; j<N; j++){
                if (map[i][j] > 0){
                    

                    int cnt = map[i][j]; // 현재 위치의 나무 수

                    for (int d=0; d<4; d++){

                        for (int k=1; k<=K; k++){
                            int nx = i + dirS[d][0] * k;
                            int ny = j + dirS[d][1] * k;

                            if (isInBounds(nx, ny)) {
                                if (map[nx][ny] > 0) cnt += map[nx][ny]; // 나무가 있는 경우 카운트 증가
                                else break; // 나무가 없는 경우(벽, 빈 칸 등) 탐색 중단
                            }

                        }
                    }

                    temp[i][j] = cnt; // 계산된 값 저장
                    max = Math.max(max, cnt); // 최댓값 갱신
                }
            }
        }

        // 최댓값과 같은 위치 찾기
        List<int[]> list = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (temp[i][j] == max) {
                    list.add(new int[]{i, j}); // 최댓값을 가진 위치 저장
                }
            }
        }

        // 최적의 위치 선택
        if (list.isEmpty()) return new int[]{-1, -1}; // 최댓값이 없는 경우
        list.sort((a, b) -> (a[0] == b[0]) ? a[1] - b[1] : a[0] - b[0]); // 행, 열 기준으로 정렬
        return list.get(0); // 정렬된 리스트의 첫 번째 원소 반환
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