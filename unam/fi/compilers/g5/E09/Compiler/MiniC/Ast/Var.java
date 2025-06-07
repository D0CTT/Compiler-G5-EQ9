package unam.fi.compilers.g5.E09.Compiler.MiniC.Ast;

    public  class Var implements Expr {
        public String id;
        public Var(String id) { this.id = id; }
    }