package org.dreamcat.common.core.tree;

/**
 * Create by tuke on 2020/4/18
 *
 * @see java.util.HashMap
 */
public abstract class RBNode<Node extends RBNode<Node>> extends BinaryNode<Node> {
    /*
    root is black,
    each chain contains the same number of black nodes,
    no continual red chain
     */
    protected Node parent;
    protected boolean red;

    /**
     * balance for new <strong>red</strong> node
     * <p>
     * all unbalance cases, LLb, LLr, LRb, LRr, RRb, RRr, RLb, RLr
     * XYr just change color, XYb need ratate
     */
    protected static <Node extends RBNode<Node>> Node balanceInsertion(Node root, Node x) {
        Node p = x.parent;
        if (root == p || !p.red) return root;
        Node g = p.parent;

        if (root == g) {
            return fixInsertion(x, p, g);
        } else {
            Node gp = g.parent;
            if (gp.left == g) {
                g = fixInsertion(x, p, g);
                gp.left = g;
                if (!g.red || !gp.red) return root;
                return balanceInsertion(root, g);
            } else {
                g = fixInsertion(x, p, g);
                gp.right = g;
                if (!g.red || !gp.red) return root;
                return balanceInsertion(root, g);
            }
            //return root;
        }
    }

    private static <Node extends RBNode<Node>> Node fixInsertion(Node x, Node p, Node g) {
        Node gl, gr;
        if (p.left == x && g.left == p) {
            // LLb
            if ((gr = g.right) == null || !gr.red) {
                return rotateLLb(p, g);
            }
            // LLr
            else {
                p.red = false;
                gr.red = false;
                if (g.parent != null) {
                    g.red = true;
                }
                return g;
            }
        } else if (p.left == x && g.right == p) {
            // RLb
            if ((gl = g.left) == null || !gl.red) {
                return rotateRLb(x, p, g);
            }
            // RLr
            else {
                p.red = false;
                gl.red = false;
                if (g.parent != null) g.red = true;
                return g;
            }
        } else if (p.right == x && g.left == p) {
            // LRb
            if ((gr = g.right) == null || !gr.red) {
                return rotateLRb(x, p, g);
            }
            // LRr
            else {
                p.red = false;
                gr.red = false;
                if (g.parent != null) g.red = true;
                return g;
            }
        } else if (p.right == x && g.right == p) {
            // RRb
            if ((gl = g.left) == null || !gl.red) {
                return rotateRRb(p, g);
            }
            // RRr
            else {
                p.red = false;
                gl.red = false;
                if (g.parent != null) g.red = true;
                return g;
            }
        }

        throw new AssertionError();
    }

    /*
    LLb
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
                   gB
            pR          gr
       xR      pr
    l     r
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
              pB
       xR          gR
    l     r     pr    gr
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
     */
    private static <Node extends RBNode<Node>> Node rotateLLb(Node p, Node g) {
        Node pr = p.right;

        p.parent = g.parent;
        p.right = g;
        p.red = false;

        g.left = pr;
        g.parent = p;
        g.red = true;

        return p;
    }

    /*
    RLb
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
         gB
    gl             pR
               xR     pr
            l     r
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
             xB
        gR        pR
    gl    xl   xr  pr
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
     */
    private static <Node extends RBNode<Node>> Node rotateRLb(Node x, Node p, Node g) {
        Node xl = x.left, xr = x.right;

        x.parent = g.parent;
        x.left = g;
        x.right = p;
        x.red = false;

        g.parent = x;
        g.right = xl;
        g.red = true;

        p.parent = x;
        p.left = xr;
        p.red = true;

        return x;
    }

    /*
    LRb
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
                  gB
        pR             gr
    pl        xR
           xl   xr
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
            xB
        pR       gR
    pl    xl   xr  gr
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
     */
    private static <Node extends RBNode<Node>> Node rotateLRb(Node x, Node p, Node g) {
        Node xl = x.left, xr = x.right;

        x.parent = g.parent;
        x.left = p;
        x.right = g;
        x.red = false;

        g.parent = x;
        g.left = xr;
        g.red = true;

        p.parent = x;
        p.right = xl;
        p.red = true;

        return x;
    }

