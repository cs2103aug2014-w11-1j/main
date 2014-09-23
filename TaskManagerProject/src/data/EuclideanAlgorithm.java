package data;

/**
 * returns m, q such that nm + pq = 1.
 */
public class EuclideanAlgorithm {
    public int m;
    public int q;
    public int gcd;
    
    public EuclideanAlgorithm(int n, int p) {
        if (n < p) {
            compute(p,n);
            swapResults();
        }
        else {
            compute(n,p);
        }
    }
    
    /**
     * Assume n > p.
     * Sets m and q such that nm + pq = gcd(n,p)
     * 
     * @param n
     * @param p
     */
    private void compute(int n, int p) {
        // n - pr = s;
        int s = n%p;
        int r = n/p;
        
        if (s == 0) {
            // If s == 0, (Terminal condition)
            // then p is the GCD.
            // thus for nm + pq = gcd,
            m = 0;
            q = 1;
            gcd = p;
        }
        else {
            compute(p, s);
            // After running compute, we have m,q such that:
            // pm + sq = gcd.
            
            // We let M,Q be the new m and q respectively.
            // Objective: make nM + pQ = gcd.
            // We know n = pr + s.
            // Thus M(pr+s) + pQ = gcd.
            // prM + pQ + sM = gcd
            // p(rM + Q) + sM = gcd
            // m = rM+Q, q = M
            // M = q, Q = m - rq.
            int M = q;
            int Q = m-r*q;
            m = M;
            q = Q;
        }
    }
    
    private void swapResults() {
        int temp = m;
        m = q;
        q = temp;
    }
}
