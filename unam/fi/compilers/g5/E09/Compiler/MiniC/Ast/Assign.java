package unam.fi.compilers.g5.E09.Compiler.MiniC.Ast;

   public  class Assign implements Stmt {
        public String id;
        public Expr expr;
        public Assign(String id, Expr expr) {
            this.id = id; this.expr = expr;
        }
    }