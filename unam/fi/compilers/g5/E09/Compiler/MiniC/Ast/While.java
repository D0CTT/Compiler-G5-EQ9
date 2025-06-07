package unam.fi.compilers.g5.E09.Compiler.MiniC.Ast;
  import java.util.List;

    public class While implements Stmt {
        public Cond cond;
        public List<Stmt> body;
        public While(Cond cond, List<Stmt> body) {
            this.cond = cond; this.body = body;
        }
    }