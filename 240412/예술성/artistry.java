import java.io.*;
import java.util.*;

public class Main {
    static int n;
    static int[][] map;
    static int[][] visited;
    static int[][] temp;
    static LinkedList<Group> list = new LinkedList<>();
    static int[][] dir = {{0,1}, {0,-1}, {1,0}, {-1,0}};
    
    static class Group{
    	int num, cnt, edge;
    	
    	Group(int num, int cnt){
    		this.num = num;
    		this.cnt = cnt;
    	}
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer str;

        n = Integer.parseInt(br.readLine());
        map = new int [n][n];

        for (int i=0; i<n; i++){
            str = new StringTokenizer(br.readLine());
            for (int j=0; j<n; j++){
                map[i][j] = Integer.parseInt(str.nextToken());
            }
        }

        solve();
    }

    static void solve(){
        int solIdx = 0;
        int sum = 0;

        while (solIdx++ < 4){
            
            // 그룹 만들기
            makeGroup();
            
            // 예술 점수 계산하기
            sum += calculate();
            
            // 그림 회전하기
            rotate();
        }
        
        System.out.println(sum);
        
    }
    
    static void makeGroup() {
    	Queue<int[]> q = new LinkedList<>();
    	list = new LinkedList<>();
    	visited = new int[n][n];
    	
    	int groupIdx = 1;
    	
    	for (int i=0; i<n; i++) {
    		for (int j=0; j<n; j++) {
    			int cnt = 0;
    			
    			if (visited[i][j] == 0) {
    				q.add(new int[] {i,j});
    				visited[i][j] = groupIdx;
    		    	
    		    	while (!q.isEmpty()) {
    		    		int[] cur = q.poll();
    		    		int cx = cur[0];
    		    		int cy = cur[1];
    		    		cnt++;
    		    		
    		    		
    		    		for (int d=0; d<4; d++) {
    		    			int nx = cx + dir[d][0];
    		    			int ny = cy + dir[d][1];
    		    			
    		    			if (nx >= 0 && nx < n && ny >= 0 && ny < n && visited[nx][ny] == 0 && map[cx][cy] == map[nx][ny]) {
    		    				q.add(new int[] {nx,ny});
    		    				visited[nx][ny] = groupIdx;
    		    			}
    		    		}
    		    	}
    		    	
    		    	list.add(new Group(map[i][j], cnt));
    		    	groupIdx++;
    			} 
    		}
    	}
    	
    	
    }

    static int calculate(){
        int score = 0;
        
        for (int i=0; i<n; i++) {
        	for (int j=0; j<n; j++) {
        		int curIdx = visited[i][j] - 1;
        		for (int d=0; d<4; d++) {
        			int nx = i + dir[d][0];
        			int ny = j + dir[d][1];
        			
        			if (nx >= 0 && nx < n && ny >= 0 && ny < n && map[i][j] != map[nx][ny]) {
        				int nextIdx = visited[nx][ny] - 1;
        				Group groupA = list.get(curIdx);
        	        	Group groupB = list.get(nextIdx);
        	        	
        	        	score += (groupA.cnt + groupB.cnt) * groupA.num * groupB.num;
	    			}
        		}
        	}
        }
        
       

        return score / 2;
    }

    static void rotate(){
        temp = new int[n][n];
        int mid = n / 2;
        //1. 십자 모양 회전
        for (int i=0, j=n-1; i<n; i++, j--){
            temp[mid][i] = map[i][mid];
            temp[i][mid] = map[mid][j];
        }
        //check(temp);

        //2. 정사각형 회전
        rotateSquare(0, 0, mid);
        rotateSquare(0, mid + 1, mid);
        rotateSquare(mid + 1, 0, mid);
        rotateSquare(mid + 1, mid + 1, mid);
        //check(temp);
        
        map = temp;
    }

    static void rotateSquare(int sx, int sy, int mid) {
    	for (int x=sx; x < sx + mid; x++) {
    		for (int y=sy; y<sy + mid; y++) {
    			int ox = x -sx;
    			int oy = y - sy;
    			int rx = oy;
    			int ry = mid - ox - 1;
    			temp[rx + sx][ry + sy] = map[x][y];
    		}
    	}
    }

    static void check(int[][] map){
        for (int i=0; i<n; i++){
            for (int j=0; j<n; j++){
                System.out.print(map[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println("///////");
    }
}