import java.io.*;
import java.util.*;

public class Main {
    static int L, N, Q;
    static int[][] board;
    static int[][] knightBoard;
    static Knight[] knights;

    static int[][] dir = {{-1,0}, {0,1}, {1,0}, {0,-1}}; // 상(0), 우(1), 하(2), 좌(3);

    static class Knight{
        int r, c, h, w, k;
        boolean inBoard;
        boolean isDamaged;
        int damage;

        Knight(int r, int c, int h, int w, int k){
            this.r = r;
            this.c = c;
            this.h = h;
            this.w = w;
            this.k = k;

            inBoard = true;
            isDamaged = false;
            damage = 0;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer str = new StringTokenizer(br.readLine());

        L = Integer.parseInt(str.nextToken());
        N = Integer.parseInt(str.nextToken());
        Q = Integer.parseInt(str.nextToken());

        board = new int[L][L];
        knightBoard = new int[L][L];
        knights = new Knight[N];

        for (int i=0; i<L; i++){
            str = new StringTokenizer(br.readLine());
            for (int j=0; j<L; j++){
                board[i][j] = Integer.parseInt(str.nextToken());
            }
        }

        for (int i=0; i<N; i++){
            str = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(str.nextToken()) - 1;
            int c = Integer.parseInt(str.nextToken()) - 1;
            int h = Integer.parseInt(str.nextToken());
            int w = Integer.parseInt(str.nextToken());
            int k = Integer.parseInt(str.nextToken());

            knights[i] = new Knight(r, c, h, w, k);
            
            for (int j = 0; j < h; j++) {
                for (int l = 0; l < w; l++) {
                    knightBoard[r + j][c + l] = (i + 1);
                }
            }
        }

        // 왕의 명령 시작
        for (int q=0; q < Q; q++){
            str = new StringTokenizer(br.readLine());

            int i = Integer.parseInt(str.nextToken()) - 1;
            int d = Integer.parseInt(str.nextToken());

            // 체스판에서 사라진 기사에게 명령을 내리면 아무 반응이 없다.
            if (!knights[i].inBoard) {
                continue;
            }

            process(i, d);
            initIsDamaged(); // 명령이 종료된 후 다음 명령 수행을 위해 초기화
        }

        
        System.out.println(totalDamage());
    }

    static void initIsDamaged(){
        for (int i=0; i<N; i++){
            knights[i].isDamaged = false;
        }
    }

    static int totalDamage(){
        int totalDamage = 0;
        for (Knight knight : knights){
            if (knight.inBoard){
                totalDamage += knight.damage;
            }
        }

        return totalDamage;
    }

    static void process(int i, int d){
        if (!checkMovable(i, d)) return;

        moveKnight(i, d);
        knights[i].isDamaged = false;
        checkDamage();

    }

    static boolean checkMovable(int i, int d){
        int knightR = knights[i].r;
        int knightH = knights[i].h;
        int knightC = knights[i].c;
        int knightW = knights[i].w;

        for (int r = knightR; r < knightR + knightH; r++ ){
            for (int c = knightC ; c < knightC + knightW; c++){
                int nr = r + dir[d][0];
                int nc = c + dir[d][1];

                if (isWall(nr, nc)) {
                    return false;
                }

                int prevKnight = knightBoard[nr][nc];

                // 이동하려는 위치에 기사가 없다면 그냥 넘어간다.
                if (prevKnight == 0 || prevKnight == (i + 1)) {
                    continue;
                }

                // 이동하려는 위치에 다른 기사가 있다면 그 기사도 함께 연쇄적으로 한 칸 밀려난다.
                if (!checkMovable(prevKnight - 1, d)) {
                    return false;
                }
            }
        }

        // 기사를 이동시킬 수 있다.
        return true;
    }

    static boolean isWall(int r, int c) {
        return r < 0 || c < 0 || r >= L || c >= L || board[r][c] == 2;
    }