    /*
    RRb
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
           gB
    gl           pR
              pl     xR
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
            pB
        gR      xR
    gl     pl
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
     */
    private static <Node extends RBNode<Node>> Node rotateRRb(Node p, Node g) {
        Node pl = p.left;

        p.parent = g.parent;
        p.left = g;
        p.red = false;

        g.right = pl;
        g.parent = p;
        g.red = true;

        return p;
    }

    /**
     * balance for delete <strong>black</strong> node
     */
    protected static <Node extends RBNode<Node>> Node balanceDeletion(Node root, Node x) {
        for (Node xp, xpl, xpr; ; ) {
            if (x == null || x == root)
                return root;
            else if ((xp = x.parent) == null) {
                x.red = false;
                return x;
            } else if (x.red) {
                x.red = false;
                return root;
            }

/*
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
                pp
     p
           r
        rl
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
               pp
          r
    p
      rl
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
*/
            /*
            ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
                             xpp
                xp
            x          xprR
                   xprl   xprr
            ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
                                  xpp
                      xprB
              xpR           xprr
            x    xprl
            ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
            */
            else if ((xpl = xp.left) == x) {
                if ((xpr = xp.right) != null && xpr.red) {
                    xpr.red = false;
                    xp.red = true;
                    root = rotateLeft(root, xp);
                    xpr = (xp = x.parent) == null ? null : xp.right;
                }
                if (xpr == null)
                    x = xp;
                else {
                    Node sl = xpr.left, sr = xpr.right;
                    if ((sr == null || !sr.red) &&
                            (sl == null || !sl.red)) {
                        xpr.red = true;
                        x = xp;
                    } else {
                        if (sr == null || !sr.red) {
                            sl.red = false;
                            xpr.red = true;
                            root = rotateRight(root, xpr);
                            xpr = (xp = x.parent) == null ?
                                    null : xp.right;
                        }
                        if (xpr != null) {
                            xpr.red = xp.red;
                            if ((sr = xpr.right) != null)
                                sr.red = false;
                        }
                        if (xp != null) {
                            xp.red = false;
                            root = rotateLeft(root, xp);
                        }
                        x = root;
                    }
                }
            } else { // symmetric
                if (xpl != null && xpl.red) {
                    xpl.red = false;
                    xp.red = true;
                    root = rotateRight(root, xp);
                    xpl = (xp = x.parent) == null ? null : xp.left;
                }
                if (xpl == null)
                    x = xp;
                else {
                    Node sl = xpl.left, sr = xpl.right;
                    if ((sl == null || !sl.red) &&
                            (sr == null || !sr.red)) {
                        xpl.red = true;
                        x = xp;
                    } else {
                        if (sl == null || !sl.red) {
                            sr.red = false;
                            xpl.red = true;
                            root = rotateLeft(root, xpl);
                            xpl = (xp = x.parent) == null ?
                                    null : xp.left;
                        }
                        if (xpl != null) {
                            xpl.red = xp.red;
                            if ((sl = xpl.left) != null)
                                sl.red = false;
                        }
                        if (xp != null) {
                            xp.red = false;
                            root = rotateRight(root, xp);
                        }
                        x = root;
                    }
                }
            }
        }
    }

    // ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ====

    /*
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
                pp
     p
           r
        rl
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
               pp
          r
    p
      rl
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
     */
    private static <Node extends RBNode<Node>> Node rotateLeft(Node root, Node p) {
        Node r, pp, rl;
        if (p != null && (r = p.right) != null) {
            if ((rl = p.right = r.left) != null)
                rl.parent = p;
            if ((pp = r.parent = p.parent) == null)
                (root = r).red = false;
            else if (pp.left == p)
                pp.left = r;
            else
                pp.right = r;
            r.left = p;
            p.parent = r;
        }
        return root;
    }

    /*
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
               pp
           p
    l
       lr
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
              pp
    l
          p
       lr
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
     */
    private static <Node extends RBNode<Node>> Node rotateRight(Node root, Node p) {
        Node l, pp, lr;
        if (p != null && (l = p.left) != null) {
            if ((lr = p.left = l.right) != null)
                lr.parent = p;
            if ((pp = l.parent = p.parent) == null)
                (root = l).red = false;
            else if (pp.right == p)
                pp.right = l;
            else
                pp.left = l;
            l.right = p;
            p.parent = l;
        }
        return root;
    }

    protected void nullify() {
        left = null;
        right = null;
        parent = null;
    }


}
