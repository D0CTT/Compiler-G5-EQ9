package unam.fi.compilers.g5.E09.Compiler.MiniC.Ast;

    public  class Cond implements ASTNode {
        public  Expr left;
        public String op; // relational operator
        public Expr right;
        public Cond(Expr left, String op, Expr right) {
            this.left = left; this.op = op; this.right = right;
        }
    }