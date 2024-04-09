import java.io.*;
import java.util.*;

public class Main {
    static int N, K;
    static LinkedList<Belt> list = new LinkedList<>();
    static int zeroCnt = 0;

    static class Belt{
        int d;
        boolean robot = false;

        Belt(int d){
            this.d = d;
        }

        public void putRobot(){
            // 현재 로봇이 올려져 있지 않고 내구도가 0보다 클 때
            if (!robot && d > 0){
                robot = true;
                d--;
                if (d == 0){
                    zeroCnt++;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer str = new StringTokenizer(br.readLine(), " ");

        N = Integer.parseInt(str.nextToken());
        K = Integer.parseInt(str.nextToken());

        str = new StringTokenizer(br.readLine());
        for (int i=0; i<2*N; i++){
            int d = Integer.parseInt(str.nextToken());
            list.add(new Belt(d));
        }

        int result = 0;
        while (zeroCnt < K){
            MoveBelt();
            MoveRobot();
            result++;
        }

        System.out.println(result);
    }

    static void MoveBelt(){
        list.addFirst(list.removeLast());

        // 만약 마지막 칸에 로봇이 있다면 로봇 내려주기
        if (list.get(N-1).robot){
            list.get(N-1).robot = false;
        }
    }

    static void MoveRobot(){
        for (int i = N-2; i >= 0; i--){
            Belt cur = list.get(i);
            Belt next = list.get(i+1);

            // 현재 있는 로봇을 다음 칸으로 이동
            if (cur.robot && !next.robot && next.d > 0){
                cur.robot = false; // 현재 칸에서 내리고
                next.putRobot(); // 다음 칸에 올려주기
            }
        }

        if (list.get(0).d > 0){
            list.get(0).putRobot();
        }
    }
}