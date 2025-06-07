package unam.fi.compilers.g5.E09.Compiler.MiniC.Ast;

    public  class BinOp implements Expr {
        public Expr left;
        public String op; // "+" or "-"
        public Expr right;
        public BinOp(Expr left, String op, Expr right) {
            this.left = left; this.op = op; this.right = right;
        }
    }