    static void moveKnight(int i, int d){
        int knightR = knights[i].r;
        int knightH = knights[i].h;
        int knightC = knights[i].c;
        int knightW = knights[i].w;

        for (int r = knightR; r < knightR + knightH; r++) {
            for (int c = knightC; c < knightC + knightW; c++) {
                int nr = r + dir[d][0];
                int nc = c + dir[d][1];

                int prevKnight = knightBoard[nr][nc];

                if (prevKnight == 0 || prevKnight == (i + 1)) {
                    continue;
                }

                // 이동하려는 위치에 다른 기사가 있다면 그 기사도 함께 연쇄적으로 한 칸 밀려난다.
                moveKnight(prevKnight - 1, d);
            }
        }

        switch (d) {
            case 0:
                moveUp(i, d);
                break;
            case 1:
                moveRight(i, d);
                break;
            case 2:
                moveDown(i, d);
                break;
            case 3:
                moveLeft(i, d);
                break;
        }

        knights[i].isDamaged = true;
        knights[i].r += dir[d][0];
        knights[i].c += dir[d][1];
    }

    static void moveUp(int i, int d) {
        for (int r = knights[i].r; r < knights[i].r + knights[i].h; r++) {
            for (int c = knights[i].c; c < knights[i].c + knights[i].w; c++) {
                int nr =  r + dir[d][0];
                int nc =  c + dir[d][1];

                knightBoard[r][c] = 0;
                knightBoard[nr][nc] = (i + 1);
            }
        }
    }

    static void moveDown(int i, int d) {
        for (int r = knights[i].r + knights[i].h - 1; r >= knights[i].r; r--) {
            for (int c = knights[i].c; c < knights[i].c + knights[i].w; c++) {
                int nr = r + dir[d][0];
                int nc = c + dir[d][1];

                knightBoard[r][c] = 0;
                knightBoard[nr][nc] = (i + 1);
            }
        }
    }

    static void moveRight(int i, int d) {
        for (int c = knights[i].c + knights[i].w - 1; c >= knights[i].c; c--) {
            for (int r = knights[i].r; r < knights[i].r + knights[i].h; r++) {
                int nr = r + dir[d][0];
                int nc = c + dir[d][1];

                knightBoard[r][c] = 0;
                knightBoard[nr][nc] = (i + 1);
            }
        }
    }

    static void moveLeft(int i, int d) {
        for (int c = knights[i].c; c < knights[i].c + knights[i].w; c++) {
            for (int r = knights[i].r; r < knights[i].r + knights[i].h; r++) {
                int nr = r + dir[d][0];
                int nc = c + dir[d][1];

                knightBoard[r][c] = 0;
                knightBoard[nr][nc] = (i + 1);
            }
        }
    }

    static void checkDamage(){
        for (int i=0; i<N; i++){
            if (!knights[i].isDamaged) continue;

            // trap의 개수만큼 데미지 감소
            int damage = countTrap(i); 

            knights[i].damage += damage;
            knights[i].k -= damage;

            // 체력이 0이 되면 체스판에서 사라짐
            if (knights[i].k <= 0){
                deleteKnight(i);
            }
        }
    }

    static int countTrap(int i){
        int count = 0;
        int knightR = knights[i].r;
        int knightH = knights[i].h;
        int knightC = knights[i].c;
        int knightW = knights[i].w;
        
        for (int r = knightR; r < knightR + knightH; r++) {
            for (int c = knightC; c < knightC + knightW; c++) {
                if (isWall(r, c)) {
                    continue;
                }

                if (board[r][c] == 1) {
                    count++;
                }
            }
        }

        return count;
    }

    static void deleteKnight(int i) {
        knights[i].inBoard = false;

        for (int r = knights[i].r; r < knights[i].r + knights[i].h; r++) {
            for (int c = knights[i].c; c < knights[i].c + knights[i].w; c++) {
                knightBoard[r][c] = 0;
            }
        }
    }
